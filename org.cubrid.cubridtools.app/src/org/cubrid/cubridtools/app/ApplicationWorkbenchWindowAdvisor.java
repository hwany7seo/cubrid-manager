/*
 * Copyright (C) 2014 Search Solution Corporation. All rights reserved by Search
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

package org.cubrid.cubridtools.app;

import com.cubrid.common.core.util.ApplicationType;
import com.cubrid.common.core.util.LogUtil;
import com.cubrid.common.core.util.StringUtil;
import com.cubrid.common.ui.CommonUIPlugin;
import com.cubrid.common.ui.common.notice.control.NoticeDashboardEditor;
import com.cubrid.common.ui.common.notice.control.NoticeDashboardInput;
import com.cubrid.common.ui.common.preference.GeneralPreference;
import com.cubrid.common.ui.common.query.autosave.CheckQueryEditorTask;
import com.cubrid.common.ui.common.query.autosave.HeartBeatTaskManager;
import com.cubrid.common.ui.query.editor.InfoWindowManager;
import com.cubrid.common.ui.query.editor.QueryEditorPart;
import com.cubrid.common.ui.query.editor.QueryEditorUtil;
import com.cubrid.common.ui.spi.ResourceManager;
import com.cubrid.common.ui.spi.persist.CubridJdbcManager;
import com.cubrid.common.ui.spi.progress.JobFamily;
import com.cubrid.common.ui.spi.util.CommonUITool;
import com.cubrid.common.ui.spi.util.UrlConnUtil;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IPartService;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PerspectiveAdapter;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.part.EditorPart;
import org.slf4j.Logger;

/**
 * The workbench window advisor object is created in response to a workbench window being created
 * (one per window), and is used to configure the window.
 *
 * <p>ApplicationWorkbenchWindowAdvisor
 *
 * @author Kevin.Wang
 *     <p>Create at 2014-4-14
 */
