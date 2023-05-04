package org.cubrid.sqlformatter.parser.containers;

import java.util.HashSet;
import org.apache.commons.lang.StringUtils;

public class DDLContentStarterKeywordContainer {
  private HashSet<String> ddlContentCompoundStarterKeywordContainer = new HashSet<String>();
  
  public static DDLContentStarterKeywordContainer INSTANCE = new DDLContentStarterKeywordContainer();
  
  private DDLContentStarterKeywordContainer() {
    this.ddlContentCompoundStarterKeywordContainer.add("CLASS ATTRIBUTE");
    this.ddlContentCompoundStarterKeywordContainer.add("UNDER");
    this.ddlContentCompoundStarterKeywordContainer.add("ATTRIBUTE");
    this.ddlContentCompoundStarterKeywordContainer.add("INHERIT");
    this.ddlContentCompoundStarterKeywordContainer.add("COLUMN");
    this.ddlContentCompoundStarterKeywordContainer.add("SUPERCLASS");
    this.ddlContentCompoundStarterKeywordContainer.add("QUERY");
    this.ddlContentCompoundStarterKeywordContainer.add("ADD");
  }
  
  public boolean contains(String keyword) {
    if (StringUtils.isEmpty(keyword))
      return false; 
    return this.ddlContentCompoundStarterKeywordContainer.contains(keyword.toUpperCase());
  }
}
