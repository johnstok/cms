/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.plugins.s11n.json;

import ccc.plugins.s11n.Serializer;


/**
 * Helper class for serializers.
 *
 * @param <T> The type of object to serialize.
 *
 * @author Civic Computing Ltd.
 */
public abstract class BaseSerializer<T>
    implements
        Serializer<T> {

    private final TextParser _parser;


    /**
     * Constructor.
     *
     * @param parser The text parser for this serializer.
     */
    public BaseSerializer(final TextParser parser) { _parser = parser; }


    /**
     * Create a new container object.
     *
     * @return The container.
     */
    protected final Json newJson() { return _parser.newJson(); }


    /**
     * Parse a string to JSON.
     *
     * @param data The string to parse.
     *
     * @return The corresponding JSON object.
     */
    protected Json parse(final String data) { return _parser.parseJson(data); }
}
