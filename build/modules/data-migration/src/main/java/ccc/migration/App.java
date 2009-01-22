package ccc.migration;


import java.io.IOException;
import java.io.InputStream;
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
    /** properties : Properties. */
    private static Properties props = new Properties();


    private App() { /* NO-OP */ }

    /**
     * Entry point for this application.
     *
     * @param args String array of application arguments.
     */
    public static void main(final String[] args) {
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

        final long startTime = new Date().getTime();

        // Establish a queries instance to communicate with the legacy DB.
        final Connection legacyConnection = getLegacyConnection();
        final Connection newConnection = getConnection();

        final NewDBQueries queries = new NewDBQueries(newConnection);
        final UUID muid = queries.insertMigrationUser();

        authenticate("migration", "migration");

        final LegacyDBQueries legacyDBQueries =
            new LegacyDBQueries(legacyConnection);

        final Migrations migrations =
            new Migrations(legacyDBQueries, props);

        System.out.println(migrations.queries().loggedInUser()._email);

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
            _username = theUsername;
          _password = thePassword;
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
