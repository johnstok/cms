/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.migration.ccc6.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.dbutils.ResultSetHandler;


/**
 * API for a SQL query.
 *
 * @param <T> The type of result this query will return.
 *
 * @author Civic Computing Ltd.
 */
public interface SqlQuery<T> extends ResultSetHandler {

    /** {@inheritDoc} */
    T handle(final ResultSet rs) throws SQLException;

    /**
     * Accessor.
     *
     * @return The SQL for this query, as a string.
     */
    String getSql();
}
