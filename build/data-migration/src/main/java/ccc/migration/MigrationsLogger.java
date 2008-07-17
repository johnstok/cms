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
import java.sql.SQLException;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class MigrationsLogger implements Migrations {

    private final Console console;
    /**
     * Constructor.
     *
     * @param console
     */
    public MigrationsLogger(Console console) {
        this.console = console;
    }

    /**
     * @see ccc.migration.Migrations#migrateFolders(java.sql.ResultSet)
     */
    @Override
    public void migrateFolders(ResultSet resultSet) {
        
        try {
            while (resultSet.next()) {
                console.print(resultSet.getString("NAME"));
            }
        } catch (SQLException e) {
            throw new MigrationException(e);
        }
        
    }

}
