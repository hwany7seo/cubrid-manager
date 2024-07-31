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
package com.cubrid.common.ui.common.sqlrunner.dialog;

import com.cubrid.common.ui.perspective.PerspectiveManager;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;

/**
 * @author fulei
 * @version 1.0 - 2012-7-26 created by fulei
 */
public class RunSQLFileDialogDNDController {
    private static Map<String, TreeViewer> perspectiveTreeviewerMap =
            new HashMap<String, TreeViewer>();
    private RunSQLFileDialog dialog;

    public RunSQLFileDialogDNDController(RunSQLFileDialog dialog) {
        this.dialog = dialog;
    }

    /**
     * Register the drag source
     *
     * @param treeViewer TreeViewer
     */
    public static void registerDragSource(String perspectiveId, TreeViewer treeViewer) {
        synchronized (RunSQLFileDialogDNDController.class) {
            if (perspectiveId != null && treeViewer != null) {
                perspectiveTreeviewerMap.put(perspectiveId, treeViewer);
            }
        }
    }

    /** register drag source and DB tree target */
    public void registerDropTarget() {
        synchronized (this) {
            DropTarget target = new DropTarget(dialog.getDatabaseTableComp(), DND.DROP_MOVE);
            target.setTransfer(new Transfer[] {TextTransfer.getInstance()});
            target.addDropListener(
                    new DropTargetAdapter() {
                        /**
                         * @see
                         *     org.eclipse.swt.dnd.DropTargetAdapter#drop(org.eclipse.swt.dnd.DropTargetEvent)
                         * @param event the information associated with the drop event
                         */
                        public void drop(DropTargetEvent event) {
                            addDatabase();
                        }
                    });
        }
    }

    /** add database */
    public void addDatabase() {
        synchronized (this) {
            TreeViewer treeViewer =
                    perspectiveTreeviewerMap.get(
                            PerspectiveManager.getInstance().getCurrentPerspectiveId());
            if (treeViewer == null) {
                return;
            }
            ISelection selection = treeViewer.getSelection();
            if (!(selection instanceof TreeSelection)) {
                return;
            }

            TreeSelection ts = (TreeSelection) selection;
            Object[] objs = ts.toArray();
            dialog.addDatabase(objs);
        }
    }
}
