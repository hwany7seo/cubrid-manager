package org.cubrid.sqlformatter.lexer;

import java.io.IOException;
import java_cup.runtime.Symbol;

public interface ISqlTokenizer {
  Symbol[] getTokenList() throws IOException;
}
