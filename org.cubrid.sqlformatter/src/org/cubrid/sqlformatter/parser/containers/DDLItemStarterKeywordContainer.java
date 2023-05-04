package org.cubrid.sqlformatter.parser.containers;

import java.util.HashSet;
import org.apache.commons.lang.StringUtils;
import org.cubrid.sqlformatter.parser.constants.RegexPatterns;

public class DDLItemStarterKeywordContainer {
  private HashSet<String> ddlItemStarterKeywordContainer = new HashSet<String>();
  
  public static DDLItemStarterKeywordContainer INSTANCE = new DDLItemStarterKeywordContainer();
  
  private DDLItemStarterKeywordContainer() {
    this.ddlItemStarterKeywordContainer.add("UNDER");
    this.ddlItemStarterKeywordContainer.add("AS SUBCLASS OF");
    this.ddlItemStarterKeywordContainer.add("AUTO_INCREMENT");
    this.ddlItemStarterKeywordContainer.add("CLASS ATTRIBUTE");
    this.ddlItemStarterKeywordContainer.add("INHERIT");
    this.ddlItemStarterKeywordContainer.add("REUSE_OID");
    this.ddlItemStarterKeywordContainer.add("CHARSET");
    this.ddlItemStarterKeywordContainer.add("COLLATE");
    this.ddlItemStarterKeywordContainer.add("ADD");
    this.ddlItemStarterKeywordContainer.add("ALTER");
    this.ddlItemStarterKeywordContainer.add("DROP");
    this.ddlItemStarterKeywordContainer.add("DROP INDEX");
    this.ddlItemStarterKeywordContainer.add("RENAME");
    this.ddlItemStarterKeywordContainer.add("CHANGE");
    this.ddlItemStarterKeywordContainer.add("AS");
    this.ddlItemStarterKeywordContainer.add("WITH CHECK OPTION");
    this.ddlItemStarterKeywordContainer.add("ENGINE");
    this.ddlItemStarterKeywordContainer.add("AVG_ROW_LENGTH");
    this.ddlItemStarterKeywordContainer.add("CHECKSUM");
    this.ddlItemStarterKeywordContainer.add("COMMENT");
    this.ddlItemStarterKeywordContainer.add("CONNECTION");
    this.ddlItemStarterKeywordContainer.add("DATA DIRECTORY");
    this.ddlItemStarterKeywordContainer.add("DELAY_KEY_WRITE");
    this.ddlItemStarterKeywordContainer.add("INDEX DIRECTORY");
    this.ddlItemStarterKeywordContainer.add("INSERT_METHOD");
    this.ddlItemStarterKeywordContainer.add("KEY_BLOCK_SIZE");
    this.ddlItemStarterKeywordContainer.add("MAX_ROWS");
    this.ddlItemStarterKeywordContainer.add("MIN_ROWS");
    this.ddlItemStarterKeywordContainer.add("PACK_KEYS");
    this.ddlItemStarterKeywordContainer.add("PASSWORD");
    this.ddlItemStarterKeywordContainer.add("ROW_FORMAT");
    this.ddlItemStarterKeywordContainer.add("TABLESPACE");
    this.ddlItemStarterKeywordContainer.add("UNION");
    this.ddlItemStarterKeywordContainer.add("MODIFY");
    this.ddlItemStarterKeywordContainer.add("ENABLE");
    this.ddlItemStarterKeywordContainer.add("DISABLE");
    this.ddlItemStarterKeywordContainer.add("RENAME AS");
    this.ddlItemStarterKeywordContainer.add("CONVERT");
    this.ddlItemStarterKeywordContainer.add("FORCE");
    this.ddlItemStarterKeywordContainer.add("ALGORITHM");
    this.ddlItemStarterKeywordContainer.add("DEFINER");
    this.ddlItemStarterKeywordContainer.add("VIEW");
    this.ddlItemStarterKeywordContainer.add("WITH PARSER");
    this.ddlItemStarterKeywordContainer.add("SUBPARTITION BY");
    this.ddlItemStarterKeywordContainer.add("PARTITION");
    this.ddlItemStarterKeywordContainer.add("ON COMMIT");
    this.ddlItemStarterKeywordContainer.add("TABLESPACE");
    this.ddlItemStarterKeywordContainer.add("PCTFREE");
    this.ddlItemStarterKeywordContainer.add("PCTUSED");
    this.ddlItemStarterKeywordContainer.add("INITRANS");
    this.ddlItemStarterKeywordContainer.add("STORAGE");
    this.ddlItemStarterKeywordContainer.add("LOGGING");
    this.ddlItemStarterKeywordContainer.add("NOLOGGING");
    this.ddlItemStarterKeywordContainer.add("FILESYSTEM_LIKE_LOGGING");
    this.ddlItemStarterKeywordContainer.add("COMPRESS");
    this.ddlItemStarterKeywordContainer.add("NOCOMPRESS");
    this.ddlItemStarterKeywordContainer.add("ORGANIZATION");
    this.ddlItemStarterKeywordContainer.add("CLUSTER");
    this.ddlItemStarterKeywordContainer.add("MAPPING");
    this.ddlItemStarterKeywordContainer.add("NOMAPPING");
    this.ddlItemStarterKeywordContainer.add("PCTTHRESHOLD");
    this.ddlItemStarterKeywordContainer.add("INCLUDING");
    this.ddlItemStarterKeywordContainer.add("REJECT LIMIT");
    this.ddlItemStarterKeywordContainer.add("PARALLEL");
    this.ddlItemStarterKeywordContainer.add("NOPARALLEL");
    this.ddlItemStarterKeywordContainer.add("EXCEPTIONS INTO");
    this.ddlItemStarterKeywordContainer.add("DEALLOCATE");
    this.ddlItemStarterKeywordContainer.add("SPLIT PARTITION");
    this.ddlItemStarterKeywordContainer.add("ADD PARTITION");
    this.ddlItemStarterKeywordContainer.add("MERGE PARTITIONS");
    this.ddlItemStarterKeywordContainer.add("DROP PARTITION");
    this.ddlItemStarterKeywordContainer.add("EXCHANGE PARTITION");
    this.ddlItemStarterKeywordContainer.add("MODIFY PARTITION");
    this.ddlItemStarterKeywordContainer.add("MOVE PARTITION");
    this.ddlItemStarterKeywordContainer.add("TRUNCATE PARTITION");
    this.ddlItemStarterKeywordContainer.add("DROP STORAGE");
    this.ddlItemStarterKeywordContainer.add("RENAME PARTITION");
    this.ddlItemStarterKeywordContainer.add("NOSORT");
    this.ddlItemStarterKeywordContainer.add("SORT");
    this.ddlItemStarterKeywordContainer.add("LOCAL");
    this.ddlItemStarterKeywordContainer.add("XMLSCHEMA");
    this.ddlItemStarterKeywordContainer.add("ELEMENT");
    this.ddlItemStarterKeywordContainer.add("WITH EXECUTE AS");
    this.ddlItemStarterKeywordContainer.add("REORGANIZE PARTITION");
  }
  
  public boolean contains(String keyword) {
    if (StringUtils.isEmpty(keyword))
      return false; 
    String ucKeyword = keyword.toUpperCase();
    return (this.ddlItemStarterKeywordContainer.contains(ucKeyword) || RegexPatterns.MYSQL_TABLE_OPTION_CHARACTER_SET.matcher(ucKeyword).matches() || RegexPatterns.MYSQL_TABLE_OPTION_COLLATE.matcher(ucKeyword).matches() || RegexPatterns.MYSQL_ALTER_SPEC_TABLESPACE.matcher(ucKeyword).matches() || RegexPatterns.MYSQL_WITH_COMPOUND.matcher(ucKeyword).matches() || RegexPatterns.MYSQL_SQL_SECURITY.matcher(ucKeyword).matches() || RegexPatterns.MYSQL_INDEX_OPTION_COMPOUND_USING.matcher(ucKeyword).matches() || RegexPatterns.ORACLE_PARTITION_BY_COMPOUND.matcher(ucKeyword).matches() || RegexPatterns.ORACLE_WITH_COMPOUND.matcher(ucKeyword).matches());
  }
}
