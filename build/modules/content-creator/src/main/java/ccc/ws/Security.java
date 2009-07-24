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

package ccc.ws;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.jboss.web.tomcat.security.login.WebAuthentication;

import ccc.commons.CCCProperties;


/**
 * This class exposes parts of our public API using JAX-RS.
 *
 * @author Civic Computing Ltd.
 */
@Path("/public")
@Consumes("application/json")
@Produces("application/json")
public class Security
    implements SecurityAPI {

    @Context private HttpServletRequest _request;


    /** {@inheritDoc} */
    @Override
    public Boolean isLoggedIn() {
        return null!=_request.getUserPrincipal();
    }


    /** {@inheritDoc} */
    @Override
    public Boolean login(final String username, final String password) {
        _request.getSession(true);
        final WebAuthentication pwl = new WebAuthentication();
        final boolean authenticated = pwl.login(username, password);

        // Credentials are bad.
        if (!authenticated) {
            return false;
        }

        // Has necessary roles.
        if (_request.isUserInRole("ADMINISTRATOR")
            || _request.isUserInRole("CONTENT_CREATOR")
            || _request.isUserInRole("SITE_BUILDER")) {
            return true;
        }

        // Missing necessary roles.
        _request.getSession().invalidate();
        return false;
    }


    /** {@inheritDoc} */
    @Override
    public void logout() {
        _request.getSession().invalidate();
    }


    /** {@inheritDoc} */
    @Override
    public String readProperty(final String key) {
        return CCCProperties.get(key);
    }
}
