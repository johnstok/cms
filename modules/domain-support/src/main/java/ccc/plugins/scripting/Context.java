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
package ccc.plugins.scripting;

import java.util.HashMap;
import java.util.Map;



/**
 * The context in which a response will be rendered to text.
 *
 * @author Civic Computing Ltd.
 */
public class Context {

    private final Map<String, Object> _entries = new HashMap<String, Object>();


    /**
     * Accessor.
     *
     * @return Returns all context entries.
     */
    public final Map<String, Object> getAll() {
        return new HashMap<String, Object>(_entries);
    }


    /**
     * Add an additional value to the context.
     *
     * @param key The key used to look up the value.
     * @param value The value to store.
     *
     * @return Returns a reference to 'this' context.
     */
    public Context add(final String key, final Object value) {
        _entries.put(key, value);
        return this;
    }


    /**
     * Look up a value from the context.
     *
     * @param key The key used to look up the value.
     * @param type The type of the value.
     * @param <T> The type of the object in the repository.
     *
     * @return The value for the specified key, or NULL if no value exists.
     */
    public <T> T get(final String key, final Class<T> type) {
        return type.cast(_entries.get(key));
    }
}
