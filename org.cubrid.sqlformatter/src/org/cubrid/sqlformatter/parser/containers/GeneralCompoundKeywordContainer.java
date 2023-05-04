package org.cubrid.sqlformatter.parser.containers;

import java.util.HashSet;
import org.apache.commons.lang.StringUtils;
import org.cubrid.sqlformatter.parser.constants.RegexPatterns;

public class GeneralCompoundKeywordContainer implements CompoundKeywordContainer {
  private HashSet<String> generalCompoundKeywordContainer = new HashSet<String>();
  
  public static GeneralCompoundKeywordContainer INSTANCE = new GeneralCompoundKeywordContainer();
  
  private GeneralCompoundKeywordContainer() {
    this.generalCompoundKeywordContainer.add("GROUP BY");
    this.generalCompoundKeywordContainer.add("ORDER BY");
    this.generalCompoundKeywordContainer.add("ORDER SIBLINGS BY");
    this.generalCompoundKeywordContainer.add("USING INDEX");
    this.generalCompoundKeywordContainer.add("FOR UPDATE OF");
    this.generalCompoundKeywordContainer.add("FOR UPDATE");
    this.generalCompoundKeywordContainer.add("DEFAULT VALUES");
    this.generalCompoundKeywordContainer.add("ON DUPLICATE KEY UPDATE");
    this.generalCompoundKeywordContainer.add("ON DELETE");
    this.generalCompoundKeywordContainer.add("ON UPDATE");
    this.generalCompoundKeywordContainer.add("SET NULL");
    this.generalCompoundKeywordContainer.add("AS SUBCLASS OF");
    this.generalCompoundKeywordContainer.add("SET DEFAULT");
    this.generalCompoundKeywordContainer.add("CLASS ATTRIBUTE");
    this.generalCompoundKeywordContainer.add("SELECT ALL");
    this.generalCompoundKeywordContainer.add("SELECT DISTINCT");
    this.generalCompoundKeywordContainer.add("SELECT DISTINCTROW");
    this.generalCompoundKeywordContainer.add("SELECT UNIQUE");
    this.generalCompoundKeywordContainer.add("DROP INDEX");
    this.generalCompoundKeywordContainer.add("DROP UNIQUE INDEX");
    this.generalCompoundKeywordContainer.add("FOR JOIN");
    this.generalCompoundKeywordContainer.add("FOR GROUP BY");
    this.generalCompoundKeywordContainer.add("FOR ORDER BY");
    this.generalCompoundKeywordContainer.add("LOCK IN SHARE MODE");
    this.generalCompoundKeywordContainer.add("INDEX DIRECTORY");
    this.generalCompoundKeywordContainer.add("DATA DIRECTORY");
    this.generalCompoundKeywordContainer.add("RENAME AS");
    this.generalCompoundKeywordContainer.add("WITH PARSER");
    this.generalCompoundKeywordContainer.add("DROP PROCEDURE");
    this.generalCompoundKeywordContainer.add("START WITH");
    this.generalCompoundKeywordContainer.add("PARTITION FOR");
    this.generalCompoundKeywordContainer.add("REJECT LIMIT");
    this.generalCompoundKeywordContainer.add("STORE IN");
    this.generalCompoundKeywordContainer.add("SUBPARTITION BY");
    this.generalCompoundKeywordContainer.add("ON COMMIT");
    this.generalCompoundKeywordContainer.add("ON COMMIT");
    this.generalCompoundKeywordContainer.add("DEFAULT DIRECTORY");
    this.generalCompoundKeywordContainer.add("ACCESS PARAMETERS");
    this.generalCompoundKeywordContainer.add("EXCEPTIONS INTO");
    this.generalCompoundKeywordContainer.add("SPLIT PARTITION");
    this.generalCompoundKeywordContainer.add("ADD PARTITION");
    this.generalCompoundKeywordContainer.add("INTO PARTITION");
    this.generalCompoundKeywordContainer.add("MERGE PARTITIONS");
    this.generalCompoundKeywordContainer.add("DROP PARTITION");
    this.generalCompoundKeywordContainer.add("EXCHANGE PARTITION");
    this.generalCompoundKeywordContainer.add("MODIFY PARTITION");
    this.generalCompoundKeywordContainer.add("MOVE PARTITION");
    this.generalCompoundKeywordContainer.add("RENAME PARTITION");
    this.generalCompoundKeywordContainer.add("TRUNCATE PARTITION");
    this.generalCompoundKeywordContainer.add("DROP STORAGE");
    this.generalCompoundKeywordContainer.add("LOCAL INDEXES");
    this.generalCompoundKeywordContainer.add("BEGIN TRANSACTION");
    this.generalCompoundKeywordContainer.add("ROLLBACK TRANSACTION");
    this.generalCompoundKeywordContainer.add("COMMIT TRANSACTION");
    this.generalCompoundKeywordContainer.add("FETCH NEXT FROM");
    this.generalCompoundKeywordContainer.add("WITH EXECUTE AS");
    this.generalCompoundKeywordContainer.add("RETURN AS VALUE");
    this.generalCompoundKeywordContainer.add("MERGE UNION");
    this.generalCompoundKeywordContainer.add("WITH VALUES");
    this.generalCompoundKeywordContainer.add("REORGANIZE PARTITION");
  }
  
