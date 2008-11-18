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


/**
 * API implemented by a deserializer.
 *
 * @author Civic Computing Ltd.
 */
public interface DeSerializer extends Iterable<String> {

    /**
     * Lookup a string value from the deserializer.
     *
     * @param key The key the value is stored under.
     * @return The corresponding value.
     */
    String string(String key);

    /**
     * Lookup an integer value from the deserializer.
     *
     * @param key The key the value is stored under.
     * @return The corresponding value.
     */
    int integer(String key);

    /**
     * Lookup a dictionary (map) from the deserializer.
     *
     * @param key The key the value is stored under.
     * @return The corresponding value.
     */
    DeSerializer dict(String key);
}
