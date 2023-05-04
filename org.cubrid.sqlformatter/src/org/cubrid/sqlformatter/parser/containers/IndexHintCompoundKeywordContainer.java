package org.cubrid.sqlformatter.parser.containers;

import org.apache.commons.lang.StringUtils;
import org.cubrid.sqlformatter.parser.constants.RegexPatterns;

public class IndexHintCompoundKeywordContainer implements CompoundKeywordContainer {
  public static final IndexHintCompoundKeywordContainer INSTANCE = new IndexHintCompoundKeywordContainer();
  
  public boolean contains(String compoundKeyword) {
    if (StringUtils.isEmpty(compoundKeyword))
      return false; 
    return RegexPatterns.INDEX_HINT.matcher(compoundKeyword).matches();
  }
}
