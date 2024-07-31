/*
 * Copyright (C) 2009 Search Solution Corporation. All rights reserved by Search
 * Solution.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  - Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *  - Neither the name of the <ORGANIZATION> nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission.
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
package com.cubrid.cubridmanager.ui.cubrid.dbspace.action;

import com.cubrid.common.ui.spi.action.SelectionAction;
import com.cubrid.common.ui.spi.model.CubridServer;
import com.cubrid.common.ui.spi.model.DefaultCubridNode;
import com.cubrid.cubridmanager.ui.cubrid.dbspace.dialog.AutoAddVolumeLogDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.widgets.Shell;

/**
 * An action class that is responsible for showing the log of query
 *
 * @author lizhiqiang
 * @version 1.0 - 2009-5-21 created by lizhiqiang
 */
public class AutoAddVolumeLogAction extends SelectionAction {

    public static final String ID = AutoAddVolumeLogAction.class.getName();

    /**
     * The Constructor
     *
     * @param shell
     * @param text
     * @param icon
     */
    public AutoAddVolumeLogAction(Shell shell, String text, ImageDescriptor icon) {
        this(shell, null, text, icon);
    }

    /**
     * The Constructor
     *
     * @param shell
     * @param provider
     * @param text
     * @param icon
     */
    public AutoAddVolumeLogAction(
            Shell shell, ISelectionProvider provider, String text, ImageDescriptor icon) {
        super(shell, provider, text, icon);
        this.setId(ID);
        this.setToolTipText(text);
    }

    /** @see org.eclipse.jface.action.Action#run() */
    public void run() {
        Object[] obj = this.getSelectedObj();
        DefaultCubridNode node = null;
        CubridServer server = null;
        if (obj.length > 0 && obj[0] instanceof DefaultCubridNode) {
            node = (DefaultCubridNode) obj[0];
            server = node.getServer();
        }

        AutoAddVolumeLogDialog dlg = new AutoAddVolumeLogDialog(getShell());
        dlg.setServer(server);
        if (dlg.loadData()) {
            dlg.open();
        }
    }

    /**
     * Makes this action not support to select multi object
     *
     * @see com.cubrid.common.ui.spi.action.ISelectionAction#allowMultiSelections ()
     * @return <code>true</code> if allow multi selection;<code>false</code> otherwise
     */
    public boolean allowMultiSelections() {
        return false;
    }

    /**
     * Return whether this action support this object,if not support,this action will be disabled
     *
     * @see com.cubrid.common.ui.spi.action.ISelectionAction#isSupported(java .lang.Object)
     * @param obj the Object
     * @return <code>true</code> if support this obj;<code>false</code> otherwise
     */
    public boolean isSupported(Object obj) {
        return true;
    }
}
