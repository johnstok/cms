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
