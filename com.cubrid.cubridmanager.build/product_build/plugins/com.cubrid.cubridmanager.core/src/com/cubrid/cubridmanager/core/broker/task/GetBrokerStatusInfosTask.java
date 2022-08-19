/*
 * Copyright (C) 2012 Search Solution Corporation. All rights reserved by Search Solution. 
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
package com.cubrid.cubridmanager.core.broker.task;

import com.cubrid.cubridmanager.core.common.model.IModel;
import com.cubrid.cubridmanager.core.common.model.ServerInfo;
import com.cubrid.cubridmanager.core.common.socket.SocketTask;
import com.cubrid.cubridmanager.core.common.socket.TreeNode;

/**
 * A task that can send the items of "task","token","bname"
 * 
 * @author lizhiqiang
 * @version 1.0 - 2009-12-23 created by lizhiqiang
 * @param <T>
 */
public class GetBrokerStatusInfosTask<T extends IModel> extends SocketTask {

	private final T result;

	public GetBrokerStatusInfosTask(ServerInfo serverInfo, String[] sendMSGItems, T bean) {
		super(bean.getTaskName(), serverInfo, sendMSGItems);
		this.result = bean;
	}

	/**
	 * Show the result of the database space information
	 * 
	 * @return T
	 */
	public T getResultModel() {
		TreeNode node = (TreeNode) getResponse();
		setFieldValue(node, result);
		return result;
	}

	/**
	 * set brokerName to retrieve broker status informations
	 * 
	 * @param brokerName String
	 */
	public void setBrokerName(String brokerName) {
		super.setMsgItem("bname", brokerName);
	}
}
