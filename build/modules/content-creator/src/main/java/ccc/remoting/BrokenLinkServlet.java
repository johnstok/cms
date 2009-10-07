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
import ccc.remoting.actions.FixLinkAction;
import ccc.remoting.actions.ReaderAction;
import ccc.remoting.actions.SerialAction;
import ccc.remoting.actions.ServletAction;



/**
 * A servlet to redirect old CCC6 URLs to the corresponding CCC7 url.
 * <br>Supports links in the following forms:
 * <br>- /2334.html
 * <br>- /files/my%20file.txt
 *
 * @author Civic Computing Ltd.
 */
public final class BrokenLinkServlet
    extends
        HttpServlet {


    /** {@inheritDoc} */
    @Override
    protected void doGet(final HttpServletRequest req,
                         final HttpServletResponse resp)
                                          throws ServletException, IOException {

        // FIXME: We need to add txn & jpa filters for this servlet
        final ServletAction action =
            new ErrorHandlingAction(
                    new SerialAction(
                        new ReaderAction(),
                        new FixLinkAction()),
                getServletContext(),
                "/content/login?tg=");

        action.execute(req, resp);
    }
}
