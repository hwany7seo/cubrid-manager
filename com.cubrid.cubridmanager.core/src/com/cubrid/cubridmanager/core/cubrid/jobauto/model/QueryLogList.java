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
package com.cubrid.cubridmanager.core.cubrid.jobauto.model;

import com.cubrid.cubridmanager.core.common.model.IModel;
import java.util.ArrayList;
import java.util.List;

/**
 * A model class which includes one or more QueryLogInfo
 *
 * @author lizhiqiang 2009-4-21
 */
public class QueryLogList implements IModel {
    List<QueryLogInfo> queryLogList;

    public String getTaskName() {
        return "getautoexecqueryerrlog";
    }

    public List<QueryLogInfo> getQueryLogList() {
        return queryLogList;
    }

    public void setQueryLogList(List<QueryLogInfo> queryLogList) {
        this.queryLogList = queryLogList;
    }

    /**
     * add the model to list by reflect method
     *
     * @param bean QueryLogInfo the instance of QueryLogInfo
     */
    public void addError(QueryLogInfo bean) {
        if (queryLogList == null) {
            queryLogList = new ArrayList<QueryLogInfo>();
        }
        queryLogList.add(bean);
    }
}
