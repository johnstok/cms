/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.rendering;

import java.util.HashMap;
import java.util.Map;



/**
 * The context in which a response will be rendered to text.
 *
 * @author Civic Computing Ltd.
 */
public class Context {

    private final Map<String, Object> _extra = new HashMap<String, Object>();


    /**
     * Accessor.
     *
     * @return Returns the extra.
     */
    public final Map<String, Object> getAll() {
        return new HashMap<String, Object>(_extra);
    }


    /**
     * Add an additional value to a text processing context.
     *
     * @param key The key used to look up the value.
     * @param value The value to store.
     */
    public void add(final String key, final Object value) {
        _extra.put(key, value);
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
        return type.cast(_extra.get(key));
    }
}
