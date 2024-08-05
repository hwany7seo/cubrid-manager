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
package com.cubrid.common.ui.cubrid.synonym.action;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;

import com.cubrid.common.core.util.LogUtil;
import com.cubrid.common.core.util.QuerySyntax;
import com.cubrid.common.ui.cubrid.synonym.Messages;
import com.cubrid.common.ui.spi.action.SelectionAction;
import com.cubrid.common.ui.spi.model.DefaultSchemaNode;
import com.cubrid.common.ui.spi.model.ICubridNode;
import com.cubrid.common.ui.spi.model.ISchemaNode;
import com.cubrid.common.ui.spi.model.NodeType;
import com.cubrid.common.ui.spi.progress.CommonTaskExec;
import com.cubrid.common.ui.spi.progress.ExecTaskWithProgress;
import com.cubrid.common.ui.spi.progress.TaskExecutor;
import com.cubrid.common.ui.spi.util.ActionSupportUtil;
import com.cubrid.common.ui.spi.util.CommonUITool;
import com.cubrid.cubridmanager.core.common.task.CommonSQLExcuterTask;

public class DropSynonymAction extends SelectionAction {
	public static final String ID = DropSynonymAction.class.getName();
	private static final Logger LOGGER = LogUtil.getLogger(DropSynonymAction.class);

	public DropSynonymAction(Shell shell, String text, ImageDescriptor icon) {
		this(shell, null, text, icon);
	}

	public DropSynonymAction(Shell shell, ISelectionProvider provider, String text, ImageDescriptor icon) {
		super(shell, provider, text, icon);
		this.setId(ID);
	}

	public boolean allowMultiSelections() {
		return true;
	}

	public boolean isSupported(Object obj) {
		return ActionSupportUtil.isSupportMultiSelection(obj,
				new String[]{NodeType.SYNONYM }, true);
	}

	public void run() {
		Object[] objArr = this.getSelectedObj();
		if (objArr == null || !isSupported(objArr)) {
			this.setEnabled(false);
			return;
		}
		ISchemaNode nodeArray[] = new ISchemaNode[objArr.length];
		for (int i = 0 ; i < objArr.length; i ++) {
			nodeArray[i] = (ISchemaNode)objArr[i];
		}
		run(nodeArray);
	}

	public void run(ISchemaNode[] nodeArray) { // FIXME move this logic to core module
		if (nodeArray == null) {
			LOGGER.error("The nodeArray is a null.");
			return;
		}

		List<String> synonymNameList = new ArrayList<String>();
		StringBuffer bf = new StringBuffer();
		for (int i = 0; nodeArray != null && i < nodeArray.length; i++) {
			DefaultSchemaNode synonym = (DefaultSchemaNode) nodeArray[i];
			synonymNameList.add(synonym.getName());
			if (i == 0) {
				bf.append(synonym.getName());
			}
		}
		if (nodeArray.length > 1) {
			bf.append(", ...");
		}

		String cfmMsg = Messages.bind(Messages.dropSynonymWarnMSG1, nodeArray.length, bf.toString());
		boolean ret = CommonUITool.openConfirmBox(cfmMsg);
		if (!ret) {
			return;
		}

		ISchemaNode synonymNode = (ISchemaNode) nodeArray[0];
		CommonSQLExcuterTask task = new CommonSQLExcuterTask(
				synonymNode.getDatabase().getDatabaseInfo());
		for (String synonymName : synonymNameList) {
			String sql = "DROP SYNONYM " + QuerySyntax.escapeKeyword(synonymName);
			task.addSqls(sql);
		}

		String taskName = Messages.bind(Messages.dropSynonymTaskName, bf.toString());
		TaskExecutor taskExecutor = new CommonTaskExec(taskName);
		taskExecutor.addTask(task);

		new ExecTaskWithProgress(taskExecutor).busyCursorWhile();
		if (!taskExecutor.isSuccess()) {
			return;
		}

		String title = com.cubrid.common.ui.common.Messages.titleSuccess;
		String msg = Messages.dropSynonymSuccessMsg;
		CommonUITool.openInformationBox(title, msg);

		ISelectionProvider provider = this.getSelectionProvider();
		ICubridNode parent = synonymNode.getParent();
		if (provider instanceof TreeViewer) {
			TreeViewer viewer = (TreeViewer) provider;
			for (int i = 0; nodeArray != null && i < nodeArray.length; i++) {
				parent.removeChild((ISchemaNode) nodeArray[i]);
			}
			viewer.remove(parent, nodeArray);
			viewer.setSelection(new StructuredSelection(parent), true);
			CommonUITool.updateFolderNodeLabelIncludingChildrenCount(viewer, parent);
		}
	}
}
