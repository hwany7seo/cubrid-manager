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
package com.cubrid.common.ui.common.control;

import static com.cubrid.common.core.util.NoOp.noOp;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.cubrid.common.ui.spi.util.CommonUITool;

/**
 * This is web browser editor part
 *
 * @author Isaiah Choe
 * @version 1.0 - Apr 23, 2012 created by Isaiah Choe
 */
public class NewFeatureEditorPart extends
		EditorPart {
	public final static String ID = "com.cubrid.common.ui.common.control.NewFeatureEditorPart";
	private Browser browser;

	public void createPartControl(Composite parent) {
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.marginBottom = 0;
		layout.marginTop = 0;
		layout.marginLeft = 0;
		layout.marginRight = 0;

		parent.setLayout(layout);

		browser = new Browser(parent, SWT.None);
		browser.setLayoutData(CommonUITool.createGridData(GridData.FILL_BOTH, 1, 1, -1, -1));
	}

	public void setFocus() {
		noOp();
	}

	public void go(String url) {
		browser.setUrl(url);
	}

	public void refresh() {
		browser.refresh();
	}

	public void back() {
		browser.back();
	}

	public void forward() {
		browser.forward();
	}

	public void stop() {
		browser.stop();
	}

	public void doSave(IProgressMonitor monitor) {
		noOp();
	}

	public void doSaveAs() {
		noOp();
	}

	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
		setTitleToolTip(input.getToolTipText());
	}

	public boolean isDirty() {
		return false;
	}

	public boolean isSaveAsAllowed() {
		return false;
	}
}
