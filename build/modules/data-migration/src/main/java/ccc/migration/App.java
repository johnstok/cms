package ccc.migration;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag;

import oracle.jdbc.pool.OracleDataSource;

import org.apache.commons.dbutils.DbUtils;

/**
 * Entry class for the migration application.
 *
 */
public final class App {

    private static final long MILLISECS_PER_SEC = 1000;
    private static LoginContext ctx;

    private App() { /* NO-OP */ }

    /**
     * Entry point for this application.
     *
     * @param args String array of application arguments.
     */
    public static void main(final String[] args) {
        final long startTime = new Date().getTime();

        // Establish a queries instance to communicate with the legacy DB.
        final Connection legacyConnection = getLegacyConnection();
        final Connection newConnection = getConnection();

        final NewDBQueries queries = new NewDBQueries(newConnection);
        final UUID muid = queries.insertMigrationUser();

        authenticate("migration", "migration");


        final LegacyDBQueries legacyDBQueries =
            new LegacyDBQueries(legacyConnection);
        // Migrations migrations = consoleMigrations();
        final Migrations migrations =
            new Migrations(legacyDBQueries);

        System.out.println(migrations.userManager().loggedInUser().email());

        migrations.migrate();
        DbUtils.closeQuietly(legacyConnection);

        logout();
        queries.changeMigrationUserPw(muid);
        DbUtils.closeQuietly(newConnection);

        final long elapsedTime = new Date().getTime() - startTime;
        System.out.println(
            "Migration finished in "
            + elapsedTime/MILLISECS_PER_SEC
            + " secs");
    }

    /**
     * TODO: Add a description of this method.
     *
     */
    private static void authenticate(final String theUsername,
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
                "quest",
                new UserNamePasswordHandler(theUsername, thePassword));
            ctx.login();
         } catch (final LoginException e) {
            throw new java.lang.RuntimeException(e);
         }
    }

    /**
     * TODO: Add a description of this method.
     *
     */
    private static void logout() {

        try {
            ctx.logout();
        } catch (final LoginException e) {
            throw new java.lang.RuntimeException(e);
        }
    }



    private static Connection getLegacyConnection() {
        Connection connection = null;

        // TODO 24 Jul 2008 petteri: Read from properties
        try {
            // Load the JDBC driver
            final String driverName = "oracle.jdbc.driver.OracleDriver";
            Class.forName(driverName);

            // Create a connection to the database
            final String serverName = "poseidon";
            final String portNumber = "1521";
            final String sid = "DEV";
            final String url =
                "jdbc:oracle:thin:@"
                + serverName + ":"
                + portNumber + ":"
                + sid;
            final String username = "ccc_migration";
            final String password = "d3ccc_migration";

            final OracleDataSource ods = new OracleDataSource();
            final Properties props = new Properties();
            props.put("user", username);
            props.put("password", password);
            props.put(
                "oracle.jdbc.FreeMemoryOnEnterImplicitCache", Boolean.TRUE);
            ods.setConnectionProperties(props);
            ods.setURL(url);
            connection = ods.getConnection();

        } catch (final ClassNotFoundException e) {
            throw new MigrationException(e);
        } catch (final SQLException e) {
            throw new MigrationException(e);
        }
        return connection;
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


    /**
     * Basic {@link CallbackHandler} implementation
     * that supports a username and a password.
     */
    private static class UserNamePasswordHandler implements CallbackHandler {

       private final String _username;
       private final String _password;

        /**
         * Constructor.
         *
         * @param theUsername
         * @param thePassword
         */
        UserNamePasswordHandler(final String theUsername,
            final String thePassword) {
            this._username = theUsername;
          this._password = thePassword;
        }


       /**
        * @see javax.security.auth.callback.CallbackHandler
        * #handle(javax.security.auth.callback.Callback[])
        */
       public void handle(final Callback[] callbacks)
       throws UnsupportedCallbackException {
            for(final Callback theCallback : callbacks){
                if (theCallback instanceof NameCallback) {
                    ((NameCallback) theCallback).setName(_username);
                } else if (theCallback instanceof PasswordCallback) {
                    ((PasswordCallback) theCallback).setPassword(
                        _password.toCharArray());
                }else {
                    throw new UnsupportedCallbackException(theCallback);
                }
            }
        }
    }

}
