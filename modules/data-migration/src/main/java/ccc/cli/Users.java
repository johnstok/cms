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
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import ccc.migration.MigrationException;
import ccc.migration.NewDBQueries;


/**
 * Command line interface for user management.
 *
 * @author Civic Computing Ltd.
 */
public final class Users {
    private static Logger log = Logger.getLogger(Users.class);

    private Users() { super(); }

    /**
     * App entry point.
     * Requires three arguments: username, email, password - in that order.
     *
     * @param args Command line arguments.
     */
    public static void main(final String[] args) {
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

        final Connection newConnection = getConnection();
        try {
            final NewDBQueries queries = new NewDBQueries(newConnection);
            final UUID userId =
                queries.insertMigrationUser(username, email, password);
            log.info("Created user: "+username);
            return userId;
        } finally {
            DbUtils.closeQuietly(newConnection);
        }
    }

    private static Connection getConnection() {
        Connection connection = null;
        // FIXME: Hard coded connection string.
        try {
            Class.forName("org.h2.Driver");
//            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//            Class.forName("oracle.jdbc.driver.OracleDriver");
//            Class.forName("com.mysql.jdbc.Driver");
            connection =
                DriverManager.getConnection(
                    "jdbc:h2:tcp:localhost/mem:CCC", "CCC", "CCC");
//                    "jdbc:sqlserver://hestia:1260;DatabaseName=CCC", "CCC", "CCC");
//                    "jdbc:oracle:thin:@poseidon:1521:dev", "ccc", "d3ccc");
//                    "jdbc:mysql://hestia:3306/ccc", "CCC", "CCC");
            connection.setAutoCommit(false);
        } catch (final ClassNotFoundException e) {
            throw new MigrationException(e);
        } catch (final SQLException e) {
            throw new MigrationException(e);
        }
        return connection;
    }
}
