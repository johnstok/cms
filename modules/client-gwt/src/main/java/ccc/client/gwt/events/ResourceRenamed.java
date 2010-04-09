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
package ccc.client.gwt.events;

import java.util.UUID;

import ccc.api.types.DBC;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;


/**
 * An event indicating a resource was renamed.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceRenamed
    extends
        GwtEvent<ResourceRenamed.RenamedHandler> {

    private String _name;
    private String _path;
    private UUID   _id;


    /**
     * Constructor.
     *
     * @param name The updated name.
     * @param path The updated absolute path.
     * @param id The resource's ID.
     */
    public ResourceRenamed(final String name,
                           final String path,
                           final UUID id) {
        _name = DBC.require().notEmpty(name);
        _path = DBC.require().notEmpty(path);
        _id   = DBC.require().notNull(id);
    }


    /**
     * Accessor.
     *
     * @return Returns the name.
     */
    public String getName() {
        return _name;
    }


    /**
     * Accessor.
     *
     * @return Returns the path.
     */
    public String getPath() {
        return _path;
    }


    /**
     * Accessor.
     *
     * @return Returns the id.
     */
    public UUID getId() {
        return _id;
    }


    /** {@inheritDoc} */
    @Override
    protected void dispatch(final ResourceRenamed.RenamedHandler handler) {
        handler.onRename(this);
    }


    /** {@inheritDoc} */
    @Override
    public Type<RenamedHandler> getAssociatedType() { return TYPE; }


    /**
     * Handler for 'resource renamed' events.
     *
     * @author Civic Computing Ltd.
     */
    public static interface RenamedHandler extends EventHandler {


        /**
         * Handle a 'resource renamed' event.
         *
         * @param event The event to handle.
         */
        void onRename(ResourceRenamed event);
    }


    /** TYPE : Type. */
    public static final Type<RenamedHandler> TYPE =
        new Type<RenamedHandler>();
}
