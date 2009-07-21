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
package ccc.contentcreator.remoting;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ccc.content.actions.ErrorHandlingAction;
import ccc.content.actions.ServletAction;


/**
 * A servlet to perform user authentication for the content-creator web app.
 *
 * @author Civic Computing Ltd.
 */
public class LogoutServlet
    extends
     HttpServlet {

    /** {@inheritDoc} */
    @Override
    protected void doGet(final HttpServletRequest req,
                         final HttpServletResponse resp)
                                          throws ServletException, IOException {
        final ServletAction action =
            new ErrorHandlingAction(new LogoutAction(), getServletContext());
        action.execute(req, resp);
    }
}
