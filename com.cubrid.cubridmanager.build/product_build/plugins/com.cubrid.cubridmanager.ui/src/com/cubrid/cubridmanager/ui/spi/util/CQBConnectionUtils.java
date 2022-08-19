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
package com.cubrid.cubridmanager.ui.spi.util;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;

import com.cubrid.common.core.util.LogUtil;
import com.cubrid.common.ui.query.editor.ColumnProposalAdvisor;
import com.cubrid.common.ui.spi.Messages;
import com.cubrid.common.ui.spi.model.CubridDatabase;
import com.cubrid.common.ui.spi.persist.QueryOptions;
import com.cubrid.common.ui.spi.progress.JobFamily;
import com.cubrid.common.ui.spi.util.CommonUITool;
import com.cubrid.common.ui.spi.util.LayoutUtil;
import com.cubrid.cubridmanager.ui.spi.persist.CQBDBNodePersistManager;

/**
 * 
 * Connection utility class
 * 
 * @author pangqiren
 * @version 1.0 - 2011-3-2 created by pangqiren
 */
public final class CQBConnectionUtils {

	private static final Logger LOGGER = LogUtil.getLogger(CQBConnectionUtils.class);

	private CQBConnectionUtils() {

	}

	/**
	 * 
	 * Process the resource when logout database
	 * 
	 * @param database CubridDatabase
	 * @return boolean
	 */
	public static boolean processConnectionLogout(CubridDatabase database) {

		final JobFamily jobFamily = new JobFamily();
		String serverName = database.getServer().getName();
		String dbName = database.getName();
		jobFamily.setServerName(serverName);
		jobFamily.setDbName(dbName);
		Job[] jobs = Job.getJobManager().find(jobFamily);

		if (jobs.length > 0) {
			boolean isLogout = CommonUITool.openConfirmBox(Messages.bind(
					Messages.msgConfirmLogoutConnwithJob, dbName));
			if (!isLogout) {
				return false;
			}
		}

		// check the query editor in this database
		if (!LayoutUtil.checkAllQueryEditor(database)) {
			return false;
		}

		cancelJob(jobFamily);

		database.getLoader().setLoaded(false);
		database.setLogined(false);
		database.removeAllChild();
		
		// remove column proposal cache data because of useless after logout
		if (CubridDatabase.hasValidDatabaseInfo(database)) {
			ColumnProposalAdvisor.getInstance().removeProposal(database.getDatabaseInfo());
		}

		return true;
	}

	/**
	 * 
	 * Process resource when delete database
	 * 
	 * @param database CubridDatabase
	 * @return boolean
	 */
	public static boolean processConnectionDeleted(CubridDatabase database) {

		final JobFamily jobFamily = new JobFamily();
		String serverName = database.getServer().getName();
		String dbName = database.getName();
		jobFamily.setServerName(serverName);
		jobFamily.setDbName(dbName);
		Job[] jobs = Job.getJobManager().find(jobFamily);

		if (jobs.length > 0) {
			boolean isLogout = CommonUITool.openConfirmBox(Messages.bind(
					Messages.msgConfirmDeleteConnwithJob, dbName));
			if (!isLogout) {
				return false;
			}
		}

		// check the query editor in this database
		if (!LayoutUtil.checkAllQueryEditor(database)) {
			return false;
		}

		cancelJob(jobFamily);

		database.setLogined(false);
		database.removeAllChild();
		CQBDBNodePersistManager.getInstance().removeDatabase(database);
		QueryOptions.removePref(database.getDatabaseInfo().getServerInfo());

		return true;
	}

	/**
	 * 
	 * Cancel all jobs that belong to the job family
	 * 
	 * @param jobFamily the JobFamily
	 */
	private static void cancelJob(final JobFamily jobFamily) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				try {
					Job.getJobManager().cancel(jobFamily);
				} catch (Exception ignored) {
					LOGGER.error(ignored.getMessage(), ignored);
				}
			}
		});
	}
}
