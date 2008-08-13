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

import java.util.Map;
import java.util.UUID;

import ccc.domain.Folder;
import ccc.domain.Page;
import ccc.domain.Resource;
import ccc.domain.ResourcePath;
import ccc.services.ContentManager;


/**
 * An adaptor for the {@link ContentManager} interface. Useful for testing.
 *
 * @author Civic Computing Ltd
 */
public abstract class ContentManagerAdaptor implements ContentManager {

    /**
     * {@inheritDoc}
     */
    @Override
    public void create(final UUID folderId, final Folder newFolder) {

        throw new UnsupportedOperationException("Method not implemented.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void create(final UUID folderId, final Page newPage) {

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
    public <T extends Resource> T lookup(final ResourcePath path) {

        throw new UnsupportedOperationException("Method not implemented.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Resource> T lookup(final UUID id) {

        throw new UnsupportedOperationException("Method not implemented.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Folder lookupRoot() {

        throw new UnsupportedOperationException("Method not implemented.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(final UUID id,
                       final String newTitle,
                       final Map<String, String> newParagraphs) {

        throw new UnsupportedOperationException("Method not implemented.");
    }

}
