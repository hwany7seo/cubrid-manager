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
package com.cubrid.cubridmanager.core.cubrid.table.task;

import java.sql.SQLException;

import org.slf4j.Logger;

import com.cubrid.common.core.util.LogUtil;
import com.cubrid.common.core.util.QuerySyntax;
import com.cubrid.cubridmanager.core.Messages;
import com.cubrid.cubridmanager.core.common.jdbc.JDBCTask;
import com.cubrid.cubridmanager.core.cubrid.database.model.DatabaseInfo;

/**
 * Create like table task
 * 
 * @author pangqiren
 * @version 1.0 - 2010-4-19 created by pangqiren
 */
public class CreateLikeTableTask extends JDBCTask {
	private static final Logger LOGGER = LogUtil.getLogger(CreateLikeTableTask.class);
	private String newTableName;
	private String newOwnerName;
	private String likeTableName;
	private String likeOwnerName;
	private DatabaseInfo databaseInfo;

	public CreateLikeTableTask(DatabaseInfo dbInfo) {
		super("CreateLikeTable", dbInfo);
		databaseInfo = dbInfo;
	}

	public void execute() {
		try {
			if (errorMsg != null && errorMsg.trim().length() > 0) {
				return;
			}
			if (connection == null || connection.isClosed()) {
				errorMsg = Messages.error_getConnection;
				return;
			}
			
			String sql;
			if (isSupportUserSchema()) {
				sql = "CREATE TABLE " + QuerySyntax.escapeKeyword(newOwnerName) + "." + QuerySyntax.escapeKeyword(newTableName) + " LIKE "
					+ QuerySyntax.escapeKeyword(likeOwnerName) + "." + QuerySyntax.escapeKeyword(likeTableName);
			} else {
				sql = "CREATE TABLE " + QuerySyntax.escapeKeyword(newTableName) + " LIKE "
						+ QuerySyntax.escapeKeyword(likeTableName);
			}
			
			stmt = connection.createStatement();
			stmt.execute(sql);
			connection.commit();
		} catch (SQLException e) {
			LOGGER.error("", e);
			errorMsg = e.getMessage();
		} finally {
			finish();
		}
	}

	/**
	 * Set new table name
	 * 
	 * @param tableName The string
	 */
	public void setTableName(String tableName) {
		newTableName = tableName;
	}

	public void setOwnerName(String ownerName) {
		newOwnerName = ownerName;
	}
	
	/**
	 * Set like table name
	 * 
	 * @param tableName The string
	 */
	public void setLikeTableName(String tableName) {
		likeTableName = tableName;
	}
	
	public void setLikeOwnerName(String ownerName) {
		likeOwnerName = ownerName;
	}
	
	private boolean isSupportUserSchema() {
		if (databaseInfo != null) {
			return databaseInfo.isSupportUserSchema();
		} 
		return false;
	}
}
