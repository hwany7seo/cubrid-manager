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
package com.cubrid.cubridmanager.ui.cubrid.database.action;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.widgets.Shell;

import com.cubrid.common.ui.spi.LayoutManager;
import com.cubrid.common.ui.spi.action.SelectionAction;
import com.cubrid.common.ui.spi.model.CubridDatabase;
import com.cubrid.common.ui.spi.model.ICubridNode;
import com.cubrid.common.ui.spi.model.ISchemaNode;
import com.cubrid.cubridmanager.ui.spi.model.CubridNodeType;

/**
 * show database dashboard 
 * @author fulei
 *
 * @version 1.0 - 2012-10-12 created by fulei
 */

public class ShowDatabaseDashboardAction  extends SelectionAction {
	public static final String ID = ShowDatabaseDashboardAction.class.getName();
	
	/**
	 * The constructor
	 * 
	 * @param shell
	 * @param text
	 */
	public ShowDatabaseDashboardAction(Shell shell, String text,
			ImageDescriptor icon) {
		this(shell, null, text, icon);
	}

	/**
	 * @param shell
	 * @param provider
	 * @param text
	 */
	public ShowDatabaseDashboardAction(Shell shell, ISelectionProvider provider,
			String text, ImageDescriptor icon) {
		super(shell, provider, text, icon);
		this.setId(ID);
		this.setToolTipText(text);
	}
	
	/**
	 * 
	 * @see com.cubrid.common.ui.spi.action.ISelectionAction#allowMultiSelections
	 *      ()
	 * @return false
	 */
	public boolean allowMultiSelections() {
		return false;
	}
	

	/**
	 * 
	 * @see com.cubrid.common.ui.spi.action.ISelectionAction#isSupported(java
	 *      .lang.Object)
	 * @param obj the object
	 * @return <code>true</code> if support this object;<code>false</code>
	 *         otherwise
	 */
	public boolean isSupported(Object obj) {
		if (obj instanceof ISchemaNode) {
			ISchemaNode node = (ISchemaNode) obj;
			CubridDatabase database = node.getDatabase();
			if (CubridNodeType.DATABASE.equals(node.getType()) 
					&& database.isLogined()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Open the log editor and show log conent
	 */
	public void run() {
		Object[] obj = this.getSelectedObj();
		ICubridNode node = (ICubridNode) obj[0];
		LayoutManager.getInstance().getWorkbenchContrItem().openEditorOrView(
				node);
	}
}
