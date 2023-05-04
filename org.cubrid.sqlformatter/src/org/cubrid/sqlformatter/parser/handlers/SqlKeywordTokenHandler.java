package org.cubrid.sqlformatter.parser.handlers;

import java_cup.runtime.Symbol;
import org.apache.commons.lang.StringUtils;
import org.cubrid.sqlformatter.parser.SqlParseTree;
import org.cubrid.sqlformatter.parser.constants.RegexPatterns;
import org.cubrid.sqlformatter.parser.constants.SqlNodeType;
import org.cubrid.sqlformatter.parser.containers.FunctionContainer;
import org.cubrid.sqlformatter.parser.mode.CompoundKeyword;
import org.cubrid.sqlformatter.parser.util.SqlParserUtil;
import org.cubrid.sqlformatter.parser.util.SqlTokenUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SqlKeywordTokenHandler implements SqlTokenHandler {
  private int handleAlphaOperator(Symbol[] tokenList, int currentIndex, SqlParseTree sqlParseTree) {
    String tokenValue = (String)(tokenList[currentIndex]).value;
    sqlParseTree.saveFinalElement(SqlNodeType.AlphaOperator, tokenValue);
    return currentIndex + 1;
  }
  
  private void handleAndToken(Symbol token, SqlParseTree sqlParseTree) {
    if (sqlParseTree.ancestorNameMatches(0, SqlNodeType.LowerBound)) {
      sqlParseTree.moveToAncestorContainer(1);
      Element containerClose = sqlParseTree.saveElement(SqlNodeType.ContainerClose);
      sqlParseTree.saveFinalElement(SqlNodeType.OtherKeyword, (String)token.value, containerClose);
      sqlParseTree.saveContainerElement(SqlNodeType.UpperBound);
    } else {
      sqlParseTree.escapeAnyBetweenCondition();
      sqlParseTree.saveFinalElement(SqlNodeType.And, (String)token.value);
    } 
  }
  
  private void handleBetweenToken(Symbol token, SqlParseTree sqlParseTree) {
    sqlParseTree.saveContainerElement(SqlNodeType.Between);
    Element containerOpen = sqlParseTree.saveElement(SqlNodeType.ContainerOpen);
    sqlParseTree.saveFinalElement(SqlNodeType.OtherKeyword, (String)token.value, containerOpen);
    sqlParseTree.saveContainerElement(SqlNodeType.LowerBound);
  }
  
  private int handleDatatypeSqlKeyword(Symbol[] tokenList, int currentIndex, SqlParseTree sqlParseTree) {
    String tokenValue = (String)(tokenList[currentIndex]).value;
    if (SqlTokenUtil.nextTokenExceptCommentAndWhitespaceMatches(tokenList, currentIndex + 1, 268435457) && FunctionContainer.INSTANCE.contains(tokenValue))
      return handleSqlFunction(tokenList, currentIndex, sqlParseTree); 
    sqlParseTree.saveFinalElement(SqlNodeType.DataTypeKeyword, tokenValue);
    return currentIndex + 1;
  }
  
  private int handleJoinRelatedToken(Symbol token, SqlParseTree sqlParseTree, Symbol[] tokenList, int currentIndex) {
    if (SqlParserUtil.isSelectClause(sqlParseTree.getCurrentContainer()) && SqlTokenUtil.isEquals(tokenList[currentIndex], "STRAIGHT_JOIN")) {
      sqlParseTree.saveFinalElement(SqlNodeType.OtherKeyword, (String)token.value);
      return currentIndex + 1;
    } 
    CompoundKeyword compoundKeywordObj = SqlTokenUtil.findJoinCompoundKeyword(tokenList, currentIndex);
    int lastKeywordIndex = compoundKeywordObj.getTokenEndIndex();
    if (lastKeywordIndex != SqlTokenUtil.INDEX_NOT_FOUND) {
      sqlParseTree.escapeIndexHintList();
      sqlParseTree.escapeAnyBetweenCondition();
      sqlParseTree.escapeJoinOnSection();
      sqlParseTree.escapeAnySelectionTarget();
      sqlParseTree.saveContainerElement(SqlNodeType.JoinOn);
      if (lastKeywordIndex == currentIndex) {
        String compoundKeyword = compoundKeywordObj.getCompoundKeyword();
        sqlParseTree.saveFinalElement(SqlNodeType.OtherKeyword, compoundKeyword);
      } else {
        Element element = SqlParserUtil.createCompoundKeywordElement(tokenList, currentIndex, lastKeywordIndex, sqlParseTree.getDoc());
        sqlParseTree.saveElement(element);
      } 
      sqlParseTree.saveContainerElement(SqlNodeType.SelectionTarget);
      return lastKeywordIndex + 1;
    } 
    sqlParseTree.saveFinalElement(SqlNodeType.OtherKeyword, (String)token.value);
    return currentIndex + 1;
  }
  
  private int handleOnToken(Symbol token, SqlParseTree sqlParseTree, Symbol[] tokenList, int currentIndex) {
    sqlParseTree.escapeIndexHintList();
    sqlParseTree.escapeAnySelectionTarget();
    if (sqlParseTree.ancestorNameMatches(0, SqlNodeType.JoinOn)) {
      sqlParseTree.saveFinalElement(SqlNodeType.OtherKeyword, (String)token.value);
      sqlParseTree.saveContainerElement(SqlNodeType.OnCondition);
      return currentIndex + 1;
    } 
    CompoundKeyword compoundKeywordInfo = SqlTokenUtil.findGeneralCompoundKeyword(tokenList, currentIndex, 9);
    int lastKeywordIndex = compoundKeywordInfo.getTokenEndIndex();
    String compoundKeywords = compoundKeywordInfo.getCompoundKeyword();
    if (lastKeywordIndex != SqlTokenUtil.INDEX_NOT_FOUND) {
      if (StringUtils.equals(compoundKeywords, "ON DUPLICATE KEY UPDATE")) {
        sqlParseTree.considerStartNewClause();
      } else if (SqlParserUtil.shouldTryToStartDDLItem(compoundKeywords, sqlParseTree)) {
        sqlParseTree.jumpDDLItem();
        sqlParseTree.saveContainerElement(SqlNodeType.DDLItem);
      } 
      Element compoundKeywordElement = SqlParserUtil.createCompoundKeywordElement(tokenList, currentIndex, lastKeywordIndex, sqlParseTree.getDoc());
      sqlParseTree.saveElement(compoundKeywordElement);
      return lastKeywordIndex + 1;
    } 
    sqlParseTree.saveFinalElement(SqlNodeType.OtherKeyword, (String)token.value);
    return currentIndex + 1;
  }
  
  private int handleCompoundKeywords(Symbol token, SqlParseTree sqlParseTree, Symbol[] tokenList, int currentIndex) {
    CompoundKeyword compoundKeywordInfo = SqlTokenUtil.findGeneralCompoundKeyword(tokenList, currentIndex, 9);
    int lastKeywordIndex = compoundKeywordInfo.getTokenEndIndex();
    if (lastKeywordIndex != SqlTokenUtil.INDEX_NOT_FOUND) {
      String compoundKeyword = compoundKeywordInfo.getCompoundKeyword();
      if (SqlParserUtil.shouldTryToStartPartitionParamerter(compoundKeyword, sqlParseTree)) {
        sqlParseTree.escapePartitionParameter();
        sqlParseTree.saveContainerElement(SqlNodeType.PartitionParameter);
      } else if (SqlParserUtil.shouldTryToStartDDLItem(compoundKeyword, sqlParseTree)) {
        sqlParseTree.jumpDDLItem();
        sqlParseTree.saveContainerElement(SqlNodeType.DDLItem);
      } 
      Element compoundKeywordElement = SqlParserUtil.createCompoundKeywordElement(tokenList, currentIndex, lastKeywordIndex, sqlParseTree.getDoc());
      sqlParseTree.saveElement(compoundKeywordElement);
      Symbol nextMeaningfulToken = SqlTokenUtil.getNextMeaningfulToken(tokenList, lastKeywordIndex + 1);
      if (RegexPatterns.ORACLE_MULTIPLE_INSERT_COMPOUND.matcher(compoundKeyword).matches()) {
        sqlParseTree.saveContainerElement(SqlNodeType.MultipleInsertBlock);
      } else if (SqlParserUtil.shouldTryToStartDDLContentBlock(compoundKeyword, nextMeaningfulToken, sqlParseTree) && !SqlTokenUtil.isToken(nextMeaningfulToken, 268435457)) {
        sqlParseTree.saveContainerElement(SqlNodeType.DDLContentBlock);
      } 
      currentIndex = lastKeywordIndex + 1;
    } 
    return currentIndex;
  }
  
  private int handleControlFlowKeyword(Symbol token, Symbol[] tokenList, int currentIndex, SqlParseTree sqlParseTree) {
    if (SqlTokenUtil.isControlFlowStarter(token)) {
      CompoundKeyword controlFlowKeywords = SqlTokenUtil.findControlFlowCompoundKeyword(tokenList, currentIndex);
      CompoundKeyword generalCompoundKeywords = SqlTokenUtil.findGeneralCompoundKeyword(tokenList, currentIndex, 9);
      int lastControlKeywordIndex = controlFlowKeywords.getTokenEndIndex();
      if (lastControlKeywordIndex != SqlTokenUtil.INDEX_NOT_FOUND) {
        Element compoundKeyword = SqlParserUtil.createCompoundKeywordElement(tokenList, currentIndex, lastControlKeywordIndex, sqlParseTree.getDoc());
        if (StringUtils.equals(controlFlowKeywords.getCompoundKeyword(), "BEGIN TRY") && sqlParseTree.considerStartTryCatch()) {
          sqlParseTree.saveContainerElement(SqlNodeType.ControlFlowBlock);
          sqlParseTree.saveElementWithChild(SqlNodeType.ControlFlowStarter, compoundKeyword);
        } else if (StringUtils.equals(controlFlowKeywords.getCompoundKeyword(), "BEGIN CATCH") && sqlParseTree.considerStartControlFlowBlock()) {
          sqlParseTree.saveElementWithChild(SqlNodeType.ControlFlowStarter, compoundKeyword);
        } else {
          sqlParseTree.saveElement(compoundKeyword);
        } 
        return lastControlKeywordIndex + 1;
      } 
      if (token.sym == 978849794 && generalCompoundKeywords.getTokenEndIndex() != SqlTokenUtil.INDEX_NOT_FOUND) {
        sqlParseTree.considerStartNewStatement();
        sqlParseTree.considerStartNewClause();
        Element compoundKeyword = SqlParserUtil.createCompoundKeywordElement(tokenList, currentIndex, generalCompoundKeywords.getTokenEndIndex(), sqlParseTree.getDoc());
        sqlParseTree.saveElement(compoundKeyword);
        return generalCompoundKeywords.getTokenEndIndex() + 1;
      } 
      if (sqlParseTree.considerStartControlFlowBlock()) {
        switch (token.sym) {
          case 977801219:
          case 978849793:
            sqlParseTree.saveContainerElement(SqlNodeType.ControlFlowClause);
            break;
        } 
        sqlParseTree.saveContainerElement(SqlNodeType.ControlFlowStarter);
        sqlParseTree.saveFinalElement(SqlNodeType.OtherKeyword, (String)token.value);
        sqlParseTree.moveToAncestorContainer(1);
      } else {
        sqlParseTree.saveFinalElement(SqlNodeType.OtherKeyword, (String)token.value);
      } 
    } else {
      sqlParseTree.saveFinalElement(SqlNodeType.OtherKeyword, (String)token.value);
    } 
    return currentIndex + 1;
  }
  
  private int processCompoundOrFunctionKeyword(Symbol token, Symbol[] tokenList, int currentIndex, SqlParseTree sqlParseTree) {
    if (SqlTokenUtil.isCompoundKeywordStarter(token)) {
      int nextIndex = handleCompoundKeywords(token, sqlParseTree, tokenList, currentIndex);
      if (currentIndex != nextIndex)
        return nextIndex; 
    } 
    if (FunctionContainer.INSTANCE.contains((String)token.value) && SqlTokenUtil.nextTokenExceptCommentAndWhitespaceMatches(tokenList, currentIndex + 1, 268435457)) {
      sqlParseTree.saveFinalElement(SqlNodeType.SqlFunction, (String)token.value);
      return currentIndex + 1;
    } 
    return currentIndex;
  }
  
  private int handleOtherSqlKeyword(Symbol token, Symbol[] tokenList, int currentIndex, SqlParseTree sqlParseTree) {
    int returnIndex = processCompoundOrFunctionKeyword(token, tokenList, currentIndex, sqlParseTree);
    if (returnIndex != currentIndex)
      return returnIndex; 
    if (SqlParserUtil.shouldTryToStartPartitionParamerter((String)token.value, sqlParseTree)) {
      sqlParseTree.escapePartitionParameter();
      sqlParseTree.saveContainerElement(SqlNodeType.PartitionParameter);
    } else if (SqlParserUtil.shouldTryToStartDDLItem((String)token.value, sqlParseTree)) {
      sqlParseTree.jumpDDLItem();
      sqlParseTree.saveContainerElement(SqlNodeType.DDLItem);
    } 
    sqlParseTree.saveFinalElement(SqlNodeType.OtherKeyword, (String)token.value);
    Symbol nextMeaningfulToken = SqlTokenUtil.getNextMeaningfulToken(tokenList, currentIndex + 1);
    if (SqlParserUtil.shouldTryToStartDDLContentBlock((String)token.value, nextMeaningfulToken, sqlParseTree) && !SqlTokenUtil.isToken(nextMeaningfulToken, 268435457)) {
      if ((tokenList[currentIndex]).sym == 1069547531) {
        int next = handleTokensAfterQuery(tokenList, currentIndex + 1, sqlParseTree);
        currentIndex = next - 1;
      } 
      sqlParseTree.saveContainerElement(SqlNodeType.DDLContentBlock);
    } 
    return currentIndex + 1;
  }
  
  private int handleSqlFunction(Symbol[] tokenList, int currentIndex, SqlParseTree sqlParseTree) {
    String tokenValue = (String)(tokenList[currentIndex]).value;
    if (SqlTokenUtil.nextTokenExceptCommentAndWhitespaceMatches(tokenList, currentIndex + 1, 268435457)) {
      sqlParseTree.saveFinalElement(SqlNodeType.SqlFunction, tokenValue);
      return currentIndex + 1;
    } 
    return handleOtherSqlKeyword(tokenList[currentIndex], tokenList, currentIndex, sqlParseTree);
  }
  
  private int handleSpecialSqlKeyword(Symbol[] tokenList, int currentIndex, SqlParseTree sqlParseTree) {
    Symbol token = tokenList[currentIndex];
    switch (token.sym) {
      case 952107009:
        handleBetweenToken(token, sqlParseTree);
        return currentIndex + 1;
      case 952107010:
        handleAndToken(token, sqlParseTree);
        return currentIndex + 1;
      case 952107011:
        sqlParseTree.saveFinalElement(SqlNodeType.Or, (String)token.value);
        return currentIndex + 1;
      case 953155588:
        return handleOnToken(token, sqlParseTree, tokenList, currentIndex);
      case 953155594:
        return handleSet(token, sqlParseTree, tokenList, currentIndex);
      case 952107014:
      case 952107015:
        return handleLeftRight(token, sqlParseTree, tokenList, currentIndex);
      case 953155595:
        return handleAs(token, sqlParseTree, tokenList, currentIndex);
      case 952107020:
        return handleReplace(token, sqlParseTree, tokenList, currentIndex);
      case 952107021:
        return handleCase(tokenList, currentIndex, sqlParseTree);
      case 952107022:
        return handleWhen(tokenList, currentIndex, sqlParseTree);
      case 952107023:
        return handleThen(tokenList, currentIndex, sqlParseTree);
      case 952107024:
        return handleElse(tokenList, currentIndex, sqlParseTree);
      case 952107025:
        return handleEnd(tokenList, currentIndex, sqlParseTree);
      case 953155589:
        return handleUsing(sqlParseTree, tokenList, currentIndex);
      case 953155592:
        return handleCharacter(sqlParseTree, tokenList, currentIndex);
      case 952107026:
        sqlParseTree.startNewStatementInSqlRoot();
        sqlParseTree.saveFinalElement(SqlNodeType.OtherKeyword, (String)(tokenList[currentIndex]).value);
        sqlParseTree.startNewStatementInSqlRoot();
        return currentIndex + 1;
    } 
    return currentIndex;
  }
  
  private int handleUsing(SqlParseTree sqlParseTree, Symbol[] tokenList, int currentIndex) {
    Element container = sqlParseTree.getCurrentContainer();
    sqlParseTree.escapeIndexHintList();
    if (sqlParseTree.escapeAnySelectionTarget() && sqlParseTree.ancestorNameMatches(0, SqlNodeType.JoinOn)) {
      sqlParseTree.saveFinalElement(SqlNodeType.OtherKeyword, (String)(tokenList[currentIndex]).value);
      sqlParseTree.saveContainerElement(SqlNodeType.OnCondition);
      return currentIndex + 1;
    } 
    if (SqlParserUtil.isDDLOtherBlockElement(container)) {
      if (SqlParserUtil.containsChildElement(container, SqlNodeType.DDLParens)) {
        int i = handleCompoundKeywords(tokenList[currentIndex], sqlParseTree, tokenList, currentIndex);
        if (i != currentIndex)
          return i; 
      } 
      sqlParseTree.saveFinalElement(SqlNodeType.OtherKeyword, (String)(tokenList[currentIndex]).value);
      return currentIndex + 1;
    } 
    sqlParseTree.considerStartNewClause();
    int nextIndex = handleCompoundKeywords(tokenList[currentIndex], sqlParseTree, tokenList, currentIndex);
    if (nextIndex == currentIndex) {
      sqlParseTree.saveFinalElement(SqlNodeType.OtherKeyword, (String)(tokenList[currentIndex]).value);
      nextIndex++;
    } 
    return nextIndex;
  }
  
  private int handleCharacter(SqlParseTree sqlParseTree, Symbol[] tokenList, int currentIndex) {
    CompoundKeyword compoundKeyword = SqlTokenUtil.findGeneralCompoundKeyword(tokenList, currentIndex, 2);
    int endIndex = compoundKeyword.getTokenEndIndex();
    if (endIndex != SqlTokenUtil.INDEX_NOT_FOUND) {
      if (SqlParserUtil.shouldTryToStartDDLItem(compoundKeyword.getCompoundKeyword(), sqlParseTree)) {
        sqlParseTree.jumpDDLItem();
        sqlParseTree.saveContainerElement(SqlNodeType.DDLItem);
      } 
      Element compoundKeywordElement = SqlParserUtil.createCompoundKeywordElement(tokenList, currentIndex, endIndex, sqlParseTree.getDoc());
      sqlParseTree.saveElement(compoundKeywordElement);
      return endIndex + 1;
    } 
    sqlParseTree.saveFinalElement(SqlNodeType.DataTypeKeyword, (String)(tokenList[currentIndex]).value);
    return currentIndex + 1;
  }
  
  private int handleLeftRight(Symbol token, SqlParseTree sqlParseTree, Symbol[] tokenList, int currentIndex) {
    if (SqlTokenUtil.nextTokenExceptCommentAndWhitespaceMatches(tokenList, currentIndex + 1, 268435457))
      return handleSqlFunction(tokenList, currentIndex, sqlParseTree); 
    return handleJoinRelatedToken(token, sqlParseTree, tokenList, currentIndex);
  }
  
  private int handleReplace(Symbol token, SqlParseTree sqlParseTree, Symbol[] tokenList, int currentIndex) {
    if (SqlTokenUtil.nextTokenExceptCommentAndWhitespaceMatches(tokenList, currentIndex + 1, 268435457))
      return handleSqlFunction(tokenList, currentIndex, sqlParseTree); 
    return handleStarterKW(tokenList, currentIndex, sqlParseTree);
  }
  
  private int handleAs(Symbol token, SqlParseTree sqlParseTree, Symbol[] tokenList, int currentIndex) {
    int nextIndex = SqlTokenUtil.getNextTokenIndexExceptCommentAndWhitespace(tokenList, currentIndex + 1);
    if (nextIndex != SqlTokenUtil.INDEX_NOT_FOUND && SqlTokenUtil.isTokenToStartDDLAsBlock(tokenList[nextIndex]) && SqlParserUtil.shouldTryToStartDDLItem((String)token.value, sqlParseTree)) {
      sqlParseTree.jumpDDLItem();
      sqlParseTree.saveContainerElement(SqlNodeType.DDLItem);
      sqlParseTree.saveFinalElement(SqlNodeType.OtherKeyword, (String)token.value);
      sqlParseTree.saveContainerElement(SqlNodeType.DDLAsBlock);
      return currentIndex + 1;
    } 
    nextIndex = handleCompoundKeywords(token, sqlParseTree, tokenList, currentIndex);
    if (nextIndex != currentIndex)
      return nextIndex; 
    sqlParseTree.saveFinalElement(SqlNodeType.OtherKeyword, token.value.toString());
    return currentIndex + 1;
  }
  
  private int handleSet(Symbol token, SqlParseTree sqlParseTree, Symbol[] tokenList, int currentIndex) {
    int nextIndex = handleCompoundKeywords(token, sqlParseTree, tokenList, currentIndex);
    if (nextIndex != currentIndex)
      return nextIndex; 
    if (SqlParserUtil.isDDLDetailParensElement(sqlParseTree.getCurrentContainer())) {
      sqlParseTree.saveFinalElement(SqlNodeType.DataTypeKeyword, token.value.toString());
      return currentIndex + 1;
    } 
    int previousNonWSNonCmtToken = SqlTokenUtil.getPreviousTokenIndexExceptCommentAndWhitespace(tokenList, currentIndex - 1);
    if (previousNonWSNonCmtToken != SqlTokenUtil.INDEX_NOT_FOUND && SqlTokenUtil.isEquals(tokenList[previousNonWSNonCmtToken], "AS")) {
      sqlParseTree.saveFinalElement(SqlNodeType.DataTypeKeyword, token.value.toString());
      return currentIndex + 1;
    } 
    sqlParseTree.considerStartNewClause();
    sqlParseTree.saveFinalElement(SqlNodeType.OtherKeyword, token.value.toString());
    return currentIndex + 1;
  }
  
  private int handleStarterKW(Symbol[] tokenList, int currentIndex, SqlParseTree sqlParseTree) {
    Symbol token = tokenList[currentIndex];
    if (SqlParserUtil.shouldTryToStartNewStatement(token, sqlParseTree))
      sqlParseTree.considerStartNewStatement(); 
    if (SqlParserUtil.shouldTryToStartNewClause(token, sqlParseTree)) {
      if (token.sym == 945553409)
        sqlParseTree.escapeMultipleInsertBlock(); 
      sqlParseTree.considerStartNewClause();
    } 
    if (SqlParserUtil.shouldTryToStartDDLProcedureBlock(tokenList, currentIndex, sqlParseTree)) {
      sqlParseTree.saveContainerElement(SqlNodeType.DDLProcedureBlock);
      sqlParseTree.saveFinalElement(SqlNodeType.OtherKeyword, (String)token.value);
      return currentIndex + 1;
    } 
    if (SqlParserUtil.shouldTryToStartDDLOtherBlock(token, sqlParseTree)) {
      sqlParseTree.saveContainerElement(SqlNodeType.DDLOtherBlock);
      if (SqlTokenUtil.isCompoundKeywordStarter(token)) {
        int nextIndex = handleCompoundKeywords(token, sqlParseTree, tokenList, currentIndex);
        if (currentIndex != nextIndex)
          return nextIndex; 
      } 
      sqlParseTree.saveFinalElement(SqlNodeType.OtherKeyword, (String)token.value);
      return currentIndex + 1;
    } 
    if (SqlParserUtil.shouldTryToStartCursorDeclaration(tokenList, currentIndex, sqlParseTree)) {
      sqlParseTree.saveContainerElement(SqlNodeType.CursorDeclaration);
      sqlParseTree.saveFinalElement(SqlNodeType.OtherKeyword, (String)token.value);
      return currentIndex + 1;
    } 
    if (SqlParserUtil.shouldTryToStartCursorForBlock(tokenList, currentIndex, sqlParseTree)) {
      sqlParseTree.startNewContainer(SqlNodeType.CursorForBlock, (String)(tokenList[currentIndex]).value, SqlNodeType.ContainerContentBody);
      return currentIndex + 1;
    } 
    if (SqlParserUtil.shouldTryToStartCursorForOptions(tokenList, currentIndex, sqlParseTree)) {
      SqlParserUtil.jumpToContainer(SqlNodeType.CursorDeclaration, sqlParseTree);
      sqlParseTree.startNewContainer(SqlNodeType.CursorForOptions, (String)(tokenList[currentIndex]).value, SqlNodeType.ContainerContentBody);
      return currentIndex + 1;
    } 
    if ((token.sym & 0x7C10000) == 4259840 && !SqlParserUtil.isDDLOtherBlockElement(sqlParseTree.getCurrentContainer()) && !SqlParserUtil.isDDLItemElement(sqlParseTree.getCurrentContainer())) {
      Element setOperatorElement = sqlParseTree.saveElement(SqlNodeType.SetOperatorClause);
      int nextTokenIndex = SqlTokenUtil.getNextTokenIndexExceptCommentAndWhitespace(tokenList, currentIndex + 1);
      if (nextTokenIndex != SqlTokenUtil.INDEX_NOT_FOUND && isPossibleCompoundKeyword(tokenList[nextTokenIndex])) {
        Document doc = sqlParseTree.getDoc();
        Element compoundKeyword = SqlParserUtil.createCompoundKeywordElement(tokenList, currentIndex, nextTokenIndex, doc);
        setOperatorElement.appendChild(compoundKeyword);
        currentIndex = nextTokenIndex + 1;
        return currentIndex;
      } 
      sqlParseTree.saveFinalElement(SqlNodeType.OtherKeyword, (String)(tokenList[currentIndex]).value, setOperatorElement);
      currentIndex++;
      return currentIndex;
    } 
    if (SqlTokenUtil.isCompoundKeywordStarter(token)) {
      int nextIndex = handleCompoundKeywords(token, sqlParseTree, tokenList, currentIndex);
      if (currentIndex != nextIndex)
        return nextIndex; 
    } 
    if (SqlParserUtil.shouldTryToStartDDLItem((String)token.value, sqlParseTree)) {
      sqlParseTree.jumpDDLItem();
      sqlParseTree.saveContainerElement(SqlNodeType.DDLItem);
    } 
    sqlParseTree.saveFinalElement(SqlNodeType.OtherKeyword, (String)(tokenList[currentIndex]).value);
    if (token.sym == 943980549 || token.sym == 945553412 || token.sym == 953155589)
      sqlParseTree.saveContainerElement(SqlNodeType.SelectionTarget); 
    currentIndex++;
    return currentIndex;
  }
  
  private boolean isPossibleCompoundKeyword(Symbol token) {
    return (token.sym == 1069547521 || token.sym == 1069547522 || token.sym == 1069547523 || token.sym == 1069547524);
  }
  
  private int handleTokensAfterQuery(Symbol[] tokenList, int startIndex, SqlParseTree sqlParseTree) {
    while (startIndex < tokenList.length) {
      Symbol token = tokenList[startIndex];
      if (SqlTokenUtil.isWhitespace(token)) {
        sqlParseTree.saveFinalElement(SqlNodeType.WhiteSpace, (String)token.value);
        startIndex++;
        continue;
      } 
      if (SqlTokenUtil.isComment(token)) {
        Element comment = SqlParserUtil.createCommentElement(tokenList, startIndex, sqlParseTree.getDoc());
        sqlParseTree.saveElement(comment);
        startIndex++;
        continue;
      } 
      if (SqlTokenUtil.isNumber(token)) {
        sqlParseTree.saveFinalElement(SqlNodeType.Number, (String)token.value);
        startIndex++;
        continue;
      } 
      if (SqlTokenUtil.isComma(token)) {
        sqlParseTree.saveFinalElement(SqlNodeType.Comma, (String)token.value);
        startIndex++;
      } 
    } 
    return startIndex;
  }
  
  private int handleDDLContentBreaker(Symbol[] tokenList, int currentIndex, SqlParseTree sqlParseTree) {
    if ((tokenList[currentIndex]).sym == 960528384)
      sqlParseTree.jumpDDLContentBlock(); 
    sqlParseTree.saveFinalElement(SqlNodeType.OtherKeyword, (tokenList[currentIndex]).value.toString());
    return currentIndex + 1;
  }
  
  private boolean isStartedWithBracket(Symbol[] tokenList, int currentIndex, SqlParseTree sqlParseTree) {
    int previousNonWSNonCmtToken = SqlTokenUtil.getPreviousTokenIndexExceptCommentAndWhitespace(tokenList, currentIndex - 1);
    if (previousNonWSNonCmtToken != SqlTokenUtil.INDEX_NOT_FOUND && SqlTokenUtil.isEquals(tokenList[previousNonWSNonCmtToken], "["))
      return true; 
    return false;
  }
  
  private int handleIdentifier(Symbol[] tokenList, int currentIndex, SqlParseTree sqlParseTree) {
    Symbol token = tokenList[currentIndex];
    sqlParseTree.saveFinalElement(SqlNodeType.Identifier, (String)token.value);
    return currentIndex + 1;
  }
  
  private int handleCase(Symbol[] tokenList, int currentIndex, SqlParseTree sqlParseTree) {
    sqlParseTree.startNewContainer(SqlNodeType.Case, (String)(tokenList[currentIndex]).value, SqlNodeType.Input);
    return currentIndex + 1;
  }
  
  private int handleWhen(Symbol[] tokenList, int currentIndex, SqlParseTree sqlParseTree) {
    sqlParseTree.escapeInput();
    sqlParseTree.escapeWhen();
    sqlParseTree.startNewContainer(SqlNodeType.When, (String)(tokenList[currentIndex]).value, SqlNodeType.ContainerContentBody);
    return currentIndex + 1;
  }
  
  private int handleThen(Symbol[] tokenList, int currentIndex, SqlParseTree sqlParseTree) {
    if (sqlParseTree.escapeWhenCondition()) {
      sqlParseTree.startNewContainer(SqlNodeType.Then, (String)(tokenList[currentIndex]).value, SqlNodeType.BranchContentBody);
    } else {
      sqlParseTree.saveFinalElement(SqlNodeType.OtherKeyword, (String)(tokenList[currentIndex]).value);
    } 
    return currentIndex + 1;
  }
  
  private int handleElse(Symbol[] tokenList, int currentIndex, SqlParseTree sqlParseTree) {
    if (sqlParseTree.escapeWhen()) {
      sqlParseTree.startNewContainer(SqlNodeType.Else, (String)(tokenList[currentIndex]).value, SqlNodeType.BranchContentBody);
    } else if (sqlParseTree.considerStartElseControlFlowClause()) {
      sqlParseTree.saveContainerElement(SqlNodeType.ControlFlowStarter);
      sqlParseTree.saveFinalElement(SqlNodeType.OtherKeyword, (String)(tokenList[currentIndex]).value);
      sqlParseTree.moveToAncestorContainer(1);
    } else {
      sqlParseTree.saveFinalElement(SqlNodeType.OtherKeyword, (String)(tokenList[currentIndex]).value);
    } 
    return currentIndex + 1;
  }
  
  private int handleEnd(Symbol[] tokenList, int currentIndex, SqlParseTree sqlParseTree) {
    sqlParseTree.escapeInput();
    sqlParseTree.escapeWhen();
    sqlParseTree.escapeElse();
    if (SqlParserUtil.isCaseElement(sqlParseTree.getCurrentContainer())) {
      Element containerClose = sqlParseTree.saveElement(SqlNodeType.ContainerClose);
      sqlParseTree.saveFinalElement(SqlNodeType.OtherKeyword, (String)(tokenList[currentIndex]).value, containerClose);
      sqlParseTree.moveToAncestorContainer(1);
    } else if (SqlParserUtil.jumpToControlFlowBlockStartWithBegin(sqlParseTree)) {
      sqlParseTree.saveContainerElement(SqlNodeType.ControlFlowEnd);
      CompoundKeyword controlFlowKeywords = SqlTokenUtil.findControlFlowCompoundKeyword(tokenList, currentIndex);
      int lastKeywordIndex = controlFlowKeywords.getTokenEndIndex();
      if (lastKeywordIndex != SqlTokenUtil.INDEX_NOT_FOUND) {
        Element element = SqlParserUtil.createCompoundKeywordElement(tokenList, currentIndex, lastKeywordIndex, sqlParseTree.getDoc());
        sqlParseTree.saveElement(element);
        currentIndex = lastKeywordIndex;
      } else {
        sqlParseTree.saveFinalElement(SqlNodeType.OtherKeyword, (String)(tokenList[currentIndex]).value);
      } 
      sqlParseTree.moveToAncestorContainer(2);
      if (StringUtils.equalsIgnoreCase(controlFlowKeywords.getCompoundKeyword(), "END CATCH"))
        sqlParseTree.moveToAncestorContainer(1); 
    } else {
      sqlParseTree.saveFinalElement(SqlNodeType.OtherKeyword, (String)(tokenList[currentIndex]).value);
    } 
    return currentIndex + 1;
  }
  
  private int handleHintSqlKeyword(Symbol[] tokenList, int currentIndex, SqlParseTree sqlParseTree) {
    CompoundKeyword indexHintKeyword = SqlTokenUtil.findIndexHintCompoundKeyword(tokenList, currentIndex);
    int endIndex = indexHintKeyword.getTokenEndIndex();
    if (endIndex != SqlTokenUtil.INDEX_NOT_FOUND) {
      if (!sqlParseTree.jumpIndexHint())
        sqlParseTree.saveContainerElement(SqlNodeType.IndexHintList); 
      sqlParseTree.saveContainerElement(SqlNodeType.IndexHint);
      Element indexHintElement = SqlParserUtil.createCompoundKeywordElement(tokenList, currentIndex, endIndex, sqlParseTree.getDoc());
      sqlParseTree.saveElement(indexHintElement);
      return endIndex + 1;
    } 
    String value = (String)(tokenList[currentIndex]).value;
    if (StringUtils.equals(value, "FORCE") && SqlParserUtil.shouldTryToStartDDLItem(value, sqlParseTree)) {
      sqlParseTree.jumpDDLItem();
      sqlParseTree.saveContainerElement(SqlNodeType.DDLItem);
    } 
    sqlParseTree.saveFinalElement(SqlNodeType.OtherKeyword, value);
    return currentIndex + 1;
  }
  
  public int handleToken(Symbol[] tokenList, int currentIndex, SqlParseTree sqlParseTree) {
    Symbol token = tokenList[currentIndex];
    int sqlKWType = token.sym & 0x7C00000;
    if (isStartedWithBracket(tokenList, currentIndex, sqlParseTree))
      sqlKWType = 671088640; 
    switch (sqlKWType) {
      case 671088640:
        return handleIdentifier(tokenList, currentIndex, sqlParseTree);
      case 4194304:
        return handleStarterKW(tokenList, currentIndex, sqlParseTree);
      case 8388608:
        return handleAlphaOperator(tokenList, currentIndex, sqlParseTree);
      case 16777216:
        return handleDatatypeSqlKeyword(tokenList, currentIndex, sqlParseTree);
      case 12582912:
        return handleSpecialSqlKeyword(tokenList, currentIndex, sqlParseTree);
      case 37748736:
        return handleControlFlowKeyword(token, tokenList, currentIndex, sqlParseTree);
      case 130023424:
        return handleOtherSqlKeyword(token, tokenList, currentIndex, sqlParseTree);
      case 20971520:
        return handleDDLContentBreaker(tokenList, currentIndex, sqlParseTree);
      case 25165824:
        return handleSqlFunction(tokenList, currentIndex, sqlParseTree);
      case 29360128:
        return handleHintSqlKeyword(tokenList, currentIndex, sqlParseTree);
      case 33554432:
        return handleJoinRelatedToken(token, sqlParseTree, tokenList, currentIndex);
    } 
    return currentIndex;
  }
}
