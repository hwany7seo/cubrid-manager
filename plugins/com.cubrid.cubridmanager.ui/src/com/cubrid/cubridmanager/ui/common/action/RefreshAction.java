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
package com.cubrid.cubridmanager.ui.common.action;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Shell;

import com.cubrid.common.ui.spi.LayoutManager;
import com.cubrid.common.ui.spi.action.SelectionAction;
import com.cubrid.common.ui.spi.model.ICubridNode;
import com.cubrid.common.ui.spi.model.ISchemaNode;
import com.cubrid.common.ui.spi.util.CommonUITool;
import com.cubrid.cubridmanager.ui.spi.model.CubridNodeType;
import com.cubrid.cubridmanager.ui.spi.model.CubridNodeTypeManager;

/**
 * 
 * This action is responsible to reload CUBRID navigator Node
 * 
 * @author pangqiren
 * @version 1.0 - 2009-6-4 created by pangqiren
 */
public class RefreshAction extends
		SelectionAction {

	public static final String ID = RefreshAction.class.getName();

	/**
	 * The constructor
	 * 
	 * @param shell
	 * @param text
	 * @param icon
	 */
	public RefreshAction(Shell shell, String text, ImageDescriptor icon, ImageDescriptor disabledIcon) {
		this(shell, null, text, icon, disabledIcon);
	}
	
	/**
	 * The constructor
	 * 
	 * @param shell
	 * @param text
	 * @param icon
	 */
	public RefreshAction(Shell shell, String text, ImageDescriptor icon,
			ImageDescriptor disabledIcon, boolean bigButton) {
		super(shell, null, text, icon);
		this.setDisabledImageDescriptor(disabledIcon);
		this.setId(bigButton ? ID + ".big" : ID);
		this.setToolTipText(text);
	}

	/**
	 * The constructor
	 * 
	 * @param shell
	 * @param provider
	 * @param text
	 * @param icon
	 */
	public RefreshAction(Shell shell, ISelectionProvider provider, String text,
			ImageDescriptor icon, ImageDescriptor disabledIcon) {
		super(shell, provider, text, icon);
		this.setDisabledImageDescriptor(disabledIcon);
		this.setId(ID);
		this.setToolTipText(text);
	}

	/**
	 * 
	 * Return whether this action support to select multi object,if not
	 * support,this action will be disabled
	 * 
	 * @return <code>true</code> if allow multi selection;<code>false</code>
	 *         otherwise
	 */
	public boolean allowMultiSelections() {
		return false;
	}

	/**
	 * 
	 * Return whether this action support this object,if not support,this action
	 * will be disabled
	 * 
	 * @param obj the Object
	 * @return <code>true</code> if support this obj;<code>false</code>
	 *         otherwise
	 */
	public boolean isSupported(Object obj) {
		if (obj instanceof ICubridNode) {
			ICubridNode node = (ICubridNode) obj;
			if (CubridNodeTypeManager.isCanRefresh(node.getType())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Reload the selected CUBRID node
	 */
	public void run() {
		final Object[] obj = this.getSelectedObj();
		if (obj == null || obj.length == 0 || !isSupported(obj[0])) {
			return;
		}
		ICubridNode cubridNode = (ICubridNode) obj[0];
		String nodeType = cubridNode.getType();
		if (CubridNodeType.USER_TABLE.equals(nodeType)
				|| CubridNodeType.SYSTEM_TABLE.equals(nodeType)
				|| CubridNodeType.USER_PARTITIONED_TABLE_FOLDER.equals(nodeType)
				|| CubridNodeType.USER_PARTITIONED_TABLE.equals(nodeType)
				|| CubridNodeType.SYSTEM_VIEW.equals(nodeType)
				|| CubridNodeType.USER_VIEW.equals(nodeType)) {
			ISchemaNode schemaNode = (ISchemaNode) cubridNode;
			schemaNode.getDatabase().getDatabaseInfo().removeSchema(
					cubridNode.getLabel());
		}
		LayoutManager.getInstance().getWorkbenchContrItem().reopenEditorOrView(
				cubridNode);
		if (CubridNodeType.GENERIC_VOLUME_FOLDER.equals(nodeType)
				|| CubridNodeType.DATA_VOLUME_FOLDER.equals(nodeType)
				|| CubridNodeType.INDEX_VOLUME_FOLDER.equals(nodeType)
				|| CubridNodeType.TEMP_VOLUME_FOLDER.equals(nodeType)) {
			cubridNode = cubridNode.getParent();
		} else if (CubridNodeType.ACTIVE_LOG_FOLDER.equals(nodeType)
				|| CubridNodeType.ARCHIVE_LOG_FOLDER.equals(nodeType)) {
			cubridNode = cubridNode.getParent() == null ? null
					: cubridNode.getParent().getParent();
		}

		ISelectionProvider provider = this.getSelectionProvider();
		if ((provider instanceof TreeViewer) && cubridNode != null
				&& cubridNode.isContainer()) {
			TreeViewer viewer = (TreeViewer) provider;
			CommonUITool.refreshNavigatorTree(viewer, cubridNode);
		}

	}
}
