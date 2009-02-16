package ccc.migration;


import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Date;
import java.util.Properties;

import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag;

import org.apache.log4j.Logger;

import ccc.commons.JNDI;
import ccc.services.api.Commands;
import ccc.services.api.Queries;
import ccc.services.api.ServiceNames;

/**
 * Entry class for the 'create' application.
 */
public final class Create {
    private static final long MILLISECS_PER_SEC = 1000;
    private static final long START_TIME = new Date().getTime();
    private static final Logger LOG = Logger.getLogger(Create.class);

    private static LoginContext ctx;
    private static Properties props = new Properties();

    private Create() { /* NO-OP */ }


    /**
     * Entry point for this application.
     *
     * @param args String array of application arguments.
     */
    public static void main(final String[] args) {
        LOG.info("Starting.");

        Users.create("super", "super@civicuk.com", "sup3r2008");

        loadSettings();

        login("super", "sup3r2008");

        createSchemaStructure();

        logout();

        reportFinish(START_TIME);
    }

    private static void createSchemaStructure() {
        final Migrations migrations =
            new Migrations(
                null,
                props,
                new JNDI().<Commands>get(ServiceNames.PUBLIC_COMMANDS),
                new JNDI().<Queries>get(ServiceNames.PUBLIC_QUERIES));
        migrations.createDefaultFolderStructure();
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
}
