package org.cubrid.sqlformatter.parser.constants;

import java.util.regex.Pattern;

public interface RegexPatterns {
  public static final Pattern INDEX_HINT = Pattern.compile("(USE|IGNORE|FORCE) (INDEX|KEY)");
  
  public static final Pattern DROP_COMPOUND = Pattern.compile("DROP (TABLE|CLASS|VIEW|VCLASS)( IF EXISTS)?");
  
  public static final Pattern CUBRID_JOIN = Pattern.compile("((INNER )|(CROSS )|((LEFT |RIGHT )(OUTER )?))?JOIN");
  
  public static final Pattern MYSQL_JOIN = Pattern.compile("(STRAIGHT_JOIN)|(((INNER )|(CROSS )|((NATURAL )?((LEFT |RIGHT )(OUTER )?)?))?JOIN)");
  
  public static final Pattern ORACLE_JOIN = Pattern.compile("((CROSS )|((NATURAL )?(INNER )?)|((NATURAL )?(((FULL|LEFT|RIGHT) )(OUTER )?)?))?JOIN");
  
  public static final Pattern MYSQL_INSERT = Pattern.compile("INSERT( LOW_PRIORITY| DELAYED| HIGH_PRIORITY)?( IGNORE)?( INTO)?");
  
  public static final Pattern CUBRID_INSERT = Pattern.compile("INSERT( INTO)?");
  
  public static final Pattern MYSQL_DELETE = Pattern.compile("DELETE( LOW_PRIORITY)?( QUICK)?( IGNORE)?( FROM)?");
  
  public static final Pattern CUBRID_DELETE = Pattern.compile("DELETE( FROM)?");
  
  public static final Pattern MYSQL_UPDATE = Pattern.compile("UPDATE( LOW_PRIORITY)?( IGNORE)?");
  
  public static final Pattern MYSQL_TABLE_OPTION_CHARACTER_SET = Pattern.compile("(DEFAULT )?CHARACTER SET");
  
  public static final Pattern MYSQL_TABLE_OPTION_COLLATE = Pattern.compile("(DEFAULT )?COLLATE");
  
  public static final Pattern MYSQL_COMPOUND_DEFAULT = Pattern.compile("(SET|DROP) DEFAULT");
  
  public static final Pattern MYSQL_ALTER_SPEC_TABLESPACE = Pattern.compile("(DISCARD|IMPORT) TABLESPACE");
  
  public static final Pattern MYSQL_WITH_COMPOUND = Pattern.compile("WITH( CASCADED| LOCAL)? CHECK OPTION");
  
  public static final Pattern CREATE_ALTER_COMPOUND = Pattern.compile("(CREATE|ALTER) (VIEW|(OR REPLACE( VIEW)?))");
  
  public static final Pattern MYSQL_SQL_SECURITY = Pattern.compile("SQL SECURITY (DEFINER|INVOKER)");
  
  public static final Pattern MYSQL_INDEX_OPTION_COMPOUND_USING = Pattern.compile("USING (HASH|BTREE)");
  
  public static final Pattern CONNECT_BY_COMPOUND = Pattern.compile("CONNECT BY( NOCYCLE)?");
  
  public static final Pattern CUBRID_RENAME_COMPOUND = Pattern.compile("RENAME (TABLE|CLASS|VIEW|VCLASS)");
  
  public static final Pattern MYSQL_DROP_COMPOUND = Pattern.compile("DROP((( TEMPORARY)? TABLE( IF EXISTS)?)|(( (OFFLINE|ONLINE))? INDEX))");
  
  public static final Pattern MYSQL_SELECT_COMPOUND = Pattern.compile("SELECT( (ALL|DISTINCT|DISTINCTROW))?( HIGH_PRIORITY)?( STRAIGHT_JOIN)?( SQL_SMALL_RESULT)?( SQL_BIG_RESULT)?( SQL_BUFFER_RESULT)?( (SQL_CACHE|SQL_NO_CACHE))?( SQL_CALC_FOUND_ROWS)?");
  
  public static final Pattern MYSQL_CREATE_COMPOUND = Pattern.compile("CREATE( TEMPORARY)? TABLE( IF NOT EXISTS)?");
  
  public static final Pattern ORACLE_PIVOT_COMPOUND = Pattern.compile("(PIVOT XML)|(UNPIVOT (INCLUDE|EXCLUDE) NULLS)");
  
  public static final Pattern ORACLE_LOG_COMPOUND = Pattern.compile("LOG ERRORS( INTO)?");
  
  public static final Pattern ORACLE_MULTIPLE_INSERT_COMPOUND = Pattern.compile("INSERT (ALL|FIRST)");
  
  public static final Pattern ORACLE_STORAGE_COMPOUND = Pattern.compile("(ENABLE|DISABLE) STORAGE");
  
  public static final Pattern ORACLE_PARTITION_BY_COMPOUND = Pattern.compile("(GLOBAL )?PARTITION BY");
  
  public static final Pattern ORACLE_REBUILD_COMPOUND = Pattern.compile("REBUILD( (PARALLEL|PARTITION))?");
  
  public static final Pattern ORACLE_WITH_COMPOUND = Pattern.compile("WITH ((READ ONLY)|(CHECK OPTION)|(OBJECT))?");
  
  public static final Pattern ORACLE_CREATE_VIEW_COMPOUND = Pattern.compile("CREATE( OR REPLACE)?(( NO)? FORCE)? VIEW");
  
  public static final Pattern SQLSERVER_CONTROL_FLOW_COMPOUND = Pattern.compile("(BEGIN|END) (TRY|CATCH)");
}
