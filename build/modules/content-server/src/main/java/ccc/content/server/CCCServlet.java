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

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

import ccc.commons.DBC;
import ccc.commons.JNDI;
import ccc.commons.Registry;
import ccc.services.AssetManager;
import ccc.services.ContentManager;
import ccc.services.DataManager;


/**
 * Shared behaviour for CCC servlets.
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
    protected ContentManager contentManager() {
        return _registry.get("ContentManagerEJB/local");
    }

    /**
     * Accessor for the asset manager.
     *
     * @return A AssetManager.
     */
    protected AssetManager assetManager() {
        return _registry.get("AssetManagerEJB/local");
    }

    /**
     * Accessor for the data manager.
     *
     * @return A DataManager.
     */
    protected DataManager dataManager() {
        return _registry.get("DataManagerEJB/local");
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
        response.setHeader("Expires", "0");         // already expired
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

}