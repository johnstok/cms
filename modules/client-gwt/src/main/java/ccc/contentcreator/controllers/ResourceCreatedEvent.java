/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.controllers;

import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.client.Event;


/**
 * An event indicating a new resource was created.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceCreatedEvent implements Event {

    private final ResourceSummaryModelData _resource;
    private final ResourceSummaryModelData _parentFolder;


    /**
     * Constructor.
     *
     * @param resource The newly created resource.
     * @param parentFolder The parent folder for the resource.
     */
    public ResourceCreatedEvent(final ResourceSummaryModelData resource,
                                final ResourceSummaryModelData parentFolder) {
        _resource = resource;
        _parentFolder = parentFolder;
    }


    /** {@inheritDoc} */
    @Override
    public Type getType() {
        return Event.Type.RESOURCE_CREATED;
    }


    /**
     * Accessor.
     *
     * @return Returns the resource.
     */
    public ResourceSummaryModelData getResource() {
        return _resource;
    }


    /**
     * Accessor.
     *
     * @return Returns the parentFolder.
     */
    public ResourceSummaryModelData getParentFolder() {
        return _parentFolder;
    }
}
