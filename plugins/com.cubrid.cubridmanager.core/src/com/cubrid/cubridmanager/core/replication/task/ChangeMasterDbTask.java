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

import com.cubrid.cubridmanager.core.common.model.ServerInfo;
import com.cubrid.cubridmanager.core.common.socket.SocketTask;

/**
 * 
 * A task that defined the task of "changeMasterdb"
 * 
 * @author wuyingshi
 * @version 1.0 - 2009-8-25 created by wuyingshi
 */
public class ChangeMasterDbTask extends
		SocketTask {

	private static final String[] SEND_MSG_ITEMS = new String[] {"task",
			"token", "old_master_name", "old_master_host", "new_master_name",
			"new_master_host", "dist_name", "dist_host", "dist_password" };

	/**
	 * The Constructor
	 * 
	 * @param serverInfo
	 */
	public ChangeMasterDbTask(ServerInfo serverInfo) {
		super("cmtask_change_master", serverInfo, SEND_MSG_ITEMS);
	}

	/**
	 * set old masterdb name
	 * 
	 * @param param the old masterdb name
	 */
	public void setOldMasterDbName(String param) {
		super.setMsgItem("old_master_name", param);
	}

	/**
	 * set old master host ip
	 * 
	 * @param param the master host ip
	 */
	public void setOldMasterHostIp(String param) {
		super.setMsgItem("old_master_host", param);
	}

	/**
	 * set new masterdb name
	 * 
	 * @param param the new masterdb name
	 */
	public void setNewMasterDbName(String param) {
		super.setMsgItem("new_master_name", param);
	}

	/**
	 * set new master host ip
	 * 
	 * @param param the new master host ip
	 */
	public void setNewMasterHostIp(String param) {
		super.setMsgItem("new_master_host", param);
	}

	/**
	 * set dist host ip
	 * 
	 * @param param the dist host ip
	 */
	public void setDistHostIp(String param) {
		super.setMsgItem("dist_host", param);
	}

	/**
	 * set the distdb name
	 * 
	 * @param param the distdb name
	 */
	public void setDistDbName(String param) {
		super.setMsgItem("dist_name", param);
	}

	/**
	 * set distdb dba password
	 * 
	 * @param param the password
	 */
	public void setDistDbDbaPasswd(String param) {
		super.setMsgItem("dist_password", param);
	}

}
