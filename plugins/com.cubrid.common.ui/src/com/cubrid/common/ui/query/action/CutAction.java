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
package com.cubrid.common.ui.query.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.actions.ActionFactory;

import com.cubrid.common.ui.spi.action.ActionManager;
import com.cubrid.common.ui.spi.action.FocusAction;
import com.cubrid.common.ui.spi.util.CommonUITool;

/**
 * 
 * This action is responsible to cut query editor content to the clipboard
 * 
 * @author pangqiren 2009-3-2
 */
public class CutAction extends
		FocusAction {

	public static final String ID = ActionFactory.CUT.getId();

	/**
	 * The constructor
	 * 
	 * @param shell
	 * @param focusProvider
	 * @param text
	 * @param icon
	 */
	public CutAction(Shell shell, Control focusProvider, String text,
			ImageDescriptor icon) {
		super(shell, focusProvider, text, icon);
		this.setId(ID);
		this.setActionDefinitionId("org.eclipse.ui.edit.cut");
	}

	/**
	 * The constructor
	 * 
	 * @param shell
	 * @param text
	 * @param icon
	 */
	public CutAction(Shell shell, String text, ImageDescriptor icon) {
		this(shell, null, text, icon);
	}

	/**
	 * Notifies that the focus gained event
	 * 
	 * @param event an event containing information about the focus change
	 */
	public void focusGained(FocusEvent event) {
		setEnabled(false);
		if (event.getSource() instanceof StyledText) {
			StyledText stext = (StyledText) event.getSource();
			boolean isEnabled = stext != null
					&& stext.getSelectionText() != null
					&& stext.getSelectionText().length() > 0
					&& stext.isEnabled() && stext.getEditable();
			setEnabled(isEnabled);
		}
	}

	/**
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		TextTransfer textTransfer = TextTransfer.getInstance();
		Clipboard clipboard = CommonUITool.getClipboard();
		Control control = getFocusProvider();
		if (control instanceof StyledText) {
			StyledText stext = (StyledText) control;
			String data = stext.getSelectionText();
			if (data != null && !data.equals("")) {
				clipboard.setContents(new Object[]{data },
						new Transfer[]{textTransfer });
				Point range = stext.getSelectionRange();
				stext.replaceTextRange(range.x, range.y, "");
				stext.setSelection(range.x);

				IAction pasteAction = ActionManager.getInstance().getAction(
						PasteAction.ID);
				FocusAction.changeActionStatus(pasteAction, stext);
			}
		}
	}
}