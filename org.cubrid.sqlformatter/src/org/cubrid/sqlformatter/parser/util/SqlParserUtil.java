package org.cubrid.sqlformatter.parser.util;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java_cup.runtime.Symbol;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.lang.StringUtils;
import org.cubrid.sqlformatter.parser.SqlParseTree;
import org.cubrid.sqlformatter.parser.constants.RegexPatterns;
import org.cubrid.sqlformatter.parser.constants.SqlNodeType;
import org.cubrid.sqlformatter.parser.constants.SqlParserConstants;
import org.cubrid.sqlformatter.parser.containers.DDLContentStarterKeywordContainer;
import org.cubrid.sqlformatter.parser.containers.DDLDetailParenKWContainer;
import org.cubrid.sqlformatter.parser.containers.DDLItemStarterKeywordContainer;
import org.cubrid.sqlformatter.parser.containers.ParameterContainer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SqlParserUtil implements SqlParserConstants {
	private static final DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
			.newInstance();

	private static DocumentBuilder docBuilder = null;

	static {
		try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	public static Element createCompoundKeywordElement(Symbol[] tokenList,
			int start, int end, Document doc) {
		Element compoundKeyword = doc.createElement(SqlNodeType.CompoundKeyword
				.name());
		while (start <= end) {
			String name = null;
			if (SqlTokenUtil.isSqlKeyword(tokenList[start])) {
				name = SqlNodeType.OtherKeyword.name();
			} else if (SqlTokenUtil.isWhitespace(tokenList[start])) {
				name = SqlNodeType.WhiteSpace.name();
			} else if (SqlTokenUtil.isComment(tokenList[start])) {
				Element commentElement = createCommentElement(tokenList, start,
						doc);
				compoundKeyword.appendChild(commentElement);
				start++;
				continue;
			}
			Element element = doc.createElement(name);
			element.setTextContent((String) (tokenList[start]).value);
			compoundKeyword.appendChild(element);
			start++;
		}
		return compoundKeyword;
	}

	public static Element createCommentElement(Symbol[] tokenList,
			int currentIndex, Document doc) {
		String comment = (tokenList[currentIndex]).value.toString().trim();
		Boolean multipleLine = Boolean.valueOf(false);
		Boolean precedingByLinebreak = Boolean.valueOf(false);
		Boolean followingByLinebreak = Boolean.valueOf(false);
		int tokenType = (tokenList[currentIndex]).sym;
		if (tokenType == 536870915) {
			multipleLine = Boolean.valueOf(true);
			if (currentIndex < tokenList.length - 1
					&& SqlTokenUtil
							.isWhitespaceWithLinebreak(tokenList[currentIndex + 1]))
				followingByLinebreak = Boolean.valueOf(true);
		} else {
			followingByLinebreak = Boolean.valueOf(true);
		}
		if (currentIndex > 0
				&& (SqlTokenUtil
						.isWhitespaceWithLinebreak(tokenList[currentIndex - 1]) || SqlTokenUtil
						.isSingleLineComment(tokenList[currentIndex - 1]))) {
			precedingByLinebreak = Boolean.valueOf(true);
		} else if (currentIndex > 1
				&& SqlTokenUtil.isWhitespace(tokenList[currentIndex - 1])
				&& SqlTokenUtil
						.isSingleLineComment(tokenList[currentIndex - 2])) {
			precedingByLinebreak = Boolean.valueOf(true);
		}
		Element commentElement = doc.createElement(SqlNodeType.Comment.name());
		commentElement.setTextContent(comment);
		commentElement.setAttribute("multipleLine", multipleLine.toString());
		commentElement.setAttribute("precedingByBR",
				precedingByLinebreak.toString());
		commentElement.setAttribute("followingByBR",
				followingByLinebreak.toString());
		return commentElement;
	}

	public static Element findFirstChildExceptCommentWhitespace(Element element) {
		Node child = element.getFirstChild();
		while (child != null
				&& (child.getNodeType() != 1
						|| SqlNodeTypeUtil.sameTagName(child,
								SqlNodeType.WhiteSpace) || SqlNodeTypeUtil
							.sameTagName(child, SqlNodeType.Comment)))
			child = child.getNextSibling();
		return (Element) child;
	}

	public static Element findFirstChildExceptWhitespace(Element element) {
		Node child = element.getFirstChild();
		while (child != null
				&& (child.getNodeType() != 1 || SqlNodeTypeUtil.sameTagName(
						child, SqlNodeType.WhiteSpace)))
			child = child.getNextSibling();
		return (Element) child;
	}

	public static Element findFirstNonWSNonCmtSiblingElement(Element element) {
		Element parent = (Element) element.getParentNode();
		Element sibling = findFirstChildExceptCommentWhitespace(parent);
		return (sibling == element) ? null : sibling;
	}

	public static List<Element> findCommentAtLast(Element element) {
		List<Element> comments = new ArrayList<Element>();
		if (element == null)
			return comments;
		Element lastNonWSChild = findLastNonWSChildElement(element);
		while (lastNonWSChild != null
				&& !SqlNodeTypeUtil.sameTagName(lastNonWSChild,
						SqlNodeType.Comment))
			lastNonWSChild = findLastNonWSChildElement(lastNonWSChild);
		Element firstCommentAfterLineBreak = null;
		while (lastNonWSChild != null
				&& SqlNodeTypeUtil.sameTagName(lastNonWSChild,
						SqlNodeType.Comment)) {
			boolean precedingByBR = Boolean.parseBoolean(lastNonWSChild
					.getAttribute("precedingByBR"));
			if (precedingByBR)
				firstCommentAfterLineBreak = lastNonWSChild;
			lastNonWSChild = getPreviousNonWSSiblingElement(lastNonWSChild);
		}
		Element nextSibling = firstCommentAfterLineBreak;
		while (nextSibling != null) {
			comments.add(nextSibling);
			nextSibling = getNextSiblingElement(nextSibling);
		}
		return comments;
	}

	public static boolean isEmptyStatement(Element statementElement) {
		Element firstNonWhiteSpaceChild = findFirstChildExceptWhitespace(statementElement);
		if (firstNonWhiteSpaceChild == null)
			return true;
		if (SqlNodeTypeUtil.sameTagName(firstNonWhiteSpaceChild,
				SqlNodeType.Comment))
			return false;
		firstNonWhiteSpaceChild = findFirstChildExceptWhitespace(firstNonWhiteSpaceChild);
		return (firstNonWhiteSpaceChild == null);
	}

	public static Element findLastNonWSNonCmtChildElement(Element element) {
		Node child = element.getLastChild();
		while (child != null
				&& (child.getNodeType() != 1
						|| SqlNodeTypeUtil.sameTagName(child,
								SqlNodeType.WhiteSpace) || SqlNodeTypeUtil
							.sameTagName(child, SqlNodeType.Comment)))
			child = child.getPreviousSibling();
		return (Element) child;
	}

	public static Element findLastNonWSChildElement(Element element) {
		Node child = element.getLastChild();
		while (child != null
				&& (child.getNodeType() != 1 || SqlNodeTypeUtil.sameTagName(
						(Element) child, SqlNodeType.WhiteSpace)))
			child = child.getPreviousSibling();
		return (Element) child;
	}

	public static Element findPreNonWSNonCmtSiblingElement(Element element) {
		Node preNonWSNonCmtSiblingElement = element.getPreviousSibling();
		while (preNonWSNonCmtSiblingElement != null
				&& (preNonWSNonCmtSiblingElement.getNodeType() != 1
						|| SqlNodeTypeUtil.sameTagName(
								(Element) preNonWSNonCmtSiblingElement,
								SqlNodeType.WhiteSpace) || SqlNodeTypeUtil
							.sameTagName(
									(Element) preNonWSNonCmtSiblingElement,
									SqlNodeType.Comment)))
			preNonWSNonCmtSiblingElement = preNonWSNonCmtSiblingElement
					.getPreviousSibling();
		return (Element) preNonWSNonCmtSiblingElement;
	}

	public static Element findNextNonWSNonCmtSiblingElement(Element element) {
		Node nextNonWSNonCmtSiblingElement = element.getNextSibling();
		while (nextNonWSNonCmtSiblingElement != null
				&& (nextNonWSNonCmtSiblingElement.getNodeType() != 1
						|| SqlNodeTypeUtil.sameTagName(
								(Element) nextNonWSNonCmtSiblingElement,
								SqlNodeType.WhiteSpace) || SqlNodeTypeUtil
							.sameTagName(
									(Element) nextNonWSNonCmtSiblingElement,
									SqlNodeType.Comment)))
			nextNonWSNonCmtSiblingElement = nextNonWSNonCmtSiblingElement
					.getNextSibling();
		return (Element) nextNonWSNonCmtSiblingElement;
	}

	public static Element getNextNonWSSiblingElement(Element element) {
		Node nextSibling = element.getNextSibling();
		while (nextSibling != null
				&& (nextSibling.getNodeType() != 1 || SqlNodeTypeUtil
						.sameTagName((Element) nextSibling,
								SqlNodeType.WhiteSpace)))
			nextSibling = nextSibling.getNextSibling();
		return (Element) nextSibling;
	}

	public static Element getPreviousNonWSSiblingElement(Element element) {
		Node preSibling = element.getPreviousSibling();
		while (preSibling != null
				&& (preSibling.getNodeType() != 1 || SqlNodeTypeUtil
						.sameTagName((Element) preSibling,
								SqlNodeType.WhiteSpace)))
			preSibling = preSibling.getPreviousSibling();
		return (Element) preSibling;
	}

	public static Element getNextSiblingElement(Element element) {
		Node nextSibling = element.getNextSibling();
		while (nextSibling != null && !(nextSibling instanceof Element))
			nextSibling = nextSibling.getNextSibling();
		return (Element) nextSibling;
	}

	public static Element getPreviousSiblingElement(Element element) {
		Node previousSibling = element.getPreviousSibling();
		while (previousSibling != null && !(previousSibling instanceof Element))
			previousSibling = previousSibling.getPreviousSibling();
		return (Element) previousSibling;
	}

	public static List<Element> getChildElements(Element parent) {
		List<Element> children = new ArrayList<Element>();
		if (parent == null)
			return children;
		NodeList nodes = parent.getChildNodes();
		int length = nodes.getLength();
		for (int i = 0; i < length; i++) {
			Node node = nodes.item(i);
			if (node.getNodeType() == 1)
				children.add((Element) node);
		}
		return children;
	}

	public static List<Element> getChildElements(Element parent,
			String childElementName) {
		List<Element> children = new ArrayList<Element>();
		if (parent == null || childElementName == null)
			return children;
		NodeList nodes = parent.getChildNodes();
		int length = nodes.getLength();
		for (int i = 0; i < length; i++) {
			Node node = nodes.item(i);
			if (node.getNodeType() == 1
					&& node.getNodeName().equals(childElementName))
				children.add((Element) node);
		}
		return children;
	}

	public static String getClauseStartKeyword(Element clauseElement) {
		Element firstNonWSNonCmtChildElement = findFirstChildExceptCommentWhitespace(clauseElement);
		if (firstNonWSNonCmtChildElement == null)
			return "";
		String eName = firstNonWSNonCmtChildElement.getTagName();
		if (eName.equals(SqlNodeType.OtherKeyword.toString()))
			return firstNonWSNonCmtChildElement.getTextContent();
		if (eName.equals(SqlNodeType.CompoundKeyword.toString()))
			return getCompoundKeyword(firstNonWSNonCmtChildElement);
		return "";
	}

	public static String getCompoundKeyword(Element compoundKeywordElement) {
		if (!elementNameMatches(compoundKeywordElement,
				SqlNodeType.CompoundKeyword))
			return null;
		StringBuilder compoundKeyword = new StringBuilder();
		Element childElement = (Element) compoundKeywordElement.getFirstChild();
		while (childElement != null) {
			if (SqlNodeTypeUtil.sameTagName(childElement,
					SqlNodeType.OtherKeyword)) {
				compoundKeyword.append(childElement.getTextContent());
			} else if (SqlNodeTypeUtil.sameTagName(childElement,
					SqlNodeType.WhiteSpace)) {
				compoundKeyword.append(" ");
			}
			childElement = (Element) childElement.getNextSibling();
		}
		return compoundKeyword.toString();
	}

	public static boolean hasNonWhiteSNonCommentChildElement(Element element) {
		Element firstNonWSNonCmtChildElement = findFirstChildExceptCommentWhitespace(element);
		return (firstNonWSNonCmtChildElement != null);
	}

	public static Element initSQLParseDoc2(Document sqlParseDoc) {
		Element parent = sqlParseDoc.createElement(SqlNodeType.SqlRoot
				.toString());
		sqlParseDoc.appendChild(parent);
		return startNewStatementInSqlRoot(sqlParseDoc);
	}

	public static Element initSQLParseTree(Document sqlParseDoc) {
		Element parent = sqlParseDoc.createElement(SqlNodeType.SqlRoot
				.toString());
		sqlParseDoc.appendChild(parent);
		return startNewStatementInSqlRoot(sqlParseDoc);
	}

	public static boolean isDDLDetailParenKWElement(Element element) {
		if (element == null)
			return false;
		String ddlDetailValue = "";
		if (SqlNodeTypeUtil.sameTagName(element, SqlNodeType.DataTypeKeyword)
				|| SqlNodeTypeUtil.sameTagName(element,
						SqlNodeType.OtherKeyword))
			ddlDetailValue = element.getTextContent();
		return DDLDetailParenKWContainer.INSTANCE.contains(ddlDetailValue);
	}

	public static boolean isFunctionParenElement(Element element) {
		if (element == null)
			return false;
		String keyword = "";
		return elementNameMatches(element, SqlNodeType.SqlFunction);
	}

	public static boolean isDDLDetailParensElement(Element element) {
		if (element == null)
			return false;
		return SqlNodeTypeUtil
				.sameTagName(element, SqlNodeType.DDLDetailParens);
	}

	public static boolean isPivotElement(Element element) {
		String sqlKeywords = getSqlKeywords(element);
		return (StringUtils.startsWithIgnoreCase(sqlKeywords, "PIVOT") || StringUtils
				.startsWithIgnoreCase(sqlKeywords, "UNPIVOT"));
	}

	public static boolean isClauseElement(Element element) {
		return (element != null && SqlNodeTypeUtil.sameTagName(element,
				SqlNodeType.SqlClause));
	}

	public static boolean isStatementElement(Element element) {
		return (element != null && SqlNodeTypeUtil.sameTagName(element,
				SqlNodeType.SqlStatement));
	}

	public static boolean isDDLClause(Element element) {
		if (!isClauseElement(element))
			return false;
		Element firstNonWSNonCmtChildElement = findFirstChildExceptCommentWhitespace(element);
		if (isDDLOtherBlockElement(firstNonWSNonCmtChildElement))
			return true;
		return false;
	}

	public static boolean isDDLOtherBlockElement(Element element) {
		return SqlNodeTypeUtil.sameTagName(element, SqlNodeType.DDLOtherBlock);
	}

	public static boolean isDDLProcedureBlockElement(Element element) {
		return SqlNodeTypeUtil.sameTagName(element,
				SqlNodeType.DDLProcedureBlock);
	}

	public static boolean isCursorDeclarationElement(Element element) {
		return SqlNodeTypeUtil.sameTagName(element,
				SqlNodeType.CursorDeclaration);
	}

	public static boolean isInControlFlowBlock(Element element) {
		return (SqlNodeTypeUtil.sameTagName(element,
				SqlNodeType.ControlFlowBlock) || SqlNodeTypeUtil
				.sameTagName((Element) element.getParentNode(),
						SqlNodeType.ControlFlowBlock));
	}

	public static boolean isInControlFlowClause(Element element) {
		return (SqlNodeTypeUtil.sameTagName(element,
				SqlNodeType.ControlFlowClause) || SqlNodeTypeUtil.sameTagName(
				(Element) element.getParentNode(),
				SqlNodeType.ControlFlowClause));
	}

	public static boolean isBeginEndControlFlowBlock(Element element) {
		if (!isControlFlowBlock(element))
			return false;
		Element firstChild = findFirstChildExceptCommentWhitespace(element);
		return isControlFlowKeyword(firstChild, "BEGIN");
	}

	public static boolean isControlFlowBlockStartWithBegin(Element element) {
		if (!isControlFlowBlock(element))
			return false;
		Element firstChild = findFirstChildExceptCommentWhitespace(element);
		return isControlFlowKeywordStartWith(firstChild, "BEGIN");
	}

	public static boolean isControlFlowBlock(Element element) {
		return SqlNodeTypeUtil.sameTagName(element,
				SqlNodeType.ControlFlowBlock);
	}

	public static boolean isControlFlowClause(Element element) {
		return SqlNodeTypeUtil.sameTagName(element,
				SqlNodeType.ControlFlowClause);
	}

	public static boolean isIfControlFlowClause(Element element) {
		if (!isControlFlowClause(element))
			return false;
		Element firstChild = findFirstChildExceptCommentWhitespace(element);
		return isControlFlowKeyword(firstChild, "IF");
	}

	public static boolean isElseControlFlowClause(Element element) {
		if (!isControlFlowClause(element))
			return false;
		Element firstChild = findFirstChildExceptCommentWhitespace(element);
		return isControlFlowKeyword(firstChild, "ELSE");
	}

	public static boolean isControlFlowKeyword(Element element, String keyword) {
		if (!SqlNodeTypeUtil.sameTagName(element,
				SqlNodeType.ControlFlowStarter)
				&& !SqlNodeTypeUtil.sameTagName(element,
						SqlNodeType.ControlFlowEnd))
			return false;
		String keywords = getSqlKeywords((Element) element.getFirstChild());
		return StringUtils.equalsIgnoreCase(keywords, keyword);
	}

	public static boolean isControlFlowKeywordStartWith(Element element,
			String keyword) {
		if (!SqlNodeTypeUtil.sameTagName(element,
				SqlNodeType.ControlFlowStarter)
				&& !SqlNodeTypeUtil.sameTagName(element,
						SqlNodeType.ControlFlowEnd))
			return false;
		String keywords = getSqlKeywords((Element) element.getFirstChild());
		return StringUtils.startsWithIgnoreCase(keywords, keyword);
	}

	public static boolean isDDLItemElement(Element element) {
		return elementNameMatches(element, SqlNodeType.DDLItem);
	}

	public static boolean isAlterDDLOtherBlockElement(Element element) {
		if (!isDDLOtherBlockElement(element))
			return false;
		Element firstChild = findFirstChildExceptCommentWhitespace(element);
		return isSqlKeywordElementStartsWith(firstChild, "ALTER");
	}

	public static boolean isInsideAlterDDLOtherBlock(Element element) {
		if (isAlterDDLOtherBlockElement(element))
			return true;
		Element ddlOtherBlockAncestor = findAncestor(element,
				SqlNodeType.DDLOtherBlock);
		return isAlterDDLOtherBlockElement(ddlOtherBlockAncestor);
	}

	public static boolean isInsideCursorDeclaration(Element element) {
		return (findAncestor(element, SqlNodeType.CursorDeclaration) != null);
	}

	public static boolean isInsideDDLItem(Element element) {
		if (isDDLItemElement(element))
			return true;
		Element ddlOtherBlockAncestor = findAncestor(element,
				SqlNodeType.DDLItem);
		return (ddlOtherBlockAncestor != null);
	}

	public static boolean isDDLContentBlockElement(Element element) {
		return SqlNodeTypeUtil
				.sameTagName(element, SqlNodeType.DDLContentBlock);
	}

	public static boolean isDDLAsBlockElement(Element element) {
		return SqlNodeTypeUtil.sameTagName(element, SqlNodeType.DDLAsBlock);
	}

	public static Element findDDLContentAncestor(Element element) {
		return findAncestor(element, SqlNodeType.DDLContentBlock);
	}

	public static Element findIndexHintAncestor(Element element) {
		return findAncestor(element, SqlNodeType.IndexHint);
	}

	public static Element findAncestor(Element element,
			SqlNodeType ancestorNodeType) {
		if (element == null)
			return null;
		Node parent = element;
		while (parent != null) {
			if (parent instanceof Element
					&& elementNameMatches((Element) parent, ancestorNodeType))
				return (Element) parent;
			parent = parent.getParentNode();
		}
		return null;
	}

	public static boolean isInsertOrValuesClause(
			Element firstNonWSNonCmtChildElement) {
		if (firstNonWSNonCmtChildElement == null)
			return false;
		String elementName = firstNonWSNonCmtChildElement.getTagName();
		if (elementName.equals(SqlNodeType.OtherKeyword.toString())) {
			String keyword = firstNonWSNonCmtChildElement.getTextContent()
					.toUpperCase();
			if (keyword.equals("INSERT") || keyword.equals("VALUES"))
				return true;
		} else if (elementName.equals(SqlNodeType.CompoundKeyword.toString())) {
			String firstKeyword = findFirstChildExceptCommentWhitespace(
					firstNonWSNonCmtChildElement).getTextContent()
					.toUpperCase();
			if (firstKeyword.equals("INSERT"))
				return true;
		}
		return false;
	}

	public static boolean isInsertClause(Element clauseElement) {
		return isClauseStartsWith(clauseElement, "INSERT");
	}

	public static boolean isClauseStartsWith(Element clauseElement,
			String sqlKeywords) {
		if (!isClauseElement(clauseElement))
			return false;
		String clauseStartKeyword = getClauseStartKeyword(clauseElement);
		return StringUtils
				.startsWithIgnoreCase(clauseStartKeyword, sqlKeywords);
	}

	private static boolean isOptionClause(Element clauseElement) {
		return isClauseStartsWith(clauseElement, "OPTION");
	}

	public static boolean isInInsertClause(SqlParseTree sqlParseTree) {
		Element currentContainer = sqlParseTree.getCurrentContainer();
		if (isInsertClause(currentContainer))
			return true;
		if (isClauseStartsWith(currentContainer, "INTO")
				&& isInsertClause(findPreNonWSNonCmtSiblingElement(currentContainer)))
			return true;
		if (isClauseStartsWith(currentContainer, "OUTPUT")) {
			Element previous = findPreNonWSNonCmtSiblingElement(currentContainer);
			if (isInsertClause(previous))
				return true;
			previous = findPreNonWSNonCmtSiblingElement(previous);
			if (isInsertClause(previous))
				return true;
			return false;
		}
		if (isClauseElement(currentContainer)
				&& sqlParseTree.ancestorNameMatches(1,
						SqlNodeType.MultipleInsertBlock))
			return true;
		if (isClauseElement(currentContainer)
				&& sqlParseTree.ancestorNameMatches(1,
						SqlNodeType.BranchContentBody)
				&& ((sqlParseTree.ancestorNameMatches(2, SqlNodeType.Then)
						&& sqlParseTree
								.ancestorNameMatches(3, SqlNodeType.When) && sqlParseTree
							.ancestorNameMatches(4,
									SqlNodeType.MultipleInsertBlock)) || (sqlParseTree
						.ancestorNameMatches(2, SqlNodeType.Else) && sqlParseTree
						.ancestorNameMatches(3, SqlNodeType.MultipleInsertBlock))))
			return true;
		return false;
	}

	public static boolean isMultipleInsertBlock(Element element) {
		return elementNameMatches(element, SqlNodeType.MultipleInsertBlock);
	}

	public static boolean isSelectClause(Element clauseElement) {
		return isClauseStartsWith(clauseElement, "SELECT");
	}

	public static boolean elementNameMatches(Element element,
			SqlNodeType nodeType) {
		if (element == null || nodeType == null)
			return false;
		return StringUtils.equals(element.getTagName(), nodeType.toString());
	}

	public static boolean isIndexHintList(Element element) {
		return elementNameMatches(element, SqlNodeType.IndexHintList);
	}

	public static boolean isIndexHint(Element element) {
		return elementNameMatches(element, SqlNodeType.IndexHint);
	}

	public static boolean isSuitableToAddSelectionTargetParens(
			Element containerElement, Element firstNonWSNonCmtSibling,
			Element lastNonWSNonCmtSibling) {
		if (containerElement == null
				|| !containerElement.getTagName().equals(
						SqlNodeType.SelectionTarget.toString()))
			return false;
		if (firstNonWSNonCmtSibling == null)
			return true;
		if (isSqlKeywordElement(lastNonWSNonCmtSibling, "TABLE"))
			return true;
		return false;
	}

	public static boolean isSuitableToAddDDLParens(Element containerElement,
			Element firstNonWSNonCmtSibling, Element lastNonWSNonCmtSibling) {
		if (isDDLOtherBlockElement(containerElement)
				|| isDDLItemElement(containerElement)
				|| isDDLProcedureBlockElement(containerElement))
			return true;
		if (isClauseElement(containerElement)
				&& isSqlKeywordElement(firstNonWSNonCmtSibling, "VALUES"))
			return true;
		if (isSqlKeywordElementStartsWith(firstNonWSNonCmtSibling, "INSERT")
				&& firstNonWSNonCmtSibling != lastNonWSNonCmtSibling
				&& !isSqlKeywordElement(lastNonWSNonCmtSibling, "TABLE"))
			return true;
		return false;
	}

	public static boolean shouldTryToStartNewStatement(Symbol token,
			SqlParseTree sqlParseTree) {
		boolean shouldTryToStartNewStatement = SqlTokenUtil
				.isStatementStarter(token);
		if (!shouldTryToStartNewStatement)
			return shouldTryToStartNewStatement;
		Element container = sqlParseTree.getCurrentContainer();
		if (token.sym == 945553409) {
			shouldTryToStartNewStatement = sqlParseTree
					.shouldTryToStartSelectStatement();
		} else if ((isDDLOtherBlockElement(container) || isDDLItemElement(container))
				&& token.sym == 944046081) {
			shouldTryToStartNewStatement = false;
		} else if (isInsideAlterDDLOtherBlock(container)
				&& (token.sym == 945422338 || token.sym == 945553413
						|| token.sym == 945553414 || token.sym == 945553427)) {
			shouldTryToStartNewStatement = false;
		}
		return shouldTryToStartNewStatement;
	}

	public static boolean shouldTryToStartNewClause(Symbol token,
			SqlParseTree sqlParseTree) {
		if (!SqlTokenUtil.isClauseStarter(token))
			return false;
		Element container = sqlParseTree.getCurrentContainer();
		if (isInControlFlowBlock(container))
			return true;
		if (SqlTokenUtil.isToken(token, 945029133)
				&& isInsideCursorDeclaration(sqlParseTree.getCurrentContainer()))
			return false;
		if (SqlTokenUtil.isToken(token, 945553412)
				&& sqlParseTree.ancestorNameMatches(0,
						SqlNodeType.ContainerContentBody)
				&& sqlParseTree.ancestorNameMatches(1,
						SqlNodeType.CursorForOptions))
			return false;
		if ((isDDLOtherBlockElement(container) || isDDLItemElement(container))
				&& token.sym == 944046081)
			return false;
		if (SqlTokenUtil.isToken(token, 943980577)
				&& (isClauseStartsWith(container, "EXECUTE") || isClauseStartsWith(
						container, "EXEC")))
			return false;
		if (isInsideAlterDDLOtherBlock(sqlParseTree.getCurrentContainer())
				&& (token.sym == 945422338 || token.sym == 945553413
						|| token.sym == 945553414 || token.sym == 944046081))
			return false;
		if (token.sym == 943980547
				&& !isSelectClause(sqlParseTree.getCurrentContainer()))
			return false;
		if (token.sym == 945029133
				&& isIndexHint(sqlParseTree.getCurrentContainer()))
			return false;
		if (token.sym == 943980546 && !isInInsertClause(sqlParseTree))
			return false;
		if (token.sym == 945029139
				&& isDDLOtherBlockElement(sqlParseTree.getCurrentContainer()))
			return false;
		return true;
	}

	public static boolean shouldTryToStartDDLOtherBlock(Symbol token,
			SqlParseTree sqlParseTree) {
		if (!SqlTokenUtil.isDDLBlockStarter(token))
			return false;
		if (token.sym == 945422338
				&& isInsideAlterDDLOtherBlock(sqlParseTree
						.getCurrentContainer()))
			return false;
		return true;
	}

	public static boolean shouldTryToStartDDLProcedureBlock(Symbol[] tokenList,
			int currentIndex, SqlParseTree sqlParseTree) {
		if ((SqlTokenUtil.isToken(tokenList[currentIndex], 945422337) || SqlTokenUtil
				.isToken(tokenList[currentIndex], 945422338))
				&& SqlTokenUtil
						.nextTokenExceptCommentAndWhitespaceEqualsIgnoreCase(
								tokenList, currentIndex + 1, "PROCEDURE"))
			return true;
		return false;
	}

	public static boolean shouldTryToStartCursorDeclaration(Symbol[] tokenList,
			int currentIndex, SqlParseTree sqlParseTree) {
		Symbol token = tokenList[currentIndex];
		int index = SqlTokenUtil.INDEX_NOT_FOUND;
		if (!SqlTokenUtil.isToken(token, 944504844))
			return false;
		index = SqlTokenUtil.getNextTokenIndexExceptCommentAndWhitespace(
				tokenList, currentIndex + 1);
		if (index == SqlTokenUtil.INDEX_NOT_FOUND
				|| !SqlTokenUtil.isToken(tokenList[index], 671088641))
			return false;
		StringBuffer keywords = new StringBuffer();
		int keywordsCount = 3, i = 0;
		while (i < keywordsCount) {
			index = SqlTokenUtil.getNextTokenIndexExceptCommentAndWhitespace(
					tokenList, index + 1);
			if (index != SqlTokenUtil.INDEX_NOT_FOUND
					&& SqlTokenUtil.isSqlKeyword(tokenList[index])) {
				keywords.append((tokenList[index]).value.toString()
						.toUpperCase());
				keywords.append(" ");
				i++;
			}
		}
		Pattern cursorPatten = Pattern
				.compile("(INSENSITIVE )?(SCROLL )?CURSOR");
		Matcher cursorMatcher = cursorPatten
				.matcher(keywords.toString().trim());
		return cursorMatcher.find();
	}

	public static boolean shouldTryToStartCursorForBlock(Symbol[] tokenList,
			int currentIndex, SqlParseTree sqlParseTree) {
		return (SqlTokenUtil.isToken(tokenList[currentIndex], 945029133) && elementNameMatches(
				sqlParseTree.getCurrentContainer(),
				SqlNodeType.CursorDeclaration));
	}

	public static boolean shouldTryToStartCursorForOptions(Symbol[] tokenList,
			int currentIndex, SqlParseTree sqlParseTree) {
		return (SqlTokenUtil.isToken(tokenList[currentIndex], 945029133) && isInsideCursorDeclaration(sqlParseTree
				.getCurrentContainer()));
	}

	public static boolean shouldTryToStartDDLItem(
			String ddlItemStarterKeywords, SqlParseTree sqlParseTree) {
		Element currentContainer = sqlParseTree.getCurrentContainer();
		if (!DDLItemStarterKeywordContainer.INSTANCE
				.contains(ddlItemStarterKeywords))
			return false;
		if (isDDLOtherBlockElement(currentContainer)) {
			if (isClusterIndexDefinition(currentContainer,
					ddlItemStarterKeywords))
				return false;
			return true;
		}
		if (isDDLProcedureBlockElement(currentContainer))
			return true;
		if (isDDLItemElement(currentContainer)) {
			if (isColumnDefinition(currentContainer, ddlItemStarterKeywords))
				return false;
			if (isChangeCharacterSet(currentContainer, ddlItemStarterKeywords))
				return false;
			if (isIndexOption(currentContainer, ddlItemStarterKeywords))
				return false;
			if (StringUtils.equalsIgnoreCase(ddlItemStarterKeywords,
					"CLASS ATTRIBUTE")) {
				Element starter = findFirstChildExceptCommentWhitespace(currentContainer);
				if (isSqlKeywordElement(starter, "ADD")
						|| isSqlKeywordElement(starter, "CHANGE"))
					return false;
				return true;
			}
			return true;
		}
		if (elementNameMatches(currentContainer, SqlNodeType.DDLContentBlock)) {
			if (isColumnDefinition(currentContainer, ddlItemStarterKeywords))
				return false;
			return true;
		}
		if (isInsideDDLItem(currentContainer)) {
			sqlParseTree.escapeIndexHintList();
			sqlParseTree.escapeJoinOnSection();
			sqlParseTree.escapeAnyBetweenCondition();
			sqlParseTree.escapeAnySelectionTarget();
			sqlParseTree.escapePartialStatementContainer();
			if (sqlParseTree.ancestorNameMatches(0, SqlNodeType.SqlClause)
					&& (sqlParseTree.ancestorNameMatches(1,
							SqlNodeType.DDLAsBlock) || sqlParseTree
							.ancestorNameMatches(1, SqlNodeType.DDLContentBlock)))
				return true;
		}
		return false;
	}

	public static boolean shouldTryToStartDDLContentBlock(
			String ddlContentStarterKeywords, Symbol nextMeaningfulToken,
			SqlParseTree sqlParseTree) {
		if (StringUtils.equalsIgnoreCase(ddlContentStarterKeywords, "ADD")
				&& SqlTokenUtil.isSqlKeyword(nextMeaningfulToken))
			return false;
		return (DDLContentStarterKeywordContainer.INSTANCE
				.contains(ddlContentStarterKeywords) && isDDLItemElement(sqlParseTree
				.getCurrentContainer()));
	}

	public static boolean shouldTryToStartPartitionParamerter(String keywords,
			SqlParseTree sqlParseTree) {
		if (!ParameterContainer.INSTANCE.contains(keywords))
			return false;
		Element currentContainer = sqlParseTree.getCurrentContainer();
		if (isPartitionParen(currentContainer)
				|| isPartitionParameter(currentContainer)
				|| isParameterParen(currentContainer)
				|| isPartitionDDLItem(currentContainer))
			return true;
		return false;
	}

	private static boolean isClusterIndexDefinition(Element ddlOtherBlock,
			String sqlKeyword) {
		return (isDDLOtherBlockElement(ddlOtherBlock)
				&& areMeaningfulChildrenStartWith(ddlOtherBlock, 2,
						"CREATE INDEX")
				&& StringUtils.equalsIgnoreCase(sqlKeyword, "CLUSTER") && isSqlKeywordElement(
					findLastNonWSNonCmtChildElement(ddlOtherBlock), "ON"));
	}

	public static boolean areMeaningfulChildrenStartWith(Element container,
			int count, String sqlKeywords) {
		Element child = findFirstChildExceptCommentWhitespace(container);
		StringBuilder sourceSqlKeywords = new StringBuilder();
		while (count > 0) {
			String sourceSqlKeyword = getSqlKeywords(child);
			if (StringUtils.isEmpty(sourceSqlKeyword))
				return false;
			sourceSqlKeywords.append(sourceSqlKeyword);
			sourceSqlKeywords.append(" ");
			child = findNextNonWSNonCmtSiblingElement(child);
			count--;
		}
		return StringUtils.equalsIgnoreCase(
				sourceSqlKeywords.toString().trim(), sqlKeywords);
	}

	private static boolean isColumnDefinition(Element container,
			String sqlKeyword) {
		if (!isColumnDefinitionSqlKeyword(sqlKeyword))
			return false;
		if (isDDLItemElement(container)) {
			Element starter = findFirstChildExceptCommentWhitespace(container);
			if (!isSqlKeywordElement(starter, "CHANGE")
					&& !isSqlKeywordElement(starter, "MODIFY"))
				return false;
		} else if (!isDDLContentBlock(container, "COLUMN")
				&& !isDDLContentBlock(container, "CLASS ATTRIBUTE")) {
			return false;
		}
		return !containsSqlKeywordChildInLastSegment(container, sqlKeyword);
	}

	private static boolean isChangeCharacterSet(Element ddlItem,
			String sqlKeyword) {
		if (!isDDLItem(ddlItem, "CONVERT"))
			return false;
		if (!isMySQLCharacterRelatedSqlKeyword(sqlKeyword))
			return false;
		return !containsSqlKeywordChildInLastSegment(ddlItem, sqlKeyword);
	}

	private static boolean containsSqlKeywordChildInLastSegment(
			Element container, String sqlKeyword) {
		Element lastChild = findLastNonWSNonCmtChildElement(container);
		while (lastChild != null
				&& !elementNameMatches(lastChild, SqlNodeType.Comma)) {
			if (isSqlKeywordElement(lastChild, sqlKeyword))
				return true;
			lastChild = findPreNonWSNonCmtSiblingElement(lastChild);
		}
		return false;
	}

	private static boolean isMySQLCharacterRelatedSqlKeyword(String sqlKeyword) {
		return (StringUtils.equalsIgnoreCase(sqlKeyword, "CHARACTER SET") || StringUtils
				.equalsIgnoreCase(sqlKeyword, "COLLATE"));
	}

	private static boolean isIndexOption(Element ddlItem, String sqlKeyword) {
		if (!isIndexOptionSqlKeyword(sqlKeyword))
			return false;
		if (!isInIndexDDLItem(ddlItem))
			return false;
		Element lastChild = findLastNonWSNonCmtChildElement(ddlItem);
		while (lastChild != null
				&& !elementNameMatches(lastChild, SqlNodeType.DDLParens)
				&& !elementNameMatches(lastChild, SqlNodeType.DDLContentBlock)) {
			if (isSqlKeywordElement(lastChild, sqlKeyword))
				return false;
			lastChild = findPreNonWSNonCmtSiblingElement(lastChild);
		}
		return true;
	}

	private static boolean isDDLItem(Element container,
			String ddlItemStarterKeyword) {
		if (!isDDLItemElement(container))
			return false;
		Element starter = findFirstChildExceptCommentWhitespace(container);
		return isSqlKeywordElement(starter, ddlItemStarterKeyword);
	}

	private static boolean isDDLItem(Element container,
			Pattern starterKeywordPattern) {
		if (!isDDLItemElement(container))
			return false;
		Element starter = findFirstChildExceptCommentWhitespace(container);
		return isSqlKeywordElement(starter, starterKeywordPattern);
	}

	public static boolean isPartitionDDLItem(Element container) {
		return (isDDLItem(container, RegexPatterns.ORACLE_PARTITION_BY_COMPOUND)
				|| isDDLItem(container, "SUBPARTITION BY")
				|| isDDLItem(container, "PARTITION")
				|| isDDLItem(container, "SPLIT PARTITION") || isDDLItem(
					container, "ADD PARTITION"));
	}

	public static boolean isOrganizationDDLItem(Element container) {
		return isDDLItem(container, "ORGANIZATION");
	}

	public static boolean isPartitionParameter(Element container) {
		return elementNameMatches(container, SqlNodeType.PartitionParameter);
	}

	public static boolean isParameterParen(Element container) {
		return elementNameMatches(container, SqlNodeType.ParameterParen);
	}

	public static boolean isPartitionParen(Element container) {
		return elementNameMatches(container, SqlNodeType.PartitionParen);
	}

	public static boolean isSuitableToAddPartitionParen(
			SqlParseTree sqlParseTree, Symbol[] tokenList, int currentIndex) {
		Element currentContainer = sqlParseTree.getCurrentContainer();
		if (isPartitionDDLItem(currentContainer))
			return true;
		if (SqlTokenUtil.nextTokenExceptCommentAndWhitespaceEqualsIgnoreCase(
				tokenList, currentIndex + 1, "PARTITION")
				&& isLocalPartitionedIndex(currentContainer))
			return true;
		if (SqlTokenUtil.nextTokenExceptCommentAndWhitespaceEqualsIgnoreCase(
				tokenList, currentIndex + 1, "SUBPARTITION")
				&& (isPartitionParen(currentContainer) || isPartitionParameter(currentContainer)))
			return true;
		return false;
	}

	private static boolean isLocalPartitionedIndex(Element container) {
		Element parent = (Element) container.getParentNode();
		return (isDDLItem(container, "LOCAL") && isDDLOtherBlockElement(parent) && (areMeaningfulChildrenStartWith(
				parent, 2, "CREATE INDEX") || areMeaningfulChildrenStartWith(
				parent, 3, "CREATE BITMAP INDEX")));
	}

	public static boolean isSuitableToAddParameterParen(
			SqlParseTree sqlParseTree, Symbol[] tokenList, int currentIndex) {
		Element currentContainer = sqlParseTree.getCurrentContainer();
		if (isPartitionParameter(currentContainer))
			return true;
		if (isOrganizationDDLItem(currentContainer)
				&& SqlTokenUtil
						.previousTokenExceptCommentAndWhitespaceEqualsIgnoreCase(
								tokenList, currentIndex - 1, "EXTERNAL"))
			return true;
		if (isDDLItemElement(currentContainer)
				&& SqlTokenUtil
						.previousTokenExceptCommentAndWhitespaceEqualsIgnoreCase(
								tokenList, currentIndex - 1, "STORAGE"))
			return true;
		if (isOptionClause(currentContainer))
			return true;
		return false;
	}

	private static boolean isDDLContentBlock(Element container,
			String contentStarterKeyword) {
		if (!elementNameMatches(container, SqlNodeType.DDLContentBlock))
			return false;
		Element contentStarterElement = findPreNonWSNonCmtSiblingElement(container);
		return isSqlKeywordElement(contentStarterElement, contentStarterKeyword);
	}

	private static boolean containsSqlKeywordChild(Element parent,
			String sqlKeyword) {
		List<Element> children = getChildElements(parent);
		for (Element child : children) {
			if (isSqlKeywordElement(child, sqlKeyword))
				return true;
		}
		return false;
	}

	public static boolean containsChildElement(Element parent, SqlNodeType child) {
		List<Element> children = getChildElements(parent, child.toString());
		return !children.isEmpty();
	}

	private static boolean isColumnDefinitionSqlKeyword(String sqlKeyword) {
		if (StringUtils.isEmpty(sqlKeyword))
			return false;
		String ucSqlKeyword = sqlKeyword.toUpperCase();
		return (StringUtils.equals(ucSqlKeyword, "CHARACTER SET")
				|| StringUtils.equals(ucSqlKeyword, "CHARACTER_SET")
				|| StringUtils.equals(ucSqlKeyword, "COLLATE")
				|| StringUtils.equals(ucSqlKeyword, "AUTO_INCREMENT") || StringUtils
					.equals(ucSqlKeyword, "COMMENT"));
	}

	private static boolean isIndexOptionSqlKeyword(String sqlKeyword) {
		if (StringUtils.isEmpty(sqlKeyword))
			return false;
		String ucSqlKeyword = sqlKeyword.toUpperCase();
		return (StringUtils.equals(ucSqlKeyword, "KEY_BLOCK_SIZE")
				|| StringUtils.equals(ucSqlKeyword, "COMMENT")
				|| RegexPatterns.MYSQL_INDEX_OPTION_COMPOUND_USING.matcher(
						ucSqlKeyword).matches() || StringUtils.equals(
				ucSqlKeyword, "WITH PARSER"));
	}

	private static boolean isInIndexDDLItem(Element ddlItem) {
		Element child = findFirstChildExceptCommentWhitespace(ddlItem);
		if (!isSqlKeywordElement(child, "ADD"))
			return false;
		while ((child = findNextNonWSNonCmtSiblingElement(child)) != null
				&& !elementNameMatches(child, SqlNodeType.DDLParens)
				&& !elementNameMatches(child, SqlNodeType.DDLContentBlock)) {
			if (isIndexSqlKeywordElement(child))
				return true;
		}
		return false;
	}

	private static boolean isIndexSqlKeywordElement(Element element) {
		if (element == null)
			return false;
		if (elementNameMatches(element, SqlNodeType.OtherKeyword)) {
			String sqlKeyword = element.getTextContent().toUpperCase();
			if (StringUtils.equals(sqlKeyword, "KEY")
					|| StringUtils.equals(sqlKeyword, "INDEX")
					|| StringUtils.equals(sqlKeyword, "UNIQUE")
					|| StringUtils.equals(sqlKeyword, "PRIMARY")
					|| StringUtils.equals(sqlKeyword, "FOREIGN")
					|| StringUtils.equals(sqlKeyword, "CONSTRAINT")
					|| StringUtils.equals(sqlKeyword, "FULLTEXT")
					|| StringUtils.equals(sqlKeyword, "SPATIAL"))
				return true;
			return false;
		}
		return false;
	}

	public static boolean isSqlKeywordElement(Element element,
			String targetSqlKeyword) {
		String sourceSqlKeyword = getSqlKeywords(element);
		return StringUtils.equalsIgnoreCase(sourceSqlKeyword, targetSqlKeyword);
	}

	public static boolean isSqlKeywordElement(Element element,
			Pattern sqlKeywordPattern) {
		String sourceSqlKeyword = getSqlKeywords(element);
		return sqlKeywordPattern.matcher(sourceSqlKeyword).matches();
	}

	public static boolean isSqlKeywordElementStartsWith(Element element,
			String sqlKeyword) {
		String sourceSqlKeywords = getSqlKeywords(element);
		return StringUtils.startsWith(sourceSqlKeywords, sqlKeyword);
	}

	private static String getSqlKeywords(Element sqlKeywordElement) {
		if (sqlKeywordElement == null)
			return "";
		String tagName = sqlKeywordElement.getTagName();
		if (tagName.equals(SqlNodeType.OtherKeyword.toString()))
			return sqlKeywordElement.getTextContent().toUpperCase();
		if (tagName.equals(SqlNodeType.CompoundKeyword.toString()))
			return getCompoundKeyword(sqlKeywordElement).toUpperCase();
		return "";
	}

	public static boolean isInputElement(Element element) {
		return SqlNodeTypeUtil.sameTagName(element, SqlNodeType.Input);
	}

	public static boolean isThenElement(Element element) {
		return SqlNodeTypeUtil.sameTagName(element, SqlNodeType.Then);
	}

	public static boolean isCaseElement(Element element) {
		return SqlNodeTypeUtil.sameTagName(element, SqlNodeType.Case);
	}

	public static boolean isContainerClose(Element element) {
		return SqlNodeTypeUtil.sameTagName(element, SqlNodeType.ContainerClose);
	}

	public static boolean isWhen(Element element) {
		return SqlNodeTypeUtil.sameTagName(element, SqlNodeType.When);
	}

	public static boolean isElse(Element element) {
		return SqlNodeTypeUtil.sameTagName(element, SqlNodeType.Else);
	}

	public static boolean jumpFromContainer(SqlNodeType containerNodeType,
			SqlNodeType parentNodeType, SqlParseTree sqlParseTree) {
		if (elementNameMatches(sqlParseTree.getCurrentContainer(),
				parentNodeType))
			return true;
		if (elementNameMatches(sqlParseTree.getCurrentContainer(),
				containerNodeType)) {
			sqlParseTree.moveToAncestorContainer(1);
			return true;
		}
		Element ancestor = findAncestor(sqlParseTree.getCurrentContainer(),
				containerNodeType);
		if (ancestor != null) {
			sqlParseTree
					.setCurrentContainer((Element) ancestor.getParentNode());
			return true;
		}
		return false;
	}

	public static boolean jumpToContainer(SqlNodeType containerNodeType,
			SqlParseTree sqlParseTree) {
		Element ancestor = findAncestor(sqlParseTree.getCurrentContainer(),
				containerNodeType);
		if (ancestor != null) {
			sqlParseTree.setCurrentContainer(ancestor);
			return true;
		}
		return false;
	}

	public static boolean jumpToControlFlowBlockStartWithBegin(
			SqlParseTree sqlParseTree) {
		Element currentContainer = sqlParseTree.getCurrentContainer();
		Element ancestor = findAncestor(currentContainer,
				SqlNodeType.ControlFlowBlock);
		while (ancestor != null && !isControlFlowBlockStartWithBegin(ancestor)) {
			currentContainer = (Element) ancestor.getParentNode();
			ancestor = findAncestor(currentContainer,
					SqlNodeType.ControlFlowBlock);
		}
		if (isControlFlowBlockStartWithBegin(ancestor)) {
			sqlParseTree.setCurrentContainer(ancestor);
			return true;
		}
		return false;
	}

	public static void moveComments(Element from, Element to, Element before) {
		List<Element> commentsAfterClause = findCommentAtLast(from);
		if (!commentsAfterClause.isEmpty()) {
			Element oldCommentParent = (Element) ((Element) commentsAfterClause
					.get(0)).getParentNode();
			for (Element e : commentsAfterClause) {
				oldCommentParent.removeChild(e);
				to.insertBefore(e, before);
			}
		}
	}

	public static void main(String[] args) {
		Document doc = newSQLParseDoc();
		Element container = initSQLParseDoc2(doc);
		Element child = doc.createElement(SqlNodeType.WhiteSpace.toString());
		child.setTextContent(" ");
		container.appendChild(child);
		System.out.println("tag name: " + doc.getDocumentElement().getTagName()
				+ "\n");
		System.out.println(doc.getDocumentElement());
		writeToXML(doc, "InitSQL.xml");
		System.out.println("Write done!");
	}

	public static Document newSQLParseDoc() {
		if (docBuilder == null) {
			return null;
		}
		return docBuilder.newDocument();
	}

	public static Element startNewStatementInSqlRoot(Document sqlParseDoc) {
		return startNewStatement(sqlParseDoc, sqlParseDoc.getDocumentElement());
	}

	public static Element startNewStatement(Document sqlParseDoc, Element parent) {
		Element child = sqlParseDoc.createElement(SqlNodeType.SqlStatement
				.toString());
		parent.appendChild(child);
		parent = child;
		child = sqlParseDoc.createElement(SqlNodeType.SqlClause.toString());
		parent.appendChild(child);
		return child;
	}

	private static void writeToXML(Document doc, String path) {
		try {
			OutputStream fileoutputStream = new FileOutputStream(path);
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(fileoutputStream);
			transformer.transform(source, result);
		} catch (Exception e) {
			System.out.println("Can't write to file: " + path);
		}
	}
}
