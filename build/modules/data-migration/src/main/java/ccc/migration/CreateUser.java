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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class CreateUser {
    private static final Logger log = Logger.getLogger(App.class);

    /**
     * TODO: Add a description of this method.
     *
     * @param args
     */
    public static void main(final String[] args) {
        final Connection newConnection = getConnection();
        final NewDBQueries queries = new NewDBQueries(newConnection);
        final String username = "migration";
        final String email = "migration@civicuk.com";
        final String password = "migration";
        final UUID muid =
            queries.insertMigrationUser(username, email, password);
        DbUtils.closeQuietly(newConnection);
        log.info("Created user: "+username);
    }

    private static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("org.h2.Driver");
            connection =
                DriverManager.getConnection("jdbc:h2:tcp:localhost/mem:CCC",
                    "CCC", "CCC");
            connection.setAutoCommit(false);
        } catch (final ClassNotFoundException e) {
            throw new MigrationException(e);
        } catch (final SQLException e) {
            throw new MigrationException(e);
        }
        return connection;
    }
}
