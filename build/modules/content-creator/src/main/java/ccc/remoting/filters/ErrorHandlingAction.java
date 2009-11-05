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
package ccc.remoting.filters;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ccc.remoting.actions.SessionKeys;
import ccc.rendering.AuthenticationRequiredException;
import ccc.rendering.NotFoundException;
import ccc.rendering.RedirectRequiredException;
import ccc.types.HttpStatusCode;


/**
 * A servlet action that catches and handles runtime exceptions.
 *
 * @author Civic Computing Ltd.
 */
public class ErrorHandlingAction
    implements
        Filter {
    private static final Logger LOG =
        Logger.getLogger(ErrorHandlingAction.class);

    // FIXME: Move to web.xml!
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
            final String relUri =
                req.getServletPath()
                + e.getResource().absolutePath().removeTop().toString();
            dispatchRedirect(req, resp, relUri);

        } catch (final AuthenticationRequiredException e) {
            final String relUri =
                _loginUri
                + "?tg="
                + e.getResource().absolutePath().removeTop().toString();
            dispatchRedirect(req, resp, relUri);

        } catch (final RuntimeException e) {
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
        final UUID errorId = UUID.randomUUID();

        if(!response.isCommitted()) {
            response.setStatus(HttpStatusCode.ERROR);
        }

        // Set the param's on the request.
        request.setAttribute(
            SessionKeys.EXCEPTION_CODE, errorId);
        request.setAttribute(
            "javax.servlet.error.request_uri", request.getRequestURI());
        request.setAttribute(
            "javax.servlet.error.exception_type", e.getClass());
        request.setAttribute(
            "javax.servlet.error.message", e.getMessage());
        request.setAttribute(
            "javax.servlet.error.exception", e);
        request.setAttribute(
            "javax.servlet.error.status_code",
            Integer.valueOf(HttpStatusCode.ERROR));

        LOG.warn(
            "Error executing servlet request."
            + "\n\t"+SessionKeys.EXCEPTION_CODE + " = " + errorId
            + "\n\tresponse.committed = " + response.isCommitted()
            + "\n\tjavax.servlet.error.status_code = "
                + request.getAttribute("javax.servlet.error.status_code")
            + "\n\tjavax.servlet.error.request_uri = "
                + request.getAttribute("javax.servlet.error.request_uri")
            + "\n\tjavax.servlet.error.servlet_name = "
                + request.getAttribute("javax.servlet.error.servlet_name")
            + "\n\tjavax.servlet.error.message = "
                + request.getAttribute("javax.servlet.error.message")
            + "\n\tjavax.servlet.error.exception_type = "
                + request.getAttribute("javax.servlet.error.exception_type"),
            e);

        if(!response.isCommitted()) {
            request.getRequestDispatcher(_errorPath).forward(request, response);
        }


    }


    /** {@inheritDoc} */
    @Override
    public void destroy() { /* NO OP */ }


    /** {@inheritDoc} */
    @Override
    public void init(final FilterConfig filterConfig) { /* NO OP */ }
}
