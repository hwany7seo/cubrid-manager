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
package com.cubrid.cubridmanager.ui.host.dialog;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.cubrid.common.core.task.ITask;
import com.cubrid.common.ui.spi.dialog.CMTitleAreaDialog;
import com.cubrid.common.ui.spi.progress.ExecTaskWithProgress;
import com.cubrid.common.ui.spi.progress.TaskExecutor;
import com.cubrid.common.ui.spi.util.CommonUITool;
import com.cubrid.common.ui.spi.util.ValidateUtil;
import com.cubrid.cubridmanager.core.common.model.ServerInfo;
import com.cubrid.cubridmanager.core.common.task.ChangeCMUserPasswordTask;
import com.cubrid.cubridmanager.ui.host.Messages;
import com.cubrid.cubridmanager.ui.spi.persist.CMHostNodePersistManager;

/**
 * 
 * Change CUBRID Maanger user password dialog
 * 
 * @author pangqiren 2009-4-7
 */
public class ChangePasswordDialog extends
		CMTitleAreaDialog implements
		ModifyListener {
	private Text newPasswordText = null;
	private Text passwordConfirmText = null;
	private Text oldPasswordText = null;
	private ServerInfo serverInfo = null;
	private final boolean isLoginChanged;

	/**
	 * The constructor
	 * 
	 * @param parentShell
	 */
	public ChangePasswordDialog(Shell parentShell, boolean isLoginChanged) {
		super(parentShell);
		this.isLoginChanged = isLoginChanged;
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
		layout.numColumns = 3;
		layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		composite.setLayout(layout);

		Label userNameLabel = new Label(composite, SWT.LEFT);
		userNameLabel.setText(Messages.lblUserName);
		userNameLabel.setLayoutData(CommonUITool.createGridData(1, 1, -1, -1));
		Text userNameText = new Text(composite, SWT.LEFT | SWT.BORDER
				| SWT.NONE);
		userNameText.setLayoutData(CommonUITool.createGridData(
				GridData.FILL_HORIZONTAL, 2, 1, 100, -1));
		userNameText.setText(serverInfo.getUserName() == null ? ""
				: serverInfo.getUserName());
		userNameText.setEditable(false);

		Label oldPasswordLabel = new Label(composite, SWT.LEFT);
		oldPasswordLabel.setText(Messages.lblOldPassword);
		oldPasswordLabel.setLayoutData(CommonUITool.createGridData(1, 1, -1, -1));
		oldPasswordText = new Text(composite, SWT.LEFT | SWT.BORDER
				| SWT.PASSWORD);
		oldPasswordText.setLayoutData(CommonUITool.createGridData(
				GridData.FILL_HORIZONTAL, 2, 1, 100, -1));
		if (isLoginChanged) {
			oldPasswordText.setText(serverInfo.getUserPassword());
			oldPasswordText.setEditable(false);
		} else {
			oldPasswordText.addModifyListener(this);
		}

		Label passwordLabel = new Label(composite, SWT.LEFT);
		passwordLabel.setText(Messages.lblNewPassword);
		passwordLabel.setLayoutData(CommonUITool.createGridData(1, 1, -1, -1));
		newPasswordText = new Text(composite, SWT.LEFT | SWT.BORDER
				| SWT.PASSWORD);
		newPasswordText.setTextLimit(ValidateUtil.MAX_NAME_LENGTH);
		newPasswordText.addModifyListener(this);
		newPasswordText.setLayoutData(CommonUITool.createGridData(
				GridData.FILL_HORIZONTAL, 2, 1, 100, -1));

		Label newPasswordLabel = new Label(composite, SWT.LEFT);
		newPasswordLabel.setText(Messages.lblPasswordConfirm);
		newPasswordLabel.setLayoutData(CommonUITool.createGridData(1, 1, -1, -1));
		passwordConfirmText = new Text(composite, SWT.LEFT | SWT.PASSWORD
				| SWT.BORDER);
		passwordConfirmText.setTextLimit(ValidateUtil.MAX_NAME_LENGTH);
		passwordConfirmText.setLayoutData(CommonUITool.createGridData(
				GridData.FILL_HORIZONTAL, 2, 1, 100, -1));
		passwordConfirmText.addModifyListener(this);

		if (isLoginChanged) {
			newPasswordText.setFocus();
		} else {
			oldPasswordText.setFocus();
		}

		setTitle(Messages.titleChangePasswordDialog);
		if (isLoginChanged) {
			setMessage(Messages.msgChangeAdminPassword);
		} else {
			setMessage(Messages.msgChangePasswordDialog);
		}
		return parentComp;
	}

	@Override
	protected int getShellStyle() {
		return super.getShellStyle() | SWT.TITLE | SWT.PRIMARY_MODAL;
	}

	/**
	 * Constrain the shell size
	 */
	protected void constrainShellSize() {
		super.constrainShellSize();
		getShell().setSize(500, 400);
		CommonUITool.centerShell(getShell());
		getShell().setText(Messages.titleChangePasswordDialog);
	}

	/**
	 * Create buttons for button bar
	 * 
	 * @param parent the parent composite
	 */
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID,
				com.cubrid.cubridmanager.ui.common.Messages.btnOK, true);
		getButton(IDialogConstants.OK_ID).setEnabled(false);
		createButton(parent, IDialogConstants.CANCEL_ID,
				com.cubrid.cubridmanager.ui.common.Messages.btnCancel, false);
	}

	/**
	 * Call this method when button in button bar is pressed
	 * 
	 * @param buttonId the button id
	 */
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			changePassword(buttonId);
		} else {
			super.buttonPressed(buttonId);
		}
	}

	/**
	 * 
	 * Execute task and change password
	 * 
	 * @param buttonId the button id
	 */
	private void changePassword(final int buttonId) {
		final String password = newPasswordText.getText();
		TaskExecutor taskExcutor = new TaskExecutor() {
			public boolean exec(final IProgressMonitor monitor) {
				Display display = getShell().getDisplay();
				if (monitor.isCanceled()) {
					return false;
				}
				monitor.beginTask(Messages.changePwdTaskName,
						IProgressMonitor.UNKNOWN);
				for (ITask task : taskList) {
					task.execute();
					final String msg = task.getErrorMsg();
					if (openErrorBox(getShell(), msg, monitor)) {
						return false;
					}
					if (monitor.isCanceled()) {
						return false;
					}
					serverInfo.setUserPassword(password);
					if (serverInfo.getLoginedUserInfo() != null) {
						serverInfo.getLoginedUserInfo().setPassword(password);
					}
					CMHostNodePersistManager.getInstance().saveServers();
				}
				if (!monitor.isCanceled()) {
					display.syncExec(new Runnable() {
						public void run() {
							if (CommonUITool.openConfirmBox(Messages.msgChangePassSuccess)) {
								setReturnCode(buttonId);
							} else {
								setReturnCode(IDialogConstants.CANCEL_ID);
							}
							close();
						}
					});
				}
				return true;
			}
		};
		ChangeCMUserPasswordTask changePasswordTask = new ChangeCMUserPasswordTask(
				serverInfo);
		changePasswordTask.setUserName(serverInfo.getUserName());
		changePasswordTask.setPassword(password);
		taskExcutor.addTask(changePasswordTask);
		new ExecTaskWithProgress(taskExcutor).exec(true, true);
	}

	/**
	 * When modify the page content and check the validation
	 * 
	 * @param event the modify event
	 */
	public void modifyText(ModifyEvent event) {
		String newPassword = newPasswordText.getText();
		String passwordConfirm = passwordConfirmText.getText();
		boolean isValidOldPassword = true;
		if (!isLoginChanged) {
			isValidOldPassword = oldPasswordText.getText().equals(
					serverInfo.getUserPassword());
		}
		if (!isValidOldPassword) {
			setErrorMessage(Messages.errOldPassword);
			setEnabled(false);
			return;
		}
		boolean isValidNewPassword = newPassword.indexOf(" ") < 0
				&& newPassword.trim().length() >= 4
				&& newPassword.trim().length() <= ValidateUtil.MAX_NAME_LENGTH
				&& !"admin".equals(newPassword);
		if (!isValidNewPassword) {
			setErrorMessage(Messages.errNewPassword);
			setEnabled(false);
			return;
		}
		if (!newPassword.equals(passwordConfirm)) {
			setErrorMessage(Messages.errNotEqualPassword);
			setEnabled(false);
			return;
		}
		setErrorMessage(null);
		setEnabled(true);
	}

	/**
	 * 
	 * Enable or disable the ok button
	 * 
	 * @param isEnabled whether it is enabled
	 */
	private void setEnabled(boolean isEnabled) {
		if (getButton(IDialogConstants.OK_ID) != null) {
			getButton(IDialogConstants.OK_ID).setEnabled(isEnabled);
		}
	}

	/**
	 * 
	 * Get ServerInfo
	 * 
	 * @return the ServerInfo
	 */
	public ServerInfo getServerInfo() {
		return serverInfo;
	}

	/**
	 * 
	 * Set serverInfo
	 * 
	 * @param serverInfo the ServerInfo object
	 */
	public void setServerInfo(ServerInfo serverInfo) {
		this.serverInfo = serverInfo;
	}
}
