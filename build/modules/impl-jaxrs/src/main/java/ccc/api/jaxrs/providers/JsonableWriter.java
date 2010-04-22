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

import ccc.plugins.s11n.Jsonable;
import ccc.plugins.s11n.Jsonable2;
import ccc.plugins.s11n.json.JsonImpl;


/**
 * A JAX-RS provider for jsonable objects.
 *
 * @author Civic Computing Ltd.
 */
@Provider
@Produces({"application/json", "text/html"})
@Consumes("application/json")
public class JsonableWriter<T extends Jsonable2>
    extends
        AbstractProvider
    implements
        MessageBodyWriter<Jsonable>,
        MessageBodyReader<T> {


    /** {@inheritDoc} */
    @Override
    public long getSize(final Jsonable object,
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
        return Jsonable.class.isAssignableFrom(clazz);
    }


    /** {@inheritDoc} */
    @Override
    public void writeTo(final Jsonable object,
                        final Class<?> clazz,
                        final Type type,
                        final Annotation[] annotations,
                        final MediaType mediaType,
                        final MultivaluedMap<String, Object> httpHeaders,
                        final OutputStream outputStream) {
        final PrintWriter pw = createWriter(outputStream);
        if (MediaType.TEXT_HTML_TYPE.equals(mediaType)) {
            pw.print("<html><body>");
        }
        final String entity = new JsonImpl(object).getDetail();
        pw.print(entity);
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
        return Jsonable2.class.isAssignableFrom(clazz);
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
            final T object = clazz.newInstance();
            object.fromJson(readJson(mimetype, is));
            return object;

        } catch (final InstantiationException e) {
            throw new WebApplicationException(e);
        } catch (final IllegalAccessException e) {
            throw new WebApplicationException(e);
        }
    }
}
