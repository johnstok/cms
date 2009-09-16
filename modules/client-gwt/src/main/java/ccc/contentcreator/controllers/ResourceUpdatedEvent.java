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
 * An event indicating a resource was updated.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceUpdatedEvent implements Event {

    private final ResourceSummaryModelData _resource;


    /**
     * Constructor.
     *
     * @param resource The updated resource.
     */
    public ResourceUpdatedEvent(final ResourceSummaryModelData resource) {
        _resource = resource;
    }


    /** {@inheritDoc} */
    @Override
    public Type getType() {
        return Event.Type.RESOURCE_UPDATED;
    }


    /**
     * Accessor.
     *
     * @return Returns the resource.
     */
    public ResourceSummaryModelData getResource() {
        return _resource;
    }
}
