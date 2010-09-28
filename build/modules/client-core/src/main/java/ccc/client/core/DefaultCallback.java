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
package ccc.client.core;

import ccc.api.types.DBC;
import ccc.client.events.Event;


/**
 * Callback that sends exceptions as events on the core bus.
 *
 * @param <T> The type of the return value.
 *
 * @author Civic Computing Ltd.
 */
public abstract class DefaultCallback<T>
    implements
        Callback<T> {

    private String            _actionName;


    /**
     * Constructor.
     *
     * @param actionName The name of this action.
     */
    public DefaultCallback(final String actionName) {
        _actionName = DBC.require().notEmpty(actionName);
    }


    /** {@inheritDoc} */
    @Override
    public void onFailure(final Throwable throwable) {
        InternalServices.CORE_BUS.fireEvent(
            new Event<CoreEvents>(CoreEvents.ERROR)
                .addProperty("exception", throwable)
                .addProperty("name",      _actionName));
    }
}
