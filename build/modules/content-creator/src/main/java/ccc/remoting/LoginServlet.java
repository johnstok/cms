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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ccc.remoting.actions.ErrorHandlingAction;
import ccc.remoting.actions.LoginAction;
import ccc.remoting.actions.ServletAction;


/**
 * A servlet to perform user authentication for the content-creator web app.
 *
 * @author Civic Computing Ltd.
 */
public class LoginServlet
    extends
     HttpServlet {

    // Move to web.xml?
    private final String _siteRoot = "/";
    private final String _loginPage = "/login.html";

    /** {@inheritDoc} */
    @Override
    protected void doPost(final HttpServletRequest req,
                          final HttpServletResponse resp)
                                          throws ServletException, IOException {
        final ServletAction action =
            new ErrorHandlingAction(
                new LoginAction(_siteRoot, _loginPage),
                getServletContext(),
                _loginPage+"?tg="
            );

        action.execute(req, resp);
    }
}
