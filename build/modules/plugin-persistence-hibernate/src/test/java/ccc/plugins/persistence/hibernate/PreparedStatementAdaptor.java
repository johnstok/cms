/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.plugins.persistence.hibernate;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


/**
 * Adaptor for to simplify definition of {@link PreparedStatement} subclasses.
 *
 * @author Civic Computing Ltd.
 */
class PreparedStatementAdaptor
    implements
        PreparedStatement {

    private Map<Integer, Object> _params = new HashMap<Integer, Object>();


    /** {@inheritDoc} */
    @Override
    public void addBatch() {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void clearParameters() {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public boolean execute() {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public ResultSet executeQuery() {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public int executeUpdate() {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public ResultSetMetaData getMetaData() {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public ParameterMetaData getParameterMetaData() {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setArray(final int parameterIndex, final Array x) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setAsciiStream(final int parameterIndex, final InputStream x) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setAsciiStream(final int parameterIndex,
                               final InputStream x,
                               final int length) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setAsciiStream(final int parameterIndex,
                               final InputStream x,
                               final long length) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setBigDecimal(final int parameterIndex, final BigDecimal x) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setBinaryStream(final int parameterIndex, final InputStream x) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setBinaryStream(final int parameterIndex,
                                final InputStream x,
                                final int length) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setBinaryStream(final int parameterIndex,
                                final InputStream x,
                                final long length) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setBlob(final int parameterIndex, final Blob x) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setBlob(final int parameterIndex,
                        final InputStream inputStream) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setBlob(final int parameterIndex,
                        final InputStream inputStream,
                        final long length) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setBoolean(final int parameterIndex, final boolean x) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setByte(final int parameterIndex, final byte x) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setBytes(final int parameterIndex, final byte[] x) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setCharacterStream(final int parameterIndex,
                                   final Reader reader) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setCharacterStream(final int parameterIndex,
                                   final Reader reader,
                                   final int length) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setCharacterStream(final int parameterIndex,
                                   final Reader reader,
                                   final long length) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setClob(final int parameterIndex, final Clob x) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setClob(final int parameterIndex, final Reader reader) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setClob(final int parameterIndex,
                        final Reader reader,
                        final long length) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setDate(final int parameterIndex, final Date x) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setDate(final int parameterIndex,
                        final Date x,
                        final Calendar cal) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setDouble(final int parameterIndex, final double x) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setFloat(final int parameterIndex, final float x) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setInt(final int parameterIndex, final int x) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setLong(final int parameterIndex, final long x) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setNCharacterStream(final int parameterIndex,
                                    final Reader value) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setNCharacterStream(final int parameterIndex,
                                    final Reader value,
                                    final long length) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setNClob(final int parameterIndex, final NClob value) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setNClob(final int parameterIndex, final Reader reader) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setNClob(final int parameterIndex,
                         final Reader reader,
                         final long length) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setNString(final int parameterIndex, final String value) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setNull(final int parameterIndex, final int sqlType) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setNull(final int parameterIndex,
                        final int sqlType,
                        final String typeName) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setObject(final int parameterIndex, final Object x) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setObject(final int parameterIndex,
                          final Object x,
                          final int targetSqlType) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setObject(final int parameterIndex,
                          final Object x,
                          final int targetSqlType,
                          final int scaleOrLength) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setRef(final int parameterIndex, final Ref x) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setRowId(final int parameterIndex, final RowId x) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setSQLXML(final int parameterIndex, final SQLXML xmlObject) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setShort(final int parameterIndex, final short x) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setString(final int parameterIndex, final String x) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setTime(final int parameterIndex, final Time x) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setTime(final int parameterIndex,
                        final Time x,
                        final Calendar cal) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setTimestamp(final int parameterIndex, final Timestamp x) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setTimestamp(final int parameterIndex,
                             final Timestamp x,
                             final Calendar cal) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setURL(final int parameterIndex, final URL x) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setUnicodeStream(final int parameterIndex,
                                 final InputStream x,
                                 final int length) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void addBatch(final String sql) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void cancel() {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void clearBatch() {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void clearWarnings() {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void close() {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public boolean execute(final String sql) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public boolean execute(final String sql, final int autoGeneratedKeys) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public boolean execute(final String sql, final int[] columnIndexes) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public boolean execute(final String sql, final String[] columnNames) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public int[] executeBatch() {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public ResultSet executeQuery(final String sql) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public int executeUpdate(final String sql) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public int executeUpdate(final String sql, final int autoGeneratedKeys) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public int executeUpdate(final String sql, final int[] columnIndexes) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public int executeUpdate(final String sql, final String[] columnNames) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public Connection getConnection() {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public int getFetchDirection() {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public int getFetchSize() {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public ResultSet getGeneratedKeys() {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public int getMaxFieldSize() {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public int getMaxRows() {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public boolean getMoreResults() {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public boolean getMoreResults(final int current) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public int getQueryTimeout() {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public ResultSet getResultSet() {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public int getResultSetConcurrency() {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public int getResultSetHoldability() {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public int getResultSetType() {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public int getUpdateCount() {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public SQLWarning getWarnings() {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public boolean isClosed() {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public boolean isPoolable() {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setCursorName(final String name) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setEscapeProcessing(final boolean enable) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setFetchDirection(final int direction) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setFetchSize(final int rows) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setMaxFieldSize(final int max) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setMaxRows(final int max) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setPoolable(final boolean poolable) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void setQueryTimeout(final int seconds) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public boolean isWrapperFor(final Class<?> iface) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public <T> T unwrap(final Class<T> iface) {
        throw new UnsupportedOperationException("Method not implemented.");
    }




    /* ====================================================================
     * Helper methods.
     * ================================================================== */

    /**
     * Set the value of a specified parameter.
     *
     * @param index The index position of the parameter in the statement.
     * @param value The value of the parameter.
     */
    protected void addParameter(final int index, final Object value) {
        _params.put(Integer.valueOf(index), value);
    }


    /**
     * Check if a specified parameter has been set to NULL.
     *
     * @param index The index position of the parameter in the statement.
     *
     * @return True if the parameter has been set to NULL, false otherwise.
     */
    public boolean isParameterNull(final int index) {
        final Integer i = Integer.valueOf(index);
        return _params.containsKey(i) && (null==_params.get(i));
    }


    /**
     * Get the value of a specified parameter.
     *
     * @param index The index position of the parameter in the statement.
     *
     * @return The value of the parameter.
     */
    public Object getParameter(final int index) {
        return _params.get(Integer.valueOf(index));
    }
}
