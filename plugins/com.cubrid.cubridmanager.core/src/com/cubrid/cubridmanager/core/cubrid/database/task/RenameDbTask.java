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
package com.cubrid.cubridmanager.core.cubrid.database.task;

import java.util.List;

import com.cubrid.cubridmanager.core.common.model.ServerInfo;
import com.cubrid.cubridmanager.core.common.socket.SocketTask;

/**
 * 
 * This task is responsible to rename database
 * 
 * @author pangqiren
 * @version 1.0 - 2009-6-4 created by pangqiren
 */
public class RenameDbTask extends
		SocketTask {

	private static final String[] SEND_MSG_ITEMS = new String[]{"task",
			"token", "dbname", "rename", "exvolpath", "advanced", "open",
			"close", "forcedel" };

	/**
	 * The constructor
	 * 
	 * @param serverInfo
	 */
	public RenameDbTask(ServerInfo serverInfo) {
		super("renamedb", serverInfo, SEND_MSG_ITEMS);
	}

	/**
	 * Set the database name
	 * 
	 * @param dbName String the database name
	 */
	public void setDbName(String dbName) {
		super.setMsgItem("dbname", dbName);
	}

	/**
	 * 
	 * Set new database name
	 * 
	 * @param dbName String the database name
	 */
	public void setNewDbName(String dbName) {
		super.setMsgItem("rename", dbName);
	}

	/**
	 * 
	 * Set extended volume path
	 * 
	 * @param volPath String teh volume path
	 */
	public void setExVolumePath(String volPath) {
		super.setMsgItem("exvolpath", volPath);
	}

	/**
	 * 
	 * Set individual volume information list
	 * 
	 * @param volumeList List<String> a list that includes the info volume
	 */
	public void setIndividualVolume(List<String> volumeList) {
		StringBuffer strBuf = new StringBuffer("volume");
		int len = volumeList.size();
		if (len > 0) {
			strBuf.append("\n");
		}
		for (int i = 0; i < len; i++) {
			strBuf.append(volumeList.get(i));
			if (i != volumeList.size() - 1) {
				strBuf.append("\n");
			}
		}
		super.setMsgItem("open", strBuf.toString());
		super.setMsgItem("close", "volume");
	}

	/**
	 * 
	 * Set whether it is advanced
	 * 
	 * @param isAdvanced boolean
	 */
	public void setAdvanced(boolean isAdvanced) {
		if (isAdvanced) {
			super.setMsgItem("advanced", "on");
			setExVolumePath("none");
		} else {
			super.setMsgItem("advanced", "off");
		}
	}

	/**
	 * 
	 * Set whether force to delete
	 * 
	 * @param isDeleted boolean whether is deleted
	 */
	public void setForceDel(boolean isDeleted) {
		if (isDeleted) {
			super.setMsgItem("forcedel", "y");
		} else {
			super.setMsgItem("forcedel", "n");
		}
	}

}
