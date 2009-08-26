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
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class Context {

    private final StatefulReader _reader;
    private final Object _resource;
    private final Map<String, String[]> _params;
    private final Map<String, Object> _extra = new HashMap<String, Object>();


    /**
     * Constructor.
     *
     * @param reader
     * @param resource
     * @param params
     */
    public Context(final StatefulReader reader,
                   final Object resource,
                   final Map<String, String[]> params) {
        _reader = reader;
        _resource = resource;
        _params = params;
    }


    /**
     * Accessor.
     *
     * @return Returns the reader.
     */
    public final StatefulReader getReader() {
        return _reader;
    }


    /**
     * Accessor.
     *
     * @return Returns the subject.
     */
    public final Object getResource() {
        return _resource;
    }


    /**
     * Accessor.
     *
     * @return Returns the param's.
     */
    public final Map<String, String[]> getParams() {
        return _params;
    }


    /**
     * Accessor.
     *
     * @return Returns the extra.
     */
    public final Map<String, Object> getExtras() {
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
}
