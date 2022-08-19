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
package com.cubrid.cubridmanager.core.monitoring.task;

import java.util.List;

import com.cubrid.cubridmanager.core.common.model.ServerInfo;
import com.cubrid.cubridmanager.core.common.socket.SocketTask;
import com.cubrid.cubridmanager.core.common.socket.TreeNode;
import com.cubrid.cubridmanager.core.monitoring.model.DiagStatusResult;

/**
 * 
 * 
 * @author lizhiqiang 2009-5-10
 */
public class GetDiagdataTask extends
		SocketTask {
	private String dbname = "";

	/**
	 * The constructor
	 * 
	 * @param serverInfo
	 */
	public GetDiagdataTask(ServerInfo serverInfo) {
		super("getdiagdata", serverInfo);
	}

	public void setDbname(String dbname) {
		this.dbname = dbname;
	}

	/**
	 * Builds a message which includes the items exclusive of task,token and
	 * 
	 * 
	 * @param list List<String>
	 */
	public void buildMsg(List<String> list) {
		StringBuffer msg = new StringBuffer();
		msg.append("db_name:" + dbname);
		msg.append("\n");
		msg.append("mon_db:yes");
		msg.append("\n");
		for (String string : list) {
			if (!string.startsWith("cas")) {
				msg.append(string + ":yes");
				msg.append("\n");
			}
		}
		msg.append("mon_cas:yes");
		msg.append("\n");
		for (String string : list) {
			if (string.startsWith("cas")) {
				msg.append(string + ":yes");
				msg.append("\n");
			}
		}
		msg.append("mon_driver:no");
		msg.append("\n");
		msg.append("mon_resource:no");
		msg.append("\n");
		msg.append("act_db:no");
		msg.append("\n");
		msg.append("act_cas:no");
		msg.append("\n");
		msg.append("act_driver:no");
		this.setAppendSendMsg(msg.toString());
	}

	/**
	 * Gets the instance of Type DiagStatusResult
	 * 
	 * @return DiagStatusResult
	 */
	public DiagStatusResult getResult() {
		DiagStatusResult diagStatusResult = new DiagStatusResult();
		TreeNode node = getResponse();
		if (node == null
				|| (getErrorMsg() != null && getErrorMsg().trim().length() > 0)) {
			finish();
			return diagStatusResult;
		}
		for (TreeNode childNode : node.getChildren()) {
			setFieldValue(childNode, diagStatusResult);
		}
		return diagStatusResult;
	}

}
