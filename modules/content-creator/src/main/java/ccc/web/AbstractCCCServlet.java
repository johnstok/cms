/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;



/**
 * Abstract servlet action providing helper methods.
 *
 * @author Civic Computing Ltd.
 */
public abstract class AbstractCCCServlet
    extends
        HttpServlet {
    private static final Logger LOG =
        Logger.getLogger(AbstractCCCServlet.class);


    /**
     * Dispatch to the 'not found' URI.
     *
     * @param request The request.
     * @param response The response.
     * @throws ServletException From servlet API.
     * @throws IOException From servlet API.
     */
    protected void dispatchNotFound(final HttpServletRequest request,
                                    final HttpServletResponse response)
                                          throws ServletException, IOException {
        LOG.info("Forwarding to /notfound for: " + requestPath(request));
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


    /**
     * Send a redirect to the client.
     *
     * @param request The incoming request.
     * @param response The outgoing response.
     * @param relUri The relative URI to redirect to.
     * @throws IOException Servlet API can throw an {@link IOException}.
     */
    protected void dispatchRedirect(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final String relUri) throws IOException {
        final String target = request.getContextPath()+relUri;
        LOG.info("Redirecting to " + target + " from " + requestPath(request));
        response.sendRedirect(target);
    }


    private String requestPath(final HttpServletRequest request) {
        final String context = request.getContextPath();
        final String servlet = request.getServletPath();
        final String info    = request.getPathInfo();
        final String query   = request.getQueryString();
        return
            context
            + servlet
            + ((null==info) ? "" : info)
            + ((null==query) ? "" : query);
    }


    /**
     * Retrieves the exception that this servlet should report.
     * Guarantees to return an exception and never NULL.
     *
     * @param request The request that the exception will be retrieved from.
     * @return The exception that should be reported.
     */
    protected Exception getException(final HttpServletRequest request) {
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
