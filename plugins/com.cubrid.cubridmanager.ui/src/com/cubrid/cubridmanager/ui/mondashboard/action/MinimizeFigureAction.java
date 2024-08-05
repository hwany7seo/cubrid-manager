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

import java.util.Iterator;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;

import com.cubrid.common.ui.spi.action.SelectionAction;
import com.cubrid.cubridmanager.ui.mondashboard.editor.parts.HANodePart;

/**
 * Show the figure which displays the selected broker's clients.
 * 
 * @author SC13425
 * @version 1.0 - 2010-8-19 created by SC13425
 */
public class MinimizeFigureAction extends
		SelectionAction {

	public final static String ID = MinimizeFigureAction.class.getName();

	public MinimizeFigureAction(Shell shell, String text, ImageDescriptor icon) {
		this(shell, null, text, icon);
	}

	protected MinimizeFigureAction(Shell shell, ISelectionProvider provider,
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
		return true;
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
		Iterator<?> iterator = selection.iterator();
		boolean minimize = true;
		while (iterator.hasNext()) {
			Object objSelected = iterator.next();
			if (objSelected instanceof HANodePart) {
				HANodePart cmp = (HANodePart) objSelected;
				if (!cmp.isMinimized()) {
					minimize = false;
					break;
				}
			}
		}
		setChecked(minimize);
		return true;
	}

	/**
	 * Create a new figure in dashboard.
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		IStructuredSelection selection = (IStructuredSelection) getSelection();
		Iterator<?> iterator = selection.iterator();
		while (iterator.hasNext()) {
			Object objSelected = iterator.next();
			if (objSelected instanceof HANodePart) {
				HANodePart cmp = (HANodePart) objSelected;
				cmp.setMinimized(isChecked());
				cmp.refresh();
			}
		}
	}
}
