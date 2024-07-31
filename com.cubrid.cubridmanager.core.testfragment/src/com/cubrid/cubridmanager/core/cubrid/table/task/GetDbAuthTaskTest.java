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
package com.cubrid.cubridmanager.core.cubrid.table.task;

import com.cubrid.cubridmanager.core.SetupJDBCTestCase;
import com.cubrid.cubridmanager.core.cubrid.table.model.DbAuth;
import java.sql.SQLException;
import java.util.List;

/**
 * Test the type of GetDBAuthTask
 *
 * @author lizhiqiang
 * @version 1.0 - 2010-09-14 created by lizhiqiang
 */
public class GetDbAuthTaskTest extends SetupJDBCTestCase {
    private GetDbAuthTask getDbAuthTask;

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        getDbAuthTask = new GetDbAuthTask(databaseInfo);
    }

    /**
     * Test method for {@link
     * com.cubrid.cubridmanager.core.cubrid.table.task.GetDbAuthTask#getDbAuths(java.lang.String)} .
     */
    public void testGetDbAuths_1() {
        List<DbAuth> list = getDbAuthTask.getDbAuths("abb");
        assertNotNull(list);
    }
    /**
     * Test method for {@link
     * com.cubrid.cubridmanager.core.cubrid.table.task.GetDbAuthTask#getDbAuths(java.lang.String)} .
     */
    public void testGetDbAuths_2() {
        List<DbAuth> list = getDbAuthTask.getDbAuths("db_auth");
        assertFalse(list.isEmpty());
    }

    /**
     * Test method for {@link
     * com.cubrid.cubridmanager.core.cubrid.table.task.GetDbAuthTask#getDbAuths(java.lang.String)} .
     */
    public void testGetDbAuths_3() {

        List<DbAuth> list = getDbAuthTask.getDbAuths("db_auth");
        assertFalse(list.isEmpty());
    }

    /**
     * Test method for {@link
     * com.cubrid.cubridmanager.core.cubrid.table.task.GetDbAuthTask#getDbAuths(java.lang.String)} .
     */
    public void testGetDbAuths_4() {
        getDbAuthTask.setErrorMsg("error");
        List<DbAuth> list = getDbAuthTask.getDbAuths("abb");
        assertNotNull(list);
    }

    /**
     * Test method for {@link
     * com.cubrid.cubridmanager.core.cubrid.table.task.GetDbAuthTask#getDbAuths(java.lang.String)} .
     *
     * @throws SQLException
     */
    public void testGetDbAuths_5() throws SQLException {
        getDbAuthTask.getConnection().close();
        List<DbAuth> list = getDbAuthTask.getDbAuths("abb");
        assertNotNull(list);
    }
}
