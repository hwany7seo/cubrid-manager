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
package com.cubrid.cubridmanager.core.broker.task;

import java.util.List;
import java.util.Map;

import com.cubrid.cubridmanager.core.SetupEnvTestCase;
import com.cubrid.cubridmanager.core.Tool;
import com.cubrid.cubridmanager.core.common.socket.MessageUtil;
import com.cubrid.cubridmanager.core.common.socket.TreeNode;
import com.cubrid.cubridmanager.core.common.task.GetHAConfParameterTask;

/**
 * Tests GetBrokerConfParameterTask
 * 
 * @author lizhiqiang
 * @version 1.0 - 2009-5-15 created by lizhiqiang
 */
public class GetBrokerConfParameterTaskTest extends
		SetupEnvTestCase {
	public void testExecute() {
		GetBrokerConfParameterTask getBrokerConfParameterTask = new GetBrokerConfParameterTask(
				serverInfo);
		getBrokerConfParameterTask.execute();
		String errorMsg = getBrokerConfParameterTask.getErrorMsg();
		assertNull(errorMsg);
		Map<String, Map<String, String>> confParas = getBrokerConfParameterTask.getConfParameters();
		assertNotNull(confParas);
		GetBrokerConfParameterTask getBrokerConfParameterTask2 = new GetBrokerConfParameterTask(
				serverInfo);
		Map<String, Map<String, String>> confParas2 = getBrokerConfParameterTask2.getConfParameters();
		assertNull(confParas2);
		
		getBrokerConfParameterTask.execute();
		getBrokerConfParameterTask.setErrorMsg("error");
		Map<String, Map<String, String>> confParas3 = getBrokerConfParameterTask.getConfParameters();
		assertNull(confParas3);
	}
	
	public void testGetContents() throws Exception {
		String filepath = this.getFilePathInPlugin("/com/cubrid/cubridmanager/core/broker/task/test.message/getbrokerconfpara_receive");
		String msg = Tool.getFileContent(filepath);

		TreeNode node = MessageUtil.parseResponse(msg);
		GetBrokerConfParameterTask task = new GetBrokerConfParameterTask(serverInfo);
		task.setResponse(node);
		task.getConfContents();
		assertEquals("success", node.getValue("status"));
		
		task.setResponse(null);
		List<String> list = task.getConfContents();	
		assertNull(list);
	}
	
}
