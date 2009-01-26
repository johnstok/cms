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
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public interface DB {

    /**
     * TODO: Add a description of this method.
     *
     * @param <T>
     * @param q
     * @param param
     * @return
     */
    public <T> T select(final SqlQuery<T> q, final Object... param);
}
