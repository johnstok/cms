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

import ccc.remoting.actions.AbstractCCCServlet;


/**
 * A servlet to perform user authentication for the content-creator web app.
 *
 * @author Civic Computing Ltd.
 */
public class LogoutServlet
    extends
    AbstractCCCServlet{

    /** {@inheritDoc} */
    @Override
    protected void doGet(final HttpServletRequest req,
                         final HttpServletResponse resp) throws IOException {
        req.getSession().invalidate();
        dispatchRedirect(req, resp, "/");
    }
}
