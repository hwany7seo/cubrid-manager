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

package com.cubrid.cubridmanager.core.logs.model;

import com.cubrid.cubridmanager.core.common.model.IModel;
import java.util.ArrayList;
import java.util.List;

/**
 * AnalyzeCasLogResult information model class
 *
 * @author wuyingshi
 * @version 1.0 - 2009-4-24 created by wuyingshi
 */
public class AnalyzeCasLogTopResultInfo implements IModel {

    private List<String> logStringList;

    /**
     * get task name.
     *
     * @return String
     */
    public String getTaskName() {
        return "getcaslogtopresult";
    }

    /**
     * get the logStringList.
     *
     * @return List<String>
     */
    public List<String> getLogString() {
        return logStringList;
    }

    /**
     * add str to the logStringList
     *
     * @param str String
     */
    public void addLogString(String str) {
        if (this.logStringList == null) {
            logStringList = new ArrayList<String>();
        }
        logStringList.add(str);
    }
}
