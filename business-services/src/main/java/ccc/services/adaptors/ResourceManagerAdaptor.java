/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */
package ccc.services.adaptors;

import ccc.domain.Resource;
import ccc.domain.ResourcePath;
import ccc.services.ResourceManager;


/**
 * An adaptor for the {@link ResourceManager} interface. Useful for testing.
 *
 * @author Civic Computing Ltd
 */
public abstract class ResourceManagerAdaptor implements ResourceManager {

    /**
     * @see ccc.services.ResourceManager#lookup(ccc.domain.ResourcePath)
     */
    @Override
    public Resource lookup(final ResourcePath absoulteURI) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /**
     * @see ccc.services.ResourceManager#createFolder(java.lang.String)
     */
    @Override
    public void createFolder(final String pathString) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /**
     * @see ccc.services.ResourceManager#createRoot()
     */
    @Override
    public void createRoot() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /**
     * @see ccc.services.ResourceManager#createContent(java.lang.String)
     */
    @Override
    public void createContent(String pathString) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

}
