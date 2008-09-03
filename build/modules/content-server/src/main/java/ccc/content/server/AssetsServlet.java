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

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ccc.commons.JNDI;
import ccc.commons.Registry;
import ccc.domain.File;
import ccc.domain.ResourcePath;
import ccc.services.AssetManager;
import ccc.services.DataManager;


/**
 * The AssetsServlet class serves resources from the 'assets' root. It can only
 * serve {@link File} resources. For a file resource it retrieves the
 * corresponding {@link Data} instance and writes the raw data as the body
 * of the HTTP response.
 *
 * @author Civic Computing Ltd.
 */
public class AssetsServlet extends HttpServlet {

    /** serialVersionUID : long. */
    private static final long serialVersionUID = -5337311771335251212L;

    /** _registry : Registry. */
    private Registry _registry = new JNDI();

    /**
     * Constructor.
     */
    public AssetsServlet() { /* NO-OP */ }

    /**
     * Constructor.
     *
     * @param registry The registry used by this class.
     */
    public AssetsServlet(final Registry registry) {
        _registry = registry;
    }

    /** {@inheritDoc}
     * @throws IOException */
    @Override protected void doGet(final HttpServletRequest req,
                                   final HttpServletResponse resp)
                            throws IOException {
        final ResourcePath path = new ResourcePath(req.getPathInfo());
        final AssetManager am = _registry.get("AssetManagerEJB/local");
        final File f = am.lookup(path).as(File.class);
        final ServletOutputStream os = resp.getOutputStream();
        final DataManager dm = _registry.get("DataManagerEJB/local");
        dm.retrieve(f.fileData(), os);
    }


}
