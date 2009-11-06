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

package ccc.rest.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.jboss.web.tomcat.security.login.WebAuthentication;

import ccc.commons.CCCProperties;
import ccc.rest.Security;
import ccc.serialization.JsonImpl;


/**
 * This class exposes parts of our public API using JAX-RS.
 *
 * @author Civic Computing Ltd.
 */
@Path("/public")
@Consumes("application/json")
@Produces("application/json")
public class SecurityImpl
    extends
        JaxrsCollection
    implements
        Security {

    private static final Logger LOG = Logger.getLogger(SecurityImpl.class);

    @Context private HttpServletRequest _request;


    /** {@inheritDoc} */
    @Override
    public Boolean isLoggedIn() {
        return Boolean.valueOf(null!=_request.getUserPrincipal());
    }


    /** {@inheritDoc} */
    @Override
    public Boolean login(final String username, final String password) {
        _request.getSession(true);
        final WebAuthentication pwl = new WebAuthentication();
        final boolean authenticated = pwl.login(username, password);

        // Credentials are bad.
        if (!authenticated) {
            return Boolean.FALSE;
        }

        // Has necessary roles.
        if (_request.isUserInRole("ADMINISTRATOR")
            || _request.isUserInRole("CONTENT_CREATOR")
            || _request.isUserInRole("SITE_BUILDER")) {
            logSuccesfulLogin(username, _request.getRemoteAddr());
            return Boolean.TRUE;
        }

        // Missing necessary roles.
        _request.getSession().invalidate();
        return Boolean.FALSE;
    }


    /** {@inheritDoc} */
    @Override
    public void logout() {
        _request.getSession().invalidate();
    }

    /** {@inheritDoc} */
    @Override
    public String readAllProperties() {
        final Map<String, String> props = new HashMap<String, String>();
        props.put("buildNumber", CCCProperties.buildNumber());
        props.put("ccc-version", CCCProperties.version());
        props.put("timestamp", CCCProperties.timestamp());
        props.put("application.name", getAppName());
        props.put("application.base-href", getBaseHref());

        final JsonImpl ss = new JsonImpl();
        ss.set("properties", props);
        return ss.getDetail();
    }


    /**
     * Log a successful login attempt..
     *
     * @param username The username used to log in.
     * @param ip The IP address the user logged in from.
     */
    public static void logSuccesfulLogin(final String username,
                                         final String ip) {
        LOG.info("Login OK for ip "+ip+" with username "+username);
    }
}
