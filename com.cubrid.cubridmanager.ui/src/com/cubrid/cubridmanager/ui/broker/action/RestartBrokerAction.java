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
package com.cubrid.cubridmanager.ui.broker.action;

import com.cubrid.common.ui.spi.action.ActionManager;
import com.cubrid.common.ui.spi.action.SelectionAction;
import com.cubrid.common.ui.spi.model.DefaultCubridNode;
import com.cubrid.common.ui.spi.progress.CommonTaskExec;
import com.cubrid.common.ui.spi.progress.ExecTaskWithProgress;
import com.cubrid.common.ui.spi.progress.TaskExecutor;
import com.cubrid.common.ui.spi.util.CommonUITool;
import com.cubrid.cubridmanager.core.broker.task.StartBrokerTask;
import com.cubrid.cubridmanager.core.broker.task.StopBrokerTask;
import com.cubrid.cubridmanager.core.common.model.CasAuthType;
import com.cubrid.cubridmanager.core.common.model.ServerInfo;
import com.cubrid.cubridmanager.core.common.model.ServerUserInfo;
import com.cubrid.cubridmanager.ui.broker.Messages;
import com.cubrid.cubridmanager.ui.spi.model.CubridBroker;
import com.cubrid.cubridmanager.ui.spi.model.CubridBrokerFolder;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Shell;

/**
 * Restart broker
 *
 * @author fulei
 * @version 1.0 - 2012-10-8 created by fulei
 */
public class RestartBrokerAction extends SelectionAction {
    public static final String ID = RestartBrokerAction.class.getName();

    public RestartBrokerAction(Shell shell, String text, ImageDescriptor icon) {
        this(shell, null, text, icon);
    }

    protected RestartBrokerAction(
            Shell shell, ISelectionProvider provider, String text, ImageDescriptor icon) {
        super(shell, provider, text, icon);
        this.setId(ID);
        this.setToolTipText(text);
    }

    public void run() {
        final Object[] obj = this.getSelectedObj();
        DefaultCubridNode selection = (DefaultCubridNode) obj[0];
        if (selection == null || selection.getServer() == null) {
            return;
        }

        ServerInfo serverInfo = selection.getServer().getServerInfo();
        if (serverInfo == null) {
            return;
        }

        // stop first
        StopBrokerTask stopTask = new StopBrokerTask(serverInfo);
        stopTask.setBrokerName(selection.getLabel());

        // then start again
        StartBrokerTask startTask = new StartBrokerTask(serverInfo);
        startTask.setBrokerName(selection.getLabel());

        final String taskName = Messages.bind(Messages.restartBrokerTaskName, selection.getLabel());
        TaskExecutor taskExecutor = new CommonTaskExec(taskName);
        taskExecutor.addTask(stopTask);
        taskExecutor.addTask(startTask);

        new ExecTaskWithProgress(taskExecutor).exec();
        if (!taskExecutor.isSuccess()) {
            return;
        }

        TreeViewer treeViewer = (TreeViewer) this.getSelectionProvider();
        CommonUITool.refreshNavigatorTree(treeViewer, selection.getParent());
        ActionManager.getInstance().fireSelectionChanged(getSelection());
    }

    public boolean allowMultiSelections() {
        return false;
    }

    public boolean isSupported(Object obj) {
        return isSupportedNode(obj);
    }

    public static boolean isSupportedNode(Object obj) {
        if (obj instanceof CubridBroker) {
            CubridBroker selection = ((CubridBroker) obj);
            if (selection.getServer() == null || selection.getServer().getServerInfo() == null) {
                return false;
            }

            ServerUserInfo userInfo = selection.getServer().getServerInfo().getLoginedUserInfo();
            if (userInfo == null || CasAuthType.AUTH_ADMIN != userInfo.getCasAuth()) {
                return false;
            }

            CubridBrokerFolder parent = (CubridBrokerFolder) (selection.getParent());
            return parent.isRunning() && selection.isRunning();
        }

        return false;
    }
}
