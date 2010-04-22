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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.json.JsonImpl;


/**
 * A provider for JSON objects.
 *
 * @author Civic Computing Ltd.
 */
@Provider
@Consumes("application/json")
@Deprecated
public class JsonReader
    extends
        AbstractProvider
    implements
        MessageBodyReader<Json>,
        MessageBodyWriter<Json> {

    /** {@inheritDoc} */
    @Override
    public boolean isReadable(final Class<?> clazz,
                              final Type type,
                              final Annotation[] annotations,
                              final MediaType mediaType) {
        return Json.class.equals(clazz);
    }

    /** {@inheritDoc} */
    @Override
    public Json readFrom(final Class<Json> arg0,
                         final Type arg1,
                         final Annotation[] arg2,
                         final MediaType arg3,
                         final MultivaluedMap<String, String> arg4,
                         final InputStream arg5) throws IOException {
        final String s = readString(arg3, arg5);
        if (0==s.trim().length()) {
            return null;
        }
        return new JsonImpl(s);
    }

    /** {@inheritDoc} */
    @Override
    public long getSize(final Json t,
                        final Class<?> type,
                        final Type genericType,
                        final Annotation[] annotations,
                        final MediaType mediaType) {
        return -1;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isWriteable(final Class<?> type,
                               final Type genericType,
                               final Annotation[] annotations,
                               final MediaType mediaType) {
        return Json.class.equals(type);
    }

    /** {@inheritDoc} */
    @Override
    public void writeTo(final Json t,
                        final Class<?> type,
                        final Type genericType,
                        final Annotation[] annotations,
                        final MediaType mediaType,
                        final MultivaluedMap<String, Object> httpHeaders,
                        final OutputStream entityStream) {
        final PrintWriter pw = new PrintWriter(entityStream);
        pw.println(((JsonImpl) t).getDetail());
        pw.flush();
    }
}
