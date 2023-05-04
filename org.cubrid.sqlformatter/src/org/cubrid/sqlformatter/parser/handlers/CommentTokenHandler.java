package org.cubrid.sqlformatter.parser.handlers;

import java_cup.runtime.Symbol;
import org.cubrid.sqlformatter.parser.SqlParseTree;
import org.cubrid.sqlformatter.parser.util.SqlParserUtil;
import org.w3c.dom.Element;

public class CommentTokenHandler implements SqlTokenHandler {
  public int handleToken(Symbol[] tokenList, int currentIndex, SqlParseTree sqlParseTree) {
    Element currentContainer = sqlParseTree.getCurrentContainer();
    Element commentElement = SqlParserUtil.createCommentElement(tokenList, currentIndex, sqlParseTree.getDoc());
    if (SqlParserUtil.isClauseElement(currentContainer) && SqlParserUtil.findFirstChildExceptCommentWhitespace(currentContainer) == null) {
      currentContainer.getParentNode().insertBefore(commentElement, currentContainer);
    } else {
      sqlParseTree.saveElement(commentElement);
    } 
    return currentIndex + 1;
  }
}
