package org.cubrid.sqlformatter.parser.containers;

import java.util.HashSet;
import org.apache.commons.lang.StringUtils;

public class DDLDetailParenKWContainer {
  private HashSet<String> ddlDetailParenKWs = new HashSet<String>();
  
  public static DDLDetailParenKWContainer INSTANCE = new DDLDetailParenKWContainer();
  
  private DDLDetailParenKWContainer() {
    this.ddlDetailParenKWs.add("NUMERIC");
    this.ddlDetailParenKWs.add("DECIMAL");
    this.ddlDetailParenKWs.add("FLOAT");
    this.ddlDetailParenKWs.add("REAL");
    this.ddlDetailParenKWs.add("BIT");
    this.ddlDetailParenKWs.add("BIT VARYING");
    this.ddlDetailParenKWs.add("CHAR");
    this.ddlDetailParenKWs.add("CHARACTER");
    this.ddlDetailParenKWs.add("VARCHAR");
    this.ddlDetailParenKWs.add("CHAR VARYING");
    this.ddlDetailParenKWs.add("CHARACTER_VARYING");
    this.ddlDetailParenKWs.add("NCHAR");
    this.ddlDetailParenKWs.add("NCHAR VARYING");
    this.ddlDetailParenKWs.add("ENUM");
    this.ddlDetailParenKWs.add("SET");
    this.ddlDetailParenKWs.add("MULTISET");
    this.ddlDetailParenKWs.add("LIST");
    this.ddlDetailParenKWs.add("SEQUENCE");
    this.ddlDetailParenKWs.add("AUTO_INCREMENT");
    this.ddlDetailParenKWs.add("KEY");
    this.ddlDetailParenKWs.add("INDEX");
    this.ddlDetailParenKWs.add("TINYINT");
    this.ddlDetailParenKWs.add("SMALLINT");
    this.ddlDetailParenKWs.add("MEDIUMINT");
    this.ddlDetailParenKWs.add("INTEGER");
    this.ddlDetailParenKWs.add("BIGINT");
    this.ddlDetailParenKWs.add("DOUBLE");
    this.ddlDetailParenKWs.add("BINARY");
    this.ddlDetailParenKWs.add("VARBINARY");
    this.ddlDetailParenKWs.add("INT");
  }
  
  public boolean contains(String ddlDetailValue) {
    if (StringUtils.isBlank(ddlDetailValue))
      return false; 
    return this.ddlDetailParenKWs.contains(ddlDetailValue.toUpperCase());
  }
}
