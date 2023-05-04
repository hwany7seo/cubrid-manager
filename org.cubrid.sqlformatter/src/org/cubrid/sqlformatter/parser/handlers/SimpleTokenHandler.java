package org.cubrid.sqlformatter.parser.handlers;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java_cup.runtime.Symbol;
import org.cubrid.sqlformatter.parser.SqlParseTree;
import org.cubrid.sqlformatter.parser.SqlTokenHandlerFactory;
import org.cubrid.sqlformatter.parser.constants.SqlNodeType;
import org.cubrid.sqlformatter.parser.mode.CompoundKeyword;
import org.cubrid.sqlformatter.parser.util.SqlParserUtil;
import org.cubrid.sqlformatter.parser.util.SqlTokenUtil;
import org.w3c.dom.Element;

public class SimpleTokenHandler implements SqlTokenHandler {
  private ElementNameResolver resolver = null;
  
  public SimpleTokenHandler() {
    this.resolver = new ElementNameResolver();
  }
  
  public int handleToken(Symbol[] tokenList, int currentIndex, SqlParseTree sqlParseTree) {
    Symbol token = tokenList[currentIndex];
    boolean newSelectionTarget = false;
    if (token.sym == 805306369) {
      Element currentContainer = sqlParseTree.getCurrentContainer();
      if (SqlParserUtil.isIndexHint(sqlParseTree.getCurrentContainer())) {
        CompoundKeyword compoundKeyword = SqlTokenUtil.findIndexHintCompoundKeyword(tokenList, currentIndex + 1);
        if (compoundKeyword.getTokenEndIndex() == SqlTokenUtil.INDEX_NOT_FOUND)
          sqlParseTree.escapeIndexHintList(); 
      } else if (SqlParserUtil.isPartitionParameter(currentContainer)) {
        sqlParseTree.escapePartitionParameter();
      } 
      sqlParseTree.escapeAnyBetweenCondition();
      newSelectionTarget = (newSelectionTarget || sqlParseTree.escapeJoinOnSection());
      newSelectionTarget = (newSelectionTarget || sqlParseTree.escapeAnySelectionTarget());
    } 
    SqlNodeType nodeType = null;
    String elementName = this.resolver.getElementName(token.sym);
    if (elementName == null) {
      nodeType = SqlNodeType.Other;
    } else {
      nodeType = SqlNodeType.valueOf(elementName);
    } 
    String tokenString = (String)token.value;
    if (nodeType == SqlNodeType.String) {
      Pattern pattern = Pattern.compile("'|\"");
      Matcher matcher = pattern.matcher(tokenString);
      if (matcher.find()) {
        String stringPrefix = tokenString.substring(0, matcher.start());
        if ("DATE".equalsIgnoreCase(stringPrefix) || "TIME".equalsIgnoreCase(stringPrefix) || "TIMESTAMP".equalsIgnoreCase(stringPrefix) || "DATETIME".equalsIgnoreCase(stringPrefix))
          nodeType = SqlNodeType.DatetimeString; 
      } 
    } 
    sqlParseTree.saveFinalElement(nodeType, tokenString);
    if (newSelectionTarget) {
      sqlParseTree.saveContainerElement(SqlNodeType.SelectionTarget);
    } else if (token.sym == 805306371) {
      int next = SqlTokenUtil.getNextTokenIndexExceptCommentAndWhitespace(tokenList, currentIndex + 1);
      int nextToken = 0;
      if (next != SqlTokenUtil.INDEX_NOT_FOUND)
        nextToken = (tokenList[next]).sym; 
      currentIndex = handleWhitespaceCommentsAtSameLine(tokenList, currentIndex + 1, sqlParseTree);
      currentIndex--;
      Element container = jumpContainerSeperatedBySemiColon(sqlParseTree, nextToken);
      if (SqlParserUtil.elementNameMatches(container, SqlNodeType.SqlRoot))
        sqlParseTree.startNewStatementInSqlRoot(); 
    } 
    return currentIndex + 1;
  }
  
