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
package com.cubrid.cubridmanager.ui.host.action;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;

import com.cubrid.common.core.util.LogUtil;
import com.cubrid.common.ui.spi.action.SelectionAction;
import com.cubrid.common.ui.spi.model.CubridServer;
import com.cubrid.cubridmanager.core.common.model.CasAuthType;
import com.cubrid.cubridmanager.core.common.model.ServerUserInfo;
import com.cubrid.cubridmanager.ui.host.Messages;
import com.cubrid.cubridmanager.ui.host.dialog.ConfigType;
import com.cubrid.cubridmanager.ui.host.editor.EditCMCubridBrokerConfPart;
import com.cubrid.cubridmanager.ui.spi.model.CubridBrokerFolder;

/**
 * 
 * This action is responsible to edit the property of cubrid.conf
 * 
 * @author lizhiqiang
 * @version 1.0 - 2011-3-24 created by lizhiqiang
 */
public class EditBrokerConfigAction extends SelectionAction {
	public static final String ID = EditBrokerConfigAction.class.getName();
	private static final Logger LOGGER = LogUtil.getLogger(EditBrokerConfigAction.class);

	public EditBrokerConfigAction(Shell shell, String text,
			ImageDescriptor enabledIcon, ImageDescriptor disabledIcon) {
		this(shell, null, text, enabledIcon, disabledIcon);
	}

	public EditBrokerConfigAction(Shell shell, ISelectionProvider provider,
			String text, ImageDescriptor enabledIcon,
			ImageDescriptor disabledIcon) {
		super(shell, provider, text, enabledIcon);
		this.setId(ID);
		this.setToolTipText(text);
		this.setDisabledImageDescriptor(disabledIcon);
	}

	public boolean allowMultiSelections() {
		return false;
	}

	public boolean isSupported(Object obj) {
		if (obj instanceof CubridServer) {
			CubridServer server = (CubridServer) obj;
			ServerUserInfo userInfo = server.getServerInfo().getLoginedUserInfo();
			if (userInfo == null
					|| CasAuthType.AUTH_ADMIN != userInfo.getCasAuth()) {
				return false;
			}
			return true;
		} else if (obj instanceof CubridBrokerFolder) {
			CubridServer server = ((CubridBrokerFolder) obj).getServer();
			ServerUserInfo userInfo = server.getServerInfo().getLoginedUserInfo();
			if (userInfo == null
					|| CasAuthType.AUTH_ADMIN != userInfo.getCasAuth()) {
				return false;
			}
			return true;
		}
		return false;
	}

	public void run() {
		final Object[] obj = this.getSelectedObj();
		if (obj == null || obj.length <= 0) {
			setEnabled(false);
			return;
		}

		CubridServer server = null;
		if (obj[0] instanceof CubridServer) {
			server = (CubridServer) obj[0];
		} else if (obj[0] instanceof CubridBrokerFolder) {
			server = ((CubridBrokerFolder) obj[0]).getServer();
		}

		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window == null || server == null) {
			return;
		}

		ConfEditInput editorInput = new ConfEditInput();
		String id = ConfigType.CUBRID_BROKER.getValue() + "&" + server.getId();
		editorInput.setId(id);
		editorInput.setServerInfo(server.getServerInfo());
		editorInput.setName(server.getName());
		editorInput.setServerName(server.getServerName());
		editorInput.setServerPort(server.getMonPort());
		String[] tipMsg = new String[] {
				ConfigType.CUBRID_BROKER.getValue(),
				server.getName(),
				server.getServerName(),
				server.getMonPort() };
		String toolTipText = Messages.bind(Messages.msgEditorTooltip, tipMsg);
		editorInput.setToolTipText(toolTipText);

		try {
			window.getActivePage().openEditor(editorInput, EditCMCubridBrokerConfPart.ID);
		} catch (Exception ex) {
			LOGGER.error("", ex);
		}
	}
}
