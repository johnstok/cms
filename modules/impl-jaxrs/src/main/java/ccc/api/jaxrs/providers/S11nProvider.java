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

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import ccc.api.core.PagedCollection;
import ccc.plugins.s11n.Serializer;
import ccc.plugins.s11n.json.JsonImpl;
import ccc.plugins.s11n.json.PagedCollectionSerializer;
import ccc.plugins.s11n.json.SerializerFactory;


/**
 * A JAX-RS provider for objects with serializers.
 *
 * @param <T> The type of object this provider will read write.
 *
 * @author Civic Computing Ltd.
 */
@Provider
@Produces({"application/json", "text/html"})
@Consumes("application/json")
public class S11nProvider<T>
    extends
        AbstractProvider
    implements
        MessageBodyWriter<T>,
        MessageBodyReader<T> {

    static {
        SerializerFactory
            .addSerializer(
                PagedCollection.class, new PagedCollectionSerializer());
    }


    /** {@inheritDoc} */
    @Override
    public long getSize(final T object,
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
        return SerializerFactory.canCreate(clazz);
    }


    /** {@inheritDoc} */
    @Override
    public void writeTo(final T object,
                        final Class<?> clazz,
                        final Type type,
                        final Annotation[] annotations,
                        final MediaType mediaType,
                        final MultivaluedMap<String, Object> httpHeaders,
                        final OutputStream outputStream) {
        final Serializer<T> s = (Serializer<T>) SerializerFactory.create(clazz);
        final JsonImpl json = new JsonImpl();
        s.write(json, object);

        final PrintWriter pw = createWriter(outputStream);
        if (MediaType.TEXT_HTML_TYPE.equals(mediaType)) {
            pw.print("<html><body>");
        }
        pw.print(json.getDetail());
        if (MediaType.TEXT_HTML_TYPE.equals(mediaType)) {
            pw.println("</body></html>");
        }
        pw.flush();
    }


    /** {@inheritDoc} */
    @Override
    public boolean isReadable(final Class<?> clazz,
                              final Type type,
                              final Annotation[] annotations,
                              final MediaType mediaType) {
        return SerializerFactory.canCreate(clazz);
    }


    /** {@inheritDoc} */
    @Override
    public T readFrom(final Class<T> clazz,
                      final Type type,
                      final Annotation[] annotations,
                      final MediaType mimetype,
                      final MultivaluedMap<String, String> httpHeaders,
                      final InputStream is) throws IOException {
        try {
            final Serializer<T> s = SerializerFactory.create(clazz);
            return s.read(readJson(mimetype, is));

        } catch (final RuntimeException e) { // FIXME: Choose correct type!
            throw new WebApplicationException(e);
        }
    }
}
