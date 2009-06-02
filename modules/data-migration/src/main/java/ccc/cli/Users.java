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
import java.util.UUID;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;
import org.kohsuke.args4j.Option;

import ccc.migration.NewDBQueries;


/**
 * Command line interface for user management.
 *
 * @author Civic Computing Ltd.
 */
public final class Users extends CccApp {
    private static final Logger LOG = Logger.getLogger(Users.class);

    private static Options options;

    private Users() { super(); }


    /**
     * App entry point.
     *
     * @param args Command line arguments.
     */
    public static void main(final String[] args) {
        options  = parseOptions(args, Options.class);
        create();
    }

    /**
     * Create a user.
     *
     * @return The UUID of the new user.
     */
    static UUID create() {

        final Connection newConnection =
            getConnection(
                getDriverForConnectionString(options._conString),
                options._conString,
                options._username,
                options._password);
        try {
            final NewDBQueries queries = new NewDBQueries(newConnection);
            final UUID userId =
                queries.insertMigrationUser(
                    options._newUsername,
                    options._newEmail,
                    options._newPassword);
            LOG.info("Created user: "+options._newUsername);
            return userId;
        } finally {
            DbUtils.closeQuietly(newConnection);
        }
    }

    /**
     * Options for the search scheduler tool.
     *
     * @author Civic Computing Ltd.
     */
    static class Options {
        @Option(
            name="-u", required=true, usage="Username for connecting to CCC.")
        String _username;

        @Option(
            name="-p", required=true, usage="Password for connecting to CCC.")
        String _password;

        @Option(
            name="-c", required=true, usage="Connection string for the DB.")
        String _conString;

        @Option(
            name="-nu", required=true, usage="Username of the user to create.")
            String _newUsername;

        @Option(
            name="-np", required=true, usage="Password of the user to create.")
            String _newPassword;

        @Option(
            name="-ne", required=true, usage="Email of the user to create.")
            String _newEmail;
    }
}
