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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ccc.content.actions.ErrorHandlingAction;
import ccc.content.actions.ServletAction;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class LoginServlet
    extends
     HttpServlet {

    // TODO: move to web.xml
    private final String _siteRoot = "/content";
    private final String _loginPage = "/content/login";

    /** {@inheritDoc} */
    @Override
    protected void doPost(final HttpServletRequest req,
                          final HttpServletResponse resp)
                                          throws ServletException, IOException {
        final ServletAction action =
            new ErrorHandlingAction(
                new LoginAction(_siteRoot, _loginPage),
                getServletContext()
            );

        action.execute(req, resp);
    }
}
