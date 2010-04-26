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
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ccc.plugins.s11n.Jsonable;
import ccc.plugins.s11n.Jsonable2;
import ccc.plugins.s11n.json.JsonImpl;


/**
 * A {@link MessageBodyWriter} a collection of Jsonable objects.
 *
 * @author Civic Computing Ltd.
 */
@Provider
@Produces("application/json")
@Consumes("application/json")
public class JsonableCollectionWriter
    extends
        AbstractProvider
    implements
        MessageBodyWriter<Collection<Jsonable>>,
        MessageBodyReader<Collection<Jsonable2>> {


    /** {@inheritDoc} */
    @Override
    public long getSize(final Collection<Jsonable> object,
                        final Class<?> clazz,
                        final Type type,
                        final Annotation[] annotations,
                        final MediaType mediaType) {
        return -1;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isWriteable(final Class<?> clazz,
                               final Type type,
                               final Annotation[] annotations,
                               final MediaType mediaType) {
        return isCollectionOfType(Jsonable.class, type);
    }


    /** {@inheritDoc} */
    @Override
    public void writeTo(final Collection<Jsonable> object,
                        final Class<?> clazz,
                        final Type type,
                        final Annotation[] annotations,
                        final MediaType mediaType,
                        final MultivaluedMap<String, Object> httpHeaders,
                        final OutputStream outputStream) {
        final PrintWriter pw = createWriter(outputStream);
        pw.println("[\n");
        int a = 0;
        for (final Jsonable rs : object) {
            a++;
            if (a == object.size()) {
                pw.println(new JsonImpl(rs).getDetail()+"\n");
            } else {
                pw.println(new JsonImpl(rs).getDetail()+",\n");
            }
        }
        pw.println("\n]");
        pw.flush();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isReadable(final Class<?> type,
                              final Type genericType,
                              final Annotation[] annotations,
                              final MediaType mediaType) {
        return isCollectionOfType(Jsonable2.class, genericType);
    }

    /** {@inheritDoc} */
    @Override
    public Collection<Jsonable2> readFrom(final Class<Collection<Jsonable2>> type,
                                          final Type genericType,
                                          final Annotation[] annotations,
                                          final MediaType mediaType,
                                          final MultivaluedMap<String, String> httpHeaders,
                                          final InputStream entityStream) throws IOException, WebApplicationException {

        try {
            final Class<Jsonable2> typeArg = getTypeArgument(genericType, 0);

            final String s = readString(mediaType, entityStream);
            final JSONArray result = new JSONArray(s);

            final ArrayList<Jsonable2> elements = new ArrayList<Jsonable2>();
            for (int i=0; i<result.length(); i++) {
                final JSONObject o = result.getJSONObject(i);
                final Jsonable2 element = typeArg.newInstance();
                element.fromJson(new JsonImpl(o));
                elements.add(element);
            }

            return elements;

        } catch (final InstantiationException e) {
            throw new WebApplicationException(e);
        } catch (final IllegalAccessException e) {
            throw new WebApplicationException(e);
        } catch (final JSONException e) {
            throw new WebApplicationException(e);
        }
    }
}
