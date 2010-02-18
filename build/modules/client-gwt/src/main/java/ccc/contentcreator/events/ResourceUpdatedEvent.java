/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.events;

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
