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

import java.sql.Connection;

import com.cubrid.common.core.util.QueryUtil;
import com.cubrid.cubridmanager.core.SetupJDBCTestCase;
import com.cubrid.cubridmanager.core.common.jdbc.JDBCConnectionManager;

/**
 * test getting all partition tables
 * 
 * @author moulinwang
 * @version 1.0 - 2009-7-1 created by moulinwang
 */
public class GetAllPartitionClassTaskTest extends
		SetupJDBCTestCase {
	String testTableName = "testGetAllPartitionClassTaskTest";
	String sql = null;

	private boolean createTestTable() {
		String sql = "create table \"" + testTableName + "\" ("
				+ "empno char(10) not null unique,"
				+ "empname varchar(20) not null," + "deptname varchar(20), "
				+ "hiredate date" + ")"
				+ "partition by range (extract (year from hiredate)) "
				+ "(partition h2000 values less than (2000),"
				+ "partition h2003 values less than (2003),"
				+ "partition hmax values less than  maxvalue)";
		;
		return executeDDL(sql);
	}

	private boolean dropTestTable() {
		String sql = "drop table \"" + testTableName + "\"";
		return executeDDL(sql);
	}

	public void testGetAllPartitionClassTaskTest() {
		createTestTable();
		GetAllPartitionClassTask task = new GetAllPartitionClassTask(
				databaseInfo);
		task.execute();
		task.setErrorMsg("errorMsg");
		task.execute();
		task.getPartitionClassMap();
		task.setErrorMsg(null);
		task.execute();	
		task.cancel();
		task.execute();	
		
		Connection conn = null;
		try {
			conn = JDBCConnectionManager.getConnection(databaseInfo, true);
			GetAllPartitionClassTask selfConnectionTask = new GetAllPartitionClassTask(
					databaseInfo, conn);
			selfConnectionTask.execute();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			QueryUtil.freeQuery(conn);
			conn = null;
		}
		
		GetAllPartitionClassTask selfConnectionTask = new GetAllPartitionClassTask(
				databaseInfo, null);
		selfConnectionTask.execute();
	}

	@Override
	protected void tearDown() throws Exception {
		dropTestTable();
		super.tearDown();
	}
}