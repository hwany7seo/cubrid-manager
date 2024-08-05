/*
 * Copyright (C) 2009 Search Solution Corporation. All rights reserved by Search Solution. 
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

import com.cubrid.cubridmanager.core.SetupJDBCTestCase;

/**
 * test renaming a table or view task
 * 
 * @author Kevin Cao
 * @version 1.0 - 2009-7-1 created by Kevin Cao
 */
public class RenameTableColumnTaskTest extends
		SetupJDBCTestCase {
	String testTableName = "testRenameTableColumnTest";
	String newName = "newfield";
	String oldName = "oldfield";
	String sql = null;

	private boolean createTestTable() {
		String sql = "create table \"" + testTableName + "\" ("
				+ "code integer," + "name character varying(40)  NOT NULL,"
				+ "gender character(1) ," + "nation_code character(3) "
				+ ",oldfield integer)";
		return executeDDL(sql);
	}

	private boolean dropTestTable() {
		String sql = "drop table \"" + testTableName + "\"";
		return executeDDL(sql);
	}

	public void testRenameTask() {
		createTestTable();
		//test normal case
		RenameTableColumnTask task = new RenameTableColumnTask(databaseInfo);
		task.setOldName(oldName);
		task.setNewName(newName);
		task.setTableName(testTableName);
		task.execute();
		assertEquals(true, task.isSuccess());

		//test exception case: connection closed
		task.execute();
		assertEquals(false, task.isSuccess());

		//test normal case
		task = new RenameTableColumnTask(databaseInfo);
		task.setOldName(newName);
		task.setNewName(oldName);
		task.setTableName(testTableName);
		task.execute();
		assertEquals(true, task.isSuccess());

		//test exception case: have error
		task = new RenameTableColumnTask(databaseInfo);
		task.setErrorMsg("err");
		task.execute();

		//test exception case: SQLException
		task = new RenameTableColumnTask(databaseInfo);
		task.setOldName("noexist");
		task.setNewName("isnoexist");
		task.setTableName("noexist");
		task.execute();
		assertEquals(false, task.isSuccess());
	}

	@Override
	protected void tearDown() throws Exception {
		dropTestTable();
		super.tearDown();
	}
}