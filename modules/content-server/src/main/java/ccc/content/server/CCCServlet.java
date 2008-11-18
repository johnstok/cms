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
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ccc.commons.DBC;
import ccc.commons.JNDI;
import ccc.commons.Registry;
import ccc.domain.File;
import ccc.services.AssetManagerLocal;
import ccc.services.ContentManagerLocal;
import ccc.services.DataManagerLocal;
import ccc.services.ServiceNames;


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
        return _registry.get(ServiceNames.CONTENT_MANAGER_LOCAL);
    }

    /**
     * Accessor for the asset manager.
     *
     * @return A AssetManager.
     */
    protected AssetManagerLocal assetManager() {
        return _registry.get(ServiceNames.ASSET_MANAGER_LOCAL);
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

    protected void writeFile(final HttpServletResponse response, final File f) throws IOException {
    
        disableCachingFor(response);
    
        response.setHeader(
            "Content-Disposition",
            "inline; filename=\""+f.name()+"\"");
        response.setHeader(
            "Content-Type",
            f.mimeType().toString());
        response.setHeader(
            "Content-Description",
            f.description());
        response.setHeader(
            "Content-Length",
            String.valueOf(f.size()));
    
        final ServletOutputStream os = response.getOutputStream();
        dataManager().retrieve(f.fileData(), os);
    }

}
