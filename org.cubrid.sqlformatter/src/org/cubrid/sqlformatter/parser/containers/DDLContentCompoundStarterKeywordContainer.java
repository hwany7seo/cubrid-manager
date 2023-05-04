package org.cubrid.sqlformatter.parser.containers;

import java.util.HashSet;
import org.apache.commons.lang.StringUtils;

public class DDLContentCompoundStarterKeywordContainer {
  private HashSet<String> ddlContentCompoundStarterKeywordContainer = new HashSet<String>();
  
  public static DDLContentCompoundStarterKeywordContainer INSTANCE = new DDLContentCompoundStarterKeywordContainer();
  
  private DDLContentCompoundStarterKeywordContainer() {
    this.ddlContentCompoundStarterKeywordContainer.add("CLASS ATTRIBUTE");
  }
  
  public boolean contains(String keyword) {
    if (StringUtils.isEmpty(keyword))
      return false; 
    return this.ddlContentCompoundStarterKeywordContainer.contains(keyword.toUpperCase());
  }
}
