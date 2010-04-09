/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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

import java.util.UUID;

import ccc.api.types.DBC;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;


/**
 * An event indicating a new resource was deleted.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceDeleted
    extends
        GwtEvent<ResourceDeleted.ResourceDeletedHandler> {

    private final UUID _resource;


    /**
     * Constructor.
     *
     * @param resource The deleted resource's ID.
     */
    public ResourceDeleted(final UUID resource) {
        _resource = DBC.require().notNull(resource);
    }


    /**
     * Accessor.
     *
     * @return Returns the deleted resource's ID.
     */
    public UUID getResource() { return _resource; }


    /** {@inheritDoc} */
    @Override
    protected void dispatch(
                        final ResourceDeleted.ResourceDeletedHandler handler) {
        handler.onDelete(this);
    }


    /** {@inheritDoc} */
    @Override
    public Type<ResourceDeletedHandler> getAssociatedType() { return TYPE; }


    /**
     * Handler for 'resource deleted' events.
     *
     * @author Civic Computing Ltd.
     */
    public static interface ResourceDeletedHandler extends EventHandler {


        /**
         * Handle a 'resource deleted' event.
         *
         * @param event The event to handle.
         */
        void onDelete(ResourceDeleted event);
    }


    /** TYPE : Type. */
    public static final Type<ResourceDeletedHandler> TYPE =
        new Type<ResourceDeletedHandler>();
}
