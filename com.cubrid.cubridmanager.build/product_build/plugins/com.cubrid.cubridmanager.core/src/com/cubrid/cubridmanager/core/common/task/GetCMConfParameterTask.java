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
package com.cubrid.cubridmanager.core.common.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cubrid.common.core.util.CompatibleUtil;
import com.cubrid.cubridmanager.core.common.model.ServerInfo;
import com.cubrid.cubridmanager.core.common.socket.SocketTask;
import com.cubrid.cubridmanager.core.common.socket.TreeNode;

/**
 * 
 * This task is responsible to get cm.conf parameters
 * 
 * @author pangqiren
 * @version 1.0 - 2009-6-2 created by pangqiren
 */
public class GetCMConfParameterTask extends
		SocketTask {
	private static final String[] SENDED_MSG_ITEMS = new String[]{"task",
			"token", "confname" };

	/**
	 * The constructor
	 * 
	 * @param taskName
	 * @param serverInfo
	 */
	public GetCMConfParameterTask(ServerInfo serverInfo) {
		super("getallsysparam", serverInfo, SENDED_MSG_ITEMS);
		this.setMsgItem("confname", "cmconf");
	}

	/**
	 * 
	 * Get cm.conf parameters
	 * 
	 * @return Map<String, String> The map that stored configure parameters
	 */
	public Map<String, String> getConfParameters() {
		TreeNode response = getResponse();
		if (response == null
				|| (this.getErrorMsg() != null && getErrorMsg().trim().length() > 0)) {
			return null;
		}
		Map<String, String> confMap = new HashMap<String, String>();
		for (int i = 0; i < response.childrenSize(); i++) {
			TreeNode node = response.getChildren().get(i);
			if (node != null && node.getValue("open").equals("conflist")) {
				buildConf(confMap, node);
			}
		}
		return confMap;
	}

	/**
	 * 
	 * Build configure parameters
	 * 
	 * @param confMap Map<String, String> The given confMap
	 * @param node TreeNode The given object of TreeNode
	 */
	private void buildConf(Map<String, String> confMap, TreeNode node) {
		String[] confData = node.getValues("confdata");
		if (confData != null && confData.length > 0) {
			for (int j = 0; j < confData.length; j++) {
				String data = confData[j].trim();
				if (data.length() == 0 || data.indexOf("#") == 0) {
					continue;
				}
				String[] entry = null;
				//serverInfo.compareVersionKey("8.3.0") >= 0
				if (CompatibleUtil.isSupportNewFormatOfCMConf(serverInfo)) {
					entry = data.split("=");
					if (entry == null || entry.length != 2) {
						entry = data.split("\\s+");
					}
				} else {
					entry = data.split("\\s+");
				}
				if (entry != null && entry.length == 2) {
					confMap.put(entry[0].trim(), entry[1].trim());
				}
			}
		}
	}

	/**
	 * 
	 * Get cm.conf parameters
	 * 
	 * @return List<String> The map that stored configure parameters
	 */
	public List<String> getConfContents() {
		String[] confData = null;
		List<String> confContents = new ArrayList<String>();
		TreeNode response = getResponse();
		if (response == null
				|| (this.getErrorMsg() != null && getErrorMsg().trim().length() > 0)) {
			return null;
		}
		for (int i = 0; i < response.childrenSize(); i++) {
			TreeNode node = response.getChildren().get(i);
			if (node != null && node.getValue("open").equals("conflist")) {
				confData = node.getValues("confdata");
			}
		}
		if (confData != null) {
			for (String line : confData) {
				confContents.add(line);
			}
		}
		return confContents;
	}
}
