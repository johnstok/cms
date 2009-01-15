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
 * Shared behaviour for CCC servlets.
 *
 * @author Civic Computing Ltd.
 */
public abstract class CCCServlet extends HttpServlet {

    /**
     * Disable caching for the response.
     *
     * @param response The response that should not be cached.
     */
    protected void disableCaching(final HttpServletResponse response) {
        response.setHeader(// non-spec, but supported by some browsers
            "Pragma",
            "no-cache");
        response.setHeader(// equivalent to 'no-cache'
            "Cache-Control",
            "private, must-revalidate, max-age=0");
        response.setHeader(// TODO: Replace with epoch?
            "Expires",
            "0");
    }

    /**
     * Dispatch to the 'not found' handler.
     *
     * @param request The request.
     * @param response The response.
     * @throws ServletException From servlet API.
     * @throws IOException From servlet API.
     */
    protected void dispatchNotFound(final HttpServletRequest request,
                                    final HttpServletResponse response)
                                          throws ServletException, IOException {
        request.getRequestDispatcher("/notfound").forward(request, response);
    }

    /**
     * Dispatch to the error handler.
     *
     * @param request The request.
     * @param response The response.
     * @param e The exception we encountered
     * @throws ServletException From servlet API.
     * @throws IOException From servlet API.
     */
    protected void dispatchError(final HttpServletRequest request,
                                 final HttpServletResponse response,
                                 final Exception e)
                                          throws ServletException, IOException {

        request.setAttribute(SessionKeys.EXCEPTION_KEY, e);
        request.getRequestDispatcher("/error").forward(request, response);
    }

    /** {@inheritDoc} */
    @Override
    protected void service(final HttpServletRequest req,
                           final HttpServletResponse resp)
                                          throws ServletException, IOException {
        try {
            super.service(req, resp);
        } catch (final RuntimeException e) {
            if(resp.isCommitted()) {
                /*
                 * Nothing we can do to rescue the response - the HTTP response
                 * code + headers has already been sent. Just log the error on
                 * the server.
                 */
                getServletContext().log(
                    "Error caught after response was committed.",
                    e);

            } else {
                getServletContext().log(
                    "Error caught on uncommited response"
                    + " - sending error message.",
                    e);
                dispatchError(req, resp, e);
            }
        }
    }
}
