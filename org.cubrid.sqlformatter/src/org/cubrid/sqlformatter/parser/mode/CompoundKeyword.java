package org.cubrid.sqlformatter.parser.mode;

public class CompoundKeyword {
  private int tokenEndIndex;
  
  private String compoundKeyword;
  
  public CompoundKeyword(int tokenEndIndex, String compoundKeyword) {
    this.tokenEndIndex = tokenEndIndex;
    this.compoundKeyword = compoundKeyword;
  }
  
  public int getTokenEndIndex() {
    return this.tokenEndIndex;
  }
  
  public void setTokenEndIndex(int tokenBeginIndex) {
    this.tokenEndIndex = tokenBeginIndex;
  }
  
  public String getCompoundKeyword() {
    return this.compoundKeyword;
  }
  
  public void setCompoundKeyword(String compoundKeyword) {
    this.compoundKeyword = compoundKeyword;
  }
}
