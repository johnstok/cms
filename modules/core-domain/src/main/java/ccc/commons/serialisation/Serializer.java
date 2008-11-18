/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.commons.serialisation;

import java.util.Date;
import java.util.Map;


/**
 * API implemented by a serializer.
 *
 * @author Civic Computing Ltd.
 */
public interface Serializer {

    /**
     * Serialize a string.
     *
     * @param key The key used to access the value.
     * @param value The value to store.
     */
    void string(String key, String value);

    /**
     * Serialize an integer.
     *
     * @param key The key used to access the value.
     * @param value The value to store.
     */
    @Deprecated
    void integer(String key, int value);

    /**
     * Serialize a map.
     *
     * @param key The key used to access the value.
     * @param value The value to store.
     */
    void dict(String key, Map<String, ? extends CanSerialize> value);

    /**
     * Serialize a date.
     *
     * @param key The key used to access the value.
     * @param value The value to store.
     */
    void date(String key, Date value);

    /**
     * Serialize a long.
     *
     * @param key The key used to access the value.
     * @param value The value to store.
     */
    void number(String key, long value);

}
