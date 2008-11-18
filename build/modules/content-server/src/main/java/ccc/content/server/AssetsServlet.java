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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ccc.commons.Registry;
import ccc.domain.File;
import ccc.domain.ResourcePath;


/**
 * The AssetsServlet class serves resources from the 'assets' root. It can only
 * serve {@link File} resources. For a file resource it retrieves the
 * corresponding {@link Data} instance and writes the raw data as the body
 * of the HTTP response.
 *
 * @author Civic Computing Ltd.
 */
public class AssetsServlet extends CCCServlet {

    /**
     * Constructor.
     */
    public AssetsServlet() { super(); }

    /**
     * Constructor.
     *
     * @param registry The registry used by this class.
     */
    public AssetsServlet(final Registry registry) {
        super(registry);
    }

    /** {@inheritDoc} */
    @Override protected void doGet(final HttpServletRequest request,
                                   final HttpServletResponse response)
                            throws IOException {

        final ResourcePath path = new ResourcePath(request.getPathInfo());
        final File f = assetManager().lookup(path).as(File.class);

        writeFile(response, f);
    }
}
