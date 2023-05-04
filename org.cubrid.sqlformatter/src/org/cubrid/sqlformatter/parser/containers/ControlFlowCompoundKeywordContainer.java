package org.cubrid.sqlformatter.parser.containers;

import org.apache.commons.lang.StringUtils;
import org.cubrid.sqlformatter.parser.constants.RegexPatterns;

public class ControlFlowCompoundKeywordContainer implements CompoundKeywordContainer {
  public static ControlFlowCompoundKeywordContainer INSTANCE = new ControlFlowCompoundKeywordContainer();
  
  public static int MAX_KEYWORD_COUNT = 2;
  
  public boolean contains(String compoundKeyword) {
    if (StringUtils.isEmpty(compoundKeyword))
      return false; 
    return RegexPatterns.SQLSERVER_CONTROL_FLOW_COMPOUND.matcher(compoundKeyword).matches();
  }
}
