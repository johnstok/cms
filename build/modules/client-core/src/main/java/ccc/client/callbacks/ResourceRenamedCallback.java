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
package ccc.client.callbacks;

import java.util.UUID;

import ccc.api.types.CommandType;
import ccc.api.types.ResourcePath;
import ccc.client.core.DefaultCallback;
import ccc.client.core.InternalServices;
import ccc.client.events.Event;

/**
 * Callback handler for renaming a resource.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceRenamedCallback extends DefaultCallback<Void> {

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
    public void onSuccess(final Void result) {
        InternalServices.remotingBus.fireEvent(_event);
    }
}
