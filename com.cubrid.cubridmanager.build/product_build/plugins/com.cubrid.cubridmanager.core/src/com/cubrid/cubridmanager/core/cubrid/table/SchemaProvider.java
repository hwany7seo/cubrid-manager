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
package com.cubrid.cubridmanager.core.cubrid.table;

import java.sql.Connection;

import com.cubrid.common.core.common.model.SchemaInfo;
import com.cubrid.cubridmanager.core.cubrid.database.model.DatabaseInfo;
import com.cubrid.cubridmanager.core.cubrid.table.task.GetSchemaTask;

/**
 * Get the schema of database objects.
 * 
 * @author SC13425
 * @version 1.0 - 2010-11-16 created by SC13425
 */
public class SchemaProvider {
	protected DatabaseInfo dbInfo;
	protected String tableName;
	protected String errorMessage;

	public SchemaProvider(DatabaseInfo dbInfo, String tableName) {
		this.dbInfo = dbInfo;
		this.tableName = tableName;
	}

	/**
	 * Retrieves the schema of database object.
	 * 
	 * @return schema of table.
	 */
	public SchemaInfo getSchema() {
		GetSchemaTask jdbcTask = new GetSchemaTask(dbInfo, tableName);
		jdbcTask.execute();
		errorMessage = jdbcTask.getErrorMsg();
		return jdbcTask.getSchema();

	}

	/**
	 * Retrieves the schema of database object.
	 * 
	 * @return schema of table.
	 */
	public SchemaInfo getSchema(Connection connection) {
		GetSchemaTask jdbcTask = new GetSchemaTask(connection, dbInfo, tableName);
		jdbcTask.execute();
		errorMessage = jdbcTask.getErrorMsg();
		return jdbcTask.getSchema();

	}
	
	/**
	 * Error messages.
	 * 
	 * @return error messages
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
}
