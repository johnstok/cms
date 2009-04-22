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
package ccc.content.actions;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public interface ServletAction {
    /**
     * Perform the action.
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    void execute(final HttpServletRequest req,
                 final HttpServletResponse resp) throws ServletException,
                                                        IOException;
}
