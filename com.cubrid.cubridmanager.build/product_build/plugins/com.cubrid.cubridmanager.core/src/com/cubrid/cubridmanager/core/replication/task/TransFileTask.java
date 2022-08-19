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
package com.cubrid.cubridmanager.core.replication.task;

import java.util.List;

import com.cubrid.cubridmanager.core.common.model.ServerInfo;
import com.cubrid.cubridmanager.core.common.socket.SocketTask;
import com.cubrid.cubridmanager.core.common.socket.TreeNode;

/**
 * 
 * This task is responsible to transfile
 * 
 * @author pangqiren
 * @version 1.0 - 2009-9-14 created by pangqiren
 */
public class TransFileTask extends
		SocketTask {
	private static final String[] SEND_MSG_ITEMS = new String[] {"task",
			"token", "masterdbdir", "open", "close", "slavedbhost",
			"slavecmserverport", "slavedbdir" };

	/**
	 * The constructor
	 * 
	 * @param serverInfo
	 */
	public TransFileTask(ServerInfo serverInfo) {
		super("transferfile", serverInfo, SEND_MSG_ITEMS);
	}

	/**
	 * 
	 * Set master database directory
	 * 
	 * @param masterDbDir the dir
	 */
	public void setMasterDbDir(String masterDbDir) {
		setMsgItem("masterdbdir", masterDbDir);
	}

	/**
	 * 
	 * Set backuped files
	 * 
	 * @param fileList the file list
	 */
	public void setBackupFileList(List<String> fileList) {
		StringBuffer buffer = new StringBuffer("backupfilelist");
		for (int i = 0; i < fileList.size(); i++) {
			buffer.append("\nbackupfilename:" + fileList.get(i));
		}
		setMsgItem("open", buffer.toString());
		setMsgItem("close", "backupfilelist");
	}

	/**
	 * 
	 * Set slave database host
	 * 
	 * @param slaveDbHost the slave database host
	 */
	public void setSlaveDbHost(String slaveDbHost) {
		setMsgItem("slavedbhost", slaveDbHost);
	}

	/**
	 * 
	 * Set slave CUBRID Manager server port
	 * 
	 * @param serverPort the server port
	 */
	public void setSlaveCmServerPort(String serverPort) {
		setMsgItem("slavecmserverport", serverPort);
	}

	/**
	 * 
	 * Set slave database dir
	 * 
	 * @param dir the dir
	 */
	public void setSlaveDbDir(String dir) {
		setMsgItem("slavedbdir", dir);
	}

	/**
	 * 
	 * Get transfer file pid
	 * 
	 * @return the pid
	 */
	public String getTransFilePid() {
		TreeNode response = getResponse();
		if (response == null
				|| (this.getErrorMsg() != null && getErrorMsg().trim().length() > 0)) {
			return null;
		}
		return response.getValue("pid");
	}
}
