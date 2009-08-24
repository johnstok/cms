/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
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
