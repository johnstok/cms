/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.actions;

import java.util.UUID;

import ccc.api.core.ResourceSummary;
import ccc.api.types.CommandType;
import ccc.api.types.DBC;
import ccc.api.types.ResourcePath;
import ccc.client.core.Globals;
import ccc.client.core.HttpMethod;
import ccc.client.core.I18n;
import ccc.client.core.InternalServices;
import ccc.client.core.RemotingAction;
import ccc.client.core.Request;
import ccc.client.core.ResponseHandlerAdapter;
import ccc.client.events.Event;


/**
 * Remote action for renaming.
 *
 * @author Civic Computing Ltd.
 */
public class RenameAction
    extends
        RemotingAction {

    private final String _name;
    private final ResourceSummary _resource;
    private final ResourcePath _newPath;


    /**
     * Constructor.
     *
     * @param resource The resource to update.
     * @param name The new name for this resource.
     * @param newPath The updated absolute path to the resource.
     */
    public RenameAction(final ResourceSummary resource,
                        final String name,
                        final ResourcePath newPath) {
        _resource = DBC.require().notNull(resource);
        _name     = DBC.require().notEmpty(name);
        _newPath  = DBC.require().notNull(newPath);
    }


    /** {@inheritDoc} */
    @Override
    protected Request getRequest() {
        return rename();
    }


    /**
     * Rename a resource.
     *
     * @return The HTTP request to rename a resource.
     */
    public Request rename() {
        return
            new Request(
                HttpMethod.POST,
                Globals.API_URL
                    + _resource.rename().build(InternalServices.ENCODER),
                    _name,
                new ResourceRenamedCallback(
                    I18n.UI_CONSTANTS.rename(),
                    _name,
                    _resource.getId(),
                    _newPath));
    }


    /**
     * Callback handler for renaming a resource.
     *
     * @author Civic Computing Ltd.
     */
    private class ResourceRenamedCallback extends ResponseHandlerAdapter {

        private final Event<CommandType> _event;

        /**
         * Constructor.
         *
         * @param name The action name.
         * @param newPath The resource's new path.
         * @param id The resource's ID.
         * @param rName The resource's new name.
         */
        public ResourceRenamedCallback(final String name,
                                       final String rName,
                                       final UUID id,
                                       final ResourcePath newPath) {
            super(name);
            _event = new Event<CommandType>(CommandType.RESOURCE_RENAME);
            _event.addProperty("name", rName);
            _event.addProperty("path", newPath);
            _event.addProperty("id", id);
        }

        /** {@inheritDoc} */
        @Override
        public void onNoContent(final ccc.client.core.Response response) {
            InternalServices.REMOTING_BUS.fireEvent(_event);
        }
    }
}
