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

import ccc.domain.CCCException;
import ccc.migration.MigrationException;
import ccc.migration.UserNamePasswordHandler;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
class CccApp {

    private static final Logger LOG = Logger.getLogger(CccApp.class);
    private static final long START_TIME = new Date().getTime();
    private static final long MILLISECS_PER_SEC = 1000;

    private static LoginContext ctx;

    /** Constructor. */
    CccApp() { super(); }


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
                @Override public AppConfigurationEntry[] getAppConfigurationEntry(final String name) {
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
     * TODO: Add a description of this method.
     *
     * @param props
     * @param resourcePath
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
        } catch (final IOException e) {
            throw new CCCException(e);
        }
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param dbProps
     * @return
     */
    static Connection getConnection(final Properties dbProps) {
        try {
            Class.forName(dbProps.getProperty("db.driver.class"));
            final Connection connection =
                DriverManager.getConnection(
                    dbProps.getProperty("db.connection.string"),
                    dbProps.getProperty("db.username"),
                    dbProps.getProperty("db.password"));
            connection.setAutoCommit(false);
            LOG.debug(
                "Connected to "+dbProps.getProperty("db.connection.string"));
            return connection;
        } catch (final ClassNotFoundException e) {
            throw new MigrationException(e);
        } catch (final SQLException e) {
            throw new MigrationException(e);
        }
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param dbProps
     * @return
     */
    static DataSource getOracleDatasource(final Properties dbProps) {
        try {
            // Load the JDBC driver
            final String driverName = "oracle.jdbc.driver.OracleDriver";
            Class.forName(driverName);

            // Create a connection to the database
            final String serverName =
                dbProps.getProperty("sourceDbServerName");
            final String portNumber =
                dbProps.getProperty("sourceDbPortNumber");
            final String sid = dbProps.getProperty("sourceDbSID");
            final String url =
                "jdbc:oracle:thin:@"
                + serverName + ":"
                + portNumber + ":"
                + sid;
            final String username = dbProps.getProperty("sourceDbUsername");
            final String password = dbProps.getProperty("sourceDbPassword");

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
}
