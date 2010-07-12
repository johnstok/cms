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
package ccc.client.events;

import java.util.HashSet;
import java.util.Set;

import ccc.api.types.DBC;


/**
 * A simple implementation of an event bus.
 *
 * @param <T> The type of events this bus will handle.
 *
 * @author Civic Computing Ltd.
 */
public class SimpleBus<T extends Enum<T>> implements Bus<T> {

    private final Set<EventHandler<T>> _handlers =
        new HashSet<EventHandler<T>>();


    /** {@inheritDoc} */
    public synchronized void fireEvent(final Event<T> event) {
        DBC.require().notNull(event);
        final Set<EventHandler<T>> handlers =
            new HashSet<EventHandler<T>>(_handlers);
        for (final EventHandler<T> handler : handlers) {
            handler.handle(event);
        }
    }


    /** {@inheritDoc} */
    public synchronized void registerHandler(final EventHandler<T> handler) {
        _handlers.add(DBC.require().notNull(handler));
    }


    /** {@inheritDoc} */
    public synchronized void unregisterHandler(final EventHandler<T> handler) {
        _handlers.remove(handler);
    }
}
