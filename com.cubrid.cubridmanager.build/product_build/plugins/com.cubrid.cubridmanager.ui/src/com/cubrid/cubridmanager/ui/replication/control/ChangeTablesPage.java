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
package com.cubrid.cubridmanager.ui.replication.control;

import java.util.List;

import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.cubrid.common.ui.spi.model.ICubridNode;
import com.cubrid.common.ui.spi.progress.ExecTaskWithProgress;
import com.cubrid.cubridmanager.core.cubrid.table.model.DBClasses;
import com.cubrid.cubridmanager.core.replication.model.MasterInfo;
import com.cubrid.cubridmanager.core.replication.model.ReplicationInfo;
import com.cubrid.cubridmanager.ui.replication.Messages;
import com.cubrid.cubridmanager.ui.spi.model.CubridNodeType;

/**
 *
 * Select replicated tables wizard page for changing replicated tables wizard
 *
 * @author pangqiren
 * @version 1.0 - 2009-8-26 created by pangqiren
 */
public class ChangeTablesPage extends
		WizardPage implements
		IPageChangedListener {

	public final static String PAGENAME = "ChangeReplTablesWizard/ChangeTablesPage";
	private final ICubridNode replNode;
	SelectTableComp selectTableComp = null;

	/**
	 * The constructor
	 */
	public ChangeTablesPage(ICubridNode node) {
		super(PAGENAME);
		replNode = node;
		selectTableComp = new SelectTableComp();
		setPageComplete(false);
	}

	/**
	 * Create the control for this page
	 *
	 * @param parent Composite
	 */
	public void createControl(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 10;
		layout.marginWidth = 10;
		composite.setLayout(layout);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		composite.setLayoutData(gridData);

		selectTableComp.createTableGroup(composite);

		setTitle(Messages.titleChangeReplTables);
		setMessage(Messages.msgSelectReplTables);

		setControl(composite);
	}

	/**
	 * initialize some values
	 */
	private void initialize() {
		MasterInfo masterInfo = null;
		if (replNode != null) {
			String type = replNode.getType();
			if (CubridNodeType.DATABASE.equals(type)) {
				ReplicationInfo replInfo = (ReplicationInfo) replNode.getAdapter(ReplicationInfo.class);
				masterInfo = replInfo != null
						&& replInfo.getMasterList() != null
						&& replInfo.getMasterList().size() > 0 ? replInfo.getMasterList().get(
						0)
						: null;
			}
		}
		if (masterInfo == null) {
			return;
		}
		List<String> replClassList = masterInfo.getReplTableList();
		boolean isReplAll = masterInfo.isReplAllTable();
		selectTableComp.setReplAllTables(isReplAll);
		selectTableComp.setSelectedTableList(replClassList);
	}

	/**
	 * @see org.eclipse.jface.dialogs.IPageChangedListener#pageChanged(org.eclipse.jface.dialogs.PageChangedEvent)
	 * @param event PageChangedEvent
	 */
	public void pageChanged(PageChangedEvent event) {
		IWizardPage page = (IWizardPage) event.getSelectedPage();
		if (page.getName().equals(PAGENAME)) {
			SetDatabaseInfoPage selectDatabasePage = (SetDatabaseInfoPage) getWizard().getPage(
					SetDatabaseInfoPage.PAGENAME);
			String ip = selectDatabasePage.getIp();
			String port = selectDatabasePage.getPort();
			String userName = selectDatabasePage.getUserName();
			String password = selectDatabasePage.getPassword();

			String dbName = selectDatabasePage.getMasterDbName();
			String dbaPassword = selectDatabasePage.getMasterDbaPassword();

			GetAllClassesTaskExecutor taskExcutor = new GetAllClassesTaskExecutor(
					getShell(), ip, port, userName, password, dbName,
					dbaPassword);
			new ExecTaskWithProgress(taskExcutor).exec(true, true);
			if (taskExcutor.isSuccess()) {
				DBClasses dbClasses = taskExcutor.getDBClasses();
				selectTableComp.fillTableViewer(dbClasses);
				initialize();
				setErrorMessage(null);
			} else {
				setErrorMessage(Messages.errPreviousPage);
			}
		}

	}

	public List<String> getReplTableList() {
		return selectTableComp.getSelectedTableList();
	}

	public boolean isReplAllTables() {
		return selectTableComp.isReplAllTables();
	}
}
