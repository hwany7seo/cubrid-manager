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

import java.sql.SQLException;

import org.slf4j.Logger;

import com.cubrid.common.core.util.LogUtil;
import com.cubrid.common.core.util.QuerySyntax;
import com.cubrid.cubridmanager.core.Messages;
import com.cubrid.cubridmanager.core.common.jdbc.JDBCTask;
import com.cubrid.cubridmanager.core.cubrid.database.model.DatabaseInfo;
import com.cubrid.jdbc.proxy.driver.CUBRIDResultSetProxy;

/**
 * GetRecordCountTask Description
 * 
 * @author moulinwang
 * @version 1.0 - 2009-5-21 created by moulinwang
 */
public class GetRecordCountTask extends JDBCTask {
	private static final Logger LOGGER = LogUtil.getLogger(GetRecordCountTask.class);

	public GetRecordCountTask(DatabaseInfo dbInfo) {
		super("GetAllAttr", dbInfo);
	}

	/**
	 * 
	 * Get the record count
	 * 
	 * @param table String the given table name
	 * @param whereCondition String
	 * @return int
	 */
	public int getRecordCount(String table, String whereCondition) {
		String sql = "SELECT COUNT(*) FROM " + QuerySyntax.escapeKeyword(table);
		if (null != whereCondition) {
			sql = sql + " " + whereCondition;
		}

		// [TOOLS-2425]Support shard broker
		sql = DatabaseInfo.wrapShardQuery(databaseInfo, sql);

		return getRecordCount(sql);
	}

	/**
	 * 
	 * Get the record count
	 * 
	 * @param table String the given table name
	 * @param column String the given column name
	 * @param whereCondition String
	 * @return int
	 */
	public int getRecordCount(String table, String column, String whereCondition) {
		String sql = "SELECT COUNT(" + QuerySyntax.escapeKeyword(column) + ") FROM " + QuerySyntax.escapeKeyword(table);
		if (null != whereCondition) {
			sql = sql + " " + whereCondition;
		}

		sql = DatabaseInfo.wrapShardQuery(databaseInfo, sql);
		return getRecordCount(sql);
	}

	/**
	 * Get record count
	 * 
	 * @param sql String
	 * @return int
	 */
	public int getRecordCount(String sql) {
		try {
			if (errorMsg != null && errorMsg.trim().length() > 0) {
				return -1;
			}

			if (connection == null || connection.isClosed()) {
				errorMsg = Messages.error_getConnection;
				return -1;
			}

			stmt = connection.createStatement();
			rs = stmt.executeQuery(sql);
			rs.next();

			return rs.getInt(1);
		} catch (SQLException e) {
			errorMsg = e.getMessage();
		} finally {
			finish();
		}

		return -1;
	}

	/**
	 * Get the record count,but not close connection,statement,result set.
	 * 
	 * @param table String
	 * @param whereCondition String
	 * @return int
	 */
	public int getRecordCountNoClose(String table, String whereCondition) {
		String sql = "SELECT COUNT(*) FROM " + QuerySyntax.escapeKeyword(table);
		if (null != whereCondition) {
			sql = sql + " " + whereCondition;
		}
		sql = DatabaseInfo.wrapShardQuery(databaseInfo, sql);
		return getRecordCountNoClose(sql);
	}

	/**
	 * Get the record count,but not close connection,statement,result set.
	 * 
	 * @param sql String
	 * @return int
	 */
	public int getRecordCountNoClose(String sql) {
		try {
			if (errorMsg != null && errorMsg.trim().length() > 0) {
				return -1;
			}

			if (connection == null || connection.isClosed()) {
				errorMsg = Messages.error_getConnection;
				return -1;
			}

			stmt = connection.createStatement();
			rs = (CUBRIDResultSetProxy) stmt.executeQuery(sql);
			rs.next();

			return rs.getInt(1);
		} catch (SQLException e) {
			LOGGER.error("", e);
			errorMsg = e.getMessage();
		} finally {
			finish();
		}

		return -1;
	}
}
