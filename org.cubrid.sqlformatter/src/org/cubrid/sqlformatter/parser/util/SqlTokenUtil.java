package org.cubrid.sqlformatter.parser.util;

import java_cup.runtime.Symbol;
import org.apache.commons.lang.StringUtils;
import org.cubrid.sqlformatter.lexer.ISqlTokenType;
import org.cubrid.sqlformatter.parser.containers.CompoundKeywordContainer;
import org.cubrid.sqlformatter.parser.containers.ControlFlowCompoundKeywordContainer;
import org.cubrid.sqlformatter.parser.containers.GeneralCompoundKeywordContainer;
import org.cubrid.sqlformatter.parser.containers.IndexHintCompoundKeywordContainer;
import org.cubrid.sqlformatter.parser.containers.JoinCompoundKeywordContainer;
import org.cubrid.sqlformatter.parser.mode.CompoundKeyword;

public class SqlTokenUtil implements ISqlTokenType {
  public static int INDEX_NOT_FOUND = -1;
  
  private static CompoundKeyword findCompoundKeyword(Symbol[] tokenList, int beginIndex, int maxKeywordCount, CompoundKeywordContainer container, boolean stopOnMatched) {
    StringBuilder keywords = new StringBuilder();
    String compoundKeyword = null;
    int lastKeywordIndex = INDEX_NOT_FOUND;
    int tokenListSize = tokenList.length;
    while (maxKeywordCount > 0 && beginIndex < tokenListSize) {
      Symbol token = tokenList[beginIndex];
      if (isWhitespace(token) || isComment(token)) {
        beginIndex++;
        continue;
      } 
      if (isSqlKeyword(token)) {
        keywords.append(token.value.toString().toUpperCase());
        String keyword = keywords.toString();
        if (container.contains(keyword)) {
          compoundKeyword = keyword;
          lastKeywordIndex = beginIndex;
          if (stopOnMatched)
            break; 
        } 
        keywords.append(" ");
        beginIndex++;
        maxKeywordCount--;
      } 
    } 
    return new CompoundKeyword(lastKeywordIndex, compoundKeyword);
  }
  
  public static CompoundKeyword findGeneralCompoundKeyword(Symbol[] tokenList, int beginIndex, int maxKeywordCount) {
    return findCompoundKeyword(tokenList, beginIndex, maxKeywordCount, (CompoundKeywordContainer)GeneralCompoundKeywordContainer.INSTANCE, false);
  }
  
  public static CompoundKeyword findJoinCompoundKeyword(Symbol[] tokenList, int beginIndex) {
    return findCompoundKeyword(tokenList, beginIndex, 4, (CompoundKeywordContainer)JoinCompoundKeywordContainer.INSTANCE, true);
  }
  
  public static CompoundKeyword findIndexHintCompoundKeyword(Symbol[] tokenList, int beginIndex) {
    return findCompoundKeyword(tokenList, beginIndex, 2, (CompoundKeywordContainer)IndexHintCompoundKeywordContainer.INSTANCE, true);
  }
  
  public static CompoundKeyword findControlFlowCompoundKeyword(Symbol[] tokenList, int beginIndex) {
    return findCompoundKeyword(tokenList, beginIndex, ControlFlowCompoundKeywordContainer.MAX_KEYWORD_COUNT, (CompoundKeywordContainer)ControlFlowCompoundKeywordContainer.INSTANCE, true);
  }
  
  public static int getNextTokenIndexExceptCommentAndWhitespace(Symbol[] tokenList, int start) {
    if (tokenList == null)
      return INDEX_NOT_FOUND; 
    int tokenListSize = tokenList.length;
    while (start < tokenListSize && (isWhitespace(tokenList[start]) || isComment(tokenList[start])))
      start++; 
    return (start >= tokenListSize) ? INDEX_NOT_FOUND : start;
  }
  
  public static Symbol getNextMeaningfulToken(Symbol[] tokenList, int start) {
    int nextMeaningfulIndex = getNextTokenIndexExceptCommentAndWhitespace(tokenList, start);
    return (nextMeaningfulIndex == INDEX_NOT_FOUND) ? null : tokenList[nextMeaningfulIndex];
  }
  
  public static int getPreviousTokenIndexExceptCommentAndWhitespace(Symbol[] tokenList, int start) {
    if (tokenList == null)
      return INDEX_NOT_FOUND; 
    while (start >= 0 && (isWhitespace(tokenList[start]) || isComment(tokenList[start])))
      start--; 
    return (start < 0) ? INDEX_NOT_FOUND : start;
  }
  
  public static boolean isComment(Symbol token) {
    int tokenType = token.sym;
    return ((tokenType & 0xF8000000) == 536870912);
  }
  
