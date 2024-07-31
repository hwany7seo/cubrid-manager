/*
 * Copyright (C) 2013 Search Solution Corporation. All rights reserved by Search
 * Solution.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: -
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. - Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. - Neither the name of the <ORGANIZATION> nor the names
 * of its contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */
package com.cubrid.common.ui.cubrid.table.action.makequery;

import com.cubrid.common.core.common.model.DBAttribute;
import com.cubrid.common.ui.cubrid.table.action.CopyToClipboardAction;
import com.cubrid.common.ui.query.format.SqlFormattingStrategy;
import com.cubrid.common.ui.spi.model.CubridDatabase;
import com.cubrid.common.ui.spi.model.DefaultSchemaNode;
import com.cubrid.common.ui.spi.util.SQLGenerateUtils;
import com.cubrid.cubridmanager.core.cubrid.database.model.DatabaseInfo;
import com.cubrid.cubridmanager.core.cubrid.table.task.GetAllAttrTask;
import java.util.List;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;

/**
 * Copy select statement SQL to clipboard
 *
 * @author pangqiren
 * @version 1.0 - 2010-8-3 created by pangqiren
 */
public class MakeSelectQueryAction extends CopyToClipboardAction {
    public static final String ID = MakeSelectQueryAction.class.getName();

    /**
     * The constructor
     *
     * @param id
     * @param shell
     * @param text
     * @param icon
     */
    public MakeSelectQueryAction(String id, Shell shell, String text, ImageDescriptor icon) {
        this(id, shell, null, text, icon);
    }

    /**
     * The constructor
     *
     * @param id
     * @param shell
     * @param provider
     * @param text
     * @param icon
     */
    public MakeSelectQueryAction(
            String id,
            Shell shell,
            ISelectionProvider provider,
            String text,
            ImageDescriptor icon) {
        super(shell, provider, text, icon);
        setId(id);
        setCopyToEditor(true);
    }

    /**
     * Sets this action support to select multi-object
     *
     * @see org.eclipse.jface.action.IAction.ISelectionAction
     * @return boolean
     */
    public boolean allowMultiSelections() {
        return true;
    }

    /**
     * Create select statement SQL
     *
     * @param schemaNode DefaultSchemaNode
     * @return String
     */
    protected String getStmtSQL(
            DefaultSchemaNode schemaNode,
            IEditorPart editorPart) { // FIXME move this logic to core module
        String sql = "";

        if (schemaNode != null) {
            CubridDatabase db = schemaNode.getDatabase();
            if (db == null) {
                return sql;
            }

            DatabaseInfo dbInfo = db.getDatabaseInfo();
            if (dbInfo == null) {
                return sql;
            }

            GetAllAttrTask task = new GetAllAttrTask(dbInfo);
            task.setClassName(schemaNode.getName());
            task.getAttrList();
            if (task.getErrorMsg() != null) {
                return sql;
            }

            List<DBAttribute> allAttrList = task.getAllAttrList();
            sql = SQLGenerateUtils.getSelectSQLNoWhere(schemaNode.getName(), allAttrList, true);
            sql = wrapShardSQL(schemaNode, editorPart, sql);
        }

        try {
            return new SqlFormattingStrategy().format(sql).trim();
        } catch (Exception ignored) {
            return sql;
        }
    }
}
