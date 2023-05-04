package org.cubrid.sqlformatter;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import java_cup.runtime.Symbol;
import org.cubrid.sqlformatter.formatter.SqlFormatter;
import org.cubrid.sqlformatter.lexer.SqlLexer;
import org.cubrid.sqlformatter.parser.SqlParser;
import org.cubrid.sqlformatter.preprocessor.SqlPreprocessor;
import org.w3c.dom.Document;

public class SqlFormatManager {
  public String format(String sql) {
    return format(sql, new SqlFormatOptions());
  }
  
  public String format(String sql, SqlFormatOptions formatOptions) {
    Reader reader = new StringReader(sql);
    SqlLexer lexer = new SqlLexer(reader);
    try {
      Symbol[] tokenList = lexer.getTokenList();
      SqlPreprocessor preprocessor = new SqlPreprocessor();
      tokenList = preprocessor.preprocess(tokenList);
      SqlParser parser = new SqlParser();
      Document sqlParseTree = parser.parseSQL(tokenList);
      if (sqlParseTree == null)
        return sql; 
      SqlFormatter formatter = new SqlFormatter(formatOptions);
      return formatter.formatSqlParseTree(sqlParseTree);
    } catch (IOException e) {
      e.printStackTrace();
      return sql;
    } 
  }
}
