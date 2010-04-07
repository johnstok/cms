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
package ccc.web.filters;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ccc.types.HttpStatusCode;
import ccc.web.SessionKeys;
import ccc.web.rendering.AuthenticationRequiredException;
import ccc.web.rendering.NotFoundException;
import ccc.web.rendering.RedirectRequiredException;


/**
 * A servlet action that catches and handles runtime exceptions.
 *
 * @author Civic Computing Ltd.
 */
public class ErrorHandlingFilter
    implements
        Filter {
    private static final Logger LOG =
        Logger.getLogger(ErrorHandlingFilter.class);

    // TODO: Move to web.xml.
    private final String _loginUri = "/login.html";
    private final String _errorPath = "/error.html";


    /** {@inheritDoc} */
    @Override
    public void doFilter(final ServletRequest request,
                         final ServletResponse response,
                         final FilterChain chain)
                                        throws IOException, ServletException {

        final HttpServletResponse resp = (HttpServletResponse) response;
        final HttpServletRequest  req  = (HttpServletRequest)  request;

        try {
            chain.doFilter(req, resp);

        } catch (final NotFoundException e) {
            dispatchNotFound(req, resp);

        } catch (final RedirectRequiredException e) {
            final String relUri = req.getServletPath() + e.getTarget();
            dispatchRedirect(req, resp, relUri);

        } catch (final AuthenticationRequiredException e) {
            final String relUri = _loginUri + "?tg=" + e.getTarget();
            dispatchRedirect(req, resp, relUri);

        } catch (final RuntimeException e) {
            dispatchError(req, resp, e);

        } catch (final IOException e) {
            dispatchError(req, resp, e);

        } catch (final ServletException e) {
            dispatchError(req, resp, e);
        }
    }


    /**
     * Dispatch to the 'not found' URI.
     *
     * @param request The request.
     * @param response The response.
     * @throws IOException From servlet API.
     */
    protected void dispatchNotFound(final HttpServletRequest request,
                                    final HttpServletResponse response)
                                                        throws IOException {
        LOG.info(
            "Sending 404 'not found' for path: "
            + request.getContextPath()
            + request.getServletPath()
            + request.getPathInfo());
        response.sendError(HttpStatusCode.NOT_FOUND);
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
        LOG.info(
            "Redirecting to "
            + target
            + " from "
            + request.getContextPath()
            + request.getServletPath()
            + request.getPathInfo());
        response.sendRedirect(target);
    }


    /**
     * Dispatch to the error handler.
     * <p>If there is no handling strategy the exception will be re-thrown.
     *
     * @param request The request.
     * @param response The response.
     * @param requestEx The exception we encountered.
     * @param <T> The type of exception encountered.
     */
    protected <T extends Exception> void dispatchError(
                                             final HttpServletRequest request,
                                             final HttpServletResponse response,
                                             final T requestEx) {

        final UUID errorId = UUID.randomUUID();
        request.setAttribute(SessionKeys.EXCEPTION_CODE, errorId.toString());

        if (clientAborted(requestEx)) {
            LOG.warn("Ignoring ClientAbortException.");
            return;

        } else if(response.isCommitted()) {
            try {
                LOG.warn("Error after response committed: "+errorId, requestEx);
                final ServletOutputStream os = response.getOutputStream();
                os.println("An error occurred: "+errorId);
            } catch (final Exception e1) {
                LOG.warn("Failed writing error code to request: "+errorId, e1);
            }

        } else {
            /* Let the container forward to the error handler in web.xml.
             * The exception will be logged by the container.             */
            throw new RuntimeException(
                "Unhandled servlet error: "+errorId, requestEx);

        }
    }


    private boolean clientAborted(final Throwable t) {
        if (t.getClass().getName().endsWith("ClientAbortException")) {
            return true;
        } else if (null!=t.getCause()) {
            return clientAborted(t.getCause());
        } else {
            return false;
        }
    }


    /** {@inheritDoc} */
    @Override
    public void destroy() { /* NO OP */ }


    /** {@inheritDoc} */
    @Override
    public void init(final FilterConfig filterConfig) { /* NO OP */ }
}
