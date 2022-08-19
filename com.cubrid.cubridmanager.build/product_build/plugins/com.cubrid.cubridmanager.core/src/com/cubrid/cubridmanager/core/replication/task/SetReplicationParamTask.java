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

import java.util.Iterator;
import java.util.Map;

import com.cubrid.common.core.util.CipherUtils;
import com.cubrid.common.core.util.CompatibleUtil;
import com.cubrid.cubridmanager.core.common.model.ServerInfo;
import com.cubrid.cubridmanager.core.common.socket.SocketTask;
import com.cubrid.cubridmanager.core.replication.model.ReplicationParamConstants;

/**
 * 
 * A task that defined the task of "cmtask_change_param"
 * 
 * @author wuyingshi
 * @version 1.0 - 2009-4-3 created by wuyingshi
 */
public class SetReplicationParamTask extends
		SocketTask {

	private static final String[] SEND_MSG_ITEMS = new String[]{"task",
			"token", "master_db_name", "slave_db_name", "dist_db_name",
			"dist_db_password", CIPHER_CHARACTER + "dist_db_password",
			ReplicationParamConstants.PERF_POLL_INTERVAL,
			ReplicationParamConstants.SIZE_OF_LOG_BUFFER,
			ReplicationParamConstants.SIZE_OF_CACHE_BUFFER,
			ReplicationParamConstants.SIZE_OF_COPYLOG,
			ReplicationParamConstants.INDEX_REPLICATION,
			ReplicationParamConstants.FOR_RECOVERY,
			ReplicationParamConstants.LOG_APPLY_INTERVAL,
			ReplicationParamConstants.RESTART_INTERVAL, "mode" };

	/**
	 * The Constructor
	 * 
	 * @param serverInfo
	 */
	public SetReplicationParamTask(ServerInfo serverInfo) {
		super("cmtask_change_param", serverInfo, SEND_MSG_ITEMS);
	}

	/**
	 * set master db name into msg
	 * 
	 * @param param the master db name
	 */
	public void setMasterDbName(String param) {
		this.setMsgItem("master_db_name", param);
	}

	/**
	 * set slave db name
	 * 
	 * @param param the slave db name
	 */
	public void setSlaveDbName(String param) {
		super.setMsgItem("slave_db_name", param);
	}

	/**
	 * set dist db name
	 * 
	 * @param param the dist db name
	 */
	public void setDistDbName(String param) {
		super.setMsgItem("dist_db_name", param);
	}

	/**
	 * set dist db password
	 * 
	 * @param param the dist db password
	 */
	public void setDistDbDbaPasswd(String param) {
		if (CompatibleUtil.isSupportCipher(serverInfo.getServerVersionKey())) {
			this.setMsgItem(CIPHER_CHARACTER + "dist_db_password",
					CipherUtils.encrypt(param));
		} else {
			this.setMsgItem("dist_db_password", param);
		}
	}

	/**
	 * 
	 * Set parameter map
	 * 
	 * @param parameterMap the parameter map
	 */
	public void setParameterMap(Map<String, String> parameterMap) {
		if (parameterMap == null) {
			return;
		}
		Iterator<Map.Entry<String, String>> it = parameterMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			String key = entry.getKey();
			String value = entry.getValue();
			if (key != null && key.trim().length() > 0 && value != null) {
				super.setMsgItem(key, value);
			}
		}
	}

	/**
	 * 
	 * Set the database running status
	 * 
	 * @param isRunning whether it is running
	 */
	public void setRunningMode(boolean isRunning) {
		if (isRunning) {
			this.setMsgItem("mode", "C");
		} else {
			this.setMsgItem("mode", "S");
		}
	}
}
