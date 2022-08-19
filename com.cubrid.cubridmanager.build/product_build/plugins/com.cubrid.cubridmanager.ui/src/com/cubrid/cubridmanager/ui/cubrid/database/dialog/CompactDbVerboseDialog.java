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
package com.cubrid.cubridmanager.ui.cubrid.database.dialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.cubrid.common.ui.spi.dialog.CMTitleAreaDialog;
import com.cubrid.common.ui.spi.util.CommonUITool;
import com.cubrid.cubridmanager.ui.cubrid.database.Messages;

/**
 * This dialog show compact database verbose information
 * 
 * @author pangqiren
 * @version 1.0 - 2010-3-24 created by pangqiren
 */
public class CompactDbVerboseDialog extends
		CMTitleAreaDialog {
	private String resultInfoStr = null;

	/**
	 * The constructor
	 * 
	 * @param parentShell
	 */
	public CompactDbVerboseDialog(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Create dialog area content
	 * 
	 * @param parent the parent composite
	 * @return the control
	 */
	protected Control createDialogArea(Composite parent) {
		Composite parentComp = (Composite) super.createDialogArea(parent);
		Composite composite = new Composite(parentComp, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout();
		layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		composite.setLayout(layout);

		StyledText resultInfoText = new StyledText(composite, SWT.LEFT
				| SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL);
		CommonUITool.registerContextMenu(resultInfoText, false);
		resultInfoText.setLayoutData(CommonUITool.createGridData(
				GridData.FILL_BOTH, 1, 1, 100, 300));
		if (resultInfoStr != null) {
			resultInfoText.setText(resultInfoStr);
		}
		resultInfoText.setEditable(false);
		setTitle(Messages.titleCompactDbVerboseDialog);
		setMessage(Messages.msgCompactDbVerboseDialog);
		return parentComp;
	}

	/**
	 * Constrain the shell size
	 */
	protected void constrainShellSize() {
		super.constrainShellSize();
		CommonUITool.centerShell(getShell());
		getShell().setText(Messages.titleCompactDbVerboseDialog);
	}

	/**
	 * Create buttons for button bar
	 * 
	 * @param parent the parent composite
	 */
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID,
				com.cubrid.cubridmanager.ui.common.Messages.btnOK, true);
	}

	/**
	 * 
	 * Set backup db result information
	 * 
	 * @param str the result information
	 */
	public void setResultInfoStr(String str) {
		this.resultInfoStr = str;
	}
}
