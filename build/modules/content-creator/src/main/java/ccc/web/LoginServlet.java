/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
package ccc.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ccc.plugins.PluginFactory;
import ccc.plugins.security.Sessions;
import ccc.web.jaxrs.SecurityImpl;


/**
 * A servlet to perform user authentication for the content-creator web app.
 *
 * @author Civic Computing Ltd.
 */
public class LoginServlet
    extends
        AbstractCCCServlet {

    // TODO: Move to web.xml.
    private final String _siteRoot = "/";
    private final String _loginPage = "/login.html";

    /** {@inheritDoc} */
    @Override
    protected void doPost(final HttpServletRequest req,
                          final HttpServletResponse resp) throws IOException {

        final String target   = getTarget(req);
        final String username = getUsername(req);
        final String password = getPassword(req);

        authenticate(req, resp, target, username, password);
    }


    private void authenticate(final HttpServletRequest req,
                              final HttpServletResponse resp,
                              final String target,
                              final String username,
                              final String password) throws IOException {
        req.getSession(true);
        final Sessions pwl = new PluginFactory().createSessions();
        if(pwl.login(username, password)) {
            SecurityImpl.logSuccesfulLogin(
                username,
                req.getRemoteAddr(),
                req.getHeader("X-Forwarded-For"));
            dispatchRedirect(req, resp, target);
        } else {
            dispatchRedirect(req, resp, _loginPage+"?tg="+target);
        }
    }


    private String getPassword(final HttpServletRequest req) {
        final String[] passwords = req.getParameterValues("pw");
        if (null==passwords || passwords.length<1) {
            return null;
        }
        return passwords[0];
    }


    private String getUsername(final HttpServletRequest req) {
        final String[] usernames = req.getParameterValues("un");
        if (null==usernames || usernames.length<1) {
            return null;
        }
        return usernames[0];
    }


    private String getTarget(final HttpServletRequest req) {
        String target = null;
        final String[] targets = req.getParameterValues("tg");
        if (null!=targets && targets.length>0) {
            target = targets[0];
        }
        if (null==target || target.trim().length()<1) {
            target = _siteRoot;
        }
        return target;
    }
}
