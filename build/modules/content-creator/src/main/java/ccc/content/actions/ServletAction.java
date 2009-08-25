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
 * An action perform during the execution of a servlet request.
 *
 * @author Civic Computing Ltd.
 */
public interface ServletAction {

    /**
     * Perform the action.
     *
     * @param req The servlet request.
     * @param resp The servlet response.
     *
     * @throws ServletException If a servlet error occurs.
     * @throws IOException If an error occurs reading from the request or
     *  writing to the response.
     */
    void execute(final HttpServletRequest req,
                 final HttpServletResponse resp) throws ServletException,
                                                        IOException;
}
