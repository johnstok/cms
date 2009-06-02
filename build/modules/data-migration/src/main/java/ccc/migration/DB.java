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
package ccc.migration;

import ccc.migration.ccc6.handlers.SqlQuery;


/**
 * API for CCC6 database operations.
 *
 * @author Civic Computing Ltd.
 */
public interface DB {

    /**
     * Execute a SQL query.
     *
     * @param <T> The type of result the select method will return.
     * @param q The SQL query.
     * @param param The queries parameters.
     * @return An instance of type T.
     */
    <T> T select(final SqlQuery<T> q, final Object... param);
}
