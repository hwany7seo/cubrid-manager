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
package com.cubrid.cubridmanager.ui.cubrid.jobauto.dialog;

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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

import com.cubrid.common.ui.spi.TableViewerSorter;
import com.cubrid.common.ui.spi.dialog.CMTitleAreaDialog;
import com.cubrid.common.ui.spi.model.CubridServer;
import com.cubrid.common.ui.spi.progress.CommonTaskExec;
import com.cubrid.common.ui.spi.progress.ExecTaskWithProgress;
import com.cubrid.common.ui.spi.progress.TaskExecutor;
import com.cubrid.common.ui.spi.util.CommonUITool;
import com.cubrid.cubridmanager.core.common.task.CommonQueryTask;
import com.cubrid.cubridmanager.core.common.task.CommonSendMsg;
import com.cubrid.cubridmanager.core.cubrid.jobauto.model.QueryLogInfo;
import com.cubrid.cubridmanager.core.cubrid.jobauto.model.QueryLogList;
import com.cubrid.cubridmanager.ui.cubrid.jobauto.Messages;

/**
 *
 * An dialog is to show the log of query
 *
 * @author lizhiqiang
 * @version 1.0 - 2009-4-21 created by lizhiqiang
 */
public class QueryLogDialog extends
		CMTitleAreaDialog {

	private Table queryLogsTable;
	private QueryLogList queryLogList;
	private CubridServer server;
	private TableViewer tableViewer;
	private List<Map<String, Object>> queryLogsInfoTableList;

	private static final String DESCRIPTION = Messages.queryLogDesc;
	private static final String ERROR_CODE = Messages.queryLogErrorCode;
	private static final String QUERY_ID = Messages.queryId;
	private static final String TIME = Messages.queryLogTime;
	private static final String CM_USER = Messages.queryLogCmUser;
	private static final String DATABASE = Messages.queryLogDb;
	private static final String LOCK_HOLDERS_GROUP_NAME = Messages.queryLogLockGroupName;

	/**
	 * The constructor
	 *
	 * @param parentShell
	 */
	public QueryLogDialog(Shell parentShell) {
		super(parentShell);

	}

	/**
	 * Create the dialog area
	 *
	 * @param parent the parent composite
	 * @return the composite
	 */
	protected Control createDialogArea(Composite parent) {
		Composite parentComp = (Composite) super.createDialogArea(parent);

		Composite composite = new Composite(parentComp, SWT.NONE);
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		createLockHoldersGroup(composite);
		setTitle(Messages.queryLogTitle);
		setMessage(Messages.queryLogMessage);

		initial();
		return parentComp;
	}

	/**
	 * Creates the Lock holders group
	 *
	 * @param composite Composite
	 */
	private void createLockHoldersGroup(Composite composite) {
		final Group lockHoldersGroup = new Group(composite, SWT.NONE);
		lockHoldersGroup.setText(LOCK_HOLDERS_GROUP_NAME);
		lockHoldersGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout(1, true);
		layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		lockHoldersGroup.setLayout(layout);

		final String[] columnNameArr = new String[]{DATABASE, CM_USER,
				QUERY_ID, TIME, ERROR_CODE, DESCRIPTION };
		tableViewer = CommonUITool.createCommonTableViewer(lockHoldersGroup,
				new TableViewerSorter(), columnNameArr,
				CommonUITool.createGridData(GridData.FILL_BOTH, 1, 1, -1, 200));
		queryLogsTable = tableViewer.getTable();
		initialTableModel();
		tableViewer.setInput(queryLogsInfoTableList);
		for (int i = 0; i < queryLogsTable.getColumnCount(); i++) {
			queryLogsTable.getColumn(i).pack();
		}
		/* TOOLS-3216 Display the error log with red color */
		for (int i = 0; i < queryLogsTable.getItemCount(); i++) {
			String logDesc = (String)queryLogsInfoTableList.get(i).get("5");
			String startDesc = "start";
			String sucDesc = "success";
			if(!startDesc.equals(logDesc) && !sucDesc.equals(logDesc)){
				queryLogsTable.getItem(i).setForeground(
						Display.getDefault().getSystemColor(SWT.COLOR_RED));
			}
		}
	}

	/**
	 * Constrain the shell size
	 */
	protected void constrainShellSize() {
		super.constrainShellSize();
		CommonUITool.centerShell(getShell());
		getShell().setSize(640, 500);
		getShell().setText(Messages.queryLogTitle);
	}

	/**
	 * Create buttons for button bar
	 *
	 * @param parent the parent composite
	 */
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.RETRY_ID, Messages.btnRefresh,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.btnCancel,
				false);
	}

	/**
	 * When press button,call it
	 *
	 * @param buttonId the button id
	 */
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.RETRY_ID) {
			if (loadData()) {
				initial();
				initialTableModel();
				tableViewer.setInput(queryLogsInfoTableList);
				for (int i = 0; i < queryLogsTable.getColumnCount(); i++) {
					queryLogsTable.getColumn(i).pack();
				}
				/* TOOLS-3216 Display the error log with red color */
				for (int i = 0; i < queryLogsTable.getItemCount(); i++) {
					String logDesc = (String)queryLogsInfoTableList.get(i).get("5");
					String startDesc = "start";
					String sucDesc = "success";
					if(!startDesc.equals(logDesc) && !sucDesc.equals(logDesc)){
						queryLogsTable.getItem(i).setForeground(
								Display.getDefault().getSystemColor(SWT.COLOR_RED));
					}
				}
			}

			return;
		}
		super.buttonPressed(buttonId);
	}

	/**
	 *
	 * Initial data
	 *
	 */
	private void initial() {
		//empty
	}

	/**
	 * Initialize the table model
	 */
	private void initialTableModel() {
		queryLogsInfoTableList = new ArrayList<Map<String, Object>>();

		if (queryLogList == null || queryLogList.getQueryLogList() == null) {
			return;
		}
		for (QueryLogInfo bean : queryLogList.getQueryLogList()) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("0", bean.getDbname());
			map.put("1", bean.getUsername());
			map.put("2", bean.getQuery_id());
			map.put("3", bean.getError_time());
			map.put("4", bean.getError_code());
			map.put("5", bean.getError_desc());
			queryLogsInfoTableList.add(map);
		}
	}

	/**
	 * load the data
	 *
	 * @return boolean
	 */
	public boolean loadData() {
		CommonQueryTask<QueryLogList> task = new CommonQueryTask<QueryLogList>(
				server.getServerInfo(), CommonSendMsg.getCommonSimpleSendMsg(),
				new QueryLogList());
		TaskExecutor taskExecutor = new CommonTaskExec(
				Messages.loadQueryLogTaskName);
		taskExecutor.addTask(task);
		new ExecTaskWithProgress(taskExecutor).exec();
		if (!taskExecutor.isSuccess()) {
			return false;
		}

		queryLogList = task.getResultModel();
		return true;

	}

	/**
	 * Sets the value of server
	 *
	 * @param server CubridServer
	 */
	public void setServer(CubridServer server) {
		this.server = server;
	}

}
