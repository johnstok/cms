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

import ccc.api.core.ResourceSummary;
import ccc.api.types.CommandType;
import ccc.client.core.DefaultCallback;
import ccc.client.core.InternalServices;
import ccc.client.events.Event;

/**
 * Callback handler for applying a working copy.
 *
 * @author Civic Computing Ltd.
 */
public class WCAppliedCallback extends DefaultCallback<Void> {

    private final Event<CommandType> _event;


    /**
     * Constructor.
     *
     * @param name The action name.
     * @param resource The resource whose WC has been applied.
     */
    public WCAppliedCallback(final String name,
                             final ResourceSummary resource) {
        super(name);
        _event = new Event<CommandType>(CommandType.RESOURCE_APPLY_WC);
        _event.addProperty("resource", resource);
    }


    /** {@inheritDoc} */
    @Override
    public void onSuccess(final Void result) {
        InternalServices.REMOTING_BUS.fireEvent(_event);
    }
}
