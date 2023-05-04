package org.cubrid.sqlformatter.parser;

import org.cubrid.sqlformatter.parser.handlers.BracketTokenHandler;
import org.cubrid.sqlformatter.parser.handlers.CommandTokenHandler;
import org.cubrid.sqlformatter.parser.handlers.CommentTokenHandler;
import org.cubrid.sqlformatter.parser.handlers.SimpleTokenHandler;
import org.cubrid.sqlformatter.parser.handlers.SqlKeywordTokenHandler;
import org.cubrid.sqlformatter.parser.handlers.SqlTokenHandler;

public class SqlTokenHandlerFactory {
  private static final SimpleTokenHandler SIMPLE_TOKEN_HANDLER = new SimpleTokenHandler();
  
  private static final BracketTokenHandler BRACKET_TOKEN_HANDLER = new BracketTokenHandler();
  
  private static final SqlKeywordTokenHandler SQL_KEYWORD_TOKEN_HANDLER = new SqlKeywordTokenHandler();
  
  private static final CommentTokenHandler COMMENT_TOKEN_HANDLER = new CommentTokenHandler();
  
  private static final CommandTokenHandler COMMAND_TOKEN_HANDLER = new CommandTokenHandler();
  
  public static SqlTokenHandler newSqlTokenHandler(int tokenType) {
    if (tokenType == -134217728)
      return (SqlTokenHandler)SIMPLE_TOKEN_HANDLER; 
    int mainType = tokenType & 0xF8000000;
    switch (mainType) {
      case -268435456:
      case 134217728:
      case 402653184:
      case 671088640:
      case 805306368:
        return (SqlTokenHandler)SIMPLE_TOKEN_HANDLER;
      case 536870912:
        return (SqlTokenHandler)COMMENT_TOKEN_HANDLER;
      case 268435456:
        return (SqlTokenHandler)BRACKET_TOKEN_HANDLER;
      case 939524096:
        return (SqlTokenHandler)SQL_KEYWORD_TOKEN_HANDLER;
      case 1207959552:
        return (SqlTokenHandler)COMMAND_TOKEN_HANDLER;
    } 
    return null;
  }
}
