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
package com.cubrid.cubridmanager.ui.mondashboard.editor.command;

import org.eclipse.gef.commands.Command;
import org.eclipse.ui.PlatformUI;

import com.cubrid.common.ui.spi.util.CommonUITool;
import com.cubrid.cubridmanager.ui.mondashboard.Messages;
import com.cubrid.cubridmanager.ui.mondashboard.editor.model.BrokerNode;
import com.cubrid.cubridmanager.ui.mondashboard.editor.model.Dashboard;
import com.cubrid.cubridmanager.ui.mondashboard.editor.model.HostNode;

/**
 * Command to delete broker monitor figure from dashboard.
 *
 * @author cyl
 * @version 1.0 - 2010-8-19 created by cyl
 */
public class DeleteBrokerMonitorCommand extends
		Command {

	private BrokerNode nodeToDelete;

	private Dashboard dashboard;

	/**
	 *
	 */
	public DeleteBrokerMonitorCommand() {
		//default constructor
	}

	/**
	 * get the node to be deleted.
	 *
	 * @return the nodeToDelete
	 */
	public BrokerNode getNodeToDelete() {
		return nodeToDelete;
	}

	/**
	 * @param nodeToDelete the nodeToDelete to set
	 */
	public void setNodeToDelete(BrokerNode nodeToDelete) {
		this.nodeToDelete = nodeToDelete;
	}

	/**
	 * remove current broker node from dashboard
	 */
	public void execute() {
		if (null == nodeToDelete || null == nodeToDelete.getParent()) {
			return;
		}
		boolean isDelete = CommonUITool.openConfirmBox(
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
				Messages.bind(Messages.msgConfirmDeleteBroker,
						nodeToDelete.getBrokerName()));
		if (!isDelete) {
			return;
		}
		HostNode parent = nodeToDelete.getParent();
		parent.removeBrokerNode(nodeToDelete);
		dashboard.refresh();
	}

	/**
	 * @param dashboard the dashboard to set
	 */
	public void setDashboard(Dashboard dashboard) {
		this.dashboard = dashboard;
	}
}
