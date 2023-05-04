package org.cubrid.sqlformatter;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class SqlFormatOptions {
  private static final int DEFAULT_STATEMENT_BREAKS = 2;
  
  private int statementBreaks = 2;
  
  private boolean upperCaseKeyword = true;
  
  private boolean breakAfterClauseKeyword = true;
  
  private boolean breakBeforeAndOr = true;
  
  private boolean expandClauseCommaList = true;
  
  private boolean expandInList = true;
  
  private boolean expandDDLBlock = true;
  
  private boolean expandIndexHintList = true;
  
  private boolean expandSelectionTargetParenCommaList = true;
  
  private boolean expandPivotParens = true;
  
  public SqlFormatOptions() {}
  
  public SqlFormatOptions(int statementBreaks, boolean upperCaseKeyword, boolean breakAfterClauseKeyword, boolean breakBeforeAndOr, boolean expandCommaList, boolean expandInList, boolean expandDDLParenList) {
    this.statementBreaks = statementBreaks;
    this.upperCaseKeyword = upperCaseKeyword;
    this.breakAfterClauseKeyword = breakAfterClauseKeyword;
    this.breakBeforeAndOr = breakBeforeAndOr;
    this.expandClauseCommaList = expandCommaList;
    this.expandInList = expandInList;
    this.expandDDLBlock = expandDDLParenList;
  }
  
  public int getStatementBreaks() {
    return this.statementBreaks;
  }
  
  public boolean isBreakAfterClauseKeyword() {
    return this.breakAfterClauseKeyword;
  }
  
  public boolean isBreakBeforeAndOr() {
    return this.breakBeforeAndOr;
  }
  
  public boolean isExpandClauseCommaList() {
    return this.expandClauseCommaList;
  }
  
  public boolean isExpandDDLBlock() {
    return this.expandDDLBlock;
  }
  
  public boolean isExpandInList() {
    return this.expandInList;
  }
  
  public boolean isUpperCaseKeyword() {
    return this.upperCaseKeyword;
  }
  
  public void setBreakAfterClauseKeyword(boolean breakAfterClauseKeyword) {
    this.breakAfterClauseKeyword = breakAfterClauseKeyword;
  }
  
  public void setBreakBeforeAndOr(boolean breakBeforeAndOr) {
    this.breakBeforeAndOr = breakBeforeAndOr;
  }
  
  public void setExpandClauseCommaList(boolean expandCommaList) {
    this.expandClauseCommaList = expandCommaList;
  }
  
  public void setExpandDDLBlock(boolean expandDDLBlock) {
    this.expandDDLBlock = expandDDLBlock;
  }
  
  public void setExpandInList(boolean expandInList) {
    this.expandInList = expandInList;
  }
  
  public void setStatementBreaks(int statementBreaks) {
    this.statementBreaks = statementBreaks;
  }
  
  public void setUpperCaseKeyword(boolean upperCaseKeyword) {
    this.upperCaseKeyword = upperCaseKeyword;
  }
  
  public void setExpandIndexHintList(boolean expandIndexHintList) {
    this.expandIndexHintList = true;
  }
  
  public boolean getExpandIndexHintList() {
    return this.expandIndexHintList;
  }
  
  public boolean isExpandSelectionTargetParenCommaList() {
    return this.expandSelectionTargetParenCommaList;
  }
  
  public void setExpandSelectionTargetParenCommaList(boolean expandSelectionTargetParenCommaList) {
    this.expandSelectionTargetParenCommaList = expandSelectionTargetParenCommaList;
  }
  
  public boolean isExpandPivotParens() {
    return this.expandPivotParens;
  }
  
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
  }
}
