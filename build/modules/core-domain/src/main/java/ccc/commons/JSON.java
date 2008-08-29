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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;



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

        private Map<String, String> _elements =
            new LinkedHashMap<String, String>();

        /**
         * Add a string field to this object.
         *
         * @param key The key for the string.
         * @param value The value of the string.
         * @return 'this' to allow method chaining.
         */
        public Object add(final String key, final String value) {
            final String escapedString = escape(value);
            _elements.put(key, "\""+escapedString+"\"");
            return this;
        }

        /**
         * Escape strings using JSON escape rules.
         *
         * @param s The string to escape.
         * @return The escaped string.
         */
        public String escape(final String s){
            if(s==null) {
                return null;
            }
            final StringBuffer sb=new StringBuffer();
            for(int i=0; i<s.length(); i++){
                final char ch=s.charAt(i);
                switch(ch){
                case '"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                case '/':
                    sb.append("\\/");
                    break;
                default:
                    if(isAsciiControlChar(ch)){
                        unicodeEscapeThenAppend(ch, sb);
                    } else {
                        sb.append(ch);
                    }
                }
            }
            return sb.toString();
        }

        /**
         * Convert a character to its unicode escaped character.
         *
         * @param ch The char to escape.
         * @param sb The escaped equivalent as a String, in the form '\u0001'.
         */
        private void unicodeEscapeThenAppend(final char ch,
                                             final StringBuffer sb) {
            final int maxZeroPadding = 4;
            final String ss=Integer.toHexString(ch);
            sb.append("\\u"); //$NON-NLS-1$
            for(int k=0; k<maxZeroPadding-ss.length(); k++){
                sb.append('0');
            }
            sb.append(ss.toUpperCase(Locale.US));
        }

        /**
         * Test to see if a character is an ASCII escape character.
         *
         * @param ch The char to test.
         * @return True if the character is an escape char, false otherwise.
         */
        private boolean isAsciiControlChar(final char ch) {
            return ch>='\u0000' && ch<='\u001F';
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {

            final StringBuilder jsonString = new StringBuilder("{");
            for (final Map.Entry<String, String> element
                                                    : _elements.entrySet()) {
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
            _elements.put(key, value.toString());
            return this;
        }

        /**
         * Add a map from String to JSONable as an object.
         *
         * @param key The key under which the object will be stored.
         * @param values The map of key -> JSONable that contains each property
         *        of the new object.
         * @return 'this' to allow method chaining.
         */
        public Object add(final String key,
                          final Map<String, ? extends JSONable> values) {

            final Object map = object();
            for (final Map.Entry<String, ? extends JSONable> value
                                                        : values.entrySet()) {

                map.add(value.getKey(), value.getValue());
            }
            final String jsonMap = map.toString();
            _elements.put(key, jsonMap);

            return this;
        }

        /**
         * Add an instance of JSONable as a new property of the object.
         *
         * @param key The key under which the object will be stored.
         * @param value The JSONable object.
         * @return 'this' to allow method chaining.
         */
        private Object add(final String key, final JSONable value) {
            _elements.put(key, value.toJSON());
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
