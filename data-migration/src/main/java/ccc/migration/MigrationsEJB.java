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

import ccc.domain.ResourceName;
import ccc.domain.ResourcePath;
import ccc.services.ResourceManager;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class MigrationsEJB implements Migrations {

    private ResourceManager manager;

    /**
     * Constructor.
     *
     * @param manager
     */
    public MigrationsEJB(ResourceManager manager) {
        this.manager = manager;
    }

    /**
     * @see ccc.migration.Migrations#migrateFolders(java.sql.ResultSet)
     */
    @Override
    public void migrateFolders(ResultSet resultSet) {

        try {
            while (resultSet.next()) {
                ResourceName name = ResourceName.escape(resultSet.getString("NAME"));
                ResourcePath path = new ResourcePath(name);
                manager.createFolder(path.toString());
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e);
        }
    }

}
