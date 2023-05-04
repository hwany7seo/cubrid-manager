package org.cubrid.sqlformatter.parser;

import java_cup.runtime.Symbol;

import org.cubrid.sqlformatter.parser.handlers.SqlTokenHandler;
import org.w3c.dom.Document;

public class SqlParser {
  public Document parseSQL(Symbol[] tokenList) {
    SqlParseTree sqlParseTree;
    sqlParseTree = new SqlParseTree(); 
    int index = 0, length = tokenList.length;
    System.out.println("length : " + length);
    while (index < length) {
      SqlTokenHandler tokenHandler = SqlTokenHandlerFactory.newSqlTokenHandler((tokenList[index]).sym);
      index = tokenHandler.handleToken(tokenList, index, sqlParseTree);
    } 
    return sqlParseTree.getDoc();
  }
}
