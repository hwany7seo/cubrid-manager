/*
 * Copyright (C) 2013 Search Solution Corporation. All rights reserved by Search Solution. 
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
package com.cubrid.cubridmanager.ui.service.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;

import com.cubrid.common.core.util.LogUtil;
import com.cubrid.cubridmanager.ui.service.Messages;
import com.cubrid.cubridmanager.ui.service.editor.ServiceDashboardEditor;
import com.cubrid.cubridmanager.ui.service.editor.ServiceDashboardEditorInput;

/**
 * ServiceDashboardAction Description
 * 
 * @author Kevin.Wang
 * @version 1.0 - 2013-3-5 created by Kevin.Wang
 */
public class ServiceDashboardAction extends
		Action {

	private static final Logger LOGGER = LogUtil.getLogger(ServiceDashboardAction.class);
	public static final String ID = ServiceDashboardAction.class.getName();

	/**
	 * The constructor
	 * 
	 * @param shell
	 * @param text
	 * @param enabledIcon
	 * @param disabledIcon
	 */
	public ServiceDashboardAction(Shell shell, String text,
			ImageDescriptor enabledIcon, ImageDescriptor disabledIcon) {
		super(text);
		this.setId(ID);
		this.setToolTipText(text);
		this.setDisabledImageDescriptor(disabledIcon);
	}

	/**
	 * @see org.eclipse.jface.action.Action#run() Override the run method in
	 *      order to complete showing brokers status server to a broker
	 */
	public void run() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (null == window) {
			return;
		}
		/*Check it open same editor*/
		ServiceDashboardEditor editorPart = findOpenedEditorPart();
		if (editorPart == null) {
			ServiceDashboardEditorInput editorInput = new ServiceDashboardEditorInput();
			editorInput.setName("");
			editorInput.setToolTipText(Messages.serviceDashboardPartToolTip);
			try {
				editorPart = (ServiceDashboardEditor) window.getActivePage().openEditor(
						editorInput, ServiceDashboardEditor.ID);
				editorPart.loadAllData();
			} catch (PartInitException ex) {
				LOGGER.error(ex.getMessage());
			}
		} else {
			window.getActivePage().activate(editorPart);
			editorPart.loadAllData();
		}

	}

	/**
	 * Get all opened query editor
	 * 
	 * @return
	 */
	private ServiceDashboardEditor findOpenedEditorPart() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window == null) {
			return null;
		}

		IEditorReference[] editorReferences = window.getActivePage().getEditorReferences();

		for (IEditorReference reference : editorReferences) {
			if (reference.getId().equals(ServiceDashboardEditor.ID)) {
				ServiceDashboardEditor editor = (ServiceDashboardEditor) reference.getEditor(false);
				if (editor != null) {
					return editor;
				}
			}
		}
		return null;

	}
}
