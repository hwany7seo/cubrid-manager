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

package com.cubrid.cubridmanager.core.cubrid.trigger.task;

import java.sql.SQLException;

import org.slf4j.Logger;

import com.cubrid.common.core.util.LogUtil;
import com.cubrid.cubridmanager.core.Messages;
import com.cubrid.cubridmanager.core.common.jdbc.JDBCTask;
import com.cubrid.cubridmanager.core.cubrid.database.model.DatabaseInfo;

/**
 * This class is to add a trigger in CUBRID database.
 * 
 * Usage: You must first set fields by invoking setXXX(\<T\>) methods, then call
 * sendMsg() method to send a request message, the response message is the
 * information of the special class.
 * 
 * @author Kevin Cao 2010-11-4
 */
public class JDBCSqlExecuteTask extends JDBCTask {
	private static final Logger LOGGER = LogUtil.getLogger(JDBCSqlExecuteTask.class);
	protected String sql = "";

	public JDBCSqlExecuteTask(String taskName, DatabaseInfo dbInfo) {
		this(taskName, dbInfo, true);
	}

	public JDBCSqlExecuteTask(String taskName, DatabaseInfo dbInfo,
			boolean isAutoCommit) {
		super(taskName, dbInfo, isAutoCommit);
	}

	public JDBCSqlExecuteTask(String taskName, DatabaseInfo dbInfo, String sql) {
		this(taskName, dbInfo, true);
		this.sql = sql;
	}

	/**
	 * Execute the tasks
	 */
	public void execute() {
		try {
			if (errorMsg != null && errorMsg.trim().length() > 0) {
				return;
			}

			if (connection == null || connection.isClosed()) {
				errorMsg = Messages.error_getConnection;
				return;
			}

			stmt = connection.createStatement();

			// [TOOLS-2425]Support shard broker
			sql = DatabaseInfo.wrapShardQuery(databaseInfo, sql);

			stmt.execute(sql);
			connection.commit();
		} catch (SQLException e) {
			errorMsg = e.getMessage();
			LOGGER.error(e.getMessage(), e);
		} finally {
			finish();
		}
	}
}
