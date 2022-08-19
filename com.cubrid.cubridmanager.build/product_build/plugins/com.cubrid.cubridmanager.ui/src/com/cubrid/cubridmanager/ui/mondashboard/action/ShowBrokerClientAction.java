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

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;

import com.cubrid.common.ui.spi.action.SelectionAction;
import com.cubrid.cubridmanager.ui.mondashboard.editor.model.BrokerNode;
import com.cubrid.cubridmanager.ui.mondashboard.editor.model.ClientNode;
import com.cubrid.cubridmanager.ui.mondashboard.editor.model.Dashboard;
import com.cubrid.cubridmanager.ui.mondashboard.editor.parts.BrokerMonitorPart;
import com.cubrid.cubridmanager.ui.mondashboard.editor.parts.DashboardPart;

/**
 * Show the figure which displays the selected broker's clients.
 * 
 * @author SC13425
 * @version 1.0 - 2010-8-19 created by SC13425
 */
public class ShowBrokerClientAction extends
		SelectionAction {

	public final static String ID = ShowBrokerClientAction.class.getName();

	public ShowBrokerClientAction(Shell shell, String text, ImageDescriptor icon) {
		this(shell, null, text, icon);
	}

	protected ShowBrokerClientAction(Shell shell, ISelectionProvider provider,
			String text, ImageDescriptor icon) {
		super(shell, provider, text, icon);
		this.setId(ID);
		this.setToolTipText(text);
		this.setChecked(false);
	}

	/**
	 * not allow multi selctions
	 * 
	 * @see com.cubrid.common.ui.spi.action.ISelectionAction#allowMultiSelections()
	 * @return boolean false
	 */
	public boolean allowMultiSelections() {
		return false;
	}

	/**
	 * Always support
	 * 
	 * @see com.cubrid.common.ui.spi.action.ISelectionAction#isSupported(java.lang.Object)
	 * @param obj Object
	 * @return boolean support:true;not support:false;
	 */
	public boolean isSupported(Object obj) {
		IStructuredSelection selection = (IStructuredSelection) getSelection();
		if (!(selection.getFirstElement() instanceof BrokerMonitorPart)) {
			return false;
		}
		BrokerMonitorPart cmp = (BrokerMonitorPart) selection.getFirstElement();
		DashboardPart dp = (DashboardPart) cmp.getParent();
		Dashboard db = (Dashboard) dp.getModel();
		BrokerNode model = (BrokerNode) cmp.getModel();
		ClientNode clientNode = db.getClientNodeByBroker(model);
		setChecked(null != clientNode && clientNode.isVisible());
		return true;
	}

	/**
	 * Create a new figure in dashboard.
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		IStructuredSelection selection = (IStructuredSelection) getSelection();
		BrokerMonitorPart cmp = (BrokerMonitorPart) selection.getFirstElement();
		DashboardPart dp = (DashboardPart) cmp.getParent();
		Dashboard db = (Dashboard) dp.getModel();
		BrokerNode model = (BrokerNode) cmp.getModel();
		ClientNode clientNode = db.getClientNodeByBroker(model);
		if (this.isChecked()) {
			if (null == clientNode) {
				clientNode = new ClientNode();
				clientNode.setBrokerNode(model);
				db.addClientNode(clientNode);
			}
			clientNode.setVisible(true);
		} else {
			clientNode.setVisible(false);
		}
		db.refresh();
	}
}
