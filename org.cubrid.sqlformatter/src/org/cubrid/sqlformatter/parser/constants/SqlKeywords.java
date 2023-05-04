package org.cubrid.sqlformatter.parser.constants;

public interface SqlKeywords {
  public static final String NUMERIC = "NUMERIC";
  
  public static final String DECIMAL = "DECIMAL";
  
  public static final String FLOAT = "FLOAT";
  
  public static final String REAL = "REAL";
  
  public static final String DOUBLE = "DOUBLE";
  
  public static final String DOUBLE_PRECISION = "DOUBLE PRECISION";
  
  public static final String BIT = "BIT";
  
  public static final String BIT_VARYING = "BIT VARYING";
  
  public static final String CHAR = "CHAR";
  
  public static final String CHARACTER = "CHARACTER";
  
  public static final String VARCHAR = "VARCHAR";
  
  public static final String CHAR_VARYING = "CHAR VARYING";
  
  public static final String CHARACTER_VARYING = "CHARACTER_VARYING";
  
  public static final String NCHAR = "NCHAR";
  
  public static final String NCHAR_VARYING = "NCHAR VARYING";
  
  public static final String ENUM = "ENUM";
  
  public static final String SET = "SET";
  
  public static final String MULTISET = "MULTISET";
  
  public static final String LIST = "LIST";
  
  public static final String SEQUENCE = "SEQUENCE";
  
  public static final String AUTO_INCREMENT = "AUTO_INCREMENT";
  
  public static final String KEY = "KEY";
  
  public static final String INDEX = "INDEX";
  
  public static final String UNIQUE = "UNIQUE";
  
  public static final String PRIMARY = "PRIMARY";
  
  public static final String FOREIGN = "FOREIGN";
  
  public static final String CONSTRAINT = "CONSTRAINT";
  
  public static final String REFERENCES = "REFERENCES";
  
  public static final String INSERT = "INSERT";
  
  public static final String INSERT_INTO = "INSERT INTO";
  
  public static final String INTO = "INTO";
  
  public static final String VALUES = "VALUES";
  
  public static final String ALTER = "ALTER";
  
  public static final String SELECT = "SELECT";
  
  public static final String DROP_INDEX = "DROP INDEX";
  
  public static final String DROP_UNIQUE_INDEX = "DROP UNIQUE INDEX";
  
  public static final String RENAME_AS = "RENAME AS";
  
  public static final String COLUMN = "COLUMN";
  
  public static final String CUBRID_CHARACTER_SET = "CHARACTER_SET";
  
  public static final String PROCEDURE = "PROCEDURE";
  
  public static final String DROP_PROCEDURE = "DROP PROCEDURE";
  
  public static final String SELECT_ALL = "SELECT ALL";
  
  public static final String SELECT_DISTINCT = "SELECT DISTINCT";
  
  public static final String SELECT_DISTINCTROW = "SELECT DISTINCTROW";
  
  public static final String SELECT_UNIQUE = "SELECT UNIQUE";
  
  public static final int MAX_COMPOUND_KEYWORDS_COUNT = 9;
  
  public static final String GROUP_BY = "GROUP BY";
  
  public static final String DELETE_FROM = "DELETE FROM";
  
  public static final String ORDER_BY = "ORDER BY";
  
  public static final String ORDER_SIBLINGS_BY = "ORDER SIBLINGS BY";
  
  public static final String USING_INDEX = "USING INDEX";
  
  public static final String FOR_UPDATE_OF = "FOR UPDATE OF";
  
  public static final String FOR_UPDATE = "FOR UPDATE";
  
  public static final String DEFAULT_VALUES = "DEFAULT VALUES";
  
  public static final String ON_DUPLICATE_KEY_UPDATE = "ON DUPLICATE KEY UPDATE";
  
  public static final String ON_UPDATE = "ON UPDATE";
  
  public static final String ON_DELETE = "ON DELETE";
  
  public static final String SET_NULL = "SET NULL";
  
  public static final String SET_DEFAULT = "SET DEFAULT";
  
  public static final String AS_SUBCLASS_OF = "AS SUBCLASS OF";
  
  public static final String UNDER = "UNDER";
  
  public static final String CLASS_ATTRIBUTE = "CLASS ATTRIBUTE";
  
  public static final String WITH_CHECK_OPTION = "WITH CHECK OPTION";
  
  public static final String INHERIT = "INHERIT";
  
