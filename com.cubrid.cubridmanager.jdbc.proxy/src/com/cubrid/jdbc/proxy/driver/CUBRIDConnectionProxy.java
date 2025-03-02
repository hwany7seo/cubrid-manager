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
package com.cubrid.jdbc.proxy.driver;

import com.cubrid.jdbc.proxy.manage.ReflectionUtil;
import java.io.InputStream;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * The proxy of cubrid.jdbc.driver.CUBRIDConnection
 *
 * @author robinhood
 */
public class CUBRIDConnectionProxy implements Connection {

    public static final int TRAN_REP_CLASS_COMMIT_INSTANCE = 16;
    public static final int TRAN_REP_CLASS_UNCOMMIT_INSTANCE = 32;
    private final Connection conn;
    private String jdbcVersion;

    public String getJdbcVersion() {
        return jdbcVersion;
    }

    public void setJdbcVersion(String jdbcVersion) {
        this.jdbcVersion = jdbcVersion;
    }

    public CUBRIDConnectionProxy(Connection conn, String jdbcVersion) {
        this.jdbcVersion = jdbcVersion;
        this.conn = conn;
    }

    /**
     * @see Connection#clearWarnings() throws SQLException
     * @exception SQLException if a database access error occurs
     */
    public void clearWarnings() throws SQLException {
        conn.clearWarnings();
    }

    /**
     * @see Connection#close() throws SQLException
     * @exception SQLException if a database access error occurs
     */
    public void close() throws SQLException {
        if (conn != null) {
            conn.close();
        }
    }

    /**
     * @see Connection#commit() throws SQLException
     * @exception SQLException if a database access error occurs or this <code>Connection</code>
     *     object is in auto-commit mode
     */
    public void commit() throws SQLException {
        conn.commit();
    }

    /**
     * @see Connection#createStatement() throws SQLException
     * @return a new default <code>Statement</code> object
     * @exception SQLException if a database access error occurs
     */
    public Statement createStatement() throws SQLException {
        CUBRIDStatementProxy statementProxy = new CUBRIDStatementProxy(conn.createStatement());
        statementProxy.setJdbcVersion(jdbcVersion);
        return statementProxy;
    }

    /**
     * @see Connection#createStatement(int resultSetType, int resultSetConcurrency) throws
     *     SQLException
     * @param resultSetType a result set type; one of <code>ResultSet.TYPE_FORWARD_ONLY</code>,
     *     <code>ResultSet.TYPE_SCROLL_INSENSITIVE</code>, or <code>ResultSet.TYPE_SCROLL_SENSITIVE
     *     </code>
     * @param resultSetConcurrency a concurrency type; one of <code>ResultSet.CONCUR_READ_ONLY
     *     </code> or <code>ResultSet.CONCUR_UPDATABLE</code>
     * @return a new <code>Statement</code> object that will generate <code>ResultSet</code> objects
     *     with the given type and concurrency
     * @exception SQLException if a database access error occurs or the given parameters are not
     *     <code>ResultSet</code> constants indicating type and concurrency
     * @since 1.2
     */
    public Statement createStatement(int resultSetType, int resultSetConcurrency)
            throws SQLException {
        CUBRIDStatementProxy statementProxy =
                new CUBRIDStatementProxy(conn.createStatement(resultSetType, resultSetConcurrency));
        statementProxy.setJdbcVersion(jdbcVersion);
        return statementProxy;
    }

    /**
     * @see Connection#createStatement(int resultSetType, int resultSetConcurrency, int
     *     resultSetHoldability) throws SQLException
     * @param resultSetType one of the following <code>ResultSet</code> constants: <code>
     *     ResultSet.TYPE_FORWARD_ONLY</code>, <code>ResultSet.TYPE_SCROLL_INSENSITIVE</code>, or
     *     <code>ResultSet.TYPE_SCROLL_SENSITIVE</code>
     * @param resultSetConcurrency one of the following <code>ResultSet</code> constants: <code>
     *     ResultSet.CONCUR_READ_ONLY</code> or <code>ResultSet.CONCUR_UPDATABLE</code>
     * @param resultSetHoldability one of the following <code>ResultSet</code> constants: <code>
     *     ResultSet.HOLD_CURSORS_OVER_COMMIT</code> or <code>ResultSet.CLOSE_CURSORS_AT_COMMIT
     *     </code>
     * @return a new <code>Statement</code> object that will generate <code>ResultSet</code> objects
     *     with the given type, concurrency, and holdability
     * @exception SQLException if a database access error occurs or the given parameters are not
     *     <code>ResultSet</code> constants indicating type, concurrency, and holdability
     * @see ResultSet
     * @since 1.4
     */
    public Statement createStatement(
            int resultSetType, int resultSetConcurrency, int resultSetHoldability)
            throws SQLException {
        CUBRIDStatementProxy statementProxy =
                new CUBRIDStatementProxy(
                        conn.createStatement(
                                resultSetType, resultSetConcurrency, resultSetHoldability));
        statementProxy.setJdbcVersion(jdbcVersion);
        return statementProxy;
    }

    /**
     * @see Connection#getAutoCommit() throws SQLException
     * @return the current state of this <code>Connection</code> object's auto-commit mode
     * @exception SQLException if a database access error occurs
     * @see #setAutoCommit
     */
    public boolean getAutoCommit() throws SQLException {
        return conn.getAutoCommit();
    }

    /**
     * @see Connection#getCatalog() throws SQLException
     * @return the current catalog name or <code>null</code> if there is none
     * @exception SQLException if a database access error occurs
     * @see #setCatalog
     */
    public String getCatalog() throws SQLException {
        return conn.getCatalog();
    }

