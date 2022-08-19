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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.slf4j.Logger;

import com.cubrid.common.core.util.CompatibleUtil;
import com.cubrid.common.core.util.LogUtil;
import com.cubrid.common.ui.spi.TableViewerSorter;
import com.cubrid.common.ui.spi.dialog.CMTrayDialog;
import com.cubrid.common.ui.spi.model.CubridDatabase;
import com.cubrid.common.ui.spi.util.CommonUITool;
import com.cubrid.cubridmanager.core.common.socket.SocketTask;
import com.cubrid.cubridmanager.core.common.task.CommonQueryTask;
import com.cubrid.cubridmanager.core.common.task.CommonSendMsg;
import com.cubrid.cubridmanager.core.cubrid.database.model.lock.DatabaseLockInfo;
import com.cubrid.cubridmanager.core.cubrid.database.model.lock.DatabaseTransaction;
import com.cubrid.cubridmanager.core.cubrid.database.model.lock.DbLotEntry;
import com.cubrid.cubridmanager.core.cubrid.database.model.lock.DbLotInfo;
import com.cubrid.cubridmanager.core.cubrid.database.model.lock.LockInfo;
import com.cubrid.cubridmanager.ui.cubrid.database.Messages;

/**
 * Show the lock info
 * 
 * @author robin 2009-3-16
 */
