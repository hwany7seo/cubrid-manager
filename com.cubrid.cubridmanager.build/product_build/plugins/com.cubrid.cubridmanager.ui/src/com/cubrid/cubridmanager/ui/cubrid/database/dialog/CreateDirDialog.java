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
package com.cubrid.cubridmanager.ui.cubrid.database.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

import com.cubrid.common.ui.spi.TableViewerSorter;
import com.cubrid.common.ui.spi.dialog.CMTitleAreaDialog;
import com.cubrid.common.ui.spi.util.CommonUITool;
import com.cubrid.cubridmanager.ui.cubrid.database.Messages;

/**
 * 
 * This dialog list all directories that will be created
 * 
 * @author pangqiren
 * @version 1.0 - 2009-6-4 created by pangqiren
 */
public class CreateDirDialog extends
		CMTitleAreaDialog {

	private String[] dirs = null;

	/**
	 * The constructor
	 * 
	 * @param parentShell
	 */
	public CreateDirDialog(Shell parentShell) {
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
		GridLayout layout = new GridLayout();
		layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		composite.setLayout(layout);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		composite.setLayoutData(gridData);

		createDirTableComp(composite);

		setTitle(Messages.titleCreateDirDialog);
		setMessage(Messages.msgCreateDirDialog);
		return parentComp;
	}

	/**
	 * 
	 * Create directory table
	 * 
	 * @param parent the parent composite
	 */
	private void createDirTableComp(Composite parent) {
		Composite comp = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		comp.setLayoutData(gridData);
		GridLayout layout = new GridLayout();
		comp.setLayout(layout);

		Label tipLabel = new Label(comp, SWT.NONE);
		tipLabel.setText(Messages.msgDirList);
		tipLabel.setLayoutData(CommonUITool.createGridData(
				GridData.FILL_HORIZONTAL, 1, 1, -1, -1));

		final String[] columnNameArr = new String[] {"Directory" };
		TableViewer tableViewer = CommonUITool.createCommonTableViewer(comp,
				new TableViewerSorter(), columnNameArr,
				CommonUITool.createGridData(GridData.FILL_BOTH, 1, 4, -1, 200));
		Table dirTable = tableViewer.getTable();
		tableViewer.setInput(getTableModel());
		for (int i = 0; i < dirTable.getColumnCount(); i++) {
			dirTable.getColumn(i).pack();
		}
	}

	/**
	 * Constrain the shell size
	 */
	protected void constrainShellSize() {
		super.constrainShellSize();
		CommonUITool.centerShell(getShell());
		getShell().setText(Messages.titleCreateDirDialog);
	}

	/**
	 * Create buttons for button bar
	 * 
	 * @param parent the parent composite
	 */
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID,
				com.cubrid.cubridmanager.ui.common.Messages.btnOK, true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				com.cubrid.cubridmanager.ui.common.Messages.btnCancel, false);
	}

	/**
	 * 
	 * Get directory list table model
	 * 
	 * @return the table model list
	 */
	private List<Map<String, String>> getTableModel() {
		List<Map<String, String>> dirList = new ArrayList<Map<String, String>>();
		for (int i = 0; this.dirs != null && i < this.dirs.length; i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("0", this.dirs[i]);
			dirList.add(map);
		}
		return dirList;
	}

	/**
	 * 
	 * Set needed checked directories
	 * 
	 * @param dirs the dirs array
	 */
	public void setDirs(String[] dirs) {
		this.dirs = dirs == null ? null : (String[]) dirs.clone();
	}
}
