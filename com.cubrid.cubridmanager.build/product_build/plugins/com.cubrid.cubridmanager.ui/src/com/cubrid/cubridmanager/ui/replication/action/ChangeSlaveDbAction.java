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

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.widgets.Shell;

import com.cubrid.common.ui.spi.action.SelectionAction;
import com.cubrid.common.ui.spi.dialog.CMWizardDialog;
import com.cubrid.common.ui.spi.model.CubridDatabase;
import com.cubrid.common.ui.spi.model.ISchemaNode;
import com.cubrid.common.ui.spi.util.CommonUITool;
import com.cubrid.cubridmanager.core.common.model.DbRunningType;
import com.cubrid.cubridmanager.core.common.model.ServerUserInfo;
import com.cubrid.cubridmanager.ui.cubrid.database.Messages;
import com.cubrid.cubridmanager.ui.replication.control.ChangeSlaveDbWizard;

/**
 * 
 * This action is responsible for changing slave database.
 * 
 * @author wuyingshi
 * @version 1.0 - 2009-9-16 created by wuyingshi
 */
public class ChangeSlaveDbAction extends
		SelectionAction {

	public static final String ID = ChangeSlaveDbAction.class.getName();

	/**
	 * The constructor
	 * 
	 * @param shell
	 * @param text
	 * @param icon
	 */
	public ChangeSlaveDbAction(Shell shell, String text, ImageDescriptor icon) {
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
	public ChangeSlaveDbAction(Shell shell, ISelectionProvider provider,
			String text, ImageDescriptor icon) {
		super(shell, provider, text, icon);
		this.setId(ID);
		this.setToolTipText(text);
	}

	/**
	 * @see com.cubrid.common.ui.spi.action.ISelectionAction#allowMultiSelections
	 *      ()
	 * @return false
	 */
	public boolean allowMultiSelections() {
		return false;
	}

	/**
	 * @see com.cubrid.common.ui.spi.action.ISelectionAction#isSupported(java
	 *      .lang.Object)
	 * @param obj Object
	 * @return boolean(whether to support)
	 */
	public boolean isSupported(Object obj) {
		if (obj instanceof CubridDatabase) {
			CubridDatabase database = (CubridDatabase) obj;
			if (!database.isLogined()
					|| database.getRunningType() == DbRunningType.STANDALONE) {
				return false;
			}
			ServerUserInfo serverUserInfo = database.getServer().getServerInfo().getLoginedUserInfo();
			if (serverUserInfo == null || !serverUserInfo.isAdmin()) {
				return false;
			}
			return database.isDistributorDb();
		}
		return false;
	}

	/**
	 * change slave database
	 */
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

		CMWizardDialog dialog = new CMWizardDialog(getShell(),
				new ChangeSlaveDbWizard(database));
		dialog.setPageSize(400, 350);
		dialog.open();

	}
}
