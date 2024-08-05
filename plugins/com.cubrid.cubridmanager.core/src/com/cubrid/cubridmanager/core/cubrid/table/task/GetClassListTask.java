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

package com.cubrid.cubridmanager.core.cubrid.table.task;

import com.cubrid.cubridmanager.core.common.model.OnOffType;
import com.cubrid.cubridmanager.core.common.model.ServerInfo;
import com.cubrid.cubridmanager.core.common.socket.SocketTask;
import com.cubrid.cubridmanager.core.common.socket.TreeNode;
import com.cubrid.cubridmanager.core.cubrid.table.model.DBClasses;

/**
 * 
 * Task of get all Class of the database
 * 
 * @author robin 2009-3-30
 */
public class GetClassListTask extends
		SocketTask {
	private static final String[] SEND_MSG_ITEMS = new String[]{"task",
			"token", "dbname", "dbstatus" };

	/**
	 * The constructor
	 * 
	 * @param serverInfo
	 */
	public GetClassListTask(ServerInfo serverInfo) {
		super("classinfo", serverInfo, SEND_MSG_ITEMS);
	}

	public GetClassListTask(ServerInfo serverInfo, String charset) {
		super("classinfo", serverInfo, SEND_MSG_ITEMS, charset, charset);
	}

	/**
	 * set dbname value
	 * 
	 * @param dbname String
	 */
	public void setDbName(String dbname) {
		super.setMsgItem("dbname", dbname);
	}

	/**
	 * set delbackup value
	 * 
	 * @param dbstatus OnOffType
	 */
	public void setDbStatus(OnOffType dbstatus) {
		super.setMsgItem("dbstatus", dbstatus.getText().toLowerCase());
	}

	/**
	 * 
	 * Get the database class info
	 * 
	 * @return DBClasses
	 */
	public DBClasses getDbClassInfo() {
		TreeNode node = (TreeNode) getResponse();
		DBClasses result = new DBClasses();
		setFieldValue(node, result);
		return result;
	}
}
