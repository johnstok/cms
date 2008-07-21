/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */
package ccc.migration;

import java.sql.ResultSet;


/**
 * API definition for supported CCC migrations.
 *
 * @author Civic Computing Ltd
 */
public interface Migrations {

    /**
     * Migrate folders into the CCC server.
     *
     * @param resultSet A sql {@link ResultSet} containing a record for each
     *  folder to be migrated.
     */
    void migrateFolders(ResultSet resultSet);

    /**
     * Create a folder for the root of all CCC content.
     */
    void createContentRoot();
}
