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
package ccc.content.server;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.web.tomcat.security.login.WebAuthentication;

import ccc.content.actions.AbstractServletAction;
import ccc.domain.CCCException;


/**
 * A servlet action that performs user login.
 *
 * @author Civic Computing Ltd.
 */
public class LoginAction
    extends
        AbstractServletAction {

    private final String _siteRoot;
    private final String _loginPage;

    /**
     * Constructor.
     *
     * @param root The root folder for the site.
     * @param page The absolute URL of the login page.
     */
    public LoginAction(final String root, final String page) {
        _siteRoot = root;
        _loginPage = page;
    }


    /** {@inheritDoc} */
    @Override
    public void execute(final HttpServletRequest req,
                        final HttpServletResponse resp) throws IOException {
        String target = null;
        final String[] targets = req.getParameterValues("tg");
        if (null!=targets && 1==targets.length) {
            target = targets[0];
        }
        if (null==target || target.trim().length()<1) {
            target = _siteRoot;
        }

        final String[] usernames = req.getParameterValues("un");
        if (null==usernames || 1!=usernames.length) {
            throw new CCCException("Bad username.");
        }
        final String[] passwords = req.getParameterValues("pw");
        if (null == passwords || 1!=passwords.length) {
            throw new CCCException("Bad username.");
        }

        final String username = usernames[0];
        final String password = passwords[0];

        req.getSession(true);
        final WebAuthentication pwl = new WebAuthentication();
        if(pwl.login(username, password)) {
            dispatchRedirect(
                req, resp, target);
        } else {
            dispatchRedirect(
                req, resp, _loginPage+"?tg="+target);
        }
    }

}
