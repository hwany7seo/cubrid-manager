/*
 * Copyright (C) 2013 Search Solution Corporation. All rights reserved by Search
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
package com.cubrid.common.ui.common.action;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.cubrid.common.ui.common.Messages;
import com.cubrid.common.ui.common.sqlrunner.dialog.RunSQLFileDialog;
import com.cubrid.common.ui.spi.action.SelectionAction;
import com.cubrid.common.ui.spi.model.CubridDatabase;
import com.cubrid.common.ui.spi.model.ISchemaNode;
import com.cubrid.common.ui.spi.util.CommonUITool;

/**
 * Show run SQL dialog
 * 
 * @author fulei
 * @version 1.0 - 2012-05-15 created by fulei
 * @version 1.1 - 2012-09-05 created by Isaiah Choe
 */
public class RunSQLFileAction extends SelectionAction {
	public static final String ID = RunSQLFileAction.class.getName();

	public RunSQLFileAction(Shell shell, String text, ImageDescriptor icon) {
		this(shell, null, text, icon);
	}

	public RunSQLFileAction(Shell shell, ISelectionProvider provider, String text, ImageDescriptor icon) {
		super(shell, provider, text, icon);
		this.setId(ID);
		this.setToolTipText(text);
	}

	public boolean allowMultiSelections() {
		return true;
	}

	public boolean isSupported(Object obj) {
		return true;
	}

	private CubridDatabase[] handleSelectionObj(Object[] objs) {
		List<CubridDatabase> returnArray = new ArrayList<CubridDatabase>();
		for (Object obj : objs) {
			if (obj instanceof ISchemaNode) {
				CubridDatabase database = ((ISchemaNode) obj).getDatabase();
				if (database != null && !returnArray.contains(database)) {
					returnArray.add(database);
				}
			}
		}

		return returnArray.toArray(new CubridDatabase[0]);
	}

	public void run() {
		CubridDatabase[] cubridDatabases = handleSelectionObj(this.getSelectedObj());
		if (cubridDatabases == null || cubridDatabases.length == 0 || !cubridDatabases[0].isLogined()) {
			CommonUITool.openWarningBox(Messages.errSelectLoginDbToRunSQL);
			return;
		}

		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		new RunSQLFileDialog(shell, cubridDatabases).open();
	}
}
