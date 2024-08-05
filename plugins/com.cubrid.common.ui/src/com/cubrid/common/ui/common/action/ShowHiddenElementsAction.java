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
package com.cubrid.common.ui.common.action;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Shell;

import com.cubrid.common.ui.common.navigator.NodeFilterManager;
import com.cubrid.common.ui.spi.action.SelectionAction;
import com.cubrid.common.ui.spi.model.ICubridNode;

/**
 * 
 * Show the hidden children element
 * 
 * @author pangqiren
 * @version 1.0 - 2010-12-10 created by pangqiren
 */
public class ShowHiddenElementsAction extends
		SelectionAction {
	public static final String ID = ShowHiddenElementsAction.class.getName();

	/**
	 * The constructor
	 * 
	 * @param shell
	 * @param text
	 * @param icon
	 */
	public ShowHiddenElementsAction(Shell shell, String text,
			ImageDescriptor icon) {
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
	public ShowHiddenElementsAction(Shell shell, ISelectionProvider provider,
			String text, ImageDescriptor icon) {
		super(shell, provider, text, icon);
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
		return true;
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
		return isSupportedNode(obj);
	}
	
	/**
	 * 
	 * Return whether this action support this object,if not support,this action
	 * will be hidden. Currently, this is same isSupported method except a static method.
	 * 
	 * @param obj the Object
	 * @return <code>true</code> if support this obj;<code>false</code>
	 *         otherwise
	 */
	public static boolean isSupportedNode(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof ICubridNode) {
			ICubridNode node = (ICubridNode) obj;
			return node.isContainer()
					&& NodeFilterManager.getInstance().isHasHiddenByPrefix(
							node.getId());
		} else if (obj instanceof Object[]) {
			return true;
		}
		return false;
	}

	/**
	 * Show the hidden child elements
	 */
	public void run() {
		Object[] objArr = this.getSelectedObj();
		if (!isSupported(objArr)) {
			setEnabled(false);
			return;
		}
		ISelectionProvider provider = this.getSelectionProvider();
		if (!(provider instanceof TreeViewer)) {
			return;
		}
		for (Object obj : objArr) {
			if (!(obj instanceof ICubridNode)) {
				continue;
			}
			ICubridNode node = (ICubridNode) obj;
			if (node.isContainer()) {
				NodeFilterManager.getInstance().removeIdFilterByPrefix(
						node.getId(), false);
				NodeFilterManager.getInstance().removeIdGrayFilterByPrefix(
						node.getId(), false);
			}
		}
		NodeFilterManager.getInstance().saveFilterSetting();
		TreeViewer viewer = (TreeViewer) provider;
		viewer.setFilters(NodeFilterManager.getInstance().getViewerFilter());
		setEnabled(false);
	}
}
