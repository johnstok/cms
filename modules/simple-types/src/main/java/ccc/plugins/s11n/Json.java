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

package ccc.plugins.s11n;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.UUID;


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
     * @param value The value, as a collection of strings.
     */
    void setStrings(String key, Collection<String> value);

    /**
     * Mutator.
     *
     * @param key The key.
     * @param values The value, as a collection of Jsonable objects.
     */
    void set(final String key, final Collection<? extends Jsonable> values);

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
    void set(final String key, final UUID value);

    /**
     * Mutator.
     *
     * @param key The key.
     * @param value The value, as a long.
     */
    void set(final String key, final Long value);

    /**
     * Mutator.
     *
     * @param key The key.
     * @param value The value, as a decimal.
     */
    void set(final String key, final BigDecimal value);

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
    UUID getId(final String key);

    /**
     * Accessor.
     *
     * @param key The key for the value.
     * @return The value, as an int.
     */
    Integer getInt(final String key);

    /**
     * Accessor.
     *
     * @param key The key for the value.
     * @return The value, as a decimal.
     */
    BigDecimal getBigDecimal(final String key);

    /**
     * Accessor.
     *
     * @param key The key for the value.
     * @return The value, as a JSON object.
     */
    Json getJson(String key);

    /**
     * Accessor.
     *
     * @param key The key for the value.
     * @return The value, as a collection of strings.
     */
    Collection<String> getStrings(final String key);

    /**
     * Accessor.
     *
     * @param key The key for the value.
     * @return The value, as a long.
     */
    Long getLong(String key);

    /**
     * Accessor.
     *
     * @param key The key for the value.
     * @return The value, as a map of strings.
     */
    Map<String, String> getStringMap(String key);
}
