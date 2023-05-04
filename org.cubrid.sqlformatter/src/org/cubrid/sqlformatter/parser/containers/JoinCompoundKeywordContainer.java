package org.cubrid.sqlformatter.parser.containers;

import java.util.HashSet;
import org.apache.commons.lang.StringUtils;
import org.cubrid.sqlformatter.parser.constants.RegexPatterns;

public class JoinCompoundKeywordContainer implements CompoundKeywordContainer {
  private HashSet<String> joinCompoundKeywordContainer = new HashSet<String>();
  
  public static JoinCompoundKeywordContainer INSTANCE = new JoinCompoundKeywordContainer();
  
  public boolean contains(String compoundKeyword) {
    if (StringUtils.isEmpty(compoundKeyword))
      return false; 
    return (RegexPatterns.CUBRID_JOIN.matcher(compoundKeyword).matches() || RegexPatterns.MYSQL_JOIN.matcher(compoundKeyword).matches() || RegexPatterns.ORACLE_JOIN.matcher(compoundKeyword).matches());
  }
}
