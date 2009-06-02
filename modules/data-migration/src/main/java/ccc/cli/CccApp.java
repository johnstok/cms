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

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;
import java.util.Properties;

import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag;
import javax.sql.DataSource;

import oracle.jdbc.pool.OracleDataSource;

import org.apache.log4j.Logger;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import ccc.domain.CCCException;
import ccc.migration.MigrationException;
import ccc.migration.UserNamePasswordHandler;


/**
 * Helper methods for the CCC command line tools.
 *
 * @author Civic Computing Ltd.
 */
class CccApp {

    private static final Logger LOG = Logger.getLogger(CccApp.class);
    private static final long START_TIME = new Date().getTime();
    private static final long MILLISECS_PER_SEC = 1000;

    private static LoginContext ctx;

    /** Constructor. */
    protected CccApp() { super(); }


    /**
     * Login to the server.
     *
     * @param username The username for login.
     * @param password The password for login.
     */
    static void login(final String username,
                      final String password) {

        Configuration.setConfiguration(
            new Configuration() {
                @Override public AppConfigurationEntry[]
                                   getAppConfigurationEntry(final String name) {
                    final AppConfigurationEntry jBoss =
                        new AppConfigurationEntry(
                            "org.jboss.security.ClientLoginModule",
                            LoginModuleControlFlag.REQUIRED,
                            Collections.<String, Object> emptyMap());
                    return new AppConfigurationEntry[] {jBoss};
                }
            }
        );

        try {
            ctx =
                new LoginContext(
                    "ccc",
                    new UserNamePasswordHandler(username, password));
            ctx.login();
        } catch (final LoginException e) {
            throw new CCCException(e);
        }
        LOG.info("Logged in.");
    }


    /**
     * Logout from the server.
     */
    static void logout() {
        try {
            ctx.logout();
        } catch (final LoginException e) {
            throw new CCCException(e);
        }
        LOG.info("Logged out.");
    }


    /**
     * Report progress, indicating the duration since app startup.
     *
     * @param prefix The text prefix to prepend to the progress report.
     */
    static void report(final String prefix) {
        final long elapsedTime = new Date().getTime() - START_TIME;
        LOG.info(
            prefix
            + elapsedTime/MILLISECS_PER_SEC
            + " seconds.");
    }

    /**
     * Load settings from the specified path into the specified properties
     * object.
     *
     * @param props The object into which the settings will be loaded.
     * @param resourcePath The path to the properties file on the classpath.
     */
    static void loadSettings(final Properties props,
                             final String resourcePath) {
        try {
            final InputStream in =
                Thread.currentThread().
                getContextClassLoader().
                getResourceAsStream(resourcePath);
            props.load(in);
            in.close();
            LOG.info("Loaded settings.");
            for (final Object o: props.keySet()) {
                LOG.debug(o+"\t\t=\t"+props.get(o));
            }
        } catch (final IOException e) {
            throw new CCCException(e);
        }
    }

    /**
     * Create a JDBC connection to a database.
     *
     * @param dbProps The properties for the connection.
     * @return A new connection.
     */
    static Connection getConnection(final Properties dbProps) {
        final String driverClass = dbProps.getProperty("db.driver.class");
        final String conString = dbProps.getProperty("db.connection.string");
        final String username = dbProps.getProperty("db.username");
        final String password = dbProps.getProperty("db.password");
        return getConnection(driverClass, conString, username, password);
    }


    /**
     * Create a JDBC connection to a database.
     *
     * @param driverClass The java class of the driver.
     * @param connectionString The DB connection string.
     * @param username The username for the DB.
     * @param password The password for the DB.
     * @return A new connection.
     */
    static Connection getConnection(final String driverClass,
                                    final String connectionString,
                                    final String username,
                                    final String password) {
        try {
            Class.forName(driverClass);
            final Connection connection =
                DriverManager.getConnection(
                    connectionString,
                    username,
                    password);
            connection.setAutoCommit(false);
            LOG.debug("Connected to "+connectionString);
            return connection;
        } catch (final ClassNotFoundException e) {
            throw new MigrationException(e);
        } catch (final SQLException e) {
            throw new MigrationException(e);
        }
    }

    /**
     * Create an oracle datasource.
     *
     * @param url The JDBC connection string.
     * @param username The DB username.
     * @param password The DB password.
     *
     * @return A new oracle datasource.
     */
    static DataSource getOracleDatasource(final String url,
                                          final String username,
                                          final String password) {
        try {
            // Load the JDBC driver
            final String driverName = "oracle.jdbc.driver.OracleDriver";
            Class.forName(driverName);

            // Create a connection to the database
            final OracleDataSource ods = new OracleDataSource();
            final Properties connectionProps = new Properties();
            connectionProps.put("user", username);
            connectionProps.put("password", password);
            connectionProps.put(
                "oracle.jdbc.FreeMemoryOnEnterImplicitCache", Boolean.TRUE);
            ods.setConnectionProperties(connectionProps);
            ods.setURL(url);
            return ods;
        } catch (final ClassNotFoundException e) {
            throw new MigrationException(e);
        } catch (final SQLException e) {
            throw new MigrationException(e);
        }
    }


    /**
     * Parse command line options.
     *
     * @param <T> The type of options instance to return.
     * @param args The arguments as an array of strings.
     * @param optionsClass The class representing type T.
     *
     * @return An instance of type T containing the options from parameter
     *  'args'.
     */
    static <T> T parseOptions(final String[] args,
                              final Class<T> optionsClass) {
        T options;
        try {
            options = optionsClass.newInstance();
        } catch (final InstantiationException e1) {
            throw new RuntimeException(e1);
        } catch (final IllegalAccessException e1) {
            throw new RuntimeException(e1);
        }
        final CmdLineParser parser = new CmdLineParser(options);
        try {
            parser.parseArgument(args);
            return options;
        } catch(final CmdLineException e) {
            System.err.println(e.getMessage());
            parser.printUsage(System.err);
            System.exit(1);
            return null; // We can't actually get here.
        }
    }

    /**
     * Determine the driver class based on connection string.
     *
     * @param conString The connection string.
     * @return The corresponding driver class.
     */
    static String getDriverForConnectionString(final String conString) {
        if (conString.startsWith("jdbc:oracle")) {
            return "oracle.jdbc.driver.OracleDriver";
        } else if (conString.startsWith("jdbc:mysql")) {
            return "com.mysql.jdbc.Driver";
        } else if (conString.startsWith("jdbc:sqlserver")) {
            return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        } else if (conString.startsWith("jdbc:h2")) {
            return "org.h2.Driver";
        } else {
            throw new MigrationException("Unsupported database");
        }

    }
}
