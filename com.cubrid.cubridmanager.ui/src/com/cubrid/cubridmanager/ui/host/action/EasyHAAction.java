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
package com.cubrid.cubridmanager.ui.host.action;

import com.cubrid.common.core.util.CompatibleUtil;
import com.cubrid.common.ui.spi.action.SelectionAction;
import com.cubrid.common.ui.spi.model.CubridServer;
import com.cubrid.cubridmanager.core.common.model.CasAuthType;
import com.cubrid.cubridmanager.core.common.model.ServerInfo;
import com.cubrid.cubridmanager.core.common.model.ServerUserInfo;
import com.cubrid.cubridmanager.ui.host.control.ConfigHAWizard;
import com.cubrid.cubridmanager.ui.host.control.ConfigHAWizardDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.widgets.Shell;

/**
 * Config HA Action Description
 *
 * @author Kevin.Wang
 * @version 1.0 - 2012-11-27 created by Kevin.Wang
 */
public class EasyHAAction extends SelectionAction {
    public static final String ID = EasyHAAction.class.getName();

    /**
     * The constructor
     *
     * @param shell
     * @param text
     * @param enabledIcon
     * @param disabledIcon
     */
    public EasyHAAction(
            Shell shell, String text, ImageDescriptor enabledIcon, ImageDescriptor disabledIcon) {
        this(shell, null, text, enabledIcon, disabledIcon);
    }

    /**
     * The constructor
     *
     * @param shell
     * @param provider
     * @param text
     * @param enabledIcon
     * @param disabledIcon
     */
    public EasyHAAction(
            Shell shell,
            ISelectionProvider provider,
            String text,
            ImageDescriptor enabledIcon,
            ImageDescriptor disabledIcon) {
        super(shell, provider, text, enabledIcon);
        this.setId(ID);
        this.setToolTipText(text);
        this.setDisabledImageDescriptor(disabledIcon);
    }

    /**
     * Return whether this action support to select multi object,if not support,this action will be
     * disabled
     *
     * @return <code>true</code> if allow multi selection;<code>false</code> otherwise
     */
    public boolean allowMultiSelections() {
        return false;
    }

    /**
     * Return whether this action support this object,if not support,this action will be disabled
     *
     * @param obj the Object
     * @return <code>true</code> if support this obj;<code>false</code> otherwise
     */
    public boolean isSupported(Object obj) {
        if (obj instanceof CubridServer) {
            CubridServer server = (CubridServer) obj;
            ServerInfo serverInfo = server.getServerInfo();
            if (!CompatibleUtil.isSupportNewHAConfFile(serverInfo)) {
                return false;
            }

            ServerUserInfo userInfo = serverInfo.getLoginedUserInfo();
            if (userInfo == null || CasAuthType.AUTH_ADMIN != userInfo.getCasAuth()) {
                return false;
            }

            return true;
        }

        return false;
    }

    /** Open the edit broker property editor */
    public void run() {
        final Object[] obj = this.getSelectedObj();
        if (obj == null || obj.length <= 0) {
            setEnabled(false);
            return;
        }

        CubridServer server = (CubridServer) getSelectedObj()[0];
        ConfigHAWizardDialog dlg = new ConfigHAWizardDialog(getShell(), new ConfigHAWizard(server));
        dlg.setPageSize(800, 400);
        dlg.open();
    }
}
