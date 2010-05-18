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
package ccc.api.temp;

import java.util.ArrayList;
import java.util.Map;

import ccc.api.core.PagedCollection;
import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.JsonKeys;
import ccc.plugins.s11n.Serializer;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class PagedCollectionReader {

    public static <T> PagedCollection<T> read(final Json json,
                                              final Class<T> elementClass) {
        if (null==json) { return null; }

        final Serializer<T> serializer =
            SerializerFactory.create(elementClass);

        final ArrayList<T> elements = new ArrayList<T>();
        for (final Json jElem : json.getCollection(JsonKeys.ELEMENTS)) {
            elements.add(serializer.read(jElem));
        }
        final PagedCollection<T> c =
            new PagedCollection<T>(
                json.getLong(JsonKeys.SIZE).longValue(),
                elementClass,
                elements);

        final Map<String, String> links = json.getStringMap("links");
        if (null!=links) { c.addLinks(links); }

        return c;
    }
}
