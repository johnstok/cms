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
package ccc.remoting;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.web.tomcat.security.login.WebAuthentication;

import ccc.remoting.actions.AbstractCCCServlet;
import ccc.rest.impl.SecurityImpl;


/**
 * A servlet to perform user authentication for the content-creator web app.
 *
 * @author Civic Computing Ltd.
 */
public class LoginServlet
    extends
        AbstractCCCServlet {

    // FIXME: Move to web.xml.
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
        final WebAuthentication pwl = new WebAuthentication();
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
