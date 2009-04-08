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
package ccc.cli;

import java.sql.Connection;
import java.util.Properties;
import java.util.UUID;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import ccc.migration.NewDBQueries;


/**
 * Command line interface for user management.
 *
 * @author Civic Computing Ltd.
 */
public final class Users extends CccApp {
    private static final Logger LOG = Logger.getLogger(Users.class);
    private static Properties props = new Properties();

    private Users() { super(); }


    /**
     * App entry point.
     * Requires three arguments: username, email, password - in that order.
     *
     * @param args Command line arguments.
     */
    public static void main(final String[] args) {
        loadSettings(props, "create-user.properties");
        create(args[0], args[1], args[2]);
    }

    /**
     * Create a user.
     *
     * @param username username.
     * @param email email address.
     * @param password password.
     * @return The UUID of the new user.
     */
    static UUID create(final String username,
                       final String email,
                       final String password) {

        final Connection newConnection = getConnection(props);
        try {
            final NewDBQueries queries = new NewDBQueries(newConnection);
            final UUID userId =
                queries.insertMigrationUser(username, email, password);
            LOG.info("Created user: "+username);
            return userId;
        } finally {
            DbUtils.closeQuietly(newConnection);
        }
    }
}
