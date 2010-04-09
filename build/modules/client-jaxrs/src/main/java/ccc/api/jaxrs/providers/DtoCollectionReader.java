/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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
package ccc.api.jaxrs.providers;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import ccc.api.dto.DtoCollection;
import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.JsonKeys;
import ccc.plugins.s11n.Jsonable2;


/**
 * A reader for DTO collections.
 *
 * @author Civic Computing Ltd.
 */
@Provider
@Consumes("application/json")
public class DtoCollectionReader
    extends
        AbstractProvider
    implements
        MessageBodyReader<DtoCollection<Jsonable2>> {

    /** {@inheritDoc} */
    @Override
    public boolean isReadable(final Class<?> clazz,
                              final Type type,
                              final Annotation[] annotations,
                              final MediaType mediaType) {
        return DtoCollection.class.equals(clazz);
    }

    /** {@inheritDoc} */
    @Override
    public DtoCollection<Jsonable2> readFrom(
                                  final Class<DtoCollection<Jsonable2>> arg0,
                                  final Type type,
                                  final Annotation[] arg2,
                                  final MediaType arg3,
                                  final MultivaluedMap<String, String> arg4,
                                  final InputStream arg5) throws IOException {

        try {
            final Class<Jsonable2> typeArg = getTypeArgument(type, 0);

            final Json json = readJson(arg3, arg5);

            final ArrayList<Jsonable2> elements = new ArrayList<Jsonable2>();
            for (final Json jElem : json.getCollection(JsonKeys.ELEMENTS)) {
                final Jsonable2 element = typeArg.newInstance();
                element.fromJson(jElem);
                elements.add(element);
            }

            final DtoCollection<Jsonable2> d =
                new DtoCollection<Jsonable2>(
                    json.getLong(JsonKeys.SIZE).longValue(),
                    elements);
            return d;

        } catch (final InstantiationException e) {
            throw new WebApplicationException(e);
        } catch (final IllegalAccessException e) {
            throw new WebApplicationException(e);
        }
    }


    @SuppressWarnings("unchecked")
    private <T> Class<T> getTypeArgument(final Type type, final int index) {
        if (type instanceof ParameterizedType) {
            final ParameterizedType pType = (ParameterizedType) type;
            return (Class<T>) pType.getActualTypeArguments()[index];
        }
        throw new RuntimeException("Not a parameterized type: "+type);
    }
}
