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
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class JSON {

    /**
     * TODO Add Description for this type.
     *
     * @author Civic Computing Ltd
     */
    public static class Object {

        private Map<String, String> elements = new LinkedHashMap<String, String>();

        /**
         * TODO: Add a description of this method.
         *
         * @param key
         * @param value
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
            for (final Map.Entry<String, String> element : elements.entrySet()) {
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
         * TODO: Add a description of this method.
         *
         * @param key
         * @param list
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
     * TODO: Add a description of this method.
     *
     * @return
     */
    public static Object object() {

        return new Object();
    }

}