    /**
     * @see Connection#getHoldability() throws SQLException
     * @return the holdability, one of <code>ResultSet.HOLD_CURSORS_OVER_COMMIT</code> or <code>
     *     ResultSet.CLOSE_CURSORS_AT_COMMIT</code>
     * @throws SQLException if a database access occurs
     * @see #setHoldability
     * @see ResultSet
     * @since 1.4
     */
    public int getHoldability() throws SQLException {
        return conn.getHoldability();
    }

    /**
     * @see Connection#getMetaData() throws SQLException
     * @return a <code>DatabaseMetaData</code> object for this <code>Connection</code> object
     * @exception SQLException if a database access error occurs
     */
    public DatabaseMetaData getMetaData() throws SQLException {
        return conn.getMetaData();
    }

    /**
     * @see Connection#getTransactionIsolation() throws SQLException
     * @return the current transaction isolation level, which will be one of the following
     *     constants: <code>Connection.TRANSACTION_READ_UNCOMMITTED</code>, <code>
     *     Connection.TRANSACTION_READ_COMMITTED</code>, <code>
     *     Connection.TRANSACTION_REPEATABLE_READ</code>, <code>Connection.TRANSACTION_SERIALIZABLE
     *     </code>, or <code>Connection.TRANSACTION_NONE</code>.
     * @exception SQLException if a database access error occurs
     * @see #setTransactionIsolation
     */
    public int getTransactionIsolation() throws SQLException {
        return conn.getTransactionIsolation();
    }

