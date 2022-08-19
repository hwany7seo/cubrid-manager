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
 * ARISING IN ANY WAY OUT OF THE USE OF THIS S OFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */
package com.cubrid.cubridmanager.core.cubrid.table.task;

import java.sql.Connection;

import com.cubrid.common.core.common.model.SchemaInfo;
import com.cubrid.cubridmanager.core.common.socket.SocketTask;
import com.cubrid.cubridmanager.core.common.socket.TreeNode;
import com.cubrid.cubridmanager.core.cubrid.database.model.DatabaseInfo;
import com.cubrid.cubridmanager.core.utils.ModelUtil;

/**
 * This class is to get information of a class in CUBRID database, whether it is
 * a class or virtual class(view).
 *
 * Usage: You must first set dbname and classname by invoking setDbName(String)
 * and setClassName(String) methods, then call sendMsg() method to send a
 * request message, the response message is the information of the special
 * class.
 *
 * @author moulinwang 2009-3-2
 */
public class ClassTask extends
		SocketTask {
	private DatabaseInfo dbInfo;
	private final static String[] SEND_MSG_ITEMS = new String[]{"task",
			"token", "dbname", "classname", };

	public ClassTask(DatabaseInfo dbInfo) {
		super("class", dbInfo.getServerInfo(), SEND_MSG_ITEMS);
		this.dbInfo = dbInfo;
	}

	public ClassTask(DatabaseInfo dbInfo, String charset) {
		super("class", dbInfo.getServerInfo(), SEND_MSG_ITEMS, charset, charset);
		this.dbInfo = dbInfo;
	}

	/**
	 * Set the key "dbname" in request message
	 *
	 * @param dbname String
	 */
	public void setDbName(String dbname) {
		this.setMsgItem("dbname", dbname);
	}

	/**
	 * Set the key "classname" in request message
	 *
	 * @param className String
	 */
	public void setClassName(String className) {
		this.setMsgItem("classname", className);
	}

	/**
	 * Get the SchemaInfo object from the response
	 *
	 * @return SchemaInfo
	 */
	public SchemaInfo getSchemaInfo() {
		if (isSuccess()) {
			TreeNode classinfo = getResponse().getChildren().get(0);
			return ModelUtil.getSchemaInfo(classinfo, dbInfo);
		} else {
			return null;
		}
	}

	/**
	 * Get the SchemaInfo object from the response (using for compatibility with CQB)
	 *
	 * @param connection
	 * @return SchemaInfo
	 */
	public SchemaInfo getSchemaInfo(Connection connection) {
		if (isSuccess()) {
			TreeNode classinfo = getResponse().getChildren().get(0);
			return ModelUtil.getSchemaInfo(classinfo, dbInfo);
		} else {
			return null;
		}
	}
}