  public static final String REUSE_OID = "REUSE_OID";
  
  public static final String CHARSET = "CHARSET";
  
  public static final String COLLATE = "COLLATE";
  
  public static final String ADD = "ADD";
  
  public static final String DROP = "DROP";
  
  public static final String RENAME = "RENAME";
  
  public static final String CHANGE = "CHANGE";
  
  public static final String MODIFY = "MODIFY";
  
  public static final String DISABLE = "DISABLE";
  
  public static final String ENABLE = "ENABLE";
  
  public static final String START_WITH = "START WITH";
  
  public static final String ATTRIBUTE = "ATTRIBUTE";
  
  public static final String SUPERCLASS = "SUPERCLASS";
  
  public static final String QUERY = "QUERY";
  
  public static final String LANGUAGE = "LANGUAGE";
  
  public static final int MAX_KEYWORD_COUNT_COMPOUND_JOIN = 4;
  
  public static final String JOIN = "JOIN";
  
  public static final String FOR_JOIN = "FOR JOIN";
  
  public static final String FOR_ORDER_BY = "FOR ORDER BY";
  
  public static final String FOR_GROUP_BY = "FOR GROUP BY";
  
  public static final String STRAIGHT_JOIN = "STRAIGHT_JOIN";
  
  public static final String LOCK_IN_SHARE_MODE = "LOCK IN SHARE MODE";
  
  public static final String ENGINE = "ENGINE";
  
  public static final String AVG_ROW_LENGTH = "AVG_ROW_LENGTH";
  
  public static final String CHECKSUM = "CHECKSUM";
  
  public static final String COMMENT = "COMMENT";
  
  public static final String CONNECTION = "CONNECTION";
  
  public static final String DATA_DIRECTORY = "DATA DIRECTORY";
  
  public static final String DELAY_KEY_WRITE = "DELAY_KEY_WRITE";
  
  public static final String INDEX_DIRECTORY = "INDEX DIRECTORY";
  
  public static final String KEY_BLOCK_SIZE = "KEY_BLOCK_SIZE";
  
  public static final String MAX_ROWS = "MAX_ROWS";
  
  public static final String MIN_ROWS = "MIN_ROWS";
  
  public static final String PACK_KEYS = "PACK_KEYS";
  
  public static final String PASSWORD = "PASSWORD";
  
  public static final String ROW_FORMAT = "ROW_FORMAT";
  
  public static final String INSERT_METHOD = "INSERT_METHOD";
  
  public static final String UNION = "UNION";
  
  public static final String TINYINT = "TINYINT";
  
  public static final String SMALLINT = "SMALLINT";
  
  public static final String MEDIUMINT = "MEDIUMINT";
  
  public static final String INTEGER = "INTEGER";
  
  public static final String BIGINT = "BIGINT";
  
  public static final String BINARY = "BINARY";
  
  public static final String VARBINARY = "VARBINARY";
  
  public static final String INT = "INT";
  
  public static final String HASH = "HASH";
  
  public static final String BTREE = "BTREE";
  
  public static final String MYSQL_CHARACTER_SET = "CHARACTER SET";
  
  public static final String CONVERT = "CONVERT";
  
  public static final String FULLTEXT = "FULLTEXT";
  
  public static final String SPATIAL = "SPATIAL";
  
  public static final String FORCE = "FORCE";
  
  public static final String SQL_SECURITY = "SQL SECURITY";
  
  public static final String ALGORITHM = "ALGORITHM";
  
  public static final String DEFINER = "DEFINER";
  
  public static final String VIEW = "VIEW";
  
  public static final String WITH_PARSER = "WITH PARSER";
  
  public static final int MAX_KEYWORD_COUNT_COMPOUND_HINT = 2;
  
  public static final String PIVOT = "PIVOT";
  
  public static final String UNPIVOT = "UNPIVOT";
  
  public static final String PARTITION_FOR = "PARTITION FOR";
  
  public static final String REJECT_LIMIT = "REJECT LIMIT";
  
  public static final String PARTITION = "PARTITION";
  
  public static final String SUBPARTITION_BY = "SUBPARTITION BY";
  
  public static final String STORE_IN = "STORE IN";
  
  public static final String ON_COMMIT = "ON COMMIT";
  
  public static final String ON = "ON";
  
  public static final String ORGANIZATION = "ORGANIZATION";
  
  public static final String CLUSTER = "CLUSTER";
  
  public static final String MAPPING = "MAPPING";
  
