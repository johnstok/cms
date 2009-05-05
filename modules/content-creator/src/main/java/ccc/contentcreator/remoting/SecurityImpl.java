/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.remoting;

import javax.servlet.http.HttpServletRequest;

import org.jboss.web.tomcat.security.login.WebAuthentication;

import ccc.commons.CCCProperties;
import ccc.contentcreator.api.SecurityService;


/**
 * J2EE / jBoss implementation of the {@link SecurityService} interface.
 *
 * @author Civic Computing Ltd.
 */
public class SecurityImpl
    extends CCCRemoteServiceServlet
    implements SecurityService {

    /** {@inheritDoc} */
    @Override
    public void logout() {
        getThreadLocalRequest().getSession().invalidate();
    }

    /** {@inheritDoc} */
    @Override
    public boolean login(final String username, final String password) {
        final HttpServletRequest request = getThreadLocalRequest();
        request.getSession(true);
        final WebAuthentication pwl = new WebAuthentication();
        final boolean authenticated = pwl.login(username, password);
        if (!authenticated) {
            return false;
        } else {
            if (request.isUserInRole("ADMINISTRATOR")
                || request.isUserInRole("CONTENT_CREATOR")
                || request.isUserInRole("SITE_BUILDER")) {
                return true;
            } else {
                request.getSession().invalidate();
                return false;
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean isLoggedIn() {
        final HttpServletRequest request = getThreadLocalRequest();
        return null!=request.getUserPrincipal();
    }

    /** {@inheritDoc} */
    @Override
    public String readProperty(final String key) {
        return CCCProperties.get(key);
    }
}
