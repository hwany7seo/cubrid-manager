/*
 * Copyright (C) 2009 Search Solution Corporation. All rights reserved by Search Solution. 
 *
 * Redistribution and use in source and binary forms, with or without modification, 
 * are permitted provided that the following conditions are met: 
 *
 * - Redistributions of source code must retain the above copyright notice, 
 *   this list of conditions and the following disclaimer. 
 *
 * - Redistributions in binary form must reproduce the above copyright notice, 
 *   this list of conditions and the following disclaimer in the documentation 
 *   and/or other materials provided with the distribution. 
 *
 * - Neither the name of the <ORGANIZATION> nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software without 
 *   specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, 
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, 
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY 
 * OF SUCH DAMAGE. 
 *
 */
package com.cubrid.cubridmanager.ui.mondashboard.action;

import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.widgets.Shell;

import com.cubrid.common.ui.spi.action.SelectionAction;
import com.cubrid.common.ui.spi.dialog.CMWizardDialog;
import com.cubrid.cubridmanager.ui.mondashboard.dialog.wizard.AddHostAndDbWizard;
import com.cubrid.cubridmanager.ui.mondashboard.editor.model.Dashboard;
import com.cubrid.cubridmanager.ui.mondashboard.editor.model.HostNode;
import com.cubrid.cubridmanager.ui.mondashboard.editor.parts.DashboardPart;
import com.cubrid.cubridmanager.ui.spi.util.HAUtil;

/**
 * Add a Host Monitor to current dashboard.
 * 
 * @author SC13425
 * @version 1.0 - 2010-6-10 created by SC13425
 */
public class AddHostMonitorAction extends
		SelectionAction {

	public static final String ID = AddHostMonitorAction.class.getName();

	/**
	 * AddHostMonitorAction constructor.
	 * 
	 * @param shell window.getShell()
	 * @param text action text
	 * @param icon ImageDescriptor
	 */
	public AddHostMonitorAction(Shell shell, String text, ImageDescriptor icon) {
		this(shell, null, text, icon);
	}

	/**
	 * AddHostMonitorAction constructor.
	 * 
	 * @param shell window.getShell()
	 * @param provider ISelectionProvider
	 * @param text action text
	 * @param icon ImageDescriptor
	 */
	protected AddHostMonitorAction(Shell shell, ISelectionProvider provider,
			String text, ImageDescriptor icon) {
		super(shell, provider, text, icon);
		this.setId(ID);
		this.setToolTipText(text);
	}

	/**
	 * not allow mutli selections
	 * 
	 * @see com.cubrid.common.ui.spi.action.ISelectionAction#allowMultiSelections()
	 * @return boolean false;
	 */
	public boolean allowMultiSelections() {
		return false;
	}

	/**
	 * does action support the selection.
	 * 
	 * @see com.cubrid.common.ui.spi.action.ISelectionAction#isSupported(java.lang.Object)
	 * @param obj Object
	 * @return boolean always return true;
	 */
	public boolean isSupported(Object obj) {
		return obj instanceof DashboardPart;
	}

	/**
	 * Open add host monitor dialog.
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		Object[] objArr = this.getSelectedObj();
		if (objArr == null || objArr.length <= 0 || !isSupported(objArr[0])) {
			setEnabled(false);
			return;
		}
		DashboardPart dp = (DashboardPart) objArr[0];
		Dashboard dashboard = (Dashboard) dp.getModel();
		if (dashboard != null) {
			AddHostAndDbWizard wizard = new AddHostAndDbWizard(null,
					dashboard.getHostNodeList(), 0);
			CMWizardDialog dialog = new CMWizardDialog(getShell(), wizard);
			dialog.setPageSize(660, 380);
			int returnCode = dialog.open();
			if (returnCode == IDialogConstants.OK_ID) {
				List<HostNode> addedHostNodeList = wizard.getAddedHostNodeList();
				HAUtil.mergeHostNode(dashboard, addedHostNodeList);
				HAUtil.calcLocation(dashboard.getHostNodeList());
			}
		}
	}
}
