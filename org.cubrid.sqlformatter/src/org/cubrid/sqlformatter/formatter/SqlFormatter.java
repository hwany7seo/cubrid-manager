package org.cubrid.sqlformatter.formatter;

import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.cubrid.sqlformatter.SqlFormatOptions;
import org.cubrid.sqlformatter.parser.constants.SqlNodeType;
import org.cubrid.sqlformatter.parser.util.SqlNodeTypeUtil;
import org.cubrid.sqlformatter.parser.util.SqlParserUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SqlFormatter {
  private SqlFormatOptions options;
  
  public SqlFormatter(SqlFormatOptions options) {
    this.options = options;
  }
  
  private void breakAsExpected(SqlFormatState state) {
    if (state.expectLineBreak) {
      state.addOutputLineBreak();
      state.addOutputIndents();
      state.expectLineBreak = false;
    } 
  }
  
  private String formatSqlKeyword(String sqlKeyword) {
    if (this.options.isUpperCaseKeyword())
      return sqlKeyword.toUpperCase(); 
    return sqlKeyword.toLowerCase();
  }
  
  private String formatComment(String comment, int indentNumber) {
    StringBuffer formattedComment = new StringBuffer();
    String[] commentAtEachLine = comment.split(SqlFormatState.NEW_LINE);
    formattedComment.append(commentAtEachLine[0]);
    for (int i = 1; i < commentAtEachLine.length; i++) {
      formattedComment.append(SqlFormatState.NEW_LINE);
      int len = commentAtEachLine[i].length();
      int spaceNumber = 0;
      int addedIndents = 0;
      int j = 0;
      while (j < len && addedIndents < indentNumber) {
        char c = commentAtEachLine[i].charAt(j);
        if (c == ' ') {
          spaceNumber++;
          if (spaceNumber == 8) {
            formattedComment.append('\t');
            spaceNumber = 0;
            addedIndents++;
          } 
          j++;
          continue;
        } 
        if (c == '\t') {
          formattedComment.append('\t');
          spaceNumber = 0;
          addedIndents++;
          j++;
        } 
      } 
      while (addedIndents < indentNumber) {
        formattedComment.append('\t');
        addedIndents++;
      } 
      if (j < len)
        formattedComment.append(commentAtEachLine[i].substring(j)); 
    } 
    return formattedComment.toString();
  }
  
  public String formatSqlParseTree(Document sqlParseTree) {
    Element sqlRoot = sqlParseTree.getDocumentElement();
    SqlFormatState state = new SqlFormatState();
    processSqlNodeList(sqlRoot.getChildNodes(), state);
    return state.getOutput();
  }
  
  private void processAndOrOperator(Element element, SqlFormatState state) {
    if (this.options.isBreakBeforeAndOr())
      state.expectLineBreak = true; 
    separateWords(state);
    state.addOutputString(formatSqlKeyword(element.getTextContent()));
    state.expectWordSeperator = true;
  }
  
  private void processComma(Element commaElement, SqlFormatState state) {
    breakAsExpected(state);
    state.addOutputString(commaElement.getTextContent());
    Element parent = (Element)commaElement.getParentNode();
    String parentName = parent.getTagName();
    SqlNodeType parentNodeType = SqlNodeType.valueOf(parentName);
    if (this.options.isExpandClauseCommaList() && parentNodeType == SqlNodeType.SqlClause) {
      state.expectLineBreak = true;
      state.expectWordSeperator = false;
    } else if (this.options.isExpandDDLBlock() && (parentNodeType == SqlNodeType.DDLParens || parentNodeType == SqlNodeType.DDLContentBlock || parentNodeType == SqlNodeType.PartitionParen)) {
      state.expectLineBreak = true;
      state.expectWordSeperator = false;
    } else if (this.options.isExpandInList() && parentNodeType == SqlNodeType.InParens) {
      state.expectLineBreak = true;
      state.expectWordSeperator = false;
    } else if (this.options.isExpandSelectionTargetParenCommaList() && parentNodeType == SqlNodeType.SelectionTargetParens) {
      state.expectLineBreak = true;
      state.expectWordSeperator = false;
    } else if (this.options.isExpandPivotParens() && parentNodeType == SqlNodeType.PivotParens) {
      state.expectLineBreak = true;
      state.expectWordSeperator = false;
    } else {
      state.expectWordSeperator = true;
    } 
  }
  
  private void processCompoundKeyword(Element compoundKeywordElement, SqlFormatState state) {
    for (Element element : SqlParserUtil.getChildElements(compoundKeywordElement))
      processElement(element, state); 
    state.expectWordSeperator = true;
  }
  
  private void processDataTypeKeyword(Element dataTypeKeywordElement, SqlFormatState state) {
    separateWords(state);
    state.addOutputString(formatSqlKeyword(dataTypeKeywordElement.getTextContent()));
    state.expectWordSeperator = true;
  }
  
  private void processSqlFunction(Element dataTypeKeywordElement, SqlFormatState state) {
    separateWords(state);
    state.addOutputString(formatSqlKeyword(dataTypeKeywordElement.getTextContent()));
    state.expectWordSeperator = true;
  }
  
  private void processElement(Element element, SqlFormatState state) {
    String elementName = element.getTagName();
    switch (SqlNodeType.valueOf(elementName)) {
      case SqlStatement:
        processSQLStatement(element, state);
        break;
      case SqlClause:
        processSQLClause(element, state);
        break;
      case SetOperatorClause:
        processSetOperatorClause(element, state);
        break;
      case And:
      case Or:
        processAndOrOperator(element, state);
        break;
      case DDLParens:
      case PartitionParen:
      case ParameterParen:
      case SelectionTargetParens:
      case ExpressionParens:
      case InParens:
      case PivotParens:
        processExpandableParens(element, state);
        break;
      case DDLDetailParens:
      case FunctionParens:
        processUnexpandableParens(element, state);
        break;
      case DDLAsBlock:
      case DDLContentBlock:
        processDDLContentBlock(element, state);
        break;
      case InBraces:
        processUnexpandableBraces(element, state);
        break;
      case OpenBrace:
      case CloseBrace:
        processBrace(element, state);
        break;
      case DDLItem:
      case JoinOn:
      case When:
      case Else:
      case PartitionParameter:
      case CursorForOptions:
        if (!state.outputIsEmpty())
          state.expectLineBreak = true; 
      case OnCondition:
      case Between:
      case ContainerOpen:
      case LowerBound:
      case ContainerClose:
      case UpperBound:
      case SelectionTarget:
      case Input:
      case Then:
      case ContainerContentBody:
      case IndexHintList:
      case MultipleInsertBlock:
      case ControlFlowTryCatch:
      case CursorDeclaration:
        processSqlNodeList(element.getChildNodes(), state);
        break;
      case Case:
        processCase(element, state);
        break;
      case DDLOtherBlock:
      case DDLProcedureBlock:
        processDDLDefinitionBlock(element, state);
        break;
      case OtherKeyword:
        processOtherKeyword(element, state);
        break;
      case CompoundKeyword:
        processCompoundKeyword(element, state);
        break;
      case DataTypeKeyword:
        processDataTypeKeyword(element, state);
        break;
      case SqlFunction:
        processSqlFunction(element, state);
        break;
      case OpenBracket:
      case CloseBracket:
        processBracket(element, state);
        break;
      case IndexHint:
        processIndexHint(element, state);
        break;
      case String:
      case Identifier:
      case MonetaryValue:
      case OtherOperator:
      case Asterisk:
      case SessionCommand:
      case Other:
        processWord(element, state);
        break;
      case BinaryValue:
        processBinaryValue(element, state);
        break;
      case Number:
        processNumber(element, state);
        break;
      case DatetimeString:
        processDatetimeString(element, state);
        break;
      case AlphaOperator:
        processAlphaOperator(element, state);
        break;
      case Dot:
      case Semicolon:
      case PointTo:
        processUnspacedSymbol(element, state);
        break;
      case Comma:
        processComma(element, state);
        break;
      case Comment:
        processComment(element, state);
        break;
      case ControlFlowBlock:
        processControlFlowBlock(element, state);
        break;
      case ControlFlowStarter:
        processControlFlowStarter(element, state);
        break;
      case ControlFlowEnd:
        processControlFlowEnd(element, state);
        break;
      case ControlFlowClause:
        processSqlNodeList(element.getChildNodes(), state);
        break;
      case BranchContentBody:
        processBranchContentBody(element, state);
        break;
      case CursorForBlock:
        processCursorForBlock(element, state);
        break;
    } 
  }
  
  private void processBranchContentBody(Element branchContentBodyElement, SqlFormatState state) {
    state.increaseIndent();
    processSqlNodeList(branchContentBodyElement.getChildNodes(), state);
    state.decreaseIndent();
  }
  
  private void processCursorForBlock(Element element, SqlFormatState state) {
    state.expectLineBreak = true;
    NodeList children = element.getChildNodes();
    int length = children.getLength();
    for (int i = 0; i < length; i++) {
      Node child = children.item(i);
      if (child instanceof Element)
        if (SqlParserUtil.elementNameMatches((Element)child, SqlNodeType.ContainerContentBody)) {
          state.increaseIndent();
          processElement((Element)child, state);
          state.decreaseIndent();
        } else {
          processElement((Element)child, state);
        }  
    } 
  }
  
  private void processCursorForOptions(Element element, SqlFormatState state) {}
  
  private void processIndexHint(Element indexHintElement, SqlFormatState state) {
    if (this.options.getExpandIndexHintList()) {
      state.expectLineBreak = true;
      state.increaseIndent();
    } 
    processSqlNodeList(indexHintElement.getChildNodes(), state);
    if (this.options.getExpandIndexHintList())
      state.decreaseIndent(); 
  }
  
  private void processCase(Element caseElement, SqlFormatState state) {
    List<Element> childs = SqlParserUtil.getChildElements(caseElement);
    ListIterator<Element> childIterator = childs.listIterator();
    int containerOpenElementCount = 2;
    while (containerOpenElementCount > 0) {
      processElement(childIterator.next(), state);
      containerOpenElementCount--;
    } 
    state.increaseIndent();
    Element child = null;
    boolean ended = false;
    while (childIterator.hasNext()) {
      child = childIterator.next();
      if (SqlParserUtil.isContainerClose(child)) {
        ended = true;
        break;
      } 
      processElement(child, state);
    } 
    state.decreaseIndent();
    if (ended) {
      state.expectLineBreak = true;
      processElement(child, state);
    } 
  }
  
  private void processExpandableParens(Element expandableParenElement, SqlFormatState state) {
    separateWords(state);
    state.addOutputString("(");
    state.increaseIndent();
    SqlFormatState innerState = new SqlFormatState(state);
    processSqlNodeList(expandableParenElement.getChildNodes(), innerState);
    if (innerState.outputStartsWithComment()) {
      state.addOutputString(innerState.getOutput());
      state.decreaseIndent();
      if (innerState.expectLineBreak || innerState.outputContainsLineBreak()) {
        state.expectLineBreak = true;
        breakAsExpected(state);
      } 
    } else if (innerState.expectLineBreak || innerState.outputContainsLineBreak()) {
      state.expectLineBreak = true;
      breakAsExpected(state);
      state.addOutputString(innerState.getOutput());
      state.decreaseIndent();
      state.expectLineBreak = true;
      breakAsExpected(state);
    } else {
      state.addOutputString(innerState.getOutput());
      state.decreaseIndent();
    } 
    boolean parenClosed = Boolean.parseBoolean(expandableParenElement.getAttribute("closed"));
    if (parenClosed)
      state.addOutputString(")"); 
    state.expectWordSeperator = true;
  }
  
  private void processUnexpandableBraces(Element expandableParenElement, SqlFormatState state) {
    separateWords(state);
    state.increaseIndent();
    SqlFormatState innerState = new SqlFormatState(state);
    processSqlNodeList(expandableParenElement.getChildNodes(), innerState);
    state.addOutputString(innerState.getOutput());
    state.decreaseIndent();
    state.expectWordSeperator = true;
  }
  
  private void processOtherKeyword(Element otherKeywordElement, SqlFormatState state) {
    separateWords(state);
    state.addOutputString(formatSqlKeyword(otherKeywordElement.getTextContent()));
    state.expectWordSeperator = true;
  }
  
  private void processSetOperatorClause(Element setOperatorClasueElement, SqlFormatState state) {
    processSqlNodeList(setOperatorClasueElement.getChildNodes(), state);
  }
  
  private void processSQLClause(Element sqlClauseElement, SqlFormatState state) {
    List<Element> children = SqlParserUtil.getChildElements(sqlClauseElement);
    ListIterator<Element> childrenIterator = children.listIterator();
    Element child = null;
    boolean starterKeywordsHandled = false;
    if (!state.outputIsEmpty() && !state.outputEndWithLineBreak())
      state.expectLineBreak = true; 
    while (childrenIterator.hasNext()) {
      child = childrenIterator.next();
      if (SqlNodeTypeUtil.sameTagName(child, SqlNodeType.OtherKeyword) || SqlNodeTypeUtil.sameTagName(child, SqlNodeType.CompoundKeyword)) {
        processElement(child, state);
        starterKeywordsHandled = true;
        break;
      } 
      if (SqlNodeTypeUtil.sameTagName(child, SqlNodeType.WhiteSpace) || SqlNodeTypeUtil.sameTagName(child, SqlNodeType.Comment)) {
        processElement(child, state);
        continue;
      } 
      childrenIterator.previous();
    } 
    if (starterKeywordsHandled) {
      state.increaseIndent();
      if (this.options.isBreakAfterClauseKeyword())
        state.expectLineBreak = true; 
    } 
    while (childrenIterator.hasNext()) {
      child = childrenIterator.next();
      processElement(child, state);
    } 
    if (starterKeywordsHandled)
      state.decreaseIndent(); 
    state.expectLineBreak = true;
  }
  
  private void processSqlNodeList(NodeList nodeList, SqlFormatState state) {
    int length = nodeList.getLength();
    for (int i = 0; i < length; i++) {
      Node node = nodeList.item(i);
      if (node instanceof Element)
        processElement((Element)node, state); 
    } 
  }
  
  private void processSQLStatement(Element sqlStatementElement, SqlFormatState state) {
    if (state.expectStatementBreak && !SqlParserUtil.isEmptyStatement(sqlStatementElement))
      for (int i = 0; i < this.options.getStatementBreaks(); i++)
        state.addOutputLineBreak();  
    state.expectLineBreak = false;
    state.expectWordSeperator = false;
    processSqlNodeList(sqlStatementElement.getChildNodes(), state);
    state.expectStatementBreak = true;
  }
  
  private void processUnexpandableParens(Element unexpandableParenElement, SqlFormatState state) {
    state.expectWordSeperator = false;
    breakAsExpected(state);
    state.addOutputString("(");
    processSqlNodeList(unexpandableParenElement.getChildNodes(), state);
    breakAsExpected(state);
    boolean parenClosed = Boolean.parseBoolean(unexpandableParenElement.getAttribute("closed"));
    if (parenClosed)
      state.addOutputString(")"); 
    state.expectWordSeperator = true;
  }
  
  private void processUnspacedSymbol(Element element, SqlFormatState state) {
    state.expectWordSeperator = false;
    state.expectLineBreak = false;
    separateWords(state);
    state.addOutputString(element.getTextContent());
  }
  
  private void processComment(Element element, SqlFormatState state) {
    Boolean multipleLine = Boolean.valueOf(element.getAttribute("multipleLine"));
    Boolean precedingByBR = Boolean.valueOf(element.getAttribute("precedingByBR"));
    Boolean followingByBR = Boolean.valueOf(element.getAttribute("followingByBR"));
    String formattedComment = formatComment(element.getTextContent(), state.getIndentLevel());
    if (multipleLine.booleanValue()) {
      separateComment(state, precedingByBR, (Element)element.getParentNode());
      state.addOutputString(formattedComment);
      if (followingByBR.booleanValue()) {
        state.expectLineBreak = true;
      } else if (SqlParserUtil.isClauseElement(SqlParserUtil.getNextNonWSSiblingElement(element))) {
        state.expectLineBreak = true;
      } 
    } else {
      separateComment(state, precedingByBR, (Element)element.getParentNode());
      state.addOutputString(formattedComment);
      state.expectLineBreak = true;
    } 
  }
  
  private void separateComment(SqlFormatState state, Boolean precedingByBR, Element parent) {
    if (state.outputIsEmpty()) {
      if (!SqlParserUtil.isStatementElement(parent))
        if (precedingByBR.booleanValue()) {
          state.expectLineBreak = true;
          breakAsExpected(state);
        } else {
          state.addOutputWhiteSpace();
        }  
    } else if (precedingByBR.booleanValue()) {
      if (state.outputEndWithLineBreak()) {
        state.addOutputIndents();
      } else {
        state.expectLineBreak = true;
        breakAsExpected(state);
      } 
    } else {
      state.addOutputWhiteSpace();
    } 
  }
  
  private void processControlFlowBlock(Element element, SqlFormatState state) {
    boolean shouldIncreaseIndent = (!SqlParserUtil.isControlFlowClause((Element)element.getParentNode()) || !SqlParserUtil.isBeginEndControlFlowBlock(element));
    if (shouldIncreaseIndent)
      state.increaseIndent(); 
    if (!state.outputIsEmpty()) {
      if (!state.outputEndWithLineBreak())
        state.addOutputLineBreak(); 
      state.addOutputIndents(state.getIndentLevel() - 1);
    } 
    SqlFormatState innerState = new SqlFormatState(state);
    processSqlNodeList(element.getChildNodes(), innerState);
    state.addOutputString(innerState.getOutput());
    if (shouldIncreaseIndent)
      state.decreaseIndent(); 
    state.expectWordSeperator = true;
    state.expectLineBreak = true;
  }
  
  private void processControlFlowStarter(Element element, SqlFormatState state) {
    state.decreaseIndent();
    processSqlNodeList(element.getChildNodes(), state);
    state.increaseIndent();
  }
  
  private void processControlFlowEnd(Element element, SqlFormatState state) {
    state.decreaseIndent();
    state.expectLineBreak = true;
    processSqlNodeList(element.getChildNodes(), state);
    state.increaseIndent();
  }
  
  private void processAlphaOperator(Element element, SqlFormatState state) {
    separateWords(state);
    state.addOutputString(formatSqlKeyword(element.getTextContent()));
    state.expectWordSeperator = true;
  }
  
  private void processWord(Element element, SqlFormatState state) {
    separateWords(state);
    state.addOutputString(element.getTextContent());
    state.expectWordSeperator = true;
  }
  
  private void processBinaryValue(Element element, SqlFormatState state) {
    separateWords(state);
    String content = element.getTextContent();
    if (content.charAt(0) == '+' || content.charAt(0) == '-')
      content = content.substring(0, 1) + " " + content.substring(1); 
    state.addOutputString(content);
    state.expectWordSeperator = true;
  }
  
  private void processNumber(Element element, SqlFormatState state) {
    separateWords(state);
    String content = element.getTextContent();
    if (content.charAt(0) == '+' || content.charAt(0) == '-') {
      Element previousMeaningfulSibling = SqlParserUtil.findPreNonWSNonCmtSiblingElement(element);
      if (isPlusOrSubtractionOperator(previousMeaningfulSibling))
        content = content.substring(0, 1) + " " + content.substring(1); 
    } 
    state.addOutputString(content);
    state.expectWordSeperator = true;
  }
  
  private boolean isPlusOrSubtractionOperator(Element previousMeaningfulSibling) {
    if (previousMeaningfulSibling == null)
      return false; 
    if (SqlParserUtil.elementNameMatches(previousMeaningfulSibling, SqlNodeType.Number))
      return true; 
    if (SqlParserUtil.elementNameMatches(previousMeaningfulSibling, SqlNodeType.Identifier))
      return true; 
    if (SqlParserUtil.elementNameMatches(previousMeaningfulSibling, SqlNodeType.DatetimeString))
      return true; 
    if (SqlParserUtil.elementNameMatches(previousMeaningfulSibling, SqlNodeType.FunctionParens) || SqlParserUtil.elementNameMatches(previousMeaningfulSibling, SqlNodeType.ExpressionParens))
      return true; 
    if (SqlParserUtil.elementNameMatches(previousMeaningfulSibling, SqlNodeType.OtherKeyword) && "NULL".equalsIgnoreCase(previousMeaningfulSibling.getTextContent()))
      return true; 
    return false;
  }
  
  private void processDatetimeString(Element element, SqlFormatState state) {
    separateWords(state);
    String content = element.getTextContent();
    Pattern pattern = Pattern.compile("'|\"");
    Matcher matcher = pattern.matcher(content);
    if (matcher.find()) {
      String contentPrefix = content.substring(0, matcher.start());
      content = formatSqlKeyword(contentPrefix) + content.substring(matcher.start());
    } 
    state.addOutputString(content);
    state.expectWordSeperator = true;
  }
  
  private void processBrace(Element element, SqlFormatState state) {
    separateWords(state);
    if (SqlNodeType.OpenBrace.toString().equals(element.getNodeName())) {
      state.expectWordSeperator = false;
    } else {
      state.removeLastSpace();
      state.expectWordSeperator = true;
    } 
    state.addOutputString(element.getTextContent());
  }
  
  private void processBracket(Element element, SqlFormatState state) {
    separateWords(state);
    if (SqlNodeType.OpenBracket.toString().equals(element.getNodeName())) {
      state.expectWordSeperator = false;
    } else {
      state.removeLastSpace();
      state.expectWordSeperator = true;
    } 
    state.addOutputString(element.getTextContent());
  }
  
  private void processDDLDefinitionBlock(Element element, SqlFormatState state) {
    if (!state.outputIsEmpty() && !state.outputEndWithLineBreak())
      state.expectLineBreak = true; 
    processSqlNodeList(element.getChildNodes(), state);
  }
  
  private void processDDLContentBlock(Element element, SqlFormatState state) {
    state.increaseIndent();
    SqlFormatState innerState = new SqlFormatState(state);
    processSqlNodeList(element.getChildNodes(), innerState);
    if (innerState.outputStartsWithComment()) {
      state.addOutputString(innerState.getOutput());
      state.decreaseIndent();
      if (innerState.expectLineBreak || innerState.outputContainsLineBreak()) {
        state.expectLineBreak = true;
        state.expectWordSeperator = false;
      } 
    } else if (innerState.outputContainsLineBreak()) {
      state.expectLineBreak = true;
      breakAsExpected(state);
      state.addOutputString(innerState.getOutput());
      state.decreaseIndent();
      state.expectLineBreak = true;
      state.expectWordSeperator = false;
    } else {
      separateWords(state);
      state.addOutputString(innerState.getOutput());
      state.decreaseIndent();
      state.expectWordSeperator = true;
    } 
  }
  
  private void separateWords(SqlFormatState state) {
    if (state.expectLineBreak) {
      state.addOutputLineBreak();
      state.addOutputIndents();
      state.expectLineBreak = false;
    } else if (state.expectWordSeperator) {
      state.addOutputWhiteSpace();
    } 
    state.expectWordSeperator = false;
  }
}