    /**
     * @see Connection#getTypeMap() throws SQLException
     * @return the <code>java.util.Map</code> object associated with this <code>Connection</code>
     *     object
     * @exception SQLException if a database access error occurs
     * @since 1.2
     * @see #setTypeMap
     */
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return conn.getTypeMap();
    }

    /**
     * @see Connection#getWarnings() throws SQLException
     * @return the first <code>SQLWarning</code> object or <code>null</code> if there are none
     * @exception SQLException if a database access error occurs or this method is called on a
     *     closed connection
     * @see SQLWarning
     */
    public SQLWarning getWarnings() throws SQLException {
        return conn.getWarnings();
    }

    /**
     * @see Connection#isClosed() throws SQLException
     * @return <code>true</code> if this <code>Connection</code> object is closed; <code>false
     *     </code> if it is still open
     * @exception SQLException if a database access error occurs
     */
    public boolean isClosed() throws SQLException {
        return conn.isClosed();
    }

    /**
     * @see Connection#isReadOnly() throws SQLException
     * @return <code>true</code> if this <code>Connection</code> object is read-only; <code>false
     *     </code> otherwise
     * @exception SQLException if a database access error occurs
     */
    public boolean isReadOnly() throws SQLException {
        return conn.isReadOnly();
    }

    /**
     * @see Connection#nativeSQL(String sql) throws SQLException
     * @param sql an SQL statement that may contain one or more '?' parameter placeholders
     * @return the native form of this statement
     * @exception SQLException if a database access error occurs
     */
    public String nativeSQL(String sql) throws SQLException {
        return conn.nativeSQL(sql);
    }

    /**
     * @see Connection#prepareCall(String) throws SQLException
     * @param sql an SQL statement that may contain one or more '?' parameter placeholders.
     *     Typically this statement is a JDBC function call escape string.
     * @return a new default <code>CallableStatement</code> object containing the pre-compiled SQL
     *     statement
     * @exception SQLException if a database access error occurs
     */
    public CallableStatement prepareCall(String sql) throws SQLException {
        return conn.prepareCall(sql);
    }

    /**
     * @see Connection#prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws
     *     SQLException
     * @param sql a <code>String</code> object that is the SQL statement to be sent to the database;
     *     may contain on or more ? parameters
     * @param resultSetType a result set type; one of <code>ResultSet.TYPE_FORWARD_ONLY</code>,
     *     <code>ResultSet.TYPE_SCROLL_INSENSITIVE</code>, or <code>ResultSet.TYPE_SCROLL_SENSITIVE
     *     </code>
     * @param resultSetConcurrency a concurrency type; one of <code>ResultSet.CONCUR_READ_ONLY
     *     </code> or <code>ResultSet.CONCUR_UPDATABLE</code>
     * @return a new <code>CallableStatement</code> object containing the pre-compiled SQL statement
     *     that will produce <code>ResultSet</code> objects with the given type and concurrency
     * @exception SQLException if a database access error occurs or the given parameters are not
     *     <code>ResultSet</code> constants indicating type and concurrency
     */
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency)
            throws SQLException {
        return conn.prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    /**
     * @see Connection#prepareCall(String sql, int resultSetType, int resultSetConcurrency, int
     *     resultSetHoldability) throws SQLException
     * @param sql a <code>String</code> object that is the SQL statement to be sent to the database;
     *     may contain on or more ? parameters
     * @param resultSetType one of the following <code>ResultSet</code> constants: <code>
     *     ResultSet.TYPE_FORWARD_ONLY</code>, <code>ResultSet.TYPE_SCROLL_INSENSITIVE</code>, or
     *     <code>ResultSet.TYPE_SCROLL_SENSITIVE</code>
     * @param resultSetConcurrency one of the following <code>ResultSet</code> constants: <code>
     *     ResultSet.CONCUR_READ_ONLY</code> or <code>ResultSet.CONCUR_UPDATABLE</code>
     * @param resultSetHoldability one of the following <code>ResultSet</code> constants: <code>
     *     ResultSet.HOLD_CURSORS_OVER_COMMIT</code> or <code>ResultSet.CLOSE_CURSORS_AT_COMMIT
     *     </code>
     * @return a new <code>CallableStatement</code> object, containing the pre-compiled SQL
     *     statement, that will generate <code>ResultSet</code> objects with the given type,
     *     concurrency, and holdability
     * @exception SQLException if a database access error occurs or the given parameters are not
     *     <code>ResultSet</code> constants indicating type, concurrency, and holdability
     */
    public CallableStatement prepareCall(
            String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability)
            throws SQLException {
        return conn.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    /**
     * @see Connection#prepareStatement(String sql) throws SQLException
     * @param sql an SQL statement that may contain one or more '?' IN parameter placeholders
     * @return a new default <code>PreparedStatement</code> object containing the pre-compiled SQL
     *     statement
     * @exception SQLException if a database access error occurs
     */
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        CUBRIDPreparedStatementProxy preparedStatementProxy =
                new CUBRIDPreparedStatementProxy(conn.prepareStatement(sql));
        preparedStatementProxy.setJdbcVersion(jdbcVersion);
        return preparedStatementProxy;
    }

    /**
     * @see Connection#prepareStatement(String sql, int autoGeneratedKeys) throws SQLException
     * @param sql an SQL statement that may contain one or more '?' IN parameter placeholders
     * @param autoGeneratedKeys a flag indicating whether auto-generated keys should be returned;
     *     one of <code>Statement.RETURN_GENERATED_KEYS</code> or <code>Statement.NO_GENERATED_KEYS
     *     </code>
     * @return a new <code>PreparedStatement</code> object, containing the pre-compiled SQL
     *     statement, that will have the capability of returning auto-generated keys
     * @exception SQLException if a database access error occurs or the given parameter is not a
     *     <code>Statement</code> constant indicating whether auto-generated keys should be returned
     */
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
            throws SQLException {
        CUBRIDPreparedStatementProxy preparedStatementProxy =
                new CUBRIDPreparedStatementProxy(conn.prepareStatement(sql, autoGeneratedKeys));
        preparedStatementProxy.setJdbcVersion(jdbcVersion);
        return preparedStatementProxy;
    }

    /**
     * @see Connection#prepareStatement(String sql, int[] columnIndexes) throws SQLException
     * @param sql an SQL statement that may contain one or more '?' IN parameter placeholders
     * @param columnIndexes an array of column indexes indicating the columns that should be
     *     returned from the inserted row or rows
     * @return a new <code>PreparedStatement</code> object, containing the pre-compiled statement,
     *     that is capable of returning the auto-generated keys designated by the given array of
     *     column indexes
     * @exception SQLException if a database access error occurs
     */
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        CUBRIDPreparedStatementProxy preparedStatementProxy =
                new CUBRIDPreparedStatementProxy(conn.prepareStatement(sql, columnIndexes));
        preparedStatementProxy.setJdbcVersion(jdbcVersion);
        return preparedStatementProxy;
    }

    /**
     * @see Connection#prepareStatement(String sql, String[] columnNames) throws SQLException
     * @param sql an SQL statement that may contain one or more '?' IN parameter placeholders
     * @param columnNames an array of column names indicating the columns that should be returned
     *     from the inserted row or rows
     * @return a new <code>PreparedStatement</code> object, containing the pre-compiled statement,
     *     that is capable of returning the auto-generated keys designated by the given array of
     *     column names
     * @exception SQLException if a database access error occurs
     */
    public PreparedStatement prepareStatement(String sql, String[] columnNames)
            throws SQLException {
        CUBRIDPreparedStatementProxy preparedStatementProxy =
                new CUBRIDPreparedStatementProxy(conn.prepareStatement(sql, columnNames));
        preparedStatementProxy.setJdbcVersion(jdbcVersion);
        return preparedStatementProxy;
    }

    /**
     * @see Connection#prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
     *     throws SQLException
     * @param sql a <code>String</code> object that is the SQL statement to be sent to the database;
     *     may contain one or more ? IN parameters
     * @param resultSetType a result set type; one of <code>ResultSet.TYPE_FORWARD_ONLY</code>,
     *     <code>ResultSet.TYPE_SCROLL_INSENSITIVE</code>, or <code>ResultSet.TYPE_SCROLL_SENSITIVE
     *     </code>
     * @param resultSetConcurrency a concurrency type; one of <code>ResultSet.CONCUR_READ_ONLY
     *     </code> or <code>ResultSet.CONCUR_UPDATABLE</code>
     * @return a new PreparedStatement object containing the pre-compiled SQL statement that will
     *     produce <code>ResultSet</code> objects with the given type and concurrency
     * @exception SQLException if a database access error occurs or the given parameters are not
     *     <code>ResultSet</code> constants indicating type and concurrency
     */
    public PreparedStatement prepareStatement(
            String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        CUBRIDPreparedStatementProxy preparedStatementProxy =
                new CUBRIDPreparedStatementProxy(
                        conn.prepareStatement(sql, resultSetType, resultSetConcurrency));
        preparedStatementProxy.setJdbcVersion(jdbcVersion);
        return preparedStatementProxy;
    }

    /**
     * @see Connection#prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int
     *     resultSetHoldability) throws SQLException
     * @param sql a <code>String</code> object that is the SQL statement to be sent to the database;
     *     may contain one or more ? IN parameters
     * @param resultSetType one of the following <code>ResultSet</code> constants: <code>
     *     ResultSet.TYPE_FORWARD_ONLY</code>, <code>ResultSet.TYPE_SCROLL_INSENSITIVE</code>, or
     *     <code>ResultSet.TYPE_SCROLL_SENSITIVE</code>
     * @param resultSetConcurrency one of the following <code>ResultSet</code> constants: <code>
     *     ResultSet.CONCUR_READ_ONLY</code> or <code>ResultSet.CONCUR_UPDATABLE</code>
     * @param resultSetHoldability one of the following <code>ResultSet</code> constants: <code>
     *     ResultSet.HOLD_CURSORS_OVER_COMMIT</code> or <code>ResultSet.CLOSE_CURSORS_AT_COMMIT
     *     </code>
     * @return a new <code>PreparedStatement</code> object, containing the pre-compiled SQL
     *     statement, that will generate <code>ResultSet</code> objects with the given type,
     *     concurrency, and holdability
     * @exception SQLException if a database access error occurs or the given parameters are not
     *     <code>ResultSet</code> constants indicating type, concurrency, and holdability
     */
    public PreparedStatement prepareStatement(
            String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability)
            throws SQLException {

        CUBRIDPreparedStatementProxy preparedStatementProxy =
                new CUBRIDPreparedStatementProxy(
                        conn.prepareStatement(
                                sql, resultSetType, resultSetConcurrency, resultSetHoldability));
        preparedStatementProxy.setJdbcVersion(jdbcVersion);
        return preparedStatementProxy;
    }

    /**
     * @see Connection#releaseSavepoint(Savepoint savepoint) throws SQLException
     * @param savepoint the <code>Savepoint</code> object to be removed
     * @exception SQLException if a database access error occurs or the given <code>Savepoint</code>
     *     object is not a valid savepoint in the current transaction
     */
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        conn.releaseSavepoint(savepoint);
    }

    /**
     * @see Connection#rollback() throws SQLException
     * @exception SQLException if a database access error occurs or this <code>Connection</code>
     *     object is in auto-commit mode
     */
    public void rollback() throws SQLException {
        conn.rollback();
    }

    /**
     * @see Connection#rollback(Savepoint savepoint) throws SQLException
     * @param savepoint the <code>Savepoint</code> object to roll back to
     * @exception SQLException if a database access error occurs, the <code>Savepoint</code> object
     *     is no longer valid, or this <code>Connection</code> object is currently in auto-commit
     *     mode
     */
    public void rollback(Savepoint savepoint) throws SQLException {
        conn.rollback(savepoint);
    }

    /**
     * Invoke the setAutoGeneratedKeys method in CUBRID Connection object
     *
     * @param isGeneratedKeys whether generated keys
     * @throws SQLException the SQLException
     */
    public void setAutoGeneratedKeys(boolean isGeneratedKeys) throws SQLException {
        ReflectionUtil.invoke(
                conn,
                "setAutoGeneratedKeys",
                new Class[] {boolean.class},
                new Object[] {isGeneratedKeys});
    }

    /**
     * Invoke the setCharset method in CUBRID Connection object
     *
     * @param charsetName the charset name
     * @throws java.io.UnsupportedEncodingException the unsupported encoding exception
     * @throws SQLException the SQLException
     */
    public void setCharset(String charsetName)
            throws java.io.UnsupportedEncodingException, SQLException {
        ReflectionUtil.invoke(conn, "setCharset", String.class, charsetName);
    }

    /**
     * Invoke the setLockTimeout method in CUBRID Connection object
     *
     * @param timeout the timeout value
     * @throws SQLException the SQLException
     */
    public void setLockTimeout(int timeout) throws SQLException {
        ReflectionUtil.invoke(
                conn, "setLockTimeout", new Class<?>[] {int.class}, new Object[] {timeout});
    }

    /**
     * Invoke the SetSignedConnection method in CUBRID Connection object
     *
     * @throws SQLException the SQLException
     */
    public void setSignedConnection() throws SQLException {
        ReflectionUtil.invoke(conn, "SetSignedConnection");
    }

    /**
     * @see Connection#setAutoCommit(boolean)
     * @param autoCommit <code>true</code> to enable auto-commit mode; <code>false</code> to disable
     *     it
     * @exception SQLException if a database access error occurs
     */
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        conn.setAutoCommit(autoCommit);
    }

    /**
     * @see Connection#setCatalog(String)
     * @param catalog the name of a catalog (subspace in this <code>Connection</code> object's
     *     database) in which to work
     * @exception SQLException if a database access error occurs
     */
    public void setCatalog(String catalog) throws SQLException {
        conn.setCatalog(catalog);
    }

    /**
     * @see Connection#setHoldability(int)
     * @param holdability a <code>ResultSet</code> holdability constant; one of <code>
     *     ResultSet.HOLD_CURSORS_OVER_COMMIT</code> or <code>ResultSet.CLOSE_CURSORS_AT_COMMIT
     *     </code>
     * @throws SQLException if a database access occurs, the given parameter is not a <code>
     *     ResultSet</code> constant indicating holdability, or the given holdability is not
     *     supported
     */
    public void setHoldability(int holdability) throws SQLException {
        conn.setHoldability(holdability);
    }

    /**
     * @see Connection#setReadOnly(boolean)
     * @param readOnly <code>true</code> enables read-only mode; <code>false</code> disables it
     * @exception SQLException if a database access error occurs or this method is called during a
     *     transaction
     */
    public void setReadOnly(boolean readOnly) throws SQLException {
        conn.setReadOnly(readOnly);
    }

    /**
     * @see Connection#setSavepoint()
     * @return the new <code>Savepoint</code> object
     * @exception SQLException if a database access error occurs or this <code>Connection</code>
     *     object is currently in auto-commit mode
     */
    public Savepoint setSavepoint() throws SQLException {
        return conn.setSavepoint();
    }

    /**
     * @see Connection#setSavepoint(String)
     * @param name a <code>String</code> containing the name of the savepoint
     * @return the new <code>Savepoint</code> object
     * @exception SQLException if a database access error occurs or this <code>Connection</code>
     *     object is currently in auto-commit mode
     */
    public Savepoint setSavepoint(String name) throws SQLException {
        return conn.setSavepoint(name);
    }

    /**
     * @see Connection#setTransactionIsolation(int)
     * @param level one of the following <code>Connection</code> constants: <code>
     *     Connection.TRANSACTION_READ_UNCOMMITTED</code>, <code>
     *     Connection.TRANSACTION_READ_COMMITTED</code>, <code>
     *     Connection.TRANSACTION_REPEATABLE_READ</code>, or <code>
     *     Connection.TRANSACTION_SERIALIZABLE</code>. (Note that <code>Connection.TRANSACTION_NONE
     *     </code> cannot be used because it specifies that transactions are not supported.)
     * @exception SQLException if a database access error occurs or the given parameter is not one
     *     of the <code>Connection</code> constants
     */
    public void setTransactionIsolation(int level) throws SQLException {
        conn.setTransactionIsolation(level);
    }

    /**
     * @see Connection#setTypeMap(Map)
     * @param map the <code>java.util.Map</code> object to install as the replacement for this
     *     <code>Connection</code> object's default type map
     * @exception SQLException if a database access error occurs or the given parameter is not a
     *     <code>java.util.Map</code> object
     */
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        conn.setTypeMap(map);
    }

    /**
     * Create array(no implemented)
     *
     * @param typeName the type name
     * @param elements the elements array
     * @return the null
     * @throws SQLException the SQLException
     */
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        return null;
    }

    /**
     * Invoke the getNewGLO method in Connection object
     *
     * @param className the class name
     * @param stream the stream
     * @return the CUBRIDOIDPROXY object
     * @throws SQLException the SQLException
     */
    public CUBRIDOIDProxy getNewGLO(String className, InputStream stream) throws SQLException {

        return new CUBRIDOIDProxy(
                ReflectionUtil.invoke(
                        conn,
                        "getNewGLO",
                        new Class<?>[] {String.class, InputStream.class},
                        new Object[] {className, stream}));
    }

    /**
     * Invoke the getNewGLO method in Connection object
     *
     * @param className the class name
     * @param stream the stream
     * @param length the length
     * @return the CUBRIDOIDPROXY object
     * @throws SQLException the SQLException
     */
    public CUBRIDOIDProxy getNewGLO(String className, InputStream stream, int length)
            throws SQLException {
        return new CUBRIDOIDProxy(
                ReflectionUtil.invoke(
                        conn,
                        "getNewGLO",
                        new Class<?>[] {String.class, InputStream.class, int.class},
                        new Object[] {className, stream, length}));
    }

    public Class<?> getProxyClass() {
        return conn.getClass();
    }

    public Connection getProxyObject() {
        return conn;
    }

    /**
     * Invoke the Logout method in CUBRID Connection object
     *
     * @throws SQLException the SQLException
     */
    public void logout() throws SQLException {
        ReflectionUtil.invoke(conn, "Logout");
    }

    /**
     * Invoke the newGloFbo method in CUBRID Connection object
     *
     * @param className the class name
     * @param filename the filename
     * @return the CUBRIDOIDProxy object
     * @throws SQLException the SQLException
     */
    public CUBRIDOIDProxy newGloFbo(String className, String filename) throws SQLException {
        return new CUBRIDOIDProxy(
                ReflectionUtil.invoke(
                        conn,
                        "newGloFbo",
                        new Class<?>[] {String.class, String.class},
                        new Object[] {className, filename}));
    }

    /**
     * Invoke the newGloFbo method in CUBRID Connection object
     *
     * @param className the class name
     * @return the CUBRIDOIDProxy object
     * @throws SQLException the SQLException
     */
    public CUBRIDOIDProxy newGloLo(String className) throws SQLException {
        return new CUBRIDOIDProxy(
                ReflectionUtil.invoke(conn, "newGloFbo", String.class, className));
    }

    /**
     * Constructs an object that implements the <code>Blob</code> interface. The object returned
     * initially contains no data. The <code>setBinaryStream</code> and <code>setBytes</code>
     * methods of the <code>Blob</code> interface may be used to add data to the <code>Blob</code>.
     *
     * @return An object that implements the <code>Blob</code> interface
     * @throws SQLException if an object that implements the <code>Blob</code> interface can not be
     *     constructed, this method is called on a closed connection or a database access error
     *     occurs.
     * @since 1.6
     */
    public Blob createBlob() throws SQLException {
        return new CUBRIDBlobProxy(conn.createBlob());
    }

    /**
     * Constructs an object that implements the <code>Clob</code> interface. The object returned
     * initially contains no data. The <code>setAsciiStream</code>, <code>setCharacterStream</code>
     * and <code>setString</code> methods of the <code>Clob</code> interface may be used to add data
     * to the <code>Clob</code>.
     *
     * @return An object that implements the <code>Clob</code> interface
     * @throws SQLException if an object that implements the <code>Clob</code> interface can not be
     *     constructed, this method is called on a closed connection or a database access error
     *     occurs.
     * @since 1.6
     */
    public Clob createClob() throws SQLException {
        return new CUBRIDClobProxy(conn.createClob());
    }

    /**
     * Constructs an object that implements the <code>NClob</code> interface. The object returned
     * initially contains no data. The <code>setAsciiStream</code>, <code>setCharacterStream</code>
     * and <code>setString</code> methods of the <code>NClob</code> interface may be used to add
     * data to the <code>NClob</code>.
     *
     * @return An object that implements the <code>NClob</code> interface
     * @throws SQLException if an object that implements the <code>NClob</code> interface can not be
     *     constructed, this method is called on a closed connection or a database access error
     *     occurs.
     * @since 1.6
     */
    public NClob createNClob() throws SQLException {
        return conn.createNClob();
    }

    /**
     * Constructs an object that implements the <code>SQLXML</code> interface. The object returned
     * initially contains no data. The <code>createXmlStreamWriter</code> object and <code>setString
     * </code> method of the <code>SQLXML</code> interface may be used to add data to the <code>
     * SQLXML</code> object.
     *
     * @return An object that implements the <code>SQLXML</code> interface
     * @throws SQLException if an object that implements the <code>SQLXML</code> interface can not
     *     be constructed, this method is called on a closed connection or a database access error
     *     occurs.
     * @since 1.6
     */
    public SQLXML createSQLXML() throws SQLException {
        return conn.createSQLXML();
    }

    /**
     * Factory method for creating Struct objects.
     *
     * @param typeName the SQL type name of the SQL structured type that this <code>Struct</code>
     *     object maps to. The typeName is the name of a user-defined type that has been defined for
     *     this database. It is the value returned by <code>Struct.getSQLTypeName</code>.
     * @param attributes the attributes that populate the returned object
     * @return a Struct object that maps to the given SQL type and is populated with the given
     *     attributes
     * @throws SQLException if a database error occurs, the typeName is null or this method is
     *     called on a closed connection
     * @since 1.6
     */
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        return conn.createStruct(typeName, attributes);
    }

    /**
     * Returns a list containing the name and current value of each client info property supported
     * by the driver. The value of a client info property may be null if the property has not been
     * set and does not have a default value.
     *
     * <p>
     *
     * @return A <code>Properties</code> object that contains the name and current value of each of
     *     the client info properties supported by the driver.
     *     <p>
     * @throws SQLException if the database server returns an error when fetching the client info
     *     values from the database or this method is called on a closed connection
     *     <p>
     * @since 1.6
     */
    public Properties getClientInfo() throws SQLException {
        return conn.getClientInfo();
    }

    /**
     * Returns the value of the client info property specified by name. This method may return null
     * if the specified client info property has not been set and does not have a default value.
     * This method will also return null if the specified client info property name is not supported
     * by the driver.
     *
     * <p>Applications may use the <code>DatabaseMetaData.getClientInfoProperties</code> method to
     * determine the client info properties supported by the driver.
     *
     * <p>
     *
     * @param name The name of the client info property to retrieve
     *     <p>
     * @return The value of the client info property specified
     *     <p>
     * @throws SQLException if the database server returns an error when fetching the client info
     *     value from the database or this method is called on a closed connection
     *     <p>
     * @since 1.6
     *     <p>
     * @see java.sql.DatabaseMetaData#getClientInfoProperties
     */
    public String getClientInfo(String name) throws SQLException {
        return conn.getClientInfo(name);
    }

    /**
     * Returns true if the connection has not been closed and is still valid. The driver shall
     * submit a query on the connection or use some other mechanism that positively verifies the
     * connection is still valid when this method is called.
     *
     * <p>The query submitted by the driver to validate the connection shall be executed in the
     * context of the current transaction.
     *
     * @param timeout - The time in seconds to wait for the database operation used to validate the
     *     connection to complete. If the timeout period expires before the operation completes,
     *     this method returns false. A value of 0 indicates a timeout is not applied to the
     *     database operation.
     *     <p>
     * @return true if the connection is valid, false otherwise
     * @exception SQLException if the value supplied for <code>timeout</code> is less then 0
     * @since 1.6
     *     <p>
     * @see java.sql.DatabaseMetaData#getClientInfoProperties
     */
    public boolean isValid(int timeout) throws SQLException {
        return conn.isValid(timeout);
    }

    /**
     * Sets the value of the connection's client info properties. The <code>Properties</code> object
     * contains the names and values of the client info properties to be set. The set of client info
     * properties contained in the properties list replaces the current set of client info
     * properties on the connection. If a property that is currently set on the connection is not
     * present in the properties list, that property is cleared. Specifying an empty properties list
     * will clear all of the properties on the connection. See <code>setClientInfo (String, String)
     * </code> for more information.
     *
     * <p>If an error occurs in setting any of the client info properties, a <code>
     * SQLClientInfoException</code> is thrown. The <code>SQLClientInfoException</code> contains
     * information indicating which client info properties were not set. The state of the client
     * information is unknown because some databases do not allow multiple client info properties to
     * be set atomically. For those databases, one or more properties may have been set before the
     * error occurred.
     *
     * <p>
     *
     * @param properties the list of client info properties to set
     *     <p>
     * @see java.sql.Connection#setClientInfo(String, String) setClientInfo(String, String)
     * @since 1.6
     *     <p>
     * @throws SQLClientInfoException if the database server returns an error while setting the
     *     clientInfo values on the database server or this method is called on a closed connection
     *     <p>
     */
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        conn.setClientInfo(properties);
    }

    /**
     * Sets the value of the client info property specified by name to the value specified by value.
     *
     * <p>Applications may use the <code>DatabaseMetaData.getClientInfoProperties</code> method to
     * determine the client info properties supported by the driver and the maximum length that may
     * be specified for each property.
     *
     * <p>The driver stores the value specified in a suitable location in the database. For example
     * in a special register, session parameter, or system table column. For efficiency the driver
     * may defer setting the value in the database until the next time a statement is executed or
     * prepared. Other than storing the client information in the appropriate place in the database,
     * these methods shall not alter the behavior of the connection in anyway. The values supplied
     * to these methods are used for accounting, diagnostics and debugging purposes only.
     *
     * <p>The driver shall generate a warning if the client info name specified is not recognized by
     * the driver.
     *
     * <p>If the value specified to this method is greater than the maximum length for the property
     * the driver may either truncate the value and generate a warning or generate a <code>
     * SQLClientInfoException</code>. If the driver generates a <code>SQLClientInfoException</code>,
     * the value specified was not set on the connection.
     *
     * <p>The following are standard client info properties. Drivers are not required to support
     * these properties however if the driver supports a client info property that can be described
     * by one of the standard properties, the standard property name should be used.
     *
     * <p>
     *
     * <ul>
     *   <li>ApplicationName - The name of the application currently utilizing the connection
     *   <li>ClientUser - The name of the user that the application using the connection is
     *       performing work for. This may not be the same as the user name that was used in
     *       establishing the connection.
     *   <li>ClientHostname - The hostname of the computer the application using the connection is
     *       running on.
     * </ul>
     *
     * <p>
     *
     * @param name The name of the client info property to set
     * @param value The value to set the client info property to. If the value is null, the current
     *     value of the specified property is cleared.
     *     <p>
     * @throws SQLClientInfoException if the database server returns an error while setting the
     *     client info value on the database server or this method is called on a closed connection
     *     <p>
     * @since 1.6
     */
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        conn.setClientInfo(name, value);
    }

    /**
     * Returns true if this either implements the interface argument or is directly or indirectly a
     * wrapper for an object that does. Returns false otherwise. If this implements the interface
     * then return true, else if this is a wrapper then return the result of recursively calling
     * <code>isWrapperFor</code> on the wrapped object. If this does not implement the interface and
     * is not a wrapper, return false. This method should be implemented as a low-cost operation
     * compared to <code>unwrap</code> so that callers can use this method to avoid expensive <code>
     * unwrap</code> calls that may fail. If this method returns true then calling <code>unwrap
     * </code> with the same argument should succeed.
     *
     * @param iface a Class defining an interface.
     * @return true if this implements the interface or directly or indirectly wraps an object that
     *     does.
     * @throws java.sql.SQLException if an error occurs while determining whether this is a wrapper
     *     for an object with the given interface.
     * @since 1.6
     */
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return conn.isWrapperFor(iface);
    }

    /**
     * Returns an object that implements the given interface to allow access to non-standard
     * methods, or standard methods not exposed by the proxy.
     *
     * <p>If the receiver implements the interface then the result is the receiver or a proxy for
     * the receiver. If the receiver is a wrapper and the wrapped object implements the interface
     * then the result is the wrapped object or a proxy for the wrapped object. Otherwise return the
     * the result of calling <code>unwrap</code> recursively on the wrapped object or a proxy for
     * that result. If the receiver is not a wrapper and does not implement the interface, then an
     * <code>SQLException</code> is thrown.
     *
     * @param iface A Class defining an interface that the result must implement.
     * @param <T> Class
     * @return an object that implements the interface. May be a proxy for the actual implementing
     *     object.
     * @throws java.sql.SQLException If no object found that implements the interface
     * @since 1.6
     */
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return conn.unwrap(iface);
    }

    // --------------------------JDBC 4.1 -----------------------------

    /**
     * Terminates an open connection. Calling <code>abort</code> results in:
     *
     * <ul>
     *   <li>The connection marked as closed
     *   <li>Closes any physical connection to the database
     *   <li>Releases resources used by the connection
     *   <li>Insures that any thread that is currently accessing the connection will either progress
     *       to completion or throw an <code>SQLException</code>.
     * </ul>
     *
     * <p>Calling <code>abort</code> marks the connection closed and releases any resources. Calling
     * <code>abort</code> on a closed connection is a no-op.
     *
     * <p>It is possible that the aborting and releasing of the resources that are held by the
     * connection can take an extended period of time. When the <code>abort</code> method returns,
     * the connection will have been marked as closed and the <code>Executor</code> that was passed
     * as a parameter to abort may still be executing tasks to release resources.
     *
     * <p>This method checks to see that there is an <code>SQLPermission</code> object before
     * allowing the method to proceed. If a <code>SecurityManager</code> exists and its <code>
     * checkPermission</code> method denies calling <code>abort</code>, this method throws a <code>
     * java.lang.SecurityException</code>.
     *
     * @param executor The <code>Executor</code> implementation which will be used by <code>abort
     *     </code>.
     * @throws java.sql.SQLException if a database access error occurs or the {@code executor} is
     *     {@code null},
     * @throws java.lang.SecurityException if a security manager exists and its <code>
     *     checkPermission</code> method denies calling <code>abort</code>
     * @see SecurityManager#checkPermission
     * @see Executor
     * @since 1.7
     */
    public void abort(Executor executor) throws SQLException {
        conn.abort(executor);
    }

    /**
     * Retrieves this <code>Connection</code> object's current schema name.
     *
     * @return the current schema name or <code>null</code> if there is none
     * @exception SQLException if a database access error occurs or this method is called on a
     *     closed connection
     * @see #setSchema
     * @since 1.7
     */
    public String getSchema() throws SQLException {
        return conn.getSchema();
    }

    /**
     * Sets the given schema name to access.
     *
     * <p>If the driver does not support schemas, it will silently ignore this request.
     *
     * <p>Calling {@code setSchema} has no effect on previously created or prepared {@code
     * Statement} objects. It is implementation defined whether a DBMS prepare operation takes place
     * immediately when the {@code Connection} method {@code prepareStatement} or {@code
     * prepareCall} is invoked. For maximum portability, {@code setSchema} should be called before a
     * {@code Statement} is created or prepared.
     *
     * @param schema the name of a schema in which to work
     * @exception SQLException if a database access error occurs or this method is called on a
     *     closed connection
     * @see #getSchema
     * @since 1.7
     */
    public void setSchema(String schema) throws SQLException {
        conn.setSchema(schema);
    }

    /**
     * Retrieves the number of milliseconds the driver will wait for a database request to complete.
     * If the limit is exceeded, a <code>SQLException</code> is thrown.
     *
     * @return the current timeout limit in milliseconds; zero means there is no limit
     * @throws SQLException if a database access error occurs or this method is called on a closed
     *     <code>Connection</code>
     * @exception SQLFeatureNotSupportedException if the JDBC driver does not support this method
     * @see #setNetworkTimeout
     * @since 1.7
     */
    public int getNetworkTimeout() throws SQLException {
        return conn.getNetworkTimeout();
    }

    /**
     * Sets the maximum period a <code>Connection</code> or objects created from the <code>
     * Connection</code> will wait for the database to reply to any one request. If any request
     * remains unanswered, the waiting method will return with a <code>SQLException</code>, and the
     * <code>Connection</code> or objects created from the <code>Connection</code> will be marked as
     * closed. Any subsequent use of the objects, with the exception of the <code>close</code>,
     * <code>isClosed</code> or <code>Connection.isValid</code> methods, will result in a <code>
     * SQLException</code>.
     *
     * <p><b>Note</b>: This method is intended to address a rare but serious condition where network
     * partitions can cause threads issuing JDBC calls to hang uninterruptedly in socket reads,
     * until the OS TCP-TIMEOUT (typically 10 minutes). This method is related to the {@link #abort
     * abort() } method which provides an administrator thread a means to free any such threads in
     * cases where the JDBC connection is accessible to the administrator thread. The <code>
     * setNetworkTimeout</code> method will cover cases where there is no administrator thread, or
     * it has no access to the connection. This method is severe in it's effects, and should be
     * given a high enough value so it is never triggered before any more normal timeouts, such as
     * transaction timeouts.
     *
     * <p>JDBC driver implementations may also choose to support the {@code setNetworkTimeout}
     * method to impose a limit on database response time, in environments where no network is
     * present.
     *
     * <p>Drivers may internally implement some or all of their API calls with multiple internal
     * driver-database transmissions, and it is left to the driver implementation to determine
     * whether the limit will be applied always to the response to the API call, or to any single
     * request made during the API call.
     *
     * <p>This method can be invoked more than once, such as to set a limit for an area of JDBC
     * code, and to reset to the default on exit from this area. Invocation of this method has no
     * impact on already outstanding requests.
     *
     * <p>The {@code Statement.setQueryTimeout()} timeout value is independent of the timeout value
     * specified in {@code setNetworkTimeout}. If the query timeout expires before the network
     * timeout then the statement execution will be canceled. If the network is still active the
     * result will be that both the statement and connection are still usable. However if the
     * network timeout expires before the query timeout or if the statement timeout fails due to
     * network problems, the connection will be marked as closed, any resources held by the
     * connection will be released and both the connection and statement will be unusable.
     *
     * <p>When the driver determines that the {@code setNetworkTimeout} timeout value has expired,
     * the JDBC driver marks the connection closed and releases any resources held by the
     * connection.
     *
     * <p>This method checks to see that there is an <code>SQLPermission</code> object before
     * allowing the method to proceed. If a <code>SecurityManager</code> exists and its <code>
     * checkPermission</code> method denies calling <code>setNetworkTimeout</code>, this method
     * throws a <code>java.lang.SecurityException</code>.
     *
     * @param executor The <code>Executor</code> implementation which will be used by <code>
     *     setNetworkTimeout</code>.
     * @param milliseconds The time in milliseconds to wait for the database operation to complete.
     *     If the JDBC driver does not support milliseconds, the JDBC driver will round the value up
     *     to the nearest second. If the timeout period expires before the operation completes, a
     *     SQLException will be thrown. A value of 0 indicates that there is not timeout for
     *     database operations.
     * @throws java.sql.SQLException if a database access error occurs, this method is called on a
     *     closed connection, the {@code executor} is {@code null}, or the value specified for
     *     <code>seconds</code> is less than 0.
     * @throws java.lang.SecurityException if a security manager exists and its <code>
     *     checkPermission</code> method denies calling <code>setNetworkTimeout</code>.
     * @exception SQLFeatureNotSupportedException if the JDBC driver does not support this method
     * @see SecurityManager#checkPermission
     * @see Statement#setQueryTimeout
     * @see #getNetworkTimeout
     * @see #abort
     * @see Executor
     * @since 1.7
     */
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        conn.setNetworkTimeout(executor, milliseconds);
    }
}
