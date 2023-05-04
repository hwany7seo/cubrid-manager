package org.cubrid.sqlformatter.parser.handlers;

import java_cup.runtime.Symbol;
import org.cubrid.sqlformatter.parser.SqlParseTree;
import org.cubrid.sqlformatter.parser.constants.SqlNodeType;
import org.cubrid.sqlformatter.parser.util.SqlParserUtil;

public class CommandTokenHandler implements SqlTokenHandler {
  public int handleToken(Symbol[] tokenList, int currentIndex, SqlParseTree sqlParseTree) {
    if (!sqlParseTree.ancestorNameMatches(0, SqlNodeType.SqlClause) || !sqlParseTree.ancestorNameMatches(1, SqlNodeType.SqlStatement) || SqlParserUtil.hasNonWhiteSNonCommentChildElement(sqlParseTree.getCurrentContainer()))
      sqlParseTree.startNewStatementInSqlRoot(); 
    String tokenValue = ((String)(tokenList[currentIndex]).value).trim();
    sqlParseTree.saveFinalElement(SqlNodeType.SessionCommand, tokenValue);
    return currentIndex + 1;
  }
}
