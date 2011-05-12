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

import java.util.HashMap;
import java.util.Map;

import ccc.api.types.DBC;


/**
 * An client event that can be handled on an event bus.
 *
 * @param <T> The type of the event.
 *
 * @author Civic Computing Ltd.
 */
public class Event<T> {

    private final T _type;
    private final Map<String, Object> _properties =
        new HashMap<String, Object>();


    /**
     * Constructor.
     *
     * @param type The type of the  event.
     */
    public Event(final T type) {
        _type = DBC.require().notNull(type);
    }


    /**
     * Accessor.
     *
     * @return Returns the type.
     */
    public T getType() {
        return _type;
    }


    /**
     * Retrieve a property for this event.
     *
     * @param <S> The type of the property.
     * @param key The key for the property.
     * @return The property or NULL if no property exists for the specified key.
     */
    @SuppressWarnings("unchecked")
    public <S> S getProperty(final String key) {
        DBC.require().notNull(key);
        return (S) _properties.get(key);
    }


    /**
     * Add a property for this event.
     *
     * @param key The key the property will be stored under.
     * @param value The value of the property.
     * @return A reference to 'this', for method chaining.
     */
    public Event<T> addProperty(final String key, final Object value) {
        _properties.put(key, value);
        return this;
    }
}
