/*
 * Copyright (C) 2009 Search Solution Corporation. All rights reserved by Search
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
package com.cubrid.cubridmanager.ui.replication.action;

import com.cubrid.common.ui.spi.action.SelectionAction;
import com.cubrid.common.ui.spi.model.CubridDatabase;
import com.cubrid.common.ui.spi.model.ISchemaNode;
import com.cubrid.common.ui.spi.util.CommonUITool;
import com.cubrid.cubridmanager.core.common.model.ServerUserInfo;
import com.cubrid.cubridmanager.ui.cubrid.database.Messages;
import com.cubrid.cubridmanager.ui.replication.dialog.ChangeMasterDbDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.widgets.Shell;

/**
 * This action is responsible for changing master database.
 *
 * @author pangqiren
 * @version 1.0 - 2009-11-26 created by pangqiren
 */
public class ChangeMasterDbAction extends SelectionAction {

    public static final String ID = ChangeMasterDbAction.class.getName();

    /**
     * The constructor
     *
     * @param shell
     * @param text
     * @param icon
     */
    public ChangeMasterDbAction(Shell shell, String text, ImageDescriptor icon) {
        this(shell, null, text, icon);
    }

    /**
     * The constructor
     *
     * @param shell
     * @param provider
     * @param text
     * @param icon
     */
    public ChangeMasterDbAction(
            Shell shell, ISelectionProvider provider, String text, ImageDescriptor icon) {
        super(shell, provider, text, icon);
        this.setId(ID);
        this.setToolTipText(text);
    }

    /**
     * @see com.cubrid.common.ui.spi.action.ISelectionAction#allowMultiSelections ()
     * @return false
     */
    public boolean allowMultiSelections() {
        return false;
    }

    /**
     * @see com.cubrid.common.ui.spi.action.ISelectionAction#isSupported(java .lang.Object)
     * @param obj Object
     * @return boolean(whether to support)
     */
    public boolean isSupported(Object obj) {
        if (obj instanceof CubridDatabase) {
            CubridDatabase database = (CubridDatabase) obj;
            if (!database.isLogined()) {
                return false;
            }
            ServerUserInfo serverUserInfo =
                    database.getServer().getServerInfo().getLoginedUserInfo();
            if (serverUserInfo == null || !serverUserInfo.isAdmin()) {
                return false;
            }
            return database.isDistributorDb();
        }
        return false;
    }

    /** change master database */
    public void run() {
        Object[] obj = this.getSelectedObj();
        if (obj == null || obj.length == 0 || !isSupported(obj[0])) {
            setEnabled(false);
            return;
        }
        ISchemaNode node = (ISchemaNode) obj[0];
        CubridDatabase database = node.getDatabase();
        if (database == null) {
            CommonUITool.openErrorBox(getShell(), Messages.msgSelectDB);
            return;
        }

        ChangeMasterDbDialog changeMasterDbDialog = new ChangeMasterDbDialog(getShell());
        changeMasterDbDialog.setDatabase(database);
        changeMasterDbDialog.open();
    }
}
