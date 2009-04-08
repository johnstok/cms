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

import java.util.Collections;
import java.util.Date;

import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag;

import org.apache.log4j.Logger;

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
            throw new java.lang.RuntimeException(e);
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
            throw new java.lang.RuntimeException(e);
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
}
