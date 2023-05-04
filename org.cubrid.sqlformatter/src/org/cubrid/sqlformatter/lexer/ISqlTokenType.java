package org.cubrid.sqlformatter.lexer;

public interface ISqlTokenType {
  public static final int BIT_LENGTH_MAIN_TYPE = 5;
  
  public static final int BIT_LENGTH_SUB_TYPE = 15;
  
  public static final int BIT_LENGTH_SYMBOL_NUMBER = 12;
  
  public static final int BIT_LENGTH_SQL_KEYWORD_TYPE = 5;
  
  public static final int BIT_LENGTH_RESERVED = 3;
  
  public static final int BIT_OFFSET_MAIN_TYPE = 27;
  
  public static final int BIT_OFFSET_SUB_TYPE = 12;
  
  public static final int BIT_OFFSET_SQL_KEYWORD_TYPE = 22;
  
  public static final int MASK_MAIN_TYPE = -134217728;
  
  public static final int MASK_SUB_TYPE_SQL_KEYWORD_TYPE = 130023424;
  
  public static final int MASK_SUB_TYPE_STATEMENT_STARTER = 130547712;
  
  public static final int MASK_SUB_TYPE_CLAUSE_STARTER = 130285568;
  
  public static final int MASK_SUB_TYPE_DDLBLOCK_STARTER = 130154496;
  
  public static final int MASK_SUB_TYPE_SETOPERATOR_STARTER = 130088960;
  
  public static final int MASK_SUB_TYPE_DDLCONTENT_STARTER = 130056192;
  
  public static final int MASK_SUB_TYPE_CONTROL_FLOW = 130052096;
  
  public static final int MASK_COMPOUND_KEY_WORDS_STARTER = 1048576;
  
  public static final int OTHER = -134217728;
  
  public static final int MAIN_TYPE_VALUE = 134217728;
  
  public static final int MAIN_TYPE_BRACKET = 268435456;
  
  public static final int MAIN_TYPE_WHITE_SPACE = 402653184;
  
  public static final int MAIN_TYPE_COMMENT = 536870912;
  
  public static final int MAIN_TYPE_IDENTIFIER = 671088640;
  
  public static final int MAIN_TYPE_PUNCTUATION = 805306368;
  
  public static final int MAIN_TYPE_SQL_KEYWORD = 939524096;
  
  public static final int MAIN_TYPE_EOF = 1073741824;
  
  public static final int MAIN_TYPE_SESSION_COMMAND = 1207959552;
  
  public static final int NUMBER = 134217729;
  
  public static final int STRING = 134217730;
  
  public static final int MONETARY_VALUE = 134217731;
  
  public static final int BIT_VALUE = 134217732;
  
  public static final int OPEN_PAREN = 268435457;
  
  public static final int CLOSE_PAREN = 268435458;
  
  public static final int OPEN_BRACE = 268435459;
  
  public static final int CLOSE_BRACE = 268435460;
  
  public static final int OPEN_BRACKET = 268435461;
  
  public static final int CLOSE_BRACKET = 268435462;
  
  public static final int WHITE_SPACE = 402653185;
  
  public static final int SQL_STYLE_COMMENT = 536870913;
  
  public static final int CPLUSPLUS_STYLE_COMMENT = 536870914;
  
  public static final int C_STYLE_COMMENT = 536870915;
  
  public static final int IDENTIFIER = 671088641;
  
  public static final int COMMA = 805306369;
  
  public static final int DOT = 805306370;
  
  public static final int SEMICOLON = 805306371;
  
  public static final int SUB_TYPE_STARTER_KEYWORD = 4194304;
  
  public static final int SUB_TYPE_ALPHA_OPERATOR_KEYWORD = 8388608;
  
  public static final int SUB_TYPE_SPECIAL_SQL_KEYWORD = 12582912;
  
  public static final int SUB_TYPE_DATATYPE_SQL_KEYWORD = 16777216;
  
  public static final int SUB_TYPE_BREAKER_SQL_KEYWORD = 20971520;
  
  public static final int SUB_TYPE_FUNCTION_KEYWORD = 25165824;
  
  public static final int SUB_TYPE_HINT_SQL_KEYWORD = 29360128;
  
  public static final int SUB_TYPE_JOIN_SQL_KEYWORD = 33554432;
  
  public static final int SUB_TYPE_CONTROL_FLOW_KEYWORD = 37748736;
  
  public static final int SUB_TYPE_OTHER_SQL_KEYWORD = 130023424;
  
  public static final int SUB_TYPE_COMPOUND_KEY_WORDS_STARTER = 1048576;
  
  public static final int SUB_TYPE_DDLCONTENT_BREAKER = 21004288;
  
  public static final int SUB_TYPE_STATEMENT_STARTER = 4718592;
  
  public static final int SUB_TYPE_CLAUSE_STARTER = 4456448;
  
  public static final int SUB_TYPE_DDLBLOCK_STARTER = 4325376;
  
  public static final int SUB_TYPE_SETOPERATOR_STARTER = 4259840;
  
  public static final int SUB_TYPE_DDLCONTENT_STARTER = 4227072;
  
