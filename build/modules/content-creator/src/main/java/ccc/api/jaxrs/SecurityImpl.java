/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */

package ccc.api.jaxrs;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.jboss.resteasy.annotations.cache.NoCache;
import org.jboss.web.tomcat.security.login.WebAuthentication;

import ccc.api.Security;
import ccc.api.types.Permission;
import ccc.commons.CCCProperties;
import ccc.plugins.s11n.json.JsonImpl;


/**
 * This class exposes parts of our public API using JAX-RS.
 *
 * @author Civic Computing Ltd.
 */
@Path("/public")
@Consumes("application/json")
@Produces("application/json")
@NoCache
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
        if (_request.isUserInRole(Permission.API_ACCESS)) {
            logSuccesfulLogin(
                username,
                _request.getRemoteAddr(),
                _request.getHeader("X-Forwarded-For"));
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
        props.put("application.context", getContextName());

        final JsonImpl ss = new JsonImpl();
        ss.set("properties", props);
        return ss.getDetail();
    }


    /**
     * Log a successful login attempt..
     *
     * @param username The username used to log in.
     * @param ip The IP address the user logged in from.
     * @param forwarded The IP addresses the request has been forwarded for.
     */
    public static void logSuccesfulLogin(final String username,
                                         final String ip,
                                         final String forwarded) {
        LOG.info(
            "Login OK for username "+username
            + " [Remote-Address="+ip
            + ", X-Forwarded-For="+forwarded+"]");
    }
}
