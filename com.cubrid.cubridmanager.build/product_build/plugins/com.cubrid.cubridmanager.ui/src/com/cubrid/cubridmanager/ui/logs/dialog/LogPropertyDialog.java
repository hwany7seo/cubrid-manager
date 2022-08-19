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
package com.cubrid.cubridmanager.ui.logs.dialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.cubrid.common.core.util.FileUtil;
import com.cubrid.common.ui.spi.dialog.CMTitleAreaDialog;
import com.cubrid.common.ui.spi.model.DefaultCubridNode;
import com.cubrid.common.ui.spi.util.CommonUITool;
import com.cubrid.cubridmanager.core.logs.model.LogInfo;
import com.cubrid.cubridmanager.ui.logs.Messages;

/**
 *
 * The dialog is used to show log property.
 *
 * @author wuyingshi
 * @version 1.0 - 2009-3-18 created by wuyingshi
 */
public class LogPropertyDialog extends
		CMTitleAreaDialog {

	private LogInfo logInfo = null;
	private static Table table = null;
	private DefaultCubridNode node = null;

	/**
	 * The constructor
	 *
	 * @param parentShell
	 */
	public LogPropertyDialog(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * @see org.eclipse.jface.dialogs.TitleAreaDialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 * @param parent The parent composite to contain the dialog area
	 * @return the dialog area control
	 */
	protected Control createDialogArea(Composite parent) {
		Composite parentComp = (Composite) super.createDialogArea(parent);

		GridLayout layout = new GridLayout();
		layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);

		GridData gridData1 = new GridData(GridData.CENTER);
		gridData1.heightHint = 30;
		Composite top = new Composite(parentComp, SWT.NONE);
		top.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		top.setLayout(layout);
		top.setLayoutData(new GridData(GridData.FILL_BOTH));

		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessVerticalSpace = true;
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		table = new Table(top, SWT.FULL_SELECTION | SWT.BORDER);
		table.setHeaderVisible(true);
		table.setLayoutData(gridData);
		table.setLinesVisible(true);
		TableLayout tlayout = new TableLayout();
		table.setLayout(tlayout);
		table.setSize(530, 500);

		TableColumn tblColumn = new TableColumn(table, SWT.LEFT);
		tblColumn.setText(Messages.tableProperty);
		tblColumn.setWidth(100);
		tblColumn = new TableColumn(table, SWT.LEFT);
		tblColumn.setText(Messages.tableValue);
		tblColumn.setWidth(430);
		TableItem item;

		if (logInfo.getType() != null) {
			item = new TableItem(table, SWT.NONE);
			item.setText(0, Messages.tableLogType);
			item.setText(1, logInfo.getType());
		}
		item = new TableItem(table, SWT.NONE);
		item.setText(0, Messages.tableFileName);
		item.setText(1, logInfo.getName());

		item = new TableItem(table, SWT.NONE);
		item.setText(0, Messages.tableFileOwner);
		item.setText(1, logInfo.getOwner());

		item = new TableItem(table, SWT.NONE);
		item.setText(0, Messages.tableFileSize);
		item.setText(1, logInfo.getSize() + " " + Messages.msgFileSize);

		item = new TableItem(table, SWT.NONE);
		item.setText(0, Messages.tableChangeDate);
		item.setText(1, logInfo.getLastupdate());

		item = new TableItem(table, SWT.NONE);
		item.setText(0, Messages.tableFilePath);

		item.setText(1, FileUtil.changeSeparatorByOS(logInfo.getPath(),
				node.getServer().getServerInfo().getServerOsInfo()));

		setTitle(Messages.titleLogPropertyDialog);
		setMessage(logInfo.getName());
		return parentComp;
	}

	/**
	 * @see com.cubrid.common.ui.spi.dialog.CMTitleAreaDialog#constrainShellSize()
	 */
	protected void constrainShellSize() {
		super.constrainShellSize();
		CommonUITool.centerShell(getShell());
		getShell().setText(Messages.titleLogPropertyDialog);

	}

	/**
	 * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
	 * @param parent the button bar composite
	 */
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.buttonClose,
				true);
	}

	/**
	 * get the log information.
	 *
	 * @return logInfo
	 */
	public LogInfo getLogInfo() {
		return logInfo;
	}

	/**
	 * set the log information.
	 *
	 * @param logInfo LogInfo
	 * @param node DefaultCubridNode
	 */
	public void setLogInfo(LogInfo logInfo, DefaultCubridNode node) {
		this.logInfo = logInfo;
		this.node = node;
	}

}
