package org.cubrid.sqlformatter.parser.util;

import java.util.HashMap;
import java.util.Map;
import org.cubrid.sqlformatter.parser.constants.SqlNodeType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class SqlNodeTypeUtil {
  private static final Map<String, SqlNodeType> sqlNodeTypeMap = new HashMap<String, SqlNodeType>((SqlNodeType.values()).length);
  
  static {
    for (SqlNodeType sqlNodeType : SqlNodeType.values())
      sqlNodeTypeMap.put(sqlNodeType.name().toLowerCase(), sqlNodeType); 
  }
  
  public static boolean sameTagName(Node node, SqlNodeType sqlNodeType) {
    return sameTagName((Element)node, sqlNodeType);
  }
  
  public static boolean sameTagName(Element element, SqlNodeType sqlNodeType) {
    if (element == null || sqlNodeType == null)
      return false; 
    SqlNodeType sqlNodTypeFromElement = sqlNodeTypeMap.get(element.getTagName().toLowerCase());
    if (sqlNodTypeFromElement == null)
      return false; 
    return (sqlNodeType == sqlNodTypeFromElement);
  }
}