  public boolean contains(String compoundKeyword) {
    if (StringUtils.isEmpty(compoundKeyword))
      return false; 
    String ucCompoundKeyword = compoundKeyword.toUpperCase();
    return (this.generalCompoundKeywordContainer.contains(ucCompoundKeyword) || RegexPatterns.MYSQL_SELECT_COMPOUND.matcher(ucCompoundKeyword).matches() || RegexPatterns.MYSQL_INSERT.matcher(ucCompoundKeyword).matches() || RegexPatterns.CUBRID_INSERT.matcher(ucCompoundKeyword).matches() || RegexPatterns.MYSQL_DELETE.matcher(ucCompoundKeyword).matches() || RegexPatterns.CUBRID_DELETE.matcher(ucCompoundKeyword).matches() || RegexPatterns.MYSQL_UPDATE.matcher(ucCompoundKeyword).matches() || RegexPatterns.DROP_COMPOUND.matcher(ucCompoundKeyword).matches() || RegexPatterns.MYSQL_CREATE_COMPOUND.matcher(ucCompoundKeyword).matches() || RegexPatterns.MYSQL_TABLE_OPTION_CHARACTER_SET.matcher(ucCompoundKeyword).matches() || RegexPatterns.MYSQL_COMPOUND_DEFAULT.matcher(ucCompoundKeyword).matches() || RegexPatterns.MYSQL_ALTER_SPEC_TABLESPACE.matcher(ucCompoundKeyword).matches() || RegexPatterns.MYSQL_WITH_COMPOUND.matcher(ucCompoundKeyword).matches() || RegexPatterns.CREATE_ALTER_COMPOUND.matcher(ucCompoundKeyword).matches() || RegexPatterns.MYSQL_SQL_SECURITY.matcher(ucCompoundKeyword).matches() || RegexPatterns.MYSQL_INDEX_OPTION_COMPOUND_USING.matcher(ucCompoundKeyword).matches() || RegexPatterns.CONNECT_BY_COMPOUND.matcher(ucCompoundKeyword).matches() || RegexPatterns.CUBRID_RENAME_COMPOUND.matcher(ucCompoundKeyword).matches() || RegexPatterns.MYSQL_DROP_COMPOUND.matcher(ucCompoundKeyword).matches() || RegexPatterns.ORACLE_PIVOT_COMPOUND.matcher(ucCompoundKeyword).matches() || RegexPatterns.ORACLE_LOG_COMPOUND.matcher(ucCompoundKeyword).matches() || RegexPatterns.ORACLE_MULTIPLE_INSERT_COMPOUND.matcher(ucCompoundKeyword).matches() || RegexPatterns.ORACLE_STORAGE_COMPOUND.matcher(ucCompoundKeyword).matches() || RegexPatterns.ORACLE_PARTITION_BY_COMPOUND.matcher(ucCompoundKeyword).matches() || RegexPatterns.ORACLE_REBUILD_COMPOUND.matcher(ucCompoundKeyword).matches() || RegexPatterns.ORACLE_WITH_COMPOUND.matcher(ucCompoundKeyword).matches() || RegexPatterns.ORACLE_CREATE_VIEW_COMPOUND.matcher(ucCompoundKeyword).matches());
  }
}
