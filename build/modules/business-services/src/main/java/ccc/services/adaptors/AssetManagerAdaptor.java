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

import java.util.UUID;

import ccc.domain.Resource;
import ccc.domain.Template;
import ccc.services.AssetManager;


/**
 * An adaptor for the {@link AssetManager} interface. Useful for testing.
 *
 * @author Civic Computing Ltd
 */
public abstract class AssetManagerAdaptor implements AssetManager {

    /**
     * {@inheritDoc}
     */
    @Override
    public void createDisplayTemplate(final Template template) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createRoot() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Resource> T lookup(final UUID id) {

        throw new UnsupportedOperationException("Method not implemented.");
    }


}