  public static final String NOMAPPING = "NOMAPPING";
  
  public static final String PCTTHRESHOLD = "PCTTHRESHOLD";
  
  public static final String INCLUDING = "INCLUDING";
  
  public static final String ACCESS_PARAMETERS = "ACCESS PARAMETERS";
  
  public static final String LOCATION = "LOCATION";
  
  public static final String DEFAULT_DIRECTORY = "DEFAULT DIRECTORY";
  
  public static final String TYPE = "TYPE";
  
  public static final String EXTERNAL = "EXTERNAL";
  
  public static final String PARALLEL = "PARALLEL";
  
  public static final String NOPARALLEL = "NOPARALLEL";
  
  public static final String EXCEPTIONS_INTO = "EXCEPTIONS INTO";
  
  public static final String DEALLOCATE = "DEALLOCATE";
  
  public static final String SPLIT_PARTITION = "SPLIT PARTITION";
  
  public static final String ADD_PARTITION = "ADD PARTITION";
  
  public static final String INTO_PARTITION = "INTO PARTITION";
  
  public static final String MERGE_PARTITIONS = "MERGE PARTITIONS";
  
  public static final String DROP_PARTITION = "DROP PARTITION";
  
  public static final String EXCHANGE_PARTITION = "EXCHANGE PARTITION";
  
  public static final String MODIFY_PARTITION = "MODIFY PARTITION";
  
  public static final String MOVE_PARTITION = "MOVE PARTITION";
  
  public static final String RENAME_PARTITION = "RENAME PARTITION";
  
  public static final String TRUNCATE_PARTITION = "TRUNCATE PARTITION";
  
  public static final String REORGANIZE_PARTITION = "REORGANIZE PARTITION";
  
  public static final String DROP_STORAGE = "DROP STORAGE";
  
  public static final String NOSORT = "NOSORT";
  
  public static final String SORT = "SORT";
  
  public static final String CREATE_INDEX = "CREATE INDEX";
  
  public static final String CREATE_BITMAP_INDEX = "CREATE BITMAP INDEX";
  
  public static final String LOCAL = "LOCAL";
  
  public static final String LOCAL_INDEXES = "LOCAL INDEXES";
  
  public static final String XMLSCHEMA = "XMLSCHEMA";
  
  public static final String ELEMENT = "ELEMENT";
  
  public static final String TABLESPACE = "TABLESPACE";
  
  public static final String PCTFREE = "PCTFREE";
  
  public static final String PCTUSED = "PCTUSED";
  
  public static final String INITRANS = "INITRANS";
  
  public static final String STORAGE = "STORAGE";
  
  public static final String INITIAL = "INITIAL";
  
  public static final String MINEXTENTS = "MINEXTENTS";
  
  public static final String MAXEXTENTS = "MAXEXTENTS";
  
  public static final String MAXSIZE = "MAXSIZE";
  
  public static final String PCTINCREASE = "PCTINCREASE";
  
  public static final String FREELISTS = "FREELISTS";
  
  public static final String OPTIMAL = "OPTIMAL";
  
  public static final String BUFFER_POOL = "BUFFER_POOL";
  
  public static final String RECYCLE = "RECYCLE";
  
  public static final String ENCRYPT = "ENCRYPT";
  
  public static final String LOGGING = "LOGGING";
  
  public static final String NOLOGGING = "NOLOGGING";
  
  public static final String FILESYSTEM_LIKE_LOGGING = "FILESYSTEM_LIKE_LOGGING";
  
  public static final String COMPRESS = "COMPRESS";
  
  public static final String NOCOMPRESS = "NOCOMPRESS";
  
  public static final String OVERFLOW = "OVERFLOW";
  
  public static final String LOB = "LOB";
  
  public static final String CHUNK = "CHUNK";
  
  public static final String PCTVERSION = "PCTVERSION";
  
  public static final String FREEPOOLS = "FREEPOOLS";
  
  public static final String RETENTION = "RETENTION";
  
  public static final String DEDUPLICATE = "DEDUPLICATE";
  
  public static final String KEEP_DUPLICATES = "KEEP_DUPLICATES";
  
  public static final String DECRYPT = "DECRYPT";
  
  public static final String CACHE = "CACHE";
  
  public static final String NOCACHE = "NOCACHE";
  
  public static final String VARRAY = "VARRAY";
  
  public static final String SUBPARTITIONS = "SUBPARTITIONS";
  
