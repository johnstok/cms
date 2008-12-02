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
package ccc.services.ejb3.remote;

import java.util.ArrayList;
import java.util.Collection;

import ccc.domain.Folder;
import ccc.domain.Resource;
import ccc.services.api.ResourceSummary;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class ModelTranslation {

    /**
     * Map a collection of Folder to a collection of FolderSummary.
     *
     * @param folders The collection of folders to map.
     * @return The corresponding collection of FolderSummary.
     */
    protected Collection<ResourceSummary> mapFolders(final Collection<Folder> folders) {
        final Collection<ResourceSummary> mapped = new ArrayList<ResourceSummary>();
        for (final Folder f : folders) {
            mapped.add(map(f));
        }
        return mapped;
    }

    /**
     * Map a collection of Resource to a collection of ResourceSummary.
     *
     * @param resources The collection of resources to map.
     * @return The corresponding collection of ResourceSummary.
     */
    protected Collection<ResourceSummary> mapResources(final Collection<Resource> resources) {
        final Collection<ResourceSummary> mapped = new ArrayList<ResourceSummary>();
        for (final Resource r : resources) {
            mapped.add(map(r));
        }
        return mapped;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param r
     * @return
     */
    protected ResourceSummary map(final Resource r) {
        final ResourceSummary rs = new ResourceSummary();
        rs._id = r.id().toString();
        rs._name = r.name().toString();
        rs._type = r.type().name();
        rs._lockedBy = (r.isLocked()) ? r.lockedBy().username() : null;
        rs._title = r.title();
        rs._publishedBy = (r.isPublished()) ? r.publishedBy().username() : null;
        rs._childCount = 0;
        rs._folderCount = 0;
        return rs;
    }

    /**
     * Map a Folder to a FolderSummary.
     *
     * @param f
     * @return
     */
    protected ResourceSummary map(final Folder f) {
        final ResourceSummary rs = map((Resource) f);
        rs._childCount = f.entries().size();
        rs._folderCount = f.folders().size();
        return rs;
    }
}
