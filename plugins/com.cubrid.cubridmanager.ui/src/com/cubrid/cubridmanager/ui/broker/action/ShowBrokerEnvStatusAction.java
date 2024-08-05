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
package com.cubrid.cubridmanager.ui.broker.action;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.widgets.Shell;

import com.cubrid.common.ui.spi.LayoutManager;
import com.cubrid.common.ui.spi.action.SelectionAction;
import com.cubrid.common.ui.spi.model.ICubridNode;
import com.cubrid.cubridmanager.core.common.model.CasAuthType;
import com.cubrid.cubridmanager.core.common.model.ServerUserInfo;
import com.cubrid.cubridmanager.ui.spi.model.CubridBrokerFolder;

/**
 * A action in order to show the brokers status editor part
 * 
 * @author lizhiqiang
 * @version 1.0 - 2009-5-21 created by lizhiqiang
 */
public class ShowBrokerEnvStatusAction extends
		SelectionAction {

	public static final String ID = ShowBrokerEnvStatusAction.class.getName();

	/**
	 * The constructor
	 * 
	 * @param shell
	 * @param text
	 */
	public ShowBrokerEnvStatusAction(Shell shell, String text) {
		this(shell, text, null);
	}

	/**
	 * The constructor
	 * 
	 * @param shell
	 * @param text
	 * @param icon
	 */
	public ShowBrokerEnvStatusAction(Shell shell, String text,
			ImageDescriptor icon) {
		this(shell, null, text, icon);
	}

	/**
	 * The constructor
	 * 
	 * @param shell
	 * @param text
	 */
	protected ShowBrokerEnvStatusAction(Shell shell,
			ISelectionProvider provider, String text, ImageDescriptor icon) {
		super(shell, provider, text, icon);
		this.setId(ID);
		this.setToolTipText(text);

	}

	/**
	 * Override the run method in order to complete showing brokers status
	 * server to a broker
	 * 
	 */
	public void run() {
		Object[] obj = this.getSelectedObj();
		ICubridNode node = (ICubridNode) obj[0];
		LayoutManager.getInstance().getWorkbenchContrItem().openEditorOrView(
				node);
	}

	/**
	 * 
	 * Makes this action not support to select multi object
	 * 
	 * @see com.cubrid.common.ui.spi.action.ISelectionAction#allowMultiSelections
	 *      ()
	 * @return false
	 */
	public boolean allowMultiSelections() {
		return false;
	}

	/**
	 * Return whether this action support this object,if not support,this action
	 * will be disabled
	 * 
	 * @see com.cubrid.common.ui.spi.action.ISelectionAction#isSupported(java
	 *      .lang.Object)
	 * @param obj the object
	 * @return <code>true</code> if supported;<code>false</code>
	 */
	public boolean isSupported(Object obj) {
		if (obj instanceof CubridBrokerFolder) {
			CubridBrokerFolder selection = ((CubridBrokerFolder) obj);
			ServerUserInfo userInfo = selection.getServer().getServerInfo().getLoginedUserInfo();
			if (userInfo == null
					|| (CasAuthType.AUTH_ADMIN != userInfo.getCasAuth() && CasAuthType.AUTH_MONITOR != userInfo.getCasAuth())) {
				return false;
			}
			return true;
		}
		return false;
	}

}
