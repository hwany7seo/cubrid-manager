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
package com.cubrid.cubridmanager.core.cubrid.database.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * This class will cache database backup information
 * 
 * @author pangqiren
 * @version 1.0 - 2009-6-4 created by pangqiren
 */
public class DbBackupInfo {

	private String dbDir = null;
	private String freeSpace = null;
	private List<DbBackupHistoryInfo> backupHistoryList = new ArrayList<DbBackupHistoryInfo>();

	/**
	 * 
	 * Get database dir
	 * 
	 * @return String
	 */
	public String getDbDir() {
		return dbDir;
	}

	/**
	 * 
	 * Set database dir
	 * 
	 * @param dbDir String
	 */
	public void setDbDir(String dbDir) {
		this.dbDir = dbDir;
	}

	/**
	 * 
	 * Get free space
	 * 
	 * @return String
	 */
	public String getFreeSpace() {
		return freeSpace;
	}

	/**
	 * 
	 * Set free space
	 * 
	 * @param freeSpace String
	 */
	public void setFreeSpace(String freeSpace) {
		this.freeSpace = freeSpace;
	}

	/**
	 * 
	 * Get database backup history information list
	 * 
	 * @return List<DbBackupHistoryInfo> The list includes some instance of
	 *         DbBackupHistoryInfo
	 */
	public List<DbBackupHistoryInfo> getBackupHistoryList() {
		return backupHistoryList;
	}

	/**
	 * 
	 * Set database backup history information list
	 * 
	 * @param backupHistoryList List<DbBackupHistoryInfo> The list includes some
	 *        instance of DbBackupHistoryInfo
	 */
	public void setBackupHistoryList(List<DbBackupHistoryInfo> backupHistoryList) {
		this.backupHistoryList = backupHistoryList;
	}

	/**
	 * 
	 * Add backup history information
	 * 
	 * @param historyInfo List<DbBackupHistoryInfo> The list includes some
	 *        instance of DbBackupHistoryInfo
	 */
	public void addDbBackupHistoryInfo(DbBackupHistoryInfo historyInfo) {
		if (!backupHistoryList.contains(historyInfo)) {
			backupHistoryList.add(historyInfo);
		}
	}
}
