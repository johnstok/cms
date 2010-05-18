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

import java.util.ArrayList;
import java.util.Collection;

import ccc.api.core.PagedCollection;
import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.JsonKeys;
import ccc.plugins.s11n.Serializer;


/**
 * Serializer for {@link PagedCollection}s.
 *
 * @param <T> The type of collection to be serialized / deserialized.
 *
 * @author Civic Computing Ltd.
 */
public class PagedCollectionSerializer<T>
    implements
        Serializer<PagedCollection<T>> {


    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public PagedCollection<T> read(final Json json) {
        try {
            final String className = json.getString(JsonKeys.TYPE);
            final Class<T> elementClass =
                (Class<T>) Class.forName(className);
            return PagedCollectionReader.read(json, elementClass);

        } catch (final ClassNotFoundException e) {
            throw new RuntimeException("Paged collection type missing.", e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public Json write(final Json json, final PagedCollection<T> instance) {
        if (null==instance) { return null; }

        json.set("links", instance.getLinks());
        json.set(JsonKeys.SIZE, Long.valueOf(instance.getTotalCount()));
        json.set(JsonKeys.TYPE, instance.getElementClass().getName());
        final Serializer<T> serializer =
            SerializerFactory.create(instance.getElementClass());
        final Collection<Json> jsonElements = new ArrayList<Json>();
        for (final T element : instance.getElements()) {
            jsonElements.add(serializer.write(json.create(), element));
        }
        json.setJsons(JsonKeys.ELEMENTS, jsonElements);

        return json;
    }
}
