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

package ccc.commons.jee;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ccc.domain.JSONable;


/**
 * A helper class that provides JSON serialisation.
 *
 * @author Civic Computing Ltd
 */
public final class JSON {

    private JSON() { /* NO-OP */ }

    /**
     * This class models a JSON object.
     *
     * @author Civic Computing Ltd
     */
    public static final class Object {

        private Map<String, String> elements =
            new LinkedHashMap<String, String>();

        /**
         * Add a string field to this object.
         *
         * @param key The key for the string.
         * @param value The value of the string.
         * @return 'this' to allow method chaining.
         */
        public Object add(final String key, final String value) {
            elements.put(key, "\""+value+"\"");
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {

            final StringBuilder jsonString = new StringBuilder("{");
            for (final Map.Entry<String, String> element
                                                    : elements.entrySet()) {
                jsonString.append("\"");
                jsonString.append(element.getKey());
                jsonString.append("\": ");
                jsonString.append(element.getValue());
                jsonString.append(",");
            }
            if (jsonString.length()>1) {
                jsonString.deleteCharAt(jsonString.length()-1);
            }
            jsonString.append("}");

            return jsonString.toString();
        }

        /**
         * Add a list of {@link JSONable} objects as an array.
         *
         * @param key The key for the array.
         * @param list The elements of the array, as a list.
         * @return 'this' to allow method chaining.
         */
        public Object add(final String key,
                          final List<? extends JSONable> list) {

            final StringBuilder value = new StringBuilder("[");
            for (final JSONable jsonable : list) {
                final String jsonString = jsonable.toJSON();
                value.append(jsonString);
                value.append(",");
            }
            if (value.length()>1) {
                value.deleteCharAt(value.length()-1);
            }
            value.append("]");
            elements.put(key, value.toString());
            return this;
        }

    }

    /**
     * Create a new JSON object.
     *
     * @return The JSON object.
     */
    public static Object object() {
        return new Object();
    }

}
