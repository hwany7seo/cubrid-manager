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
package com.cubrid.cubridmanager.ui.logs.action;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.cubrid.common.ui.spi.action.SelectionAction;
import com.cubrid.common.ui.spi.model.DefaultCubridNode;
import com.cubrid.common.ui.spi.model.ICubridNode;
import com.cubrid.common.ui.spi.progress.CommonTaskExec;
import com.cubrid.common.ui.spi.progress.ExecTaskWithProgress;
import com.cubrid.common.ui.spi.progress.TaskExecutor;
import com.cubrid.common.ui.spi.util.CommonUITool;
import com.cubrid.cubridmanager.core.common.model.ServerUserInfo;
import com.cubrid.cubridmanager.core.logs.model.LogInfo;
import com.cubrid.cubridmanager.core.logs.task.DelAllLogTask;
import com.cubrid.cubridmanager.ui.logs.Messages;
import com.cubrid.cubridmanager.ui.spi.model.CubridNodeType;

/**
 *
 * This action is responsible to RemoveAllDbLogAction
 *
 * @author wuyingshi
 * @version 1.0 - 2009-3-10 created by wuyingshi
 */
public class RemoveAllDbLogAction extends
		SelectionAction {

	public static final String ID = RemoveAllDbLogAction.class.getName();

	/**
	 * The Constructor
	 *
	 * @param shell
	 * @param text
	 * @param icon
	 */
	public RemoveAllDbLogAction(Shell shell, String text, ImageDescriptor icon) {
		this(shell, null, text, icon);
	}

	/**
	 * The Constructor
	 *
	 * @param shell
	 * @param provider
	 * @param text
	 * @param icon
	 */
	public RemoveAllDbLogAction(Shell shell, ISelectionProvider provider,
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
		if (obj instanceof ICubridNode) {
			ICubridNode node = (ICubridNode) obj;
			if (node.getServer() == null) {
				return false;
			}
			ServerUserInfo serverUserInfo = node.getServer().getServerInfo().getLoginedUserInfo();
			if (serverUserInfo == null || !serverUserInfo.isAdmin()) {
				return false;
			}

			if (CubridNodeType.LOGS_SERVER_DATABASE_FOLDER.equals(node.getType())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @see com.cubrid.common.ui.spi.action.ISelectionAction#selectionChanged(java
	 *      .lang.Object)
	 * @param selection ISelection
	 */
	protected void selectionChanged(ISelection selection) {
		if (selection == null || selection.isEmpty()) {
			setEnabled(true);
			return;
		}
		super.selectionChanged(selection);
	}

	/**
	 * Remove all database logs
	 */
	public void run() {
		if (!CommonUITool.openConfirmBox(Messages.warningRemoveLog)) {
			return;
		}
		Object[] selected = this.getSelectedObj();

		DelAllLogTask delAllLogTask = new DelAllLogTask(
				((DefaultCubridNode) selected[0]).getServer().getServerInfo());
		String[] path = new String[((DefaultCubridNode) selected[0]).getChildren().size()];
		//get last file name
		String lastDBLog = "";
		LogInfo currLogFile;
		for (int i = 0, len = path.length; i < len; i++) {
			currLogFile = ((LogInfo) (((DefaultCubridNode) selected[0]).getChildren().get(
					i).getAdapter(LogInfo.class)));
			if (lastDBLog.compareTo(currLogFile.getPath()) < 0) {
				lastDBLog = currLogFile.getPath();
			}
		}
		//get last file name

		for (int i = 0, len = path.length - 1; i < len; i++) {
			if (!((LogInfo) (((DefaultCubridNode) selected[0]).getChildren().get(
					i).getAdapter(LogInfo.class))).getPath().equals(lastDBLog)) {
				path[i] = ((LogInfo) (((DefaultCubridNode) selected[0]).getChildren().get(
						i).getAdapter(LogInfo.class))).getPath();
			}
		}
		delAllLogTask.setPath(path);
		TaskExecutor taskExecutor = new CommonTaskExec(
				Messages.removeLogTaskName);
		taskExecutor.addTask(delAllLogTask);
		new ExecTaskWithProgress(taskExecutor).busyCursorWhile();
		if (!taskExecutor.isSuccess()) {
			return;
		}
		TreeViewer treeViewer = (TreeViewer) this.getSelectionProvider();
		DefaultCubridNode delNode = ((DefaultCubridNode) selected[0]);
		ICubridNode parentNode = delNode.getParent().getParent();
		parentNode.removeChild(delNode);

		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window == null) {
			return;
		}
		IWorkbenchPage activePage = window.getActivePage();
		for (int i = 0, len = path.length; i < len; i++) {
			IEditorPart editor = activePage.findEditor(((DefaultCubridNode) selected[0]).getChildren().get(
					i));
			if (!((LogInfo) (((DefaultCubridNode) selected[0]).getChildren().get(
					i).getAdapter(LogInfo.class))).getPath().equals(lastDBLog)) {
				treeViewer.remove(((DefaultCubridNode) selected[0]).getChildren().get(
						i));
				if (null != editor) {
					activePage.closeEditor(editor, true);
				}
			}
		}

	}

}
