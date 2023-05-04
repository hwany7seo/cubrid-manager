package org.cubrid.sqlformatter.parser.handlers;

import java_cup.runtime.Symbol;
import org.cubrid.sqlformatter.parser.SqlParseTree;
import org.cubrid.sqlformatter.parser.constants.SqlNodeType;
import org.cubrid.sqlformatter.parser.constants.SqlParserConstants;
import org.cubrid.sqlformatter.parser.util.SqlParserUtil;
import org.w3c.dom.Element;

public class BracketTokenHandler implements SqlTokenHandler, SqlParserConstants {
  private static final String trueValue = Boolean.TRUE.toString();
  
  private static final String falseValue = Boolean.FALSE.toString();
  
  private void handleOpenParen(SqlParseTree sqlParseTree, Symbol[] tokenList, int currentIndex) {
    SqlNodeType sqlNodeType;
    Element currentContainer = sqlParseTree.getCurrentContainer();
    Element firstNonWSNonCmtChildElement = SqlParserUtil.findFirstChildExceptCommentWhitespace(currentContainer);
    Element lastNonWSNonCmtChildElement = SqlParserUtil.findLastNonWSNonCmtChildElement(currentContainer);
    if (SqlParserUtil.isSuitableToAddSelectionTargetParens(currentContainer, firstNonWSNonCmtChildElement, lastNonWSNonCmtChildElement)) {
      sqlNodeType = SqlNodeType.SelectionTargetParens;
    } else if (SqlParserUtil.elementNameMatches(firstNonWSNonCmtChildElement, SqlNodeType.SetOperatorClause)) {
      sqlParseTree.considerStartNewClause();
      sqlNodeType = SqlNodeType.SelectionTargetParens;
    } else if (SqlParserUtil.isFunctionParenElement(lastNonWSNonCmtChildElement)) {
      sqlNodeType = SqlNodeType.FunctionParens;
    } else if (SqlParserUtil.elementNameMatches(lastNonWSNonCmtChildElement, SqlNodeType.AlphaOperator) && lastNonWSNonCmtChildElement.getTextContent().toUpperCase().equals("IN")) {
      sqlNodeType = SqlNodeType.InParens;
    } else if (SqlParserUtil.isSuitableToAddPartitionParen(sqlParseTree, tokenList, currentIndex)) {
      sqlParseTree.escapePartitionParameter();
      sqlNodeType = SqlNodeType.PartitionParen;
    } else if (SqlParserUtil.isSuitableToAddParameterParen(sqlParseTree, tokenList, currentIndex)) {
      sqlNodeType = SqlNodeType.ParameterParen;
    } else if (SqlParserUtil.isSuitableToAddDDLParens(currentContainer, firstNonWSNonCmtChildElement, lastNonWSNonCmtChildElement)) {
      sqlNodeType = SqlNodeType.DDLParens;
    } else if (SqlParserUtil.isDDLDetailParenKWElement(lastNonWSNonCmtChildElement)) {
      sqlNodeType = SqlNodeType.DDLDetailParens;
    } else if (SqlParserUtil.isPivotElement(lastNonWSNonCmtChildElement)) {
      sqlNodeType = SqlNodeType.PivotParens;
    } else if (SqlParserUtil.elementNameMatches(lastNonWSNonCmtChildElement, SqlNodeType.Identifier)) {
      Element preNonWSNonCmtSiblingElement = SqlParserUtil.findPreNonWSNonCmtSiblingElement(lastNonWSNonCmtChildElement);
      if (SqlParserUtil.isDDLDetailParenKWElement(preNonWSNonCmtSiblingElement)) {
        sqlNodeType = SqlNodeType.DDLDetailParens;
      } else {
        sqlNodeType = SqlNodeType.FunctionParens;
      } 
    } else {
      sqlNodeType = SqlNodeType.ExpressionParens;
    } 
    sqlParseTree.saveContainerElement(sqlNodeType);
    currentContainer = sqlParseTree.getCurrentContainer();
    currentContainer.setAttribute("closed", falseValue);
  }
  
  private void handleCloseParen(SqlParseTree sqlParseTree) {
    sqlParseTree.escapeAnySingleOrPartialStatementContainers();
    sqlParseTree.escapePartitionParameter();
    Element currentElement = sqlParseTree.getCurrentContainer();
    SqlNodeType nodeType = SqlNodeType.valueOf(currentElement.getTagName());
    if (nodeType == SqlNodeType.SelectionTargetParens || nodeType == SqlNodeType.DDLParens || nodeType == SqlNodeType.DDLDetailParens || nodeType == SqlNodeType.ExpressionParens || nodeType == SqlNodeType.FunctionParens || nodeType == SqlNodeType.InParens || nodeType == SqlNodeType.PivotParens || nodeType == SqlNodeType.PartitionParen || nodeType == SqlNodeType.ParameterParen) {
      currentElement.setAttribute("closed", trueValue);
      sqlParseTree.moveToAncestorContainer(1);
    } else if (nodeType == SqlNodeType.SqlClause) {
      Element parentElement = (Element)currentElement.getParentNode();
      SqlNodeType parentNodeType = SqlNodeType.valueOf(parentElement.getTagName());
      if (parentNodeType == SqlNodeType.SelectionTargetParens || parentNodeType == SqlNodeType.InParens || parentNodeType == SqlNodeType.ExpressionParens || parentNodeType == SqlNodeType.PivotParens) {
        parentElement.setAttribute("closed", trueValue);
        sqlParseTree.moveToAncestorContainer(2);
      } else {
        sqlParseTree.saveFinalElement(SqlNodeType.Other, ")");
      } 
    } else {
      sqlParseTree.saveFinalElement(SqlNodeType.Other, ")");
    } 
  }
  
  private void handleOpenBrace(SqlParseTree sqlParseTree) {
    sqlParseTree.saveContainerElement(SqlNodeType.InBraces);
    sqlParseTree.saveFinalElement(SqlNodeType.OpenBrace, "{");
  }
  
  private void handleCloseBrace(SqlParseTree sqlParseTree) {
    sqlParseTree.saveFinalElement(SqlNodeType.CloseBrace, "}");
    Element currentElement = sqlParseTree.getCurrentContainer();
    SqlNodeType nodeType = SqlNodeType.valueOf(currentElement.getTagName());
    if (nodeType == SqlNodeType.InBraces)
      sqlParseTree.moveToAncestorContainer(1); 
  }
  
  private void handleOpenBracket(SqlParseTree sqlParseTree) {
    sqlParseTree.saveFinalElement(SqlNodeType.OpenBracket, "[");
  }
  
  private void handleCloseBracket(SqlParseTree sqlParseTree) {
    sqlParseTree.saveFinalElement(SqlNodeType.CloseBracket, "]");
  }
  
  public int handleToken(Symbol[] tokenList, int currentIndex, SqlParseTree sqlParseTree) {
    Symbol token = tokenList[currentIndex];
    switch (token.sym) {
      case 268435457:
        handleOpenParen(sqlParseTree, tokenList, currentIndex);
        break;
      case 268435458:
        handleCloseParen(sqlParseTree);
        break;
      case 268435459:
        handleOpenBrace(sqlParseTree);
        break;
      case 268435460:
        handleCloseBrace(sqlParseTree);
        break;
      case 268435461:
        handleOpenBracket(sqlParseTree);
        break;
      case 268435462:
        handleCloseBracket(sqlParseTree);
        break;
    } 
    return currentIndex + 1;
  }
}
