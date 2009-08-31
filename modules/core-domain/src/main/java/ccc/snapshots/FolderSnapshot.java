/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.snapshots;

import java.util.List;
import java.util.Map;

import ccc.domain.Folder;
import ccc.domain.Page;
import ccc.domain.Resource;
import ccc.entities.IFolder;
import ccc.entities.IResource;
import ccc.persistence.DataManager;
import ccc.rendering.NotFoundException;
import ccc.rendering.RedirectRequiredException;
import ccc.rendering.Response;
import ccc.rendering.StatefulReader;
import ccc.search.SearchEngine;
import ccc.types.ResourceType;


/**
 * A read-only view of a folder.
 *
 * @author Civic Computing Ltd.
 */
public class FolderSnapshot extends ResourceSnapshot implements IFolder {

    private final Folder _delegate;


    /**
     * Constructor.
     *
     * @param f The Folder to wrap.
     */
    public FolderSnapshot(final Folder f) {
        super(f);
        _delegate = f;
    }


    /** {@inheritDoc} */
    @Override
    public List<Resource> entries() {
        return _delegate.entries(); // FIXME: wrap entries in snapshots.
    }


    /** {@inheritDoc} */
    @Override
    public List<? extends IResource> entries(final int count,
                                             final int page,
                                             final String sortOrder) {
        return _delegate.entries(count, page, sortOrder); // FIXME: wrap entries in snapshots.
    }

    /** {@inheritDoc} */
    @Override
    public Response render(final Map<String, String[]> parameters,
                           final SearchEngine search,
                           final StatefulReader reader,
                           final DataManager dm) {
        if (indexPage() != null) {
            throw new RedirectRequiredException(indexPage());
        }

        for (final Resource r : entries()) {
            if (ResourceType.PAGE.equals(r.type())
                && r.isPublished()) {
                throw new RedirectRequiredException(r);
            }
        }
        throw new NotFoundException();
    }


    private Page indexPage() {
        return _delegate.indexPage();
    }
}
