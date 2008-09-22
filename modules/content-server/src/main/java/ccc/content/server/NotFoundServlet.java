/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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


/**
 * HTTP Servlet for handling 404 errors.
 *
 * @author Civic Computing Ltd.
 */
public final class NotFoundServlet extends HttpServlet {


    /**
     * {@inheritDoc}
     */
    @Override
    protected void doGet(final HttpServletRequest request,
                         final HttpServletResponse response)
                  throws ServletException, IOException {

        response.reset();
        // disable caching
        // set mime type
        // set char encoding
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        response.getWriter().write("<H1>Narp</H1>"); //TODO: Change.
    }

    /**
     * Retrieves the exception that this servlet should report.
     * Guarantees to return an exception and never NULL.
     * TODO: Should return Throwable?
     *
     * @param request The request that the exception will be retrieved from.
     * @return The exception that should be reported.
     */
    Exception getException(final HttpServletRequest request) {
        final Object o = request.getAttribute(SessionKeys.EXCEPTION_KEY);
        if (null==o) {
            return new RuntimeException(
                "No exception was found at the expected location: "
                +SessionKeys.EXCEPTION_KEY);
        } else if (!(o instanceof Exception)) {
            return new RuntimeException(
                "Object at location: "
                +SessionKeys.EXCEPTION_KEY
                +" was not an exception.");
        } else {
            return Exception.class.cast(o);
        }
    }
}
