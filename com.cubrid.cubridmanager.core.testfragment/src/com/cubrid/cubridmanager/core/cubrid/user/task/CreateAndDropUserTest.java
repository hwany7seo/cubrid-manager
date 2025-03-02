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
package com.cubrid.cubridmanager.core.cubrid.user.task;

import com.cubrid.cubridmanager.core.SetupJDBCTestCase;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fulei
 * @version 1.0 - 2012-12-25 created by fulei
 */
public class CreateAndDropUserTest extends SetupJDBCTestCase {

    private String testUserName = "testCreateAndDropUser";

    protected void setUp() throws Exception {
        super.setUp();
        DropUserTask task = new DropUserTask(databaseInfo, testUserName);
        task.execute();
    }

    public void testCreateUser() {
        List<String> groupList = new ArrayList<String>();
        groupList.add("public");
        List<String> memberList = new ArrayList<String>();
        CreateUserTask createTask =
                new CreateUserTask(databaseInfo, testUserName, "1111", groupList, memberList, null);
        createTask.execute();
        assertNull(createTask.getErrorMsg());

        DropUserTask dropTask = new DropUserTask(databaseInfo, testUserName);
        dropTask.execute();
        assertNull(dropTask.getErrorMsg());
    }
}
