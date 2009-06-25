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

import ccc.content.actions.AbstractServletAction;


/**
 * A servlet action that performs user login.
 *
 * @author Civic Computing Ltd.
 */
public class LogoutAction
    extends
        AbstractServletAction {


    /** {@inheritDoc} */
    @Override
    public void execute(final HttpServletRequest req,
                        final HttpServletResponse resp) throws IOException {
        req.getSession().invalidate();
        dispatchRedirect(req, resp, "/");
    }
}
