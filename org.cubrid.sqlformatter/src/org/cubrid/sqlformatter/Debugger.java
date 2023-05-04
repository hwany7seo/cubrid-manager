package org.cubrid.sqlformatter;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java_cup.runtime.Symbol;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.cubrid.sqlformatter.lexer.ISqlTokenType;
import org.w3c.dom.Document;

public class Debugger {
  public static void debugTokens(Symbol[] tokens) {
    for (Symbol token : tokens)
      System.out.println(" --<" + token.value + ">--<" + getTokenName(token.sym) + ">--"); 
  }
  
  private static String getTokenName(int token) {
    try {
      Field[] classFields = ISqlTokenType.class.getFields();
      for (int i = 0; i < classFields.length; i++) {
        if (classFields[i].getInt(null) == token)
          return classFields[i].getName(); 
      } 
    } catch (Exception e) {
      e.printStackTrace(System.err);
    } 
    return "UNKNOWN TOKEN";
  }
  
  public static void debugSqlParseTree(Document sqlParseTree) {
    writeToXML(sqlParseTree, "C:\\Users\\Administrator\\Desktop\\parseTree.xml");
  }
  
  private static void writeToXML(Document doc, String path) {
    try {
      OutputStream fileOutputStream = new FileOutputStream(path);
      TransformerFactory tFactory = TransformerFactory.newInstance();
      Transformer transformer = tFactory.newTransformer();
      DOMSource source = new DOMSource(doc);
      StreamResult result = new StreamResult(fileOutputStream);
      transformer.transform(source, result);
    } catch (Exception e) {
      System.out.println("Can't write to file: " + path);
    } 
  }
}
