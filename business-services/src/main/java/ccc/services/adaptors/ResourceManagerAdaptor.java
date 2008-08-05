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

import ccc.domain.Paragraph;
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
     * {@inheritDoc}
     */
    @Override
    public void saveContent(final String id,
                            final String title,
                        final Map<String, String> paragraphs) {

        throw new UnsupportedOperationException("Method not implemented.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Resource lookup(final UUID id) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Resource lookup(final ResourcePath absoulteURI) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createFolder(final String pathString) {
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
    public void createContent(final String pathString) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createParagraphsForContent(
                                   final String patString,
                                   final Map<String, Paragraph> paragraphs) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

}
