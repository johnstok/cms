/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.cli;

import java.util.Collections;

import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag;

import org.apache.log4j.Logger;

import ccc.migration.UserNamePasswordHandler;


/**
 * Base class for legacy applications that use RMI.
 *
 * @author Civic Computing Ltd.
 */
public class LegacyApp
    extends
        CccApp {
    private static final Logger LOG = Logger.getLogger(LegacyApp.class);

    private static LoginContext ctx;


    /**
     * Logout from the server.
     */
    static void logout() {
        try {
            ctx.logout();
        } catch (final LoginException e) {
            throw new RuntimeException(e);
        }
        LOG.info("Logged out.");
    }


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
            throw new RuntimeException(e);
        }
        LOG.info("Logged in.");
    }
}
