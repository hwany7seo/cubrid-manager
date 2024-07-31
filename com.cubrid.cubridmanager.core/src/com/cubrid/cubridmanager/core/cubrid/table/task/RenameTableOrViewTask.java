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

import com.cubrid.common.core.util.QuerySyntax;
import com.cubrid.cubridmanager.core.Messages;
import com.cubrid.cubridmanager.core.common.jdbc.JDBCTask;
import com.cubrid.cubridmanager.core.cubrid.database.model.DatabaseInfo;
import java.sql.SQLException;

/**
 * rename a table or a view
 *
 * @author moulinwang
 * @version 1.0 - 2009-6-16 created by moulinwang
 */
public class RenameTableOrViewTask extends JDBCTask {

    private String oldTableOrViewName;
    private String newTableOrViewName;
    private boolean isTable;

    public RenameTableOrViewTask(DatabaseInfo dbInfo) {
        super("RenameTableOrViewTask", dbInfo);
    }

    /**
     * Set the key "oldclassname"
     *
     * @param oldClassName String
     */
    public void setOldClassName(String oldClassName) {
        this.oldTableOrViewName = oldClassName;
    }

    /**
     * Set the key "newclassname"
     *
     * @param newClassName String
     */
    public void setNewClassName(String newClassName) {
        this.newTableOrViewName = newClassName;
    }

    /**
     * Get sql
     *
     * @return String
     */
    public String getSQL() {
        StringBuffer bf = new StringBuffer();
        bf.append("RENAME");
        if (isTable) {
            bf.append(" TABLE ");
        } else {
            bf.append(" VIEW ");
        }
        bf.append(QuerySyntax.escapeKeyword(oldTableOrViewName));
        bf.append(" AS ");
        bf.append(QuerySyntax.escapeKeyword(newTableOrViewName));

        return bf.toString();
    }

    /** Execute the tasks */
    public void execute() {
        try {
            if (errorMsg != null && errorMsg.trim().length() > 0) {
                return;
            }

            if (connection == null || connection.isClosed()) {
                errorMsg = Messages.error_getConnection;
                return;
            }

            String sql = getSQL();

            // [TOOLS-2425]Support shard broker
            sql = databaseInfo.wrapShardQuery(sql);

            stmt = connection.createStatement();
            stmt.executeUpdate(sql);
            connection.commit();
            stmt.close();
        } catch (SQLException e) {
            errorMsg = e.getMessage();
        } finally {
            finish();
        }
    }

    public void setTable(boolean isTable) {
        this.isTable = isTable;
    }
}