  public static final int SELECT = 945553409;
  
  public static final int INSERT = 945553410;
  
  public static final int DELETE = 945553411;
  
  public static final int UPDATE = 945553412;
  
  public static final int DROP = 945553413;
  
  public static final int RENAME = 945553414;
  
  public static final int PRINT = 944504839;
  
  public static final int ROLLBACK = 945553417;
  
  public static final int WAITFOR = 944504842;
  
  public static final int EXECUTE = 944504843;
  
  public static final int DECLARE = 944504844;
  
  public static final int EXEC = 944504845;
  
  public static final int FETCH = 945553422;
  
  public static final int OPEN = 944504847;
  
  public static final int CLOSE = 944504848;
  
  public static final int COMMIT = 945553425;
  
  public static final int UPDATETEXT = 944504850;
  
  public static final int MERGE = 945553427;
  
  public static final int REVOKE = 944504852;
  
  public static final int CALL = 944504853;
  
  public static final int VALUES = 943980545;
  
  public static final int VALUE = 943980546;
  
  public static final int TO = 943980547;
  
  public static final int INTO = 945029124;
  
  public static final int FROM = 943980549;
  
  public static final int LOG = 945029126;
  
  public static final int GROUP = 945029127;
  
  public static final int ORDER = 945029128;
  
  public static final int HAVING = 943980553;
  
  public static final int WHERE = 943980554;
  
  public static final int LIMIT = 943980555;
  
  public static final int FOR = 945029133;
  
  public static final int PROCEDURE = 943980558;
  
  public static final int LOCK = 945029135;
  
  public static final int CONNECT = 945029136;
  
  public static final int START = 945029137;
  
  public static final int RETURNING = 943980562;
  
  public static final int RETURN = 945029139;
  
  public static final int BREAK = 943980564;
  
  public static final int CONTINUE = 943980565;
  
  public static final int OPTION = 943980566;
  
  public static final int OUTPUT = 943980577;
  
  public static final int CREATE = 945422337;
  
  public static final int ALTER = 945422338;
  
  public static final int UNION = 944046081;
  
  public static final int DIFFERENCE = 944046082;
  
  public static final int INTERSECT = 944046083;
  
  public static final int INTERSECTION = 944046084;
  
  public static final int MINUS = 944046085;
  
  public static final int DDL_CONTENT_STARTER = 943751168;
  
  public static final int ALPHA_OPERATOR = 947912704;
  
  public static final int BETWEEN = 952107009;
  
  public static final int AND = 952107010;
  
  public static final int OR = 952107011;
  
  public static final int ON = 953155588;
  
  public static final int USING = 953155589;
  
  public static final int LEFT = 952107014;
  
  public static final int RIGHT = 952107015;
  
  public static final int CHARACTER = 953155592;
  
  public static final int SET = 953155594;
  
  public static final int AS = 953155595;
  
  public static final int REPLACE = 952107020;
  
  public static final int CASE = 952107021;
  
  public static final int WHEN = 952107022;
  
  public static final int THEN = 952107023;
  
  public static final int ELSE = 952107024;
  
  public static final int END = 952107025;
  
  public static final int GO = 952107026;
  
  public static final int DATA_TYPE_SQL_KEYWORD = 956301312;
  
  public static final int DDLCONTENT_BREAKER_KEYWORD = 960528384;
  
  public static final int HINT_SQL_KEYWORD = 968884224;
  
  public static final int JOIN_SQL_KEYWORD = 973078528;
  
  public static final int OTHER_SQL_KEYWORD = 1069547520;
  
  public static final int SUB_TYPE_CONTROL_FLOW_STARTER = 37752832;
  
  public static final int SUB_TYPE_CONTROL_FLOW_END = 37756928;
  
  public static final int CONTROL_FLOW_KEYWORD_STARTER = 977276928;
  
  public static final int CONTROL_FLOW_KEYWORD_END = 977281024;
  
  public static final int IF = 978849793;
  
  public static final int BEGIN = 978849794;
  
  public static final int WHILE = 977801219;
  
  public static final int DISTINCT = 1069547521;
  
  public static final int DISTINCTROW = 1069547522;
  
  public static final int UNIQUE = 1069547523;
  
  public static final int ALL = 1069547524;
  
  public static final int OTHER_COMPOUND_SQL_KEYWORD_STARTER = 1070596096;
  
  public static final int DEFAULT = 1070596101;
  
  public static final int CLASS = 1070596102;
  
  public static final int WITH = 1070596103;
  
  public static final int CURSOR = 1069547528;
  
  public static final int REBUILD = 1070596106;
  
  public static final int QUERY = 1069547531;
  
  public static final int EOF = 1073741825;
  
  public static final int SESSION_COMMAND = 1207959553;
  
  public static final int MAIN_TYPE_OTHER = -268435456;
  
  public static final int OTHER_OPERATOR = -268431360;
  
  public static final int POINT_TO = -268431359;
  
  public static final int ASTERISK = -268431358;
  
  public static final int SQL_FUNCTION = 964689920;
  
  public static final int NA = 0;
}
