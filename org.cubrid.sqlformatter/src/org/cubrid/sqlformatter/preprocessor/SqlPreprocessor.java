package org.cubrid.sqlformatter.preprocessor;

import java.util.ArrayList;
import java.util.List;
import java_cup.runtime.Symbol;
import org.cubrid.sqlformatter.parser.util.SqlTokenUtil;

public class SqlPreprocessor {
  public Symbol[] preprocess(Symbol[] tokens) {
    preprocessUnknownTokens(tokens);
    return eliminateNulls(tokens);
  }
  
  private void preprocessUnknownTokens(Symbol[] tokens) {
    int mergeIndex = 0, currentIndex = 0, separatorIndex = -1;
    boolean unknownTokenExist = false;
    while (currentIndex < tokens.length && tokens[currentIndex] != null) {
      Symbol symbol = tokens[currentIndex];
      if (SqlTokenUtil.isSeparatorToken(symbol)) {
        if (separatorIndex + 1 < currentIndex)
          mergeIndex = processTokensBetweenSeparator(tokens, separatorIndex + 1, currentIndex - 1, unknownTokenExist, mergeIndex); 
        tokens[mergeIndex++] = tokens[currentIndex];
        separatorIndex = currentIndex;
        unknownTokenExist = false;
      } else if (SqlTokenUtil.isOther(symbol)) {
        unknownTokenExist = true;
      } 
      currentIndex++;
    } 
    if (separatorIndex < tokens.length - 1)
      mergeIndex = processTokensBetweenSeparator(tokens, separatorIndex + 1, tokens.length - 1, unknownTokenExist, mergeIndex); 
    while (mergeIndex < tokens.length)
      tokens[mergeIndex++] = null; 
  }
  
  private int processTokensBetweenSeparator(Symbol[] tokens, int beginIndex, int endIndex, boolean unknownTokenExist, int targetBeginIndex) {
    if (!unknownTokenExist) {
      leftShift(tokens, beginIndex, endIndex, targetBeginIndex);
      targetBeginIndex += endIndex - beginIndex + 1;
    } else {
      Symbol mergedUnknownToken = mergeUnknownTokens(tokens, beginIndex, endIndex);
      tokens[targetBeginIndex++] = mergedUnknownToken;
    } 
    return targetBeginIndex;
  }
  
  private void leftShift(Symbol[] tokens, int sourceBegin, int sourceEnd, int targetBegin) {
    if (targetBegin == sourceBegin)
      return; 
    if (targetBegin < sourceBegin)
      while (sourceBegin <= sourceEnd)
        tokens[targetBegin++] = tokens[sourceBegin++];  
  }
  
  private Symbol mergeUnknownTokens(Symbol[] tokens, int begin, int end) {
    if (begin == end)
      return tokens[begin]; 
    StringBuilder tokenValue = new StringBuilder();
    while (begin <= end) {
      tokenValue.append((tokens[begin]).value.toString());
      begin++;
    } 
    Symbol mergedUnknownToken = new Symbol(-134217728, tokenValue.toString());
    return mergedUnknownToken;
  }
  
  private Symbol[] eliminateNulls(Symbol[] tokens) {
    List<Symbol> tokensAfterEliminateNulls = new ArrayList<Symbol>();
    int i = 0;
    while (i < tokens.length && tokens[i] != null) {
      tokensAfterEliminateNulls.add(tokens[i]);
      i++;
    } 
    return tokensAfterEliminateNulls.<Symbol>toArray(new Symbol[0]);
  }
}
