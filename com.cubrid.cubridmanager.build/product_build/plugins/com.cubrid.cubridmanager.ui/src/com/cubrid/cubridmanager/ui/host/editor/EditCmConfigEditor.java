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
package com.cubrid.cubridmanager.ui.host.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;

import com.cubrid.cubridmanager.core.common.model.ServerInfo;
import com.cubrid.cubridmanager.ui.host.action.ConfEditInput;
import com.cubrid.cubridmanager.ui.host.dialog.ConfigType;
import com.cubrid.cubridmanager.ui.spi.util.ConfigParaHelp;

/**
 * This editor is responsible for edit cubrid property
 * 
 * @author lizhiqiang
 * @version 1.0 - 2011-3-24 created by lizhiqiang
 */
public class EditCmConfigEditor extends
		EditConfigEditor {
	public static final String ID = EditCmConfigEditor.class.getName();
	private ConfEditInput editorInput;

	/**
	 * @see com.cubrid.common.ui.spi.part.CubridEditorPart#init(org.eclipse.ui.IEditorSite,
	 *      org.eclipse.ui.IEditorInput)
	 * @param site the editor site
	 * @param input the editor input
	 * @exception PartInitException if this editor was not initialized
	 *            successfully
	 */
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);
		if (input instanceof ConfEditInput) {
			editorInput = (ConfEditInput) input;
			ServerInfo serverInfo = editorInput.getServerInfo();
			contents = ConfigParaHelp.performGetCmConf(serverInfo);
		}
	}

	/**
	 * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 * @param monitor IProgressMonitor
	 */
	public void doSave(IProgressMonitor monitor) {
		if (editorInput == null) {
			return;
		}
		if (!isSaveAllowed()) {
			return;
		}
		String contents = propEditor.getDocument().get();
		ConfigParaHelp.performImportCmConf(editorInput.getServerInfo(),
				contents);
		super.doSave(monitor);
	}

	/**
	 * Do the import
	 */
	protected void doImport() {
		doImport(ConfigType.CUBRID_MANAGER);
	}

	/**
	 * Do the export
	 */
	protected void doExport() {
		doExport(ConfigType.CUBRID_MANAGER);
	}

}
