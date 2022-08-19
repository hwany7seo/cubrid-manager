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
package com.cubrid.cubridmanager.core.cubrid.user.task;

import java.sql.SQLException;

import com.cubrid.cubridmanager.core.Messages;
import com.cubrid.cubridmanager.core.common.jdbc.JDBCTask;
import com.cubrid.cubridmanager.core.cubrid.database.model.DatabaseInfo;

/**
 * Change database user password
 * 
 * @author pangqiren
 * @version 1.0 - 2010-3-19 created by pangqiren
 */
public class ChangeDbUserPwdTask extends JDBCTask {
	private String dbUserName;
	private String newPwd;

	/**
	 * The constructor
	 * 
	 * @param dbInfo
	 */
	public ChangeDbUserPwdTask(DatabaseInfo dbInfo) {
		super("ChangeDbUserPwd", dbInfo);
	}

	/**
	 * Set database user name
	 * 
	 * @param userName the database user name
	 */
	public void setDbUserName(String userName) {
		dbUserName = userName;
	}

	/**
	 * Set new password
	 *
	 * @param newPwd the new password
	 */
	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}

	/**
	 * Get the SQL for changing password
	 *
	 * @return SQL
	 */
	private String getSQL() {
		return "ALTER USER " + dbUserName + " PASSWORD '" + newPwd + "'";
	}

	/**
	 * Change database user password
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

			String sql = getSQL();

			// [TOOLS-2425]Support shard broker
			sql = DatabaseInfo.wrapShardQuery(databaseInfo, sql);

			stmt = connection.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			errorMsg = e.getMessage();
		} finally {
			finish();
		}
	}
}
