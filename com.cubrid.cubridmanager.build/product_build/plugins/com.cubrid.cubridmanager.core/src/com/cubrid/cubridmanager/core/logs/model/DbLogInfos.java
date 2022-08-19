/*
 * Copyright (C) 2009 Search Solution Corporation. All rights reserved by Search
 * Solution.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  - Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *  - Neither the name of the <ORGANIZATION> nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission.
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

package com.cubrid.cubridmanager.core.logs.model;

import com.cubrid.cubridmanager.core.common.model.IModel;

/**
 * 
 * This class is responsible to store all database log information
 * 
 * @author wuyingshi
 * @version 1.0 - 2009-5-18 created by wuyingshi
 */
public class DbLogInfos implements
		IModel {

	private String dbname = null;
	private DbLogInfoList dbLogInfoList = null;

	/**
	 * The constructor
	 */
	public DbLogInfos() {
		dbLogInfoList = new DbLogInfoList();
	}

	/**
	 * get task name.
	 * 
	 * @return String
	 */
	public String getTaskName() {
		return "getloginfo";
	}

	/**
	 * get the dbname.
	 * 
	 * @return String
	 */
	public String getDbname() {
		return dbname;
	}

	/**
	 * set the dbname.
	 * 
	 * @param dbname String
	 */
	public void setDbname(String dbname) {
		this.dbname = dbname;
	}

	/**
	 * get the dbLogInfoList.
	 * 
	 * @return DbLogInfoList
	 */
	public DbLogInfoList getDbLogInfoList() {
		if (dbLogInfoList == null) {
			dbLogInfoList = new DbLogInfoList();
		}
		return dbLogInfoList;
	}

	/**
	 * set the dbLogInfoList.
	 * 
	 * @param dbLogInfoList DbLogInfoList
	 */
	public void addLogInfo(DbLogInfoList dbLogInfoList) {
		this.dbLogInfoList = dbLogInfoList;
	}

	/**
	 * get log information of it's path equal to parameter path.
	 * 
	 * @param path String
	 * @return LogInfo
	 */
	public LogInfo getDbLogInfo(String path) {
		if (dbLogInfoList != null) {
			return dbLogInfoList.getDbLogInfo(path);
		}
		return null;
	}

	/**
	 * @param dbLogInfoList the dbLogInfoList to set
	 */
	public void setDbLogInfoList(DbLogInfoList dbLogInfoList) {
		this.dbLogInfoList = dbLogInfoList;
	}
}