  private Element jumpContainerSeperatedBySemiColon(SqlParseTree sqlParseTree, int nextMeaningfulToken) {
    sqlParseTree.escapeIndexHintList();
    sqlParseTree.escapeJoinOnSection();
    sqlParseTree.escapeAnyBetweenCondition();
    sqlParseTree.escapeAnySelectionTarget();
    sqlParseTree.escapePartialStatementContainer();
    Element container = sqlParseTree.getCurrentContainer();
    SqlNodeType containerNodeType = SqlNodeType.valueOf(container.getTagName());
    boolean stop = false;
    while (!stop) {
      switch (containerNodeType) {
        case SqlStatement:
          container = (Element)container.getParentNode();
          containerNodeType = SqlNodeType.valueOf(container.getTagName());
          stop = true;
          continue;
        case ControlFlowClause:
          if (SqlParserUtil.isIfControlFlowClause(container)) {
            container = (Element)container.getParentNode();
            if (nextMeaningfulToken == 952107024) {
              stop = true;
            } else {
              container = (Element)container.getParentNode();
            } 
          } else {
            container = (Element)container.getParentNode().getParentNode();
          } 
          containerNodeType = SqlNodeType.valueOf(container.getTagName());
          continue;
        case ControlFlowBlock:
          if (SqlParserUtil.isControlFlowBlockStartWithBegin(container)) {
            stop = true;
            continue;
          } 
          container = (Element)container.getParentNode();
          containerNodeType = SqlNodeType.valueOf(container.getTagName());
          continue;
        case DDLAsBlock:
          if (SqlParserUtil.isDDLProcedureBlockElement((Element)container.getParentNode().getParentNode())) {
            stop = true;
            continue;
          } 
          container = (Element)container.getParentNode();
          containerNodeType = SqlNodeType.valueOf(container.getTagName());
          continue;
      } 
      container = (Element)container.getParentNode();
      containerNodeType = SqlNodeType.valueOf(container.getTagName());
    } 
    sqlParseTree.setCurrentContainer(container);
    return container;
  }
  
  private int handleWhitespaceCommentsAtSameLine(Symbol[] tokenList, int currentIndex, SqlParseTree sqlParseTree) {
    while (currentIndex < tokenList.length) {
      Symbol token = tokenList[currentIndex];
      if (SqlTokenUtil.isComment(token)) {
        SqlTokenHandler tokenHandler = SqlTokenHandlerFactory.newSqlTokenHandler(token.sym);
        currentIndex = tokenHandler.handleToken(tokenList, currentIndex, sqlParseTree);
        if (SqlTokenUtil.isSingleLineComment(token))
          break; 
        continue;
      } 
      if (SqlTokenUtil.isWhitespace(token)) {
        SqlTokenHandler tokenHandler = SqlTokenHandlerFactory.newSqlTokenHandler(token.sym);
        currentIndex = tokenHandler.handleToken(tokenList, currentIndex, sqlParseTree);
        if (SqlTokenUtil.isWhitespaceWithLinebreak(token))
          break; 
      } 
    } 
    return currentIndex;
  }
  
  class ElementNameResolver {
    private HashMap<Integer, String> tokenElementNames = new HashMap<Integer, String>();
    
    public ElementNameResolver() {
      this.tokenElementNames.put(Integer.valueOf(134217730), SqlNodeType.String.toString());
      this.tokenElementNames.put(Integer.valueOf(134217729), SqlNodeType.Number.toString());
      this.tokenElementNames.put(Integer.valueOf(134217731), SqlNodeType.MonetaryValue.toString());
      this.tokenElementNames.put(Integer.valueOf(134217732), SqlNodeType.BinaryValue.toString());
      this.tokenElementNames.put(Integer.valueOf(402653185), SqlNodeType.WhiteSpace.toString());
      this.tokenElementNames.put(Integer.valueOf(671088641), SqlNodeType.Identifier.toString());
      this.tokenElementNames.put(Integer.valueOf(805306369), SqlNodeType.Comma.toString());
      this.tokenElementNames.put(Integer.valueOf(805306370), SqlNodeType.Dot.toString());
      this.tokenElementNames.put(Integer.valueOf(805306371), SqlNodeType.Semicolon.toString());
      this.tokenElementNames.put(Integer.valueOf(-268431358), SqlNodeType.Asterisk.toString());
      this.tokenElementNames.put(Integer.valueOf(-268431360), SqlNodeType.OtherOperator.toString());
      this.tokenElementNames.put(Integer.valueOf(-268431359), SqlNodeType.PointTo.toString());
      this.tokenElementNames.put(Integer.valueOf(-134217728), SqlNodeType.Other.toString());
    }
    
    public String getElementName(int tokenType) {
      return this.tokenElementNames.get(Integer.valueOf(tokenType));
    }
  }
}