  public static final String SUBPARTITION = "SUBPARTITION";
  
  public static final String BEGIN = "BEGIN";
  
  public static final String ELSE = "ELSE";
  
  public static final String BEGIN_TRANSACTION = "BEGIN TRANSACTION";
  
  public static final String ROLLBACK_TRANSACTION = "ROLLBACK TRANSACTION";
  
  public static final String COMMIT_TRANSACTION = "COMMIT TRANSACTION";
  
  public static final String END = "END";
  
  public static final String FETCH_NEXT_FROM = "FETCH NEXT FROM";
  
  public static final String BEGIN_TRY = "BEGIN TRY";
  
  public static final String END_TRY = "END TRY";
  
  public static final String BEGIN_CATCH = "BEGIN CATCH";
  
  public static final String END_CATCH = "END CATCH";
  
  public static final String WITH_EXECUTE_AS = "WITH EXECUTE AS";
  
  public static final String RETURN_AS_VALUE = "RETURN AS VALUE";
  
  public static final String MERGE_UNION = "MERGE UNION";
  
  public static final String OPTION = "OPTION";
  
  public static final String WITH_VALUES = "WITH VALUES";
  
  public static final String EXECUTE = "EXECUTE";
  
  public static final String EXEC = "EXEC";
  
  public static final String OUTPUT = "OUTPUT";
  
  public static final String IN = "IN";
  
  public static final String TABLE = "TABLE";
  
  public static final String AS = "AS";
  
  public static final String REBUILD = "REBUILD";
  
  public static final String SUBSTR = "SUBSTR";
  
  public static final String POSITION = "POSITION";
  
  public static final String ASCII = "ASCII";
  
  public static final String BIN = "BIN";
  
  public static final String BIT_LENGTH = "BIT_LENGTH";
  
  public static final String CHAR_LENGTH = "CHAR_LENGTH";
  
  public static final String CHARACTER_LENGTH = "CHARACTER_LENGTH";
  
  public static final String LENGTHB = "LENGTHB";
  
  public static final String LENGTH = "LENGTH";
  
  public static final String CHR = "CHR";
  
  public static final String CONCAT = "CONCAT";
  
  public static final String CONCAT_WS = "CONCAT_WS";
  
  public static final String ELT = "ELT";
  
  public static final String FIELD = "FIELD";
  
  public static final String FIND_IN_SET = "FIND_IN_SET";
  
  public static final String INSTR = "INSTR";
  
  public static final String LCASE = "LCASE";
  
  public static final String LOWER = "LOWER";
  
  public static final String LEFT = "LEFT";
  
  public static final String LOCATE = "LOCATE";
  
  public static final String LPAD = "LPAD";
  
  public static final String LTRIM = "LTRIM";
  
  public static final String MID = "MID";
  
  public static final String OCTET_LENGTH = "OCTET_LENGTH";
  
  public static final String REPEAT = "REPEAT";
  
  public static final String REPLACE = "REPLACE";
  
  public static final String REVERSE = "REVERSE";
  
  public static final String RIGHT = "RIGHT";
  
  public static final String RPAD = "RPAD";
  
  public static final String RTRIM = "RTRIM";
  
  public static final String SPACE = "SPACE";
  
  public static final String STRCMP = "STRCMP";
  
  public static final String SUBSTRING = "SUBSTRING";
  
  public static final String SUBSTRING_INDEX = "SUBSTRING_INDEX";
  
  public static final String TRANSLATE = "TRANSLATE";
  
  public static final String TRIM = "TRIM";
  
  public static final String UCASE = "UCASE";
  
  public static final String UPPER = "UPPER";
  
  public static final String ABS = "ABS";
  
  public static final String ACOS = "ACOS";
  
  public static final String ASIN = "ASIN";
  
  public static final String ATAN = "ATAN";
  
  public static final String ATAN2 = "ATAN2";
  
  public static final String CEIL = "CEIL";
  
  public static final String CONV = "CONV";
  
  public static final String COS = "COS";
  
  public static final String COT = "COT";
  
  public static final String DEGREES = "DEGREES";
  
  public static final String DRANDOM = "DRANDOM";
  
  public static final String DRAND = "DRAND";
  
  public static final String EXP = "EXP";
  
  public static final String FLOOR = "FLOOR";
  
  public static final String HEX = "HEX";
  
  public static final String LN = "LN";
  
  public static final String LOG2 = "LOG2";
  
