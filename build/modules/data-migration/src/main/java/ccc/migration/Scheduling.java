package ccc.migration;


import java.util.Collections;
import java.util.Date;

import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag;

import org.apache.log4j.Logger;

import ccc.commons.JNDI;
import ccc.services.api.Scheduler;
import ccc.services.api.ServiceNames;

/**
 * Entry class for the migration application.
 *
 */
public final class Scheduling {
    private static final long MILLISECS_PER_SEC = 1000;
    private static final long START_TIME = new Date().getTime();
    private static final Logger LOG = Logger.getLogger(Scheduling.class);

    private static LoginContext ctx;

    private Scheduling() { /* NO-OP */ }


    /**
     * Entry point for this application.
     *
     * @param args String array of application arguments.
     */
    public static void main(final String[] args) {
        LOG.info("Starting.");

        login("super", "sup3r2008");

        final Scheduler s =
            new JNDI().<Scheduler>get(ServiceNames.PUBLIC_SCHEDULER);
        s.stop();

        logout();

        reportFinish(START_TIME);
    }

    private static void reportFinish(final long startTime) {
        final long elapsedTime = new Date().getTime() - startTime;
        LOG.info(
            "Migration finished in "
            + elapsedTime/MILLISECS_PER_SEC + " secs.");
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
