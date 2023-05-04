package org.cubrid.sqlformatter.parser;

import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.cubrid.sqlformatter.parser.constants.SqlNodeType;
import org.cubrid.sqlformatter.parser.util.SqlParserUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class SqlParseTree {
    private Document doc = SqlParserUtil.newSQLParseDoc();

    private Element currentContainer = SqlParserUtil.initSQLParseTree(this.doc);

    public boolean ancestorNameMatches(int level, SqlNodeType sqlNodeType) {
        Element ancestor = this.currentContainer;
        while (level > 0) {
            Node parent = ancestor.getParentNode();
            if (parent == null || !(parent instanceof Element)) return false;
            ancestor = (Element) ancestor.getParentNode();
            level--;
        }
        return ancestor.getTagName().equals(sqlNodeType.toString());
    }

    public void considerStartNewClause() {
        escapeIndexHintList();
        escapeJoinOnSection();
        escapeAnyBetweenCondition();
        escapeAnySelectionTarget();
        escapePartialStatementContainer();
        Element clauseParent = (Element) this.currentContainer.getParentNode();
        String currentEName = this.currentContainer.getTagName();
        SqlNodeType nodeType = SqlNodeType.valueOf(currentEName);
        SqlNodeType parentNodeType = SqlNodeType.valueOf(clauseParent.getTagName());
        if (nodeType == SqlNodeType.SqlClause
                && SqlParserUtil.hasNonWhiteSNonCommentChildElement(this.currentContainer)) {
            List<Element> commentsAfterClause =
                    SqlParserUtil.findCommentAtLast(this.currentContainer);
            if (!commentsAfterClause.isEmpty()) {
                Element commentParent =
                        (Element) ((Element) commentsAfterClause.get(0)).getParentNode();
                for (Element e : commentsAfterClause) {
                    commentParent.removeChild(e);
                    clauseParent.appendChild(e);
                }
            }
            saveContainerElement(SqlNodeType.SqlClause, clauseParent);
        } else if (nodeType == SqlNodeType.InParens
                || nodeType == SqlNodeType.ExpressionParens
                || nodeType == SqlNodeType.SelectionTargetParens
                || nodeType == SqlNodeType.SqlStatement
                || nodeType == SqlNodeType.DDLContentBlock
                || nodeType == SqlNodeType.ControlFlowBlock
                || parentNodeType == SqlNodeType.ControlFlowBlock
                || nodeType == SqlNodeType.DDLContentBlock
                || nodeType == SqlNodeType.DDLAsBlock
                || nodeType == SqlNodeType.PivotParens
                || nodeType == SqlNodeType.MultipleInsertBlock
                || nodeType == SqlNodeType.BranchContentBody
                || nodeType == SqlNodeType.ContainerContentBody) {
            saveContainerElement(SqlNodeType.SqlClause);
        } else if (nodeType == SqlNodeType.ControlFlowTryCatch) {
            moveToAncestorContainer(1);
            considerStartNewClause();
        }
    }

    public boolean considerStartNewStatement() {
        escapeAnyBetweenCondition();
        escapeIndexHintList();
        escapeJoinOnSection();
        escapeAnySelectionTarget();
        escapePartialStatementContainer();
        SqlNodeType containerNodeType = SqlNodeType.valueOf(this.currentContainer.getTagName());
        Element container = this.currentContainer;
        boolean shouldStart = false;
        boolean stop = false;
        while (!stop) {
            switch (containerNodeType) {
                case SqlClause:
                    if (SqlParserUtil.hasNonWhiteSNonCommentChildElement(container)
                            && !SqlParserUtil.containsChildElement(
                                    container, SqlNodeType.SetOperatorClause)) {
                        container = (Element) container.getParentNode();
                        containerNodeType = SqlNodeType.valueOf(container.getTagName());
                        continue;
                    }
                    stop = true;
                    continue;
                case DDLOtherBlock:
                case DDLProcedureBlock:
                    container = (Element) container.getParentNode();
                    containerNodeType = SqlNodeType.valueOf(container.getTagName());
                    continue;
                case InParens:
                case ExpressionParens:
                case SelectionTargetParens:
                case DDLContentBlock:
                case DDLAsBlock:
                case ControlFlowBlock:
                case PivotParens:
                case MultipleInsertBlock:
                case BranchContentBody:
                    stop = true;
                    continue;
                case ControlFlowTryCatch:
                    container = (Element) container.getParentNode();
                    containerNodeType = SqlNodeType.valueOf(container.getTagName());
                    continue;
                case SqlStatement:
                    container = (Element) container.getParentNode();
                    shouldStart = true;
                    stop = true;
                    continue;
                case ControlFlowClause:
                    if (SqlParserUtil.containsChildElement(container, SqlNodeType.SqlClause)
                            || SqlParserUtil.containsChildElement(
                                    container, SqlNodeType.ControlFlowBlock)
                            || SqlParserUtil.containsChildElement(
                                    container, SqlNodeType.DDLOtherBlock)
                            || SqlParserUtil.containsChildElement(
                                    container, SqlNodeType.ControlFlowTryCatch)) {
                        container = (Element) container.getParentNode().getParentNode();
                        containerNodeType = SqlNodeType.valueOf(container.getTagName());
                        continue;
                    }
                    stop = true;
                    continue;
            }
            stop = true;
        }
        if (shouldStart) startNewStatementInSqlRoot();
        return shouldStart;
    }

    public boolean considerStartControlFlowBlock() {
        return considerStartNewStatementBlock(SqlNodeType.ControlFlowBlock);
    }

    public boolean considerStartElseControlFlowClause() {
        escapeIndexHintList();
        escapeJoinOnSection();
        escapeAnyBetweenCondition();
        escapeAnySelectionTarget();
        escapePartialStatementContainer();
        SqlNodeType containerNodeType = SqlNodeType.valueOf(this.currentContainer.getTagName());
        Element container = this.currentContainer;
        boolean shouldStart = false;
        boolean stop = false;
        while (!stop) {
            Element lastChild;
            switch (containerNodeType) {
                case SqlClause:
                    container = (Element) container.getParentNode();
                    containerNodeType = SqlNodeType.valueOf(container.getTagName());
                    continue;
                case ControlFlowClause:
                    if (SqlParserUtil.isIfControlFlowClause(container)) {
                        container = (Element) container.getParentNode();
                        shouldStart = true;
                        stop = true;
                    } else {
                        container = (Element) container.getParentNode().getParentNode();
                    }
                    containerNodeType = SqlNodeType.valueOf(container.getTagName());
                    continue;
                case ControlFlowBlock:
                    lastChild = SqlParserUtil.findLastNonWSNonCmtChildElement(container);
                    if (SqlParserUtil.isIfControlFlowClause(lastChild)) {
                        shouldStart = true;
                        stop = true;
                        continue;
                    }
                    if (SqlParserUtil.isElseControlFlowClause(lastChild)) {
                        container = (Element) container.getParentNode();
                        containerNodeType = SqlNodeType.valueOf(container.getTagName());
                        continue;
                    }
                    stop = true;
                    continue;
            }
            stop = true;
        }
        if (shouldStart) saveContainerElement(SqlNodeType.ControlFlowClause, container);
        return shouldStart;
    }

    public boolean considerStartTryCatch() {
        return considerStartNewStatementBlock(SqlNodeType.ControlFlowTryCatch);
    }

    private boolean considerStartNewStatementBlock(SqlNodeType statementBlockNodeType) {
        escapeIndexHintList();
        escapeJoinOnSection();
        escapeAnyBetweenCondition();
        escapeAnySelectionTarget();
        escapePartialStatementContainer();
        SqlNodeType containerNodeType = SqlNodeType.valueOf(this.currentContainer.getTagName());
        Element container = this.currentContainer;
        boolean shouldStart = false;
        boolean stop = false;
        while (!stop) {
            switch (containerNodeType) {
                case SqlClause:
                    if (SqlParserUtil.hasNonWhiteSNonCommentChildElement(container)) {
                        container = (Element) container.getParentNode();
                        containerNodeType = SqlNodeType.valueOf(container.getTagName());
                        continue;
                    }
                    shouldStart = true;
                    stop = true;
                    continue;
                case DDLOtherBlock:
                    container = (Element) container.getParentNode();
                    containerNodeType = SqlNodeType.valueOf(container.getTagName());
                    continue;
                case InParens:
                case ExpressionParens:
                case SelectionTargetParens:
                case DDLContentBlock:
                case DDLAsBlock:
                case ControlFlowBlock:
                case PivotParens:
                case MultipleInsertBlock:
                case BranchContentBody:
                    shouldStart = true;
                    stop = true;
                    continue;
                case ControlFlowTryCatch:
                    if (statementBlockNodeType == SqlNodeType.ControlFlowTryCatch) {
                        container = (Element) container.getParentNode();
                        containerNodeType = SqlNodeType.valueOf(container.getTagName());
                        continue;
                    }
                    if (statementBlockNodeType == SqlNodeType.ControlFlowBlock) {
                        shouldStart = true;
                        stop = true;
                        continue;
                    }
                    stop = true;
                    continue;
                case SqlStatement:
                    startNewStatementInSqlRoot();
                    container = this.currentContainer;
                    shouldStart = true;
                    stop = true;
                    continue;
                case ControlFlowClause:
                    if (SqlParserUtil.containsChildElement(container, SqlNodeType.SqlClause)
                            || SqlParserUtil.containsChildElement(
                                    container, SqlNodeType.ControlFlowBlock)
                            || SqlParserUtil.containsChildElement(
                                    container, SqlNodeType.DDLOtherBlock)
                            || SqlParserUtil.containsChildElement(
                                    container, SqlNodeType.ControlFlowTryCatch)) {
                        container = (Element) container.getParentNode().getParentNode();
                        containerNodeType = SqlNodeType.valueOf(container.getTagName());
                        continue;
                    }
                    shouldStart = true;
                    stop = true;
                    continue;
            }
            stop = true;
        }
        if (shouldStart) saveContainerElement(statementBlockNodeType, container);
        return shouldStart;
    }

    public boolean jumpDDLContentBlock() {
        return SqlParserUtil.jumpFromContainer(
                SqlNodeType.DDLContentBlock, SqlNodeType.DDLItem, this);
    }

    public boolean jumpDDLItem() {
        jumpDDLContentBlock();
        return SqlParserUtil.jumpFromContainer(
                SqlNodeType.DDLItem, SqlNodeType.DDLOtherBlock, this);
    }

    public boolean jumpIndexHint() {
        if (SqlParserUtil.isIndexHintList(this.currentContainer)) return true;
        if (SqlParserUtil.isIndexHint(this.currentContainer)) {
            moveToAncestorContainer(1);
            return true;
        }
        Element indexHintAncestor = SqlParserUtil.findIndexHintAncestor(this.currentContainer);
        if (indexHintAncestor != null) {
            this.currentContainer = (Element) indexHintAncestor.getParentNode();
            return true;
        }
        return false;
    }

    public boolean escapeIndexHintList() {
        if (jumpIndexHint() && SqlParserUtil.isIndexHintList(this.currentContainer)) {
            this.currentContainer = (Element) this.currentContainer.getParentNode();
            return true;
        }
        return false;
    }

    public boolean escapeInput() {
        if (SqlParserUtil.isInputElement(this.currentContainer)) {
            moveToAncestorContainer(1);
            return true;
        }
        return false;
    }

    private boolean escapeThen() {
        if (ancestorNameMatches(0, SqlNodeType.BranchContentBody)
                && ancestorNameMatches(1, SqlNodeType.Then)) {
            moveToAncestorContainer(2);
            return true;
        }
        if (ancestorNameMatches(0, SqlNodeType.SqlClause)
                && ancestorNameMatches(1, SqlNodeType.BranchContentBody)
                && ancestorNameMatches(2, SqlNodeType.Then)) {
            moveToAncestorContainer(3);
            return true;
        }
        return false;
    }

    public boolean escapeWhenCondition() {
        escapeAnyBetweenCondition();
        if (ancestorNameMatches(0, SqlNodeType.ContainerContentBody)
                && ancestorNameMatches(1, SqlNodeType.When)) {
            moveToAncestorContainer(1);
            return true;
        }
        return false;
    }

    public boolean escapeWhen() {
        if (escapeWhenCondition() || escapeThen()) {
            moveToAncestorContainer(1);
            return true;
        }
        return false;
    }

    public boolean escapeControlFlowClause() {
        Element container = this.currentContainer;
        int level = 1;
        while (!SqlParserUtil.elementNameMatches(container, SqlNodeType.ControlFlowClause)
                && !SqlParserUtil.elementNameMatches(container, SqlNodeType.SqlStatement)) {
            container = (Element) container.getParentNode();
            level++;
        }
        if (SqlParserUtil.elementNameMatches(container, SqlNodeType.SqlStatement)) return false;
        moveToAncestorContainer(level);
        return true;
    }

    public boolean escapeElse() {
        if (ancestorNameMatches(0, SqlNodeType.BranchContentBody)
                && ancestorNameMatches(1, SqlNodeType.Else)) {
            moveToAncestorContainer(2);
            return true;
        }
        return false;
    }

    public boolean escapeControlFlowBlock() {
        Element container = this.currentContainer;
        int level = 1;
        while (!SqlParserUtil.elementNameMatches(container, SqlNodeType.ControlFlowBlock)
                && !SqlParserUtil.elementNameMatches(container, SqlNodeType.SqlStatement)) {
            container = (Element) container.getParentNode();
            level++;
        }
        if (SqlParserUtil.elementNameMatches(container, SqlNodeType.SqlStatement)) return false;
        moveToAncestorContainer(level);
        return true;
    }

    public boolean escapeAnyBetweenCondition() {
        if (ancestorNameMatches(0, SqlNodeType.UpperBound)
                && ancestorNameMatches(1, SqlNodeType.Between)) {
            moveToAncestorContainer(2);
            return true;
        }
        return false;
    }

    public boolean escapeAnySelectionTarget() {
        if (SqlParserUtil.elementNameMatches(this.currentContainer, SqlNodeType.SelectionTarget)) {
            moveToAncestorContainer(1);
            return true;
        }
        return false;
    }

    public void escapeAnySingleOrPartialStatementContainers() {
        escapeAnyBetweenCondition();
        escapeIndexHintList();
        escapeJoinOnSection();
        escapeAnySelectionTarget();
        escapePartialStatementContainer();
    }

    public boolean escapeJoinOnSection() {
        if ((SqlParserUtil.elementNameMatches(this.currentContainer, SqlNodeType.OnCondition)
                        || SqlParserUtil.elementNameMatches(
                                this.currentContainer, SqlNodeType.SelectionTarget))
                && ancestorNameMatches(1, SqlNodeType.JoinOn)) {
            moveToAncestorContainer(2);
            return true;
        }
        return false;
    }

    public boolean escapePartialStatementContainer() {
        if (ancestorNameMatches(0, SqlNodeType.DDLOtherBlock)) {
            moveToAncestorContainer(1);
            return true;
        }
        return false;
    }

    public Element getCurrentContainer() {
        return this.currentContainer;
    }

    public Document getDoc() {
        return this.doc;
    }

    public void moveToAncestorContainer(int level) {
        while (level > 0) {
            this.currentContainer = (Element) this.currentContainer.getParentNode();
            level--;
        }
    }

    public void setCurrentContainer(Element currentContainer) {
        this.currentContainer = currentContainer;
    }

    public void saveContainerElement(SqlNodeType sqlNodeType) {
        saveContainerElement(sqlNodeType, this.currentContainer);
    }

    private void saveContainerElement(String elementName, Element targetParent) {
        Element e = this.doc.createElement(elementName);
        targetParent.appendChild(e);
        this.currentContainer = e;
    }

    public void saveContainerElement(SqlNodeType elementNodeType, Element targetParent) {
        saveContainerElement(elementNodeType.toString(), targetParent);
    }

    public void saveElement(Element e) {
        this.currentContainer.appendChild(e);
    }

    public void saveElementWithChild(SqlNodeType elementNodeType, Element child) {
        Element e = saveElement(elementNodeType);
        e.appendChild(child);
    }

    public Element saveElement(SqlNodeType sqlNodeType) {
        Element e = this.doc.createElement(sqlNodeType.toString());
        this.currentContainer.appendChild(e);
        return e;
    }

    public Element saveFinalElement(SqlNodeType sqlNodeType, String elementValue) {
        return saveFinalElement(sqlNodeType, elementValue, this.currentContainer);
    }

    public Element saveFinalElement(
            SqlNodeType sqlNodeType, String elementValue, Element targetParent) {
        Element e = this.doc.createElement(sqlNodeType.toString());
        e.setTextContent(elementValue);
        targetParent.appendChild(e);
        return e;
    }

    public boolean shouldTryToStartSelectStatement() {
        if (SqlParserUtil.isInInsertClause(this)) return false;
        if (SqlParserUtil.isDDLContentBlockElement(this.currentContainer)
                || SqlParserUtil.isDDLAsBlockElement(this.currentContainer)) return false;
        if (SqlParserUtil.isDDLOtherBlockElement(this.currentContainer)) return false;
        return true;
    }

    public boolean escapeMultipleInsertBlock() {
        if (!SqlParserUtil.isInInsertClause(this)) return false;
        if (SqlParserUtil.isInsertClause(this.currentContainer)) return true;
        Element multipleInsertBlock =
                SqlParserUtil.findAncestor(this.currentContainer, SqlNodeType.MultipleInsertBlock);
        if (multipleInsertBlock != null) {
            this.currentContainer = (Element) multipleInsertBlock.getParentNode();
            return true;
        }
        return false;
    }

    public boolean escapePartitionParameter() {
        if (!SqlParserUtil.isPartitionParameter(this.currentContainer)) return false;
        moveToAncestorContainer(1);
        return true;
    }

    public void startNewStatementInSqlRoot() {
        Element from = this.currentContainer;
        this.currentContainer = SqlParserUtil.startNewStatementInSqlRoot(this.doc);
        Element to = (Element) this.currentContainer.getParentNode();
        SqlParserUtil.moveComments(from, to, this.currentContainer);
    }

    public void startNewContainer(
            SqlNodeType newElementType, String containerOpenValue, SqlNodeType containerType) {
        saveContainerElement(newElementType);
        Element containerOpen = saveElement(SqlNodeType.ContainerOpen);
        saveFinalElement(SqlNodeType.OtherKeyword, containerOpenValue, containerOpen);
        saveContainerElement(containerType);
    }
}
