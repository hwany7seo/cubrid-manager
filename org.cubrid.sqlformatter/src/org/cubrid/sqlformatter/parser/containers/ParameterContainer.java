package org.cubrid.sqlformatter.parser.containers;

import java.util.TreeSet;
import org.apache.commons.lang.StringUtils;
import org.cubrid.sqlformatter.parser.constants.RegexPatterns;

public class ParameterContainer {
  private TreeSet<String> parameterContainer = new TreeSet<String>();
  
  public static ParameterContainer INSTANCE = new ParameterContainer();
  
  private ParameterContainer() {
    this.parameterContainer.add("PCTFREE");
    this.parameterContainer.add("TABLESPACE");
    this.parameterContainer.add("PCTUSED");
    this.parameterContainer.add("INITRANS");
    this.parameterContainer.add("STORAGE");
    this.parameterContainer.add("INITIAL");
    this.parameterContainer.add("MINEXTENTS");
    this.parameterContainer.add("MAXEXTENTS");
    this.parameterContainer.add("MAXSIZE");
    this.parameterContainer.add("PCTINCREASE");
    this.parameterContainer.add("FREELISTS");
    this.parameterContainer.add("OPTIMAL");
    this.parameterContainer.add("BUFFER_POOL");
    this.parameterContainer.add("RECYCLE");
    this.parameterContainer.add("ENCRYPT");
    this.parameterContainer.add("LOGGING");
    this.parameterContainer.add("NOLOGGING");
    this.parameterContainer.add("FILESYSTEM_LIKE_LOGGING");
    this.parameterContainer.add("COMPRESS");
    this.parameterContainer.add("NOCOMPRESS");
    this.parameterContainer.add("OVERFLOW");
    this.parameterContainer.add("LOB");
    this.parameterContainer.add("CHUNK");
    this.parameterContainer.add("PCTVERSION");
    this.parameterContainer.add("FREEPOOLS");
    this.parameterContainer.add("RETENTION");
    this.parameterContainer.add("DEDUPLICATE");
    this.parameterContainer.add("KEEP_DUPLICATES");
    this.parameterContainer.add("DECRYPT");
    this.parameterContainer.add("CACHE");
    this.parameterContainer.add("NOCACHE");
    this.parameterContainer.add("VARRAY");
    this.parameterContainer.add("SUBPARTITIONS");
    this.parameterContainer.add("DEFAULT DIRECTORY");
    this.parameterContainer.add("ACCESS PARAMETERS");
    this.parameterContainer.add("LOCATION");
  }
  
  public boolean contains(String keyword) {
    if (StringUtils.isEmpty(keyword))
      return false; 
    String ucKeyword = keyword.toUpperCase();
    return (this.parameterContainer.contains(ucKeyword) || RegexPatterns.ORACLE_STORAGE_COMPOUND.matcher(ucKeyword).matches());
  }
}
