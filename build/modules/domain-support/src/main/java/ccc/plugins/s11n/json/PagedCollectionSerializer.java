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
import java.util.Map;

import ccc.api.core.PagedCollection;
import ccc.plugins.s11n.Serializer;
import ccc.plugins.s11n.Serializers;


/**
 * Serializer for {@link PagedCollection}s.
 *
 * @param <T> The type of collection to be serialized / deserialized.
 *
 * @author Civic Computing Ltd.
 */
class PagedCollectionSerializer<T>
    implements
        Serializer<PagedCollection<T>> {

    private final Serializers _serializers;


    /**
     * Constructor.
     *
     * @param serializers The serializers for this serializer.
     */
    public PagedCollectionSerializer(final Serializers serializers) {
        _serializers = serializers;
    }


    /** {@inheritDoc} */
    @Override
    public PagedCollection<T> read(final Json json) {
        final String className = json.getString(JsonKeys.TYPE);
        return read(json, className);
    }


    /** {@inheritDoc} */
    @Override
    public Json write(final Json json, final PagedCollection<T> instance) {
        if (null==instance) { return null; }

        json.set("links", instance.getLinks());
        json.set(JsonKeys.SIZE, Long.valueOf(instance.getTotalCount()));
        json.set(JsonKeys.TYPE, instance.getElementClass().getName());
        final Serializer<T> serializer =
            _serializers.create(instance.getElementClass());
        final Collection<Json> jsonElements = new ArrayList<Json>();
        for (final T element : instance.getElements()) {
            jsonElements.add(serializer.write(json.create(), element));
        }
        json.setJsons(JsonKeys.ELEMENTS, jsonElements);

        return json;
    }


    private <U> PagedCollection<U> read(final Json json,
                                        final String elementClass) {

        if (null == json) { return null; }

        final Class<U> clazz = (Class<U>) _serializers.findClass(elementClass);

        final Serializer<U> serializer = _serializers.create(clazz);

        final ArrayList<U> elements = new ArrayList<U>();
        for (final Json jElem : json.getCollection(JsonKeys.ELEMENTS)) {
            elements.add(serializer.read(jElem));
        }
        final PagedCollection<U> c =
            new PagedCollection<U>(
                json.getLong(JsonKeys.SIZE).longValue(),
                clazz,
                elements);

        final Map<String, String> links = json.getStringMap("links");
        if (null != links) {
            c.addLinks(links);
        }

        return c;
    }
}
