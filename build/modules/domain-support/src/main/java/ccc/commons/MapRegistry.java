/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
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
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */
package ccc.commons;

import java.util.HashMap;

import ccc.types.DBC;


/**
 * A registry implementation using an in-memory map.
 *
 * @author Civic Computing Ltd
 */
public final class MapRegistry implements Registry {

    /**
     * Constructor. Create a map-based registry with a single item.
     *
     * @param location The location to store the item.
     * @param object The item to store.
     */
    public MapRegistry(final String location, final Object object) {
        put(location, object);
    }

    /**
     * Constructor.
     */
    public MapRegistry() { super(); }

    private final HashMap<String, Object> _map = new HashMap<String, Object>();

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(final String location) {
        DBC.require().notEmpty(location);
        return (T) _map.get(location);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Registry put(final String location, final Object object) {
        DBC.require().notEmpty(location);
        _map.put(location, object);
        return this;
    }
}