  public static final String LOG10 = "LOG10";
  
  public static final String MOD = "MOD";
  
  public static final String PI = "PI";
  
  public static final String POW = "POW";
  
  public static final String POWER = "POWER";
  
  public static final String RADIANS = "RADIANS";
  
  public static final String RANDOM = "RANDOM";
  
  public static final String RAND = "RAND";
  
  public static final String ROUND = "ROUND";
  
  public static final String SIGN = "SIGN";
  
  public static final String SIN = "SIN";
  
  public static final String SQRT = "SQRT";
  
  public static final String TAN = "TAN";
  
  public static final String TRUNC = "TRUNC";
  
  public static final String TRUNCATE = "TRUNCATE";
  
  public static final String WIDTH_BUCKET = "WIDTH_BUCKET";
  
  public static final String ADDDATE = "ADDDATE";
  
  public static final String DATE_ADD = "DATE_ADD";
  
  public static final String ADDTIME = "ADDTIME";
  
  public static final String ADD_MONTHS = "ADD_MONTHS";
  
  public static final String CURDATE = "CURDATE";
  
  public static final String CURRENT_DATE = "CURRENT_DATE";
  
  public static final String SYS_DATE = "SYS_DATE";
  
  public static final String SYSDATE = "SYSDATE";
  
  public static final String CURRENT_DATETIME = "CURRENT_DATETIME";
  
  public static final String NOW = "NOW";
  
  public static final String SYS_DATETIME = "SYS_DATETIME";
  
  public static final String SYSDATETIME = "SYSDATETIME";
  
  public static final String CURTIME = "CURTIME";
  
  public static final String CURRENT_TIME = "CURRENT_TIME";
  
  public static final String SYS_TIME = "SYS_TIME";
  
  public static final String SYSTIME = "SYSTIME";
  
  public static final String CURRENT_TIMESTAMP = "CURRENT_TIMESTAMP";
  
  public static final String SYS_TIMESTAMP = "SYS_TIMESTAMP";
  
  public static final String SYSTIMESTAMP = "SYSTIMESTAMP";
  
  public static final String LOCALTIME = "LOCALTIME";
  
  public static final String LOCALTIMESTAMP = "LOCALTIMESTAMP";
  
  public static final String DATE = "DATE";
  
  public static final String DATEDIFF = "DATEDIFF";
  
  public static final String DATE_SUB = "DATE_SUB";
  
  public static final String SUBDATE = "SUBDATE";
  
  public static final String DAY = "DAY";
  
  public static final String DAYOFMONTH = "DAYOFMONTH";
  
  public static final String DAYOFWEEK = "DAYOFWEEK";
  
  public static final String DAYOFYEAR = "DAYOFYEAR";
  
  public static final String EXTRACT = "EXTRACT";
  
  public static final String FROM_DAYS = "FROM_DAYS";
  
  public static final String FROM_UNIXTIME = "FROM_UNIXTIME";
  
  public static final String HOUR = "HOUR";
  
  public static final String LAST_DAY = "LAST_DAY";
  
  public static final String MAKEDATE = "MAKEDATE";
  
  public static final String MAKETIME = "MAKETIME";
  
  public static final String MINUTE = "MINUTE";
  
  public static final String MONTH = "MONTH";
  
  public static final String MONTHS_BETWEEN = "MONTHS_BETWEEN";
  
  public static final String QUARTER = "QUARTER";
  
  public static final String SEC_TO_TIME = "SEC_TO_TIME";
  
  public static final String SECOND = "SECOND";
  
  public static final String TIME = "TIME";
  
  public static final String TIME_TO_SEC = "TIME_TO_SEC";
  
  public static final String TIMEDIFF = "TIMEDIFF";
  
  public static final String TIMESTAMP = "TIMESTAMP";
  
  public static final String TO_DAYS = "TO_DAYS";
  
  public static final String UNIX_TIMESTAMP = "UNIX_TIMESTAMP";
  
  public static final String UTC_DATE = "UTC_DATE";
  
  public static final String UTC_TIME = "UTC_TIME";
  
  public static final String WEEK = "WEEK";
  
  public static final String WEEKDAY = "WEEKDAY";
  
  public static final String YEAR = "YEAR";
  
  public static final String BIT_TO_BLOB = "BIT_TO_BLOB";
  
  public static final String BLOB_FROM_FILE = "BLOB_FROM_FILE";
  
  public static final String BLOB_LENGTH = "BLOB_LENGTH";
  