public class LockInfoDialog extends
		CMTrayDialog {

	private static final Logger LOGGER = LogUtil.getLogger(LockInfoDialog.class);

	private Table connectionList;
	private Table lockList;
	private CTabFolder tabFolder;

	private Label lockEscLabel;
	private Label deadLockNumLabel;
	private Label lockedNumLabel;
	private Label maxLockLabel;

	private CubridDatabase database = null;
	private Composite parentComp;
	private boolean isRunning = false;
	private DatabaseLockInfo databaseLockInfo = null;
	private final List<Map<String, String>> connListData = new ArrayList<Map<String, String>>();
	private TableViewer connTableViewer;

	private final List<Map<String, String>> lockListData = new ArrayList<Map<String, String>>();
	private TableViewer lockTableViewer;

	/**
	 * The constructor
	 * 
	 * @param parentShell
	 */
	public LockInfoDialog(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Create dialog area content
	 * 
	 * @param parent the parent composite
	 * @return the control
	 */
	protected Control createDialogArea(Composite parent) {
		parentComp = (Composite) super.createDialogArea(parent);
		tabFolder = new CTabFolder(parentComp, SWT.NONE);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout();
		layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		tabFolder.setLayout(layout);

		CTabItem item = new CTabItem(tabFolder, SWT.NONE);
		item.setText(Messages.tabItemClientInfo);
		Composite lockComposite = createLockSettingComposite();
		item.setControl(lockComposite);

		item = new CTabItem(tabFolder, SWT.NONE);
		item.setText(Messages.tabItemObjectLock);
		Composite composite = createHistoryComposite();
		item.setControl(composite);

		initial();
		return parentComp;
	}

	/**
	 * 
	 * Create the lock setting composite
	 * 
	 * @return the composite
	 */
	private Composite createLockSettingComposite() {
		final Composite composit = new Composite(tabFolder, SWT.NONE);
		composit.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout();
		layout.marginWidth = 10;
		layout.marginHeight = 10;
		composit.setLayout(layout);

		final Group theLockSettingGroup = new Group(composit, SWT.NONE);
		theLockSettingGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		theLockSettingGroup.setText(Messages.grpLockSetting);
		layout = new GridLayout();
		layout.numColumns = 2;
		theLockSettingGroup.setLayout(layout);

		lockEscLabel = new Label(theLockSettingGroup, SWT.NONE);
		final GridData gdLockEscText = new GridData(SWT.FILL, SWT.FILL, false,
				false);
		lockEscLabel.setLayoutData(gdLockEscText);

		deadLockNumLabel = new Label(theLockSettingGroup, SWT.NONE);
		final GridData gdDeadLockNumText = new GridData(SWT.FILL, SWT.FILL,
				false, false);
		deadLockNumLabel.setLayoutData(gdDeadLockNumText);

		final Group clientsCurrentlyGroup = new Group(composit, SWT.NONE);
		clientsCurrentlyGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
		clientsCurrentlyGroup.setText(Messages.grpClientsCur);
		layout = new GridLayout();
		clientsCurrentlyGroup.setLayout(layout);

		final String[] columnNameArr = new String[] {
				Messages.tblColLockInfoIndex, Messages.tblColLockInfoPname,
				Messages.tblColLockInfoUid, Messages.tblColLockInfoHost,
				Messages.tblColLockInfoPid,
				Messages.tblColLockInfoIsolationLevel,
				Messages.tblColLockInfoTimeOut };
		connTableViewer = CommonUITool.createCommonTableViewer(
				clientsCurrentlyGroup, new TableViewerSorter(), columnNameArr,
				CommonUITool.createGridData(GridData.FILL_BOTH, 3, 1, -1, 200));
		connectionList = connTableViewer.getTable();
		connTableViewer.setInput(connListData);
		return composit;
	}

	/**
	 * Create history composite
	 * 
	 * @return the history composite
	 */
	private Composite createHistoryComposite() {
		final Composite composit = new Composite(tabFolder, SWT.NONE);
		composit.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout();
		layout.marginWidth = 10;
		layout.marginHeight = 10;
		composit.setLayout(layout);

		final Group lockTableGroup = new Group(composit, SWT.NONE);
		lockTableGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		layout = new GridLayout();
		lockTableGroup.setLayout(layout);
		lockTableGroup.setText(Messages.grpLockTable);

		lockedNumLabel = new Label(lockTableGroup, SWT.NONE);
		lockedNumLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				false));

		maxLockLabel = new Label(lockTableGroup, SWT.NONE);
		maxLockLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				false));

		final Group clientsTableGroup = new Group(composit, SWT.NONE);
		clientsTableGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
		clientsTableGroup.setText(Messages.grpClientsCur);
		layout = new GridLayout();
		clientsTableGroup.setLayout(layout);

		final String[] columnNameArr = new String[] {
				Messages.tblColLockInfoOid, Messages.tblColLockInfoObjectType,
				Messages.tblColLockInfoNumHolders,
				Messages.tblColLockInfoNumBlockedHolders,
				Messages.tblColLockInfoNumWaiters

		};
		lockTableViewer = CommonUITool.createCommonTableViewer(clientsTableGroup,
				new TableViewerSorter(), columnNameArr,
				CommonUITool.createGridData(GridData.FILL_BOTH, 3, 1, -1, 200));
		lockTableViewer.setInput(lockListData);
		lockList = lockTableViewer.getTable();
		lockList.addListener(SWT.MouseDoubleClick, new Listener() {
			public void handleEvent(Event event) {
				openTransactionDetail();
			}
		});

		Button detailButton = new Button(composit, SWT.PUSH);
		detailButton.setFont(JFaceResources.getDialogFont());
		detailButton.setText(com.cubrid.cubridmanager.ui.common.Messages.btnDetail);
		detailButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(
					org.eclipse.swt.events.SelectionEvent event) {
				openTransactionDetail();
			}
		});
		GridData data = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
		int widthHint = convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
		Point minSize = detailButton.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		data.widthHint = Math.max(widthHint, minSize.x);
		detailButton.setLayoutData(data);

		return composit;
	}

	/**
	 * Constrain the shell size
	 */
	protected void constrainShellSize() {
		super.constrainShellSize();
		getShell().setSize(650, 500);
		CommonUITool.centerShell(getShell());
		getShell().setText(Messages.titleLockInfoDialog);
	}

	/**
	 * Create buttons for button bar
	 * 
	 * @param parent the parent composite
	 */
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.RETRY_ID,
				com.cubrid.cubridmanager.ui.common.Messages.btnRefresh, true);

		createButton(parent, IDialogConstants.CANCEL_ID,
				com.cubrid.cubridmanager.ui.common.Messages.btnCancel, false);
	}

	/**
	 * When press button,call it
	 * 
	 * @param buttonId the button id
	 */
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.RETRY_ID) {
			loadData(getShell());
			initial();
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
		if (databaseLockInfo != null) {
			LockInfo lockInfo = databaseLockInfo.getLockInfo();
			DbLotInfo dbLotInfo = lockInfo.getDbLotInfo();
			lockEscLabel.setText(Messages.bind(Messages.lblLockEscalation,
					lockInfo.getEsc()));
			deadLockNumLabel.setText(Messages.bind(Messages.lblRunInterval,
					lockInfo.getDinterval()));

			connListData.clear();
			if (lockInfo.getTransaction() != null) {
				for (DatabaseTransaction tran : lockInfo.getTransaction()) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("0", String.valueOf(tran.getIndex()));
					map.put("1", String.valueOf(tran.getPname()));
					map.put("2", String.valueOf(tran.getUid()));
					map.put("3", String.valueOf(tran.getHost()));
					map.put("4", String.valueOf(tran.getPid()));
					map.put("5", String.valueOf(tran.getIsolevel()));
					map.put("6", String.valueOf(tran.getTimeout()));
					connListData.add(map);
				}

				connTableViewer.refresh();
				for (int i = 0; i < connectionList.getColumnCount(); i++) {
					connectionList.getColumn(i).pack();
				}
			}
			lockedNumLabel.setText(Messages.bind(
					Messages.lblCurrentLockedObjNum, dbLotInfo.getNumlocked()));
			maxLockLabel.setText(Messages.bind(Messages.lblMaxLockedObjNum,
					dbLotInfo.getMaxnumlock()));

			lockListData.clear();
			if (dbLotInfo.getDbLotEntryList() != null) {

				for (DbLotEntry entry : dbLotInfo.getDbLotEntryList()) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("0", entry.getOid());
					map.put("1", entry.getOb_type());
					map.put("2", String.valueOf(entry.getNum_holders()));
					map.put("3", String.valueOf(entry.getNum_b_holders()));
					map.put("4", String.valueOf(entry.getNum_waiters()));
					lockListData.add(map);
				}
				lockTableViewer.refresh();

				for (int i = 0; i < lockList.getColumnCount(); i++) {
					lockList.getColumn(i).pack();
				}
			}
		}
	}

	/**
	 * 
	 * Get added CubridDatabase
	 * 
	 * @return the CubridDatabase object
	 */
	public CubridDatabase getDatabase() {
		return database;
	}

	/**
	 * load the init data from the server
	 * 
	 * @param shell the shell
	 * @return <code>true</code> if load successfully;<code>false</code>
	 *         otherwise
	 */
	public boolean loadData(Shell shell) {
		CommonQueryTask<DatabaseLockInfo> task = new CommonQueryTask<DatabaseLockInfo>(
				database.getServer().getServerInfo(),
				CommonSendMsg.getCommonDatabaseSendMsg(),
				new DatabaseLockInfo());
		task.setDbName(database.getName());
		execTask(-1, new SocketTask[] {task }, true, shell);
		if (task.getErrorMsg() != null) {
			return false;
		}
		setDatabaseLockInfo(task.getResultModel());
		return true;

	}

	/**
	 * 
	 * Set edited CubridDatabase
	 * 
	 * @param database the CubridDatabase object
	 */
	public void setDatabase(CubridDatabase database) {
		this.database = database;
	}

	/**
	 * 
	 * Execute the task
	 * 
	 * @param buttonId the button id
	 * @param tasks the task array
	 * @param cancelable whether it is cancelable
	 * @param shell the shell
	 */
	public void execTask(final int buttonId, final SocketTask[] tasks,
			boolean cancelable, Shell shell) {
		final Display display = shell.getDisplay();
		isRunning = false;
		try {
			new ProgressMonitorDialog(getShell()).run(true, cancelable,
					new IRunnableWithProgress() {
						public void run(final IProgressMonitor monitor) throws InvocationTargetException,
								InterruptedException {
							monitor.beginTask(
									com.cubrid.common.ui.spi.Messages.msgRunning,
									IProgressMonitor.UNKNOWN);

							if (monitor.isCanceled()) {
								return;
							}

							isRunning = true;
							Thread thread = new Thread() {
								public void run() {
									while (!monitor.isCanceled() && isRunning) {
										try {
											sleep(1);
										} catch (InterruptedException e) {
										}
									}
									if (monitor.isCanceled()) {
										for (SocketTask task : tasks) {
											if (task != null) {
												task.cancel();
											}
										}

									}
								}
							};
							thread.start();
							if (monitor.isCanceled()) {
								isRunning = false;
								return;
							}
							for (SocketTask task : tasks) {
								if (task != null) {
									task.execute();
									final String msg = task.getErrorMsg();
									if (monitor.isCanceled()) {
										isRunning = false;
										return;
									}
									if (msg != null && msg.length() > 0 && !monitor.isCanceled()) {
										showErrorMessage(display, msg);
										isRunning = false;
										return;
									}
								}
								if (monitor.isCanceled()) {
									isRunning = false;
									return;
								}
							}
							if (monitor.isCanceled()) {
								isRunning = false;
								return;
							}
							if (!monitor.isCanceled()) {
								display.syncExec(new Runnable() {
									public void run() {
										if (buttonId > 0) {
											setReturnCode(buttonId);
											close();
										}
									}
								});
							}
							isRunning = false;
							monitor.done();
						}
					});
		} catch (InvocationTargetException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (InterruptedException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	private void showErrorMessage(Display display, final String msg) {
		display.syncExec(new Runnable() {
			public void run() {
				// Fixed temporary for the following issue.
				// [TOOLS-3185][CM]Can't get the lockdb information when the database have been using specific collation.
				if (CompatibleUtil.isAfter910(database.getDatabaseInfo())) {
					CommonUITool.openErrorBox(getShell(), com.cubrid.common.ui.spi.Messages.errLockNoUseTemporary);
				} else {
					CommonUITool.openErrorBox(getShell(), msg);
				}
			}
		});
	}

	public DatabaseLockInfo getDatabaseLockInfo() {
		return databaseLockInfo;
	}

	public void setDatabaseLockInfo(DatabaseLockInfo databaseLockInfo) {
		this.databaseLockInfo = databaseLockInfo;
	}

	/**
	 * 
	 * open the detail dialog
	 * 
	 */
	private void openTransactionDetail() {

		int i = lockList.getSelectionIndex();
		String oid = lockList.getItem(i).getText();
		if (i >= 0) {
			LockInfoDetailDialog dlg = new LockInfoDetailDialog(
					parentComp.getShell());

			List<DbLotEntry> list = databaseLockInfo.getLockInfo().getDbLotInfo().getDbLotEntryList();
			DbLotEntry bean = null;
			for (DbLotEntry d : list) {
				if (d.getOid().equals(oid)) {
					bean = d;
					break;
				}
			}
			dlg.setDbLotEntry(bean);
			dlg.open();
		}

	}
}
