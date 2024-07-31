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
package com.cubrid.common.ui.common.control;

import static com.cubrid.common.core.util.NoOp.noOp;
import static com.cubrid.common.ui.spi.util.UrlConnUtil.*;

import com.cubrid.common.core.util.LogUtil;
import com.cubrid.common.ui.common.Messages;
import com.cubrid.common.ui.spi.util.CommonUITool;
import java.net.URL;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.ui.part.EditorPart;
import org.slf4j.Logger;

/**
 * CUBRID new information editor part
 *
 * @author pangqiren
 * @version 1.0 - 2009-6-23 created by pangqiren
 */
public class CubridNewInfoEditorPart extends EditorPart {
    private static final Logger LOGGER = LogUtil.getLogger(CubridNewInfoEditorPart.class);
    public static final String ID = CubridNewInfoEditorPart.class.getName();

    /**
     * Saves the contents of this editor.
     *
     * @param monitor the progress monitor
     */
    public void doSave(IProgressMonitor monitor) {
        noOp();
    }

    /**
     * Saves the contents of this editor to another object.
     *
     * @see IEditorPart
     */
    public void doSaveAs() {
        noOp();
    }

    /**
     * Initializes this editor with the given editor site and input.
     *
     * @param site the editor site
     * @param input the editor input
     * @exception PartInitException if this editor was not initialized successfully
     */
    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        setSite(site);
        setInput(input);
        setTitleToolTip(input.getToolTipText());
    }

    /** Dispose the system resource */
    public void dispose() {
        noOp();
    }

    /**
     * Return whether the editor is dirty
     *
     * @return <code>true</code> if it is dirty;<code>false</code> otherwise
     */
    public boolean isDirty() {
        return false;
    }

    /**
     * Return whether the save as operation is allowed
     *
     * @return <code>true</code> if it is allowed;<code>false</code> otherwise
     */
    public boolean isSaveAsAllowed() {
        return false;
    }

    /**
     * Create the editor content
     *
     * @param parent the parent composite
     */
    public void createPartControl(Composite parent) {
        String url =
                Platform.getNL().equals("ko_KR") ? CHECK_NEW_INFO_URL_KO : CHECK_NEW_INFO_URL_EN;
        try {
            Browser browser = new Browser(parent, SWT.NONE);
            browser.setUrl(url);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);

            Label label = new Label(parent, SWT.NONE);
            IWorkbenchBrowserSupport support = PlatformUI.getWorkbench().getBrowserSupport();
            try {
                IWebBrowser browser = support.getExternalBrowser();
                browser.openURL(new URL(CommonUITool.urlEncodeForSpaces(url.toCharArray())));
            } catch (Exception browserEx) {
                LOGGER.warn(browserEx.getMessage(), browserEx);
                label.setText(Messages.errCannotOpenExternalBrowser);
                return;
            }
            label.setText(Messages.errCannotOpenInternalBrowser);
        }
    }

    /** When editor is focus,call this method */
    public void setFocus() {
        noOp();
    }
}
