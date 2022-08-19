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
package com.cubrid.cubridmanager.ui.host.action;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Shell;

import com.cubrid.common.ui.spi.CubridNodeManager;
import com.cubrid.common.ui.spi.action.ActionManager;
import com.cubrid.common.ui.spi.action.SelectionAction;
import com.cubrid.common.ui.spi.event.CubridNodeChangedEvent;
import com.cubrid.common.ui.spi.event.CubridNodeChangedEventType;
import com.cubrid.common.ui.spi.model.CubridServer;
import com.cubrid.common.ui.spi.model.ICubridNode;
import com.cubrid.cubridmanager.ui.host.dialog.ChangePasswordDialog;
import com.cubrid.cubridmanager.ui.spi.util.HostUtils;

/**
 * This action is responsible to change CUBRID Manager user password
 * 
 * @author pangqiren
 * @version 1.0 - 2009-6-3 created by pangqiren
 */
public class ChangeManagerPasswordAction extends SelectionAction {
	public static final String ID = ChangeManagerPasswordAction.class.getName();

	public ChangeManagerPasswordAction(Shell shell, String text, ImageDescriptor icon) {
		this(shell, null, text, icon);
	}

	public ChangeManagerPasswordAction(Shell shell, ISelectionProvider provider, String text, ImageDescriptor icon) {
		super(shell, provider, text, icon);
		this.setId(ID);
		this.setToolTipText(text);
	}

	public boolean allowMultiSelections() {
		return false;
	}

	public boolean isSupported(Object obj) {
		if (obj instanceof ICubridNode) {
			ICubridNode node = (ICubridNode) obj;
			CubridServer server = node.getServer();
			if (server == null || !server.isConnected()) {
				return false;
			}

			if (server.getServerInfo() == null) {
				return false;
			}

			if (server.getServerInfo().getLoginedUserInfo() == null) {
				return false;
			}

			return true;
		}

		return false;
	}

	/**
	 * Open change password dialog and change CUBRID Manager user password
	 */
	public void run() {
		final Object[] obj = this.getSelectedObj();
		if (!isSupported(obj[0])) {
			return;
		}
		
		ICubridNode node = (ICubridNode) obj[0];
		CubridServer server = node.getServer();
		ChangePasswordDialog changePasswordDialog = new ChangePasswordDialog(getShell(), false);
		changePasswordDialog.setServerInfo(server.getServerInfo());
		ISelectionProvider provider = this.getSelectionProvider();
		if (IDialogConstants.OK_ID == changePasswordDialog.open() && provider instanceof TreeViewer) {
			TreeViewer viewer = (TreeViewer) provider;
			boolean isContinue = HostUtils.processHostDisconnected(server);
			if (isContinue) {
				viewer.refresh(server, true);
				ActionManager.getInstance().fireSelectionChanged(getSelection());
				CubridNodeManager.getInstance().fireCubridNodeChanged(
						new CubridNodeChangedEvent(server, CubridNodeChangedEventType.SERVER_DISCONNECTED));
			}
		}
	}
}