public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {
    private static final Logger LOGGER = LogUtil.getLogger(ApplicationWorkbenchWindowAdvisor.class);
    private static final String CLIENT = ApplicationType.CUBRID_TOOLS.getRssName();
    private Timer timer = null;

    private Map<String, List<IEditorReference>> perspectiveEditorMap =
            new HashMap<String, List<IEditorReference>>();
    private Map<String, IEditorReference> lastActiveEditorMap =
            new HashMap<String, IEditorReference>();

    public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
        /*Support hide editors when switch perspective*/
        IPartService service = (IPartService) configurer.getWindow().getService(IPartService.class);
        service.addPartListener(
                new IPartListener() {
                    public void partActivated(IWorkbenchPart part) {}

                    public void partBroughtToTop(IWorkbenchPart part) {}

                    public void partClosed(IWorkbenchPart part) {
                        IWorkbenchPage page = part.getSite().getPage();
                        IPerspectiveDescriptor activePerspective = page.getPerspective();
                        List<IEditorReference> editorList =
                                perspectiveEditorMap.get(activePerspective.getId());
                        if (editorList != null) {
                            editorList.remove(part);
                        }
                    }

                    public void partDeactivated(IWorkbenchPart part) {}

                    public void partOpened(IWorkbenchPart part) {
                        if (part instanceof EditorPart) {
                            IWorkbenchPage page = part.getSite().getPage();
                            IPerspectiveDescriptor activePerspective = page.getPerspective();

                            List<IEditorReference> editorList =
                                    perspectiveEditorMap.get(activePerspective.getId());
                            if (editorList == null) {
                                editorList = new ArrayList<IEditorReference>();
                                perspectiveEditorMap.put(activePerspective.getId(), editorList);
                            }
                            for (IEditorReference reference : page.getEditorReferences()) {
                                editorList.add(reference);
                            }
                        }
                    }
                });
        /*Add perspective listener*/
        configurer
                .getWindow()
                .addPerspectiveListener(
                        new PerspectiveAdapter() {
                            public void perspectiveActivated(
                                    IWorkbenchPage page, IPerspectiveDescriptor perspective) {
                                // Hide all the editors
                                IEditorReference[] editors = page.getEditorReferences();
                                for (int i = 0; i < editors.length; i++) {
                                    page.hideEditor(editors[i]);
                                }
                                // Show the editors associated with this perspective
                                List<IEditorReference> editorList =
                                        perspectiveEditorMap.get(perspective.getId());
                                if (editorList != null) {
                                    for (IEditorReference reference : editorList) {
                                        page.showEditor(reference);
                                    }
                                    // Send the last active editor to the top
                                    IEditorReference lastActiveReference =
                                            lastActiveEditorMap.get(perspective.getId());
                                    page.bringToTop(lastActiveReference.getPart(true));
                                }
                            }

                            public void perspectiveSavedAs(
                                    IWorkbenchPage page,
                                    IPerspectiveDescriptor oldPerspective,
                                    IPerspectiveDescriptor newPerspective) {}

                            public void perspectiveDeactivated(
                                    IWorkbenchPage page, IPerspectiveDescriptor perspective) {
                                IEditorPart activeEditor = page.getActiveEditor();
                                if (activeEditor != null) {
                                    // Find the editor reference that relates to this editor input
                                    IEditorReference[] editorRefs =
                                            page.findEditors(
                                                    activeEditor.getEditorInput(),
                                                    null,
                                                    IWorkbenchPage.MATCH_INPUT);
                                    if (editorRefs.length > 0) {
                                        lastActiveEditorMap.put(perspective.getId(), editorRefs[0]);
                                    }
                                }
                            }
                        });
    }

    /**
     * Creates a new action bar advisor to configure the action bars of the window via the given
     * action bar configurer. The default implementation returns a new instance of {@link
     * ActionBarAdvisor}.
     *
     * @param configurer the action bar configurer for the window
     * @return the action bar advisor for the window
     */
    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new ApplicationActionBarAdvisor(configurer);
    }

    /** Performs arbitrary actions before the window is opened. */
    public void preWindowOpen() {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.setShowCoolBar(true);
        configurer.setShowStatusLine(true);
        configurer.setShowProgressIndicator(true);
    }

    /** Performs arbitrary actions after the window is created. */
    public void postWindowCreate() {
        Shell shell = getWindowConfigurer().getWindow().getShell();
        shell.setMaximized(GeneralPreference.isMaximizeWindowOnStartUp());
    }

    /**
     * Performs arbitrary actions as the window's shell is being closed directly, and possibly veto
     * the close.
     *
     * @return <code>true</code> to allow the window to close, and <code>false</code> to prevent the
     *     window from closing
     * @see org.eclipse.ui.IWorkbenchWindow#close
     * @see WorkbenchAdvisor#preShutdown()
     */
    public boolean preWindowShellClose() {
        Shell shell = getWindowConfigurer().getWindow().getShell();
        GeneralPreference.setMaximizeWindowOnStartUp(shell.getMaximized());

        if (timer != null) {
            timer.cancel();
            HeartBeatTaskManager.getInstance().cancel();
        }
        /*Close the information window*/
        InfoWindowManager.dispose();

        /*All opened queryEditor*/
        List<QueryEditorPart> editorPartList = QueryEditorUtil.getAllQueryEditorPart();
        boolean isNeedSaveQueryEditor = isNeedSaveQueryEditor(editorPartList);
        boolean hasJobRunning = false;
        final JobFamily jobFamily = new JobFamily();
        jobFamily.setServerName(JobFamily.ALL_SERVER);
        Job[] jobs = Job.getJobManager().find(jobFamily);
        if (jobs.length > 0) {
            hasJobRunning = true;
        }
        boolean isExit = false;
        if (hasJobRunning) {
            isExit =
                    CommonUITool.openConfirmBox(
                            getWindowConfigurer().getWindow().getShell(),
                            Messages.msgExistConfirmWithJob);
            if (isExit) {
                Display.getDefault()
                        .asyncExec(
                                new Runnable() {
                                    public void run() {
                                        try {
                                            Job.getJobManager().cancel(jobFamily);
                                        } catch (Exception e) {
                                            LOGGER.error("Stopping background jobs was failed.", e);
                                        }
                                    }
                                });
                if (isNeedSaveQueryEditor) {
                    processSaveQueryEditor();
                }
            }
        } else {
            if (isNeedSaveQueryEditor) {
                processSaveQueryEditor();
                return true;
            } else {
                if (GeneralPreference.isAlwaysExit()) {
                    return true;
                }
                MessageDialogWithToggle dialog =
                        MessageDialogWithToggle.openOkCancelConfirm(
                                getWindowConfigurer().getWindow().getShell(),
                                com.cubrid.common.ui.common.Messages.titleExitConfirm,
                                Messages.msgExistConfirm,
                                com.cubrid.common.ui.common.Messages.msgToggleExitConfirm,
                                false,
                                CommonUIPlugin.getDefault().getPreferenceStore(),
                                GeneralPreference.IS_ALWAYS_EXIT);
                isExit = dialog.getReturnCode() == 0 ? true : false;
            }
        }
        return isExit;
    }

    private void processSaveQueryEditor() {
        CheckQueryEditorTask.getInstance().doSave();
    }

    private boolean isNeedSaveQueryEditor(List<QueryEditorPart> editorPartList) {
        for (QueryEditorPart editor : editorPartList) {
            if (editor == null
                    || editor.getCombinedQueryComposite().isDisposed()
                    || editor.getAllQueries().trim().length() == 0) {
                continue;
            } else {
                return true;
            }
        }
        return false;
    }

    /** Performs arbitrary actions after the window is closed. */
    public void postWindowClose() {
        ResourceManager.dispose();
        super.postWindowClose();
    }

    private void cleanComparedLogFiles() {
        try {
            String path = CommonUITool.getWorkspacePath();
            if (StringUtil.isEmpty(path)) {
                return;
            }

            File dir = new File(path);
            File[] files =
                    dir.listFiles(
                            new FilenameFilter() {
                                public boolean accept(File file, String fileName) {
                                    if (fileName.indexOf(".compared") != -1) {
                                        return true;
                                    }
                                    return false;
                                }
                            });

            if (files != null && files.length > 0) {
                for (File file : files) {
                    if (file != null) {
                        file.delete();
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("", e.getMessage());
        }
    }

    private void showDashboard() {
        try {
            if (GeneralPreference.isCheckNewInfoOnStartUp()) {
                PlatformUI.getWorkbench()
                        .getActiveWorkbenchWindow()
                        .getActivePage()
                        .openEditor(new NoticeDashboardInput(CLIENT), NoticeDashboardEditor.ID);
            }
        } catch (Exception e) {
            LOGGER.info("", e);
        }
    }

    /** Performs arbitrary actions after the window is opened. */
    public void postWindowOpen() {
        removePlatformDependencyActions();
        showDashboard();

        new Thread(
                        new Runnable() {
                            public void run() {
                                timer = new Timer();
                                timer.schedule(
                                        HeartBeatTaskManager.getInstance(),
                                        1000,
                                        HeartBeatTaskManager.BEAT_TIME);

                                cleanComparedLogFiles();
                                UrlConnUtil.isExistNewCubridVersion(
                                        Version.buildVersionId, "CUBRID-MANAGER");
                            }
                        })
                .start();

        // P2Util.checkForUpdate(GeneralPreference.isAutoCheckUpdate());
        /*Load JDBC driver for CMT*/
        CubridJdbcManager.loadCubridJdbc(false);
    }

    private void removePlatformDependencyActions() {
        // remove some menu items
        IMenuManager mm = getWindowConfigurer().getActionBarConfigurer().getMenuManager();
        for (IContributionItem item : mm.getItems()) {
            if (item == null) {
                continue;
            }

            MenuManager sm = (MenuManager) item;
            for (IContributionItem sitem : sm.getItems()) {
                if (sitem == null || sitem.getId() == null) {
                    continue;
                }

                if (sitem.getId().equals("org.eclipse.ui.actions.showKeyAssistHandler")
                        || sitem.getId().equals("com.cubrid.common.update.p2.menu.install")
                        || sitem.getId().equals("converstLineDelimitersTo")
                        || sitem.getId().equals("save.ext")
                        || sitem.getId().equals("org.eclipse.ui.openLocalFile")
                        || sitem.getId().equals("new.ext")) {
                    sm.remove(sitem.getId());
                    sm.update(true);
                    mm.update(true);
                }
            }
        }

        // remove some tool bar items
        ICoolBarManager cm = getWindowConfigurer().getActionBarConfigurer().getCoolBarManager();
        for (IContributionItem item : cm.getItems()) {
            if (item == null || item.getId() == null) {
                continue;
            }

            if (item.getId().equals("org.eclipse.ui.edit.text.actionSet.annotationNavigation")
                    || item.getId().equals("org.eclipse.ui.edit.text.actionSet.navigation")) {
                cm.remove(item.getId());
                cm.update(true);
            }
        }

        // remove some preference items
        PreferenceManager pm =
                getWindowConfigurer().getWindow().getWorkbench().getPreferenceManager();
        for (IPreferenceNode item : pm.getRootSubNodes()) {
            if (item.getId().equals("org.eclipse.help.ui.browsersPreferencePage")
                    || item.getId().equals("org.eclipse.ui.preferencePages.Workbench")) {
                pm.remove(item.getId());
            }
        }
    }
}