  public static boolean isSingleLineComment(Symbol token) {
    int tokenType = token.sym;
    return (tokenType == 536870914 || tokenType == 536870913);
  }
  
  public static boolean isSqlKeyword(Symbol token) {
    return ((token.sym & 0xF8000000) == 939524096);
  }
  
  public static boolean isStatementStarter(Symbol token) {
    return (isSqlKeyword(token) && (token.sym & 0x7C80000) == 4718592);
  }
  
  public static boolean isClauseStarter(Symbol token) {
    return (isSqlKeyword(token) && (token.sym & 0x7C40000) == 4456448);
  }
  
  public static boolean isDDLBlockStarter(Symbol token) {
    return (isSqlKeyword(token) && (token.sym & 0x7C20000) == 4325376);
  }
  
  public static boolean isDDLContentStarter(Symbol token) {
    return (isSqlKeyword(token) && (token.sym & 0x7C08000) == 4227072);
  }
  
  public static boolean isCompoundKeywordStarter(Symbol token) {
    return ((token.sym & 0xF8000000) == 939524096 && (token.sym & 0x100000) == 1048576);
  }
  
  public static boolean isControlFlowStarter(Symbol token) {
    return ((token.sym & 0xF8000000) == 939524096 && (token.sym & 0x7C07000) == 37752832);
  }
  
  public static boolean isControlFlowEnd(Symbol token) {
    return ((token.sym & 0xF8000000) == 939524096 && (token.sym & 0x7C07000) == 37756928);
  }
  
  public static boolean isTokenToStartDDLAsBlock(Symbol tokenAfterAsKeyword) {
    return (isStatementStarter(tokenAfterAsKeyword) || isClauseStarter(tokenAfterAsKeyword) || isControlFlowStarter(tokenAfterAsKeyword) || isToken(tokenAfterAsKeyword, 953155594) || isEquals(tokenAfterAsKeyword, "LANGUAGE"));
  }
  
  public static boolean isWhitespace(Symbol token) {
    return ((token.sym & 0xF8000000) == 402653184);
  }
  
  public static boolean isWhitespaceWithLinebreak(Symbol token) {
    if (!isWhitespace(token))
      return false; 
    String value = token.value.toString();
    return (value.contains("\r\n") || value.contains("\r") || value.contains("\n"));
  }
  
  public static boolean isComma(Symbol token) {
    return (token.sym == 805306369);
  }
  
  public static boolean isNumber(Symbol token) {
    return (token.sym == 134217729);
  }
  
  public static boolean isOther(Symbol token) {
    return (token.sym == -134217728);
  }
  
  public static boolean isSeparatorToken(Symbol token) {
    return (isWhitespace(token) || isPunctuation(token) || isBracket(token));
  }
  
  public static boolean isToken(Symbol token, int sqlTokenType) {
    return (token != null && token.sym == sqlTokenType);
  }
  
  private static boolean isPunctuation(Symbol token) {
    return ((token.sym & 0xF8000000) == 805306368);
  }
  
  private static boolean isBracket(Symbol token) {
    return ((token.sym & 0xF8000000) == 268435456);
  }
  
  private static boolean isOtherOperator(Symbol token) {
    return (token.sym == -268431360);
  }
  
  public static boolean isEquals(Symbol token, String value) {
    return (token != null && token.value != null && StringUtils.equalsIgnoreCase(token.value.toString(), value));
  }
  
  public static boolean nextTokenExceptCommentAndWhitespaceMatches(Symbol[] tokenList, int start, int tokenType) {
    int nextIndex = getNextTokenIndexExceptCommentAndWhitespace(tokenList, start);
    return (nextIndex != INDEX_NOT_FOUND && (tokenList[nextIndex]).sym == tokenType);
  }
  
  public static boolean nextTokenExceptCommentAndWhitespaceEqualsIgnoreCase(Symbol[] tokenList, int start, String tokenValue) {
    int nextIndex = getNextTokenIndexExceptCommentAndWhitespace(tokenList, start);
    return (nextIndex != INDEX_NOT_FOUND && StringUtils.equalsIgnoreCase((String)(tokenList[nextIndex]).value, tokenValue));
  }
  
  public static boolean previousTokenExceptCommentAndWhitespaceEqualsIgnoreCase(Symbol[] tokenList, int start, String tokenValue) {
    int nextIndex = getPreviousTokenIndexExceptCommentAndWhitespace(tokenList, start);
    return (nextIndex != INDEX_NOT_FOUND && StringUtils.equalsIgnoreCase((String)(tokenList[nextIndex]).value, tokenValue));
  }
}