  public static final String BLOB_TO_BIT = "BLOB_TO_BIT";
  
  public static final String CHAR_TO_BLOB = "CHAR_TO_BLOB";
  
  public static final String CHAR_TO_CLOB = "CHAR_TO_CLOB";
  
  public static final String CLOB_FROM_FILE = "CLOB_FROM_FILE";
  
  public static final String CLOB_LENGTH = "CLOB_LENGTH";
  
  public static final String CLOB_TO_CHAR = "CLOB_TO_CHAR";
  
  public static final String CAST = "CAST";
  
  public static final String DATE_FORMAT = "DATE_FORMAT";
  
  public static final String FORMAT = "FORMAT";
  
  public static final String STR_TO_DATE = "STR_TO_DATE";
  
  public static final String TIME_FORMAT = "TIME_FORMAT";
  
  public static final String TO_CHAR = "TO_CHAR";
  
  public static final String TO_DATE = "TO_DATE";
  
  public static final String TO_DATETIME = "TO_DATETIME";
  
  public static final String TO_NUMBER = "TO_NUMBER";
  
  public static final String TO_TIME = "TO_TIME";
  
  public static final String TO_TIMESTAMP = "TO_TIMESTAMP";
  
  public static final String AVG = "AVG";
  
  public static final String COUNT = "COUNT";
  
  public static final String CUME_DIST = "CUME_DIST";
  
  public static final String DENSE_RANK = "DENSE_RANK";
  
  public static final String FIRST_VALUE = "FIRST_VALUE";
  
  public static final String GROUP_CONCAT = "GROUP_CONCAT";
  
  public static final String LAG = "LAG";
  
  public static final String LAST_VALUE = "LAST_VALUE";
  
  public static final String LEAD = "LEAD";
  
  public static final String MAX = "MAX";
  
  public static final String MEDIAN = "MEDIAN";
  
  public static final String MIN = "MIN";
  
  public static final String NTH_VALUE = "NTH_VALUE";
  
  public static final String NTILE = "NTILE";
  
  public static final String PERCENT_RANK = "PERCENT_RANK";
  
  public static final String RANK = "RANK";
  
  public static final String ROW_NUMBER = "ROW_NUMBER";
  
  public static final String STDDEV = "STDDEV";
  
  public static final String STDDEV_POP = "STDDEV_POP";
  
  public static final String STDDEV_SAMP = "STDDEV_SAMP";
  
  public static final String SUM = "SUM";
  
  public static final String VARIANCE = "VARIANCE";
  
  public static final String VAR_POP = "VAR_POP";
  
  public static final String VAR_SAMP = "VAR_SAMP";
  
  public static final String INCR = "INCR";
  
  public static final String DECR = "DECR";
  
  public static final String ROWNUM = "ROWNUM";
  
  public static final String INST_NUM = "INST_NUM";
  
  public static final String GROUPBY_NUM = "GROUPBY_NUM";
  
  public static final String ORDERBY_NUM = "ORDERBY_NUM";
  
  public static final String COERCIBILITY = "COERCIBILITY";
  
  public static final String COLLATION = "COLLATION";
  
  public static final String CURRENT_USER = "CURRENT_USER";
  
  public static final String DATABASE = "DATABASE";
  
  public static final String SCHEMA = "SCHEMA";
  
  public static final String INDEX_CARDINALITY = "INDEX_CARDINALITY";
  
  public static final String INET_ATON = "INET_ATON";
  
  public static final String INET_NTOA = "INET_NTOA";
  
  public static final String LAST_INSERT_ID = "LAST_INSERT_ID";
  
  public static final String LIST_DBS = "LIST_DBS";
  
  public static final String ROW_COUNT = "ROW_COUNT";
  
  public static final String USER = "USER";
  
  public static final String SYSTEM_USER = "SYSTEM_USER";
  
  public static final String VERSION = "VERSION";
  
  public static final String MD5 = "MD5";
  
  public static final String COALESCE = "COALESCE";
  
  public static final String DECODE = "DECODE";
  
  public static final String GREATEST = "GREATEST";
  
  public static final String IF = "IF";
  
  public static final String IFNULL = "IFNULL";
  
  public static final String NVL = "NVL";
  
  public static final String ISNULL = "ISNULL";
  
  public static final String LEAST = "LEAST";
  
  public static final String NULLIF = "NULLIF";
  
  public static final String NVL2 = "NVL2";
}
