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


/**
 * A JAX-RS provider for boolean values.
 *
 * @author Civic Computing Ltd.
 */
@Provider
@Consumes("application/json")
@Produces({"application/json", "text/html"})
@Deprecated // FIXME: Sending a boolean indicates procedural thinking rephrase the method.
public class BooleanProvider
    extends
        AbstractProvider
    implements
        MessageBodyReader<Boolean>,
        MessageBodyWriter<Boolean> {

    /** {@inheritDoc} */
    @Override
    public boolean isReadable(final Class<?> clazz,
                              final Type type,
                              final Annotation[] annotations,
                              final MediaType mediaType) {
        return
            Boolean.TYPE.equals(clazz)
            || Boolean.class.equals(clazz);
    }

    /** {@inheritDoc} */
    @Override
    public Boolean readFrom(final Class<Boolean> arg0,
                             final Type arg1,
                             final Annotation[] arg2,
                             final MediaType arg3,
                             final MultivaluedMap<String, String> arg4,
                             final InputStream arg5) throws IOException {
        final String s = readString(arg3, arg5);

        if ("true".equals(s)) {
            return Boolean.TRUE;
        } else if ("false".equals(s)) {
            return Boolean.FALSE;
        } else {
            throw new WebApplicationException(
                new IllegalArgumentException("Invalid JSON boolean: "+s));
        }
    }

    /** {@inheritDoc} */
    @Override
    public long getSize(final Boolean arg0,
                        final Class<?> arg1,
                        final Type arg2,
                        final Annotation[] arg3,
                        final MediaType arg4) {
        return -1;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isWriteable(final Class<?> clazz,
                               final Type arg1,
                               final Annotation[] arg2,
                               final MediaType arg3) {
        return
            Boolean.TYPE.equals(clazz)
            || Boolean.class.equals(clazz);
    }

    /** {@inheritDoc} */
    @Override
    public void writeTo(final Boolean arg0,
                        final Class<?> arg1,
                        final Type arg2,
                        final Annotation[] arg3,
                        final MediaType mediaType,
                        final MultivaluedMap<String, Object> httpHeaders,
                        final OutputStream outputStream) {
        writeContentType(mediaType, httpHeaders);

        final PrintWriter pw = createWriter(outputStream);

        if (MediaType.TEXT_HTML_TYPE.equals(mediaType)) {
            pw.print("<h2>");
            pw.print(arg0);
            pw.print("</h2>");
        } else {
            pw.print(arg0);
        }

        pw.flush();
    }
}
