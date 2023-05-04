package org.cubrid.sqlformatter.parser.handlers;

import java_cup.runtime.Symbol;
import org.cubrid.sqlformatter.parser.SqlParseTree;

public interface SqlTokenHandler {
  int handleToken(Symbol[] paramArrayOfSymbol, int paramInt, SqlParseTree paramSqlParseTree);
}
