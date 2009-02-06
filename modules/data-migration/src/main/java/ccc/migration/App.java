package ccc.migration;


import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag;
import javax.sql.DataSource;

import oracle.jdbc.pool.OracleDataSource;

import org.apache.log4j.Logger;

import ccc.commons.JNDI;
import ccc.services.api.Commands;
import ccc.services.api.Queries;
import ccc.services.api.ServiceNames;

/**
 * Entry class for the migration application.
 *
 */
public final class App {
    private static final long MILLISECS_PER_SEC = 1000;
    private static final long START_TIME = new Date().getTime();
    private static final Logger LOG = Logger.getLogger(App.class);

    private static LoginContext ctx;
    private static Properties props = new Properties();
    private static LegacyDBQueries legacyDBQueries;
    private static UUID unknownUser;


    private App() { /* NO-OP */ }


    /**
     * Entry point for this application.
     *
     * @param args String array of application arguments.
     */
    public static void main(final String[] args) {
        LOG.info("Starting.");

        Users.create("migration", "migration@civicuk.com", "migration");

        loadSettings();

        login("migration", "migration");

        connectToLegacySystem();

        performMigration();

        logout();

        reportFinish(START_TIME);
    }

    private static void connectToLegacySystem() {
        final DataSource legacyConnection = getLegacyConnection();
        legacyDBQueries = new LegacyDBQueries(new DbUtilsDB(legacyConnection));
        LOG.info("Connected to legacy DB.");
    }

    private static void performMigration() {
        final Migrations migrations =
            new Migrations(
                legacyDBQueries,
                props,
                new JNDI().<Commands>get(ServiceNames.PUBLIC_COMMANDS),
                new JNDI().<Queries>get(ServiceNames.PUBLIC_QUERIES));
        migrations.migrate();
    }

    private static void reportFinish(final long startTime) {
        final long elapsedTime = new Date().getTime() - startTime;
        LOG.info(
            "Migration finished in "
            + elapsedTime/MILLISECS_PER_SEC + " secs.");
    }

    private static void loadSettings() {
        try {
            final InputStream in =
                Thread.currentThread().
                getContextClassLoader().
                getResourceAsStream("migration.properties");
            props.load(in);
            in.close();
        } catch (final IOException e) {
            System.out.println("Properties reading failed");
        }
        LOG.info("Loaded settings.");
    }

    private static void login(final String theUsername,
                                     final String thePassword) {

        Configuration.setConfiguration(new Configuration() {

            @Override
            public AppConfigurationEntry[] getAppConfigurationEntry(
                                                            final String name) {
                final AppConfigurationEntry jBoss =
                    new AppConfigurationEntry(
                        "org.jboss.security.ClientLoginModule",
                        LoginModuleControlFlag.REQUIRED,
                        Collections.<String, Object> emptyMap());
                return new AppConfigurationEntry[] {jBoss};
            }
        });

        try {
            ctx =  new LoginContext(
                "ccc",
                new UserNamePasswordHandler(theUsername, thePassword));
            ctx.login();
         } catch (final LoginException e) {
            throw new java.lang.RuntimeException(e);
         }
         LOG.info("Logged in.");
    }

    private static void logout() {
        try {
            ctx.logout();
        } catch (final LoginException e) {
            throw new java.lang.RuntimeException(e);
        }
        LOG.info("Logged out.");
    }

    private static DataSource getLegacyConnection() {
        try {
            // Load the JDBC driver
            final String driverName = "oracle.jdbc.driver.OracleDriver";
            Class.forName(driverName);

            // Create a connection to the database
            final String serverName =
                props.getProperty("sourceDbServerName");
            final String portNumber =
                props.getProperty("sourceDbPortNumber");
            final String sid = props.getProperty("sourceDbSID");
            final String url =
                "jdbc:oracle:thin:@"
                + serverName + ":"
                + portNumber + ":"
                + sid;
            final String username = props.getProperty("sourceDbUsername");
            final String password = props.getProperty("sourceDbPassword");

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
