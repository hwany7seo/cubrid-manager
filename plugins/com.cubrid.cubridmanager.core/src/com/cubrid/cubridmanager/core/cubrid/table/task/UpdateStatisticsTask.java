/*
 * Copyright (C) 2013 Search Solution Corporation. All rights reserved by Search Solution. 
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
package com.cubrid.cubridmanager.core.cubrid.table.task;

import java.sql.SQLException;
import java.util.List;

import com.cubrid.cubridmanager.core.Messages;
import com.cubrid.cubridmanager.core.common.jdbc.JDBCTask;
import com.cubrid.cubridmanager.core.cubrid.database.model.DatabaseInfo;

/**
 * Update statistics for partition table
 * 
 * @author pangqiren
 * @version 1.0 - 2010-3-24 created by pangqiren
 */
public class UpdateStatisticsTask extends JDBCTask {

	private List<String> sqlList = null;

	/**
	 * The constructor
	 * 
	 * @param dbInfo
	 */
	public UpdateStatisticsTask(DatabaseInfo dbInfo) {
		super("UpdateStatistics", dbInfo);
	}

	/**
	 * Set executed SQLs
	 * 
	 * @param sqlList The SQL list
	 */
	public void setSqlList(List<String> sqlList) {
		this.sqlList = sqlList;
	}

	/**
	 * Execute to update statistics
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
			for (int i = 0; sqlList != null && i < sqlList.size(); i++) {
				String sql = sqlList.get(i);

				// [TOOLS-2425]Support shard broker
				sql = DatabaseInfo.wrapShardQuery(databaseInfo, sql);

				stmt.execute(sql);
			}
		} catch (SQLException e) {
			errorMsg = e.getMessage();
		} finally {
			finish();
		}
	}
}
