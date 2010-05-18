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

import java.util.Map;

import ccc.api.core.Res;
import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.Serializer;


/**
 * Serializer for {@link Res}s.
 *
 * @param <T> The type of {@link Res} to serialize.
 *
 * @author Civic Computing Ltd.
 */
public abstract class ResSerializer<T extends Res>
    implements
        Serializer<T> {


    /** {@inheritDoc} */
    @Override
    public T read(final Json json) {
        if (null==json) { return null; }

        final T r = createObject();

        final Map<String, String> links = json.getStringMap("links");
        if (null!=links) { r.addLinks(links); }

        return r;
    }


    /**
     * Create a new instance of type T.
     *
     * @return The newly created instance of T.
     */
    protected abstract T createObject();


    /** {@inheritDoc} */
    @Override
    public Json write(final Json json, final T instance) {
        if (null==instance) { return null; }

        json.set("links", instance.getLinks());

        return json;
    }
}
