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

package ccc.api;

import java.util.Collection;
import java.util.Date;
import java.util.Map;


/**
 * A JSON object.
 *
 * @author Civic Computing Ltd.
 */
public interface Json {

    /**
     * Mutator.
     *
     * @param key The key.
     * @param value The value, as a string.
     */
    void set(final String key, final String value);

    /**
     * Mutator.
     *
     * @param key The key.
     * @param snapshots The value, as a collection of snapshots.
     */
    void set(final String key, final Collection<? extends Jsonable> snapshots);

    /**
     * Mutator.
     *
     * @param key The key.
     * @param bool The value, as a boolean.
     */
    void set(final String key, final Boolean bool);

    /**
     * Mutator.
     *
     * @param key The key.
     * @param date The value, as a date.
     */
    void set(final String key, final Date date);

    /**
     * Mutator.
     *
     * @param key The key.
     * @param value The value, as an ID.
     */
    void set(final String key, final ID value);

    /**
     * Mutator.
     *
     * @param key The key.
     * @param value The value, as a long.
     */
    void set(final String key, final long value);

    /**
     * Mutator.
     *
     * @param key The key.
     * @param value The value, as a decimal.
     */
    void set(final String key, final Decimal value);

    /**
     * Mutator.
     *
     * @param key The key.
     * @param value The value, as a map of strings.
     */
    void set(final String key, final Map<String, String> value);

    /**
     * Mutator.
     *
     * @param key The key.
     * @param value The value, as a {@link Jsonable} object.
     */
    void set(final String key, final Jsonable value);

    /**
     * Accessor.
     *
     * @param key The key for the value.
     * @return The value, as a string.
     */
    String getString(final String key);

    /**
     * Accessor.
     *
     * @param key The key for the value.
     * @return The value, as a collection of snapshots.
     */
    Collection<Json> getCollection(final String key);

    /**
     * Accessor.
     *
     * @param key The key for the value.
     * @return The value, as a Date.
     */
    Date getDate(final String key);

    /**
     * Accessor.
     *
     * @param key The key for the value.
     * @return The value, as a boolean.
     */
    Boolean getBool(final String key);

    /**
     * Accessor.
     *
     * @param key The key for the value.
     * @return The value, as an ID.
     */
    ID getId(final String key);

    /**
     * Accessor.
     *
     * @param key The key for the value.
     * @return The value, as an int.
     */
    int getInt(final String key);

    /**
     * Accessor.
     *
     * @param key The key for the value.
     * @return The value, as a decimal.
     */
    Decimal getDecimal(final String key);

    /**
     * Accessor.
     *
     * @param key The key for the value.
     * @return The value, as a JSON object.
     */
    Json getJson(String key);

}