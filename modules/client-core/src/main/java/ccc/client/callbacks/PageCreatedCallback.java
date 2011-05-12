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

import ccc.api.core.Page;
import ccc.api.types.CommandType;
import ccc.client.core.DefaultCallback;
import ccc.client.core.InternalServices;
import ccc.client.events.Event;

/**
 * Callback handler for creating a page.
 *
 * @author Civic Computing Ltd.
 */
public class PageCreatedCallback extends DefaultCallback<Page> {


    /**
     * Constructor.
     *
     * @param name The action name.
     */
    public PageCreatedCallback(final String name) {
        super(name);
    }


    /** {@inheritDoc} */
    @Override
    public void onSuccess(final Page rs) {
        final Event<CommandType> event =
            new Event<CommandType>(CommandType.PAGE_CREATE);
        event.addProperty("resource", rs);
        InternalServices.remotingBus.fireEvent(event);
    }
}
