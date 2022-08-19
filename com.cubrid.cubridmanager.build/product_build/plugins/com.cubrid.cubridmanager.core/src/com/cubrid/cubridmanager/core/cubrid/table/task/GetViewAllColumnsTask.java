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
package com.cubrid.cubridmanager.core.cubrid.table.task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cubrid.cubridmanager.core.Messages;
import com.cubrid.cubridmanager.core.common.jdbc.JDBCTask;
import com.cubrid.cubridmanager.core.cubrid.database.model.DatabaseInfo;

/**
 * 
 * Get all attributes of some class
 * 
 * @author robin
 * @version 1.0 - 2009-5-31
 */
public class GetViewAllColumnsTask extends
		JDBCTask {
	private String className = null;
	private List<String> allVclassList = null;

	public GetViewAllColumnsTask(DatabaseInfo dbInfo) {
		super("GetAllAttr", dbInfo);
	}
	
	public GetViewAllColumnsTask(DatabaseInfo dbInfo, Connection connection) {
		super("GetAllAttr", dbInfo, connection);
	}
	/**
	 * 
	 * Get all views class list and task execute
	 */
	public void getAllVclassListTaskExcute() {
		allVclassList = new ArrayList<String>();
		try {
			if (errorMsg != null && errorMsg.trim().length() > 0) {
				return;
			}

			if (connection == null || connection.isClosed()) {
				errorMsg = Messages.error_getConnection;
				return;
			}

			String sql;
			if (databaseInfo.isSupportUserSchema()) {
				sql = "SELECT vclass_name, vclass_def FROM db_vclass WHERE CONCAT(owner_name, '.' , vclass_name)=?";
			}  else {
				sql = "SELECT vclass_name, vclass_def FROM db_vclass WHERE vclass_name=?";
			}

			// [TOOLS-2425]Support shard broker
			sql = databaseInfo.wrapShardQuery(sql);

			stmt = connection.prepareStatement(sql);
			((PreparedStatement) stmt).setString(1, className);
			rs = ((PreparedStatement) stmt).executeQuery();
			while (rs.next()) {
				String attrName = rs.getString("vclass_def");
				allVclassList.add(attrName);
			}
		} catch (SQLException e) {
			errorMsg = e.getMessage();
		} finally {
			finish();
		}
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public List<String> getAllVclassList() {
		return allVclassList;
	}
}
