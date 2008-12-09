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

import ccc.commons.DBC;
import ccc.commons.JNDI;
import ccc.commons.Registry;
import ccc.services.DataManagerLocal;
import ccc.services.ServiceNames;
import ccc.services.StatefulReader;


/**
 * Shared behaviour for CCC servlets.
 * TODO: Consider adding a UUID to each request?
 *
 * @author Civic Computing Ltd.
 */
public abstract class CCCServlet extends HttpServlet {

    private Registry _registry = new JNDI();

    /**
     * Constructor.
     *
     * @param registry The registry for this servlet.
     */
    public CCCServlet(final Registry registry) {
        DBC.require().notNull(registry);
        _registry = registry;
    }

    /**
     * Constructor.
     */
    public CCCServlet() { super(); }

    /**
     * Accessor for the content manager.
     *
     * @return A ContentManager.
     */
    protected StatefulReader resourceReader() {
        return _registry.get(ServiceNames.STATEFUL_READER);
    }

    /**
     * Accessor for the data manager.
     *
     * @return A DataManager.
     */
    protected DataManagerLocal dataManager() {
        return _registry.get(ServiceNames.DATA_MANAGER_LOCAL);
    }

    /**
     * Disable caching for the response.
     * TODO: already expired, set to 'now' instead...
     *
     * @param response The response that should not be cached.
     */
    protected void disableCachingFor(final HttpServletResponse response) {
        response.setHeader("Pragma", "no-cache");   // non-spec, but supported
        response.setHeader(
            "Cache-Control",
            "private, must-revalidate, max-age=0"); // equivalent to 'no-cache'
        response.setHeader("Expires", "0");
    }

    /**
     * Set the character encoding for a response.
     *
     * @param response The response for which we'll set the char encoding.
     *
     */
    protected void configureCharacterEncoding(
                                          final HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
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
        request
            .getRequestDispatcher("/notfound")
            .forward(request, response);
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
                                 final RuntimeException e)
                                          throws ServletException, IOException {

        request.setAttribute(SessionKeys.EXCEPTION_KEY, e);
        request
            .getRequestDispatcher("/error")
            .forward(request, response);
    }

    /**
     * Process a GET request.
     *
     * This method provides error handling for both committed and uncommitted
     * responses. It delegates to
     * {@link #doSafeGet(HttpServletRequest, HttpServletResponse)}
     * for implementation of business logic.
     *
     * {@inheritDoc}
     */
    @Override
    protected void doGet(final HttpServletRequest request,
                         final HttpServletResponse response)
        throws ServletException, IOException {

        try {
            doSafeGet(request, response);

        } catch (final RuntimeException e) {
            if(response.isCommitted()) {
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
                dispatchError(request, response, e);
            }
        }
    }

    /**
     * Process a GET request. Runtime exceptions can safely be thrown from this
     * method and will be handled be the
     * {@link #doGet(HttpServletRequest, HttpServletResponse)} method.
     *
     * @param request The request.
     * @param response The response.
     * @throws IOException From servlet API.
     * @throws ServletException  From servlet API.
     */
    protected abstract void doSafeGet(HttpServletRequest request,
                                      HttpServletResponse response)
        throws ServletException, IOException;
}
