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
package com.cubrid.cubridmanager.core.cubrid.jobauto.task;

import com.cubrid.cubridmanager.core.common.model.ServerInfo;
import com.cubrid.cubridmanager.core.common.socket.SocketTask;
import com.cubrid.cubridmanager.core.common.socket.TreeNode;
import com.cubrid.cubridmanager.core.cubrid.jobauto.model.QueryPlanInfo;
import java.util.ArrayList;
import java.util.List;

/**
 * This task is resposible to get all query plan list
 *
 * @author pangqiren 2009-4-1
 */
public class GetQueryPlanListTask extends SocketTask {
    private static final String[] SEND_MSG_ITEMS = new String[] {"task", "token", "dbname"};

    /**
     * @param taskName
     * @param serverInfo
     */
    public GetQueryPlanListTask(ServerInfo serverInfo) {
        super("getautoexecquery", serverInfo, SEND_MSG_ITEMS);
    }

    /**
     * Set the database name
     *
     * @param dbName String The given database name
     */
    public void setDbName(String dbName) {
        this.setMsgItem("dbname", dbName);
    }

    /**
     * Get a list that includes the instances of QueryPlanInfo
     *
     * @return List<QueryPlanInfo> a list that includes the instances of QueryPlanInfo
     */
    public List<QueryPlanInfo> getQueryPlanInfoList() {
        List<QueryPlanInfo> queryPlanInfoList = new ArrayList<QueryPlanInfo>();
        if (null != getErrorMsg()) {
            return null;
        }
        TreeNode result = getResponse();
        String dbname = result.getValue("dbname");
        if (result.childrenSize() > 0) {
            TreeNode planListNode = result.getChildren().get(0);
            int size = planListNode.childrenSize();
            for (int i = 0; i < size; i++) {
                TreeNode node = planListNode.getChildren().get(i);
                QueryPlanInfo planInfo = new QueryPlanInfo();
                SocketTask.setFieldValue(node, planInfo);
                planInfo.setDbname(dbname);
                queryPlanInfoList.add(planInfo);
            }
        }
        return queryPlanInfoList;
    }
}
