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
import ccc.services.AssetManagerLocal;
import ccc.services.ContentManagerLocal;
import ccc.services.DataManagerLocal;


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
    protected ContentManagerLocal contentManager() {
        return _registry.get("ContentManager/local");
    }

    /**
     * Accessor for the asset manager.
     *
     * @return A AssetManager.
     */
    protected AssetManagerLocal assetManager() {
        return _registry.get("AssetManager/local");
    }

    /**
     * Accessor for the data manager.
     *
     * @return A DataManager.
     */
    protected DataManagerLocal dataManager() {
        return _registry.get("DataManager/local");
    }

    /**
     * Disable caching for the response.
     *
     * @param response The response that should not be cached.
     */
    protected void disableCachingFor(final HttpServletResponse response) {
        response.setHeader("Pragma", "no-cache");   // non-spec, but supported
        response.setHeader(
            "Cache-Control",
            "private, must-revalidate, max-age=0"); // equivalent to 'no-cache'
        response.setHeader("Expires", "0");         // TODO: already expired, set to 'now' instead...
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
     * TODO: Add a description of this method.
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void dispatchNotFound(final HttpServletRequest request,
                                    final HttpServletResponse response)
                                          throws ServletException, IOException {
        request
            .getRequestDispatcher("/notfound")
            .forward(request, response);
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param request
     * @param response
     * @param e
     * @throws ServletException
     * @throws IOException
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

}
