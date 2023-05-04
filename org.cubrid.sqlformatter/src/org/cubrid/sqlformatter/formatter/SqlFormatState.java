package org.cubrid.sqlformatter.formatter;

class SqlFormatState {
  public static final char INDENT = '\t';
  
  private static final char WHITE_SPACE = ' ';
  
  public static final String NEW_LINE = System.getProperty("line.separator");
  
  public static final int SPACES_PER_TAB = 8;
  
  public boolean expectStatementBreak = false;
  
  public boolean expectLineBreak = false;
  
  public boolean expectWordSeperator = false;
  
  private int indentLevel = 0;
  
  private StringBuilder output = new StringBuilder();
  
  public SqlFormatState() {}
  
  public SqlFormatState(SqlFormatState sourceState) {
    this.indentLevel = sourceState.indentLevel;
  }
  
  public void addOutputIndents() {
    addOutputIndents(this.indentLevel);
  }
  
  public void addOutputIndents(int indentLevel) {
    for (int i = 0; i < indentLevel; i++)
      this.output.append('\t'); 
  }
  
  public void addOutputLineBreak() {
    this.output.append(NEW_LINE);
  }
  
  public void addOutputString(String content) {
    this.output.append(content);
  }
  
  public void addOutputWhiteSpace() {
    this.output.append(' ');
  }
  
  public void decreaseIndent() {
    if (this.indentLevel > 0)
      this.indentLevel--; 
  }
  
  public int getIndentLevel() {
    return this.indentLevel;
  }
  
  public String getOutput() {
    return this.output.toString();
  }
  
  public void increaseIndent() {
    this.indentLevel++;
  }
  
  public boolean outputContainsLineBreak() {
    return this.output.toString().contains(NEW_LINE);
  }
  
  public boolean outputIsEmpty() {
    return this.output.toString().isEmpty();
  }
  
  public boolean outputEndWithLineBreak() {
    String outputString = getOutput();
    return outputString.endsWith(NEW_LINE);
  }
  
  public boolean outputStartsWithComment() {
    String outputString = getOutput().trim();
    return (outputString.startsWith("--") || outputString.startsWith("//") || outputString.startsWith("/*"));
  }
  
  public void removeLastSpace() {
    if (this.output.length() > 0 && this.output.charAt(this.output.length() - 1) == ' ')
      this.output.delete(this.output.length() - 1, this.output.length()); 
  }
}
