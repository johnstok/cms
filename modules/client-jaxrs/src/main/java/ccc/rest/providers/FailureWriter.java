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
package ccc.rest.providers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import ccc.serialization.JsonImpl;
import ccc.types.Failure;


/**
 * A provider for failures.
 *
 * @author Civic Computing Ltd.
 */
@Provider
@Produces("application/json")
public class FailureWriter
    extends
        AbstractProvider
    implements
        MessageBodyWriter<Failure>,
        MessageBodyReader<Failure> {


    /** {@inheritDoc} */
    @Override
    public long getSize(final Failure object,
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
        return Failure.class.equals(clazz);
    }

    /** {@inheritDoc} */
    @Override
    public void writeTo(final Failure object,
                        final Class<?> clazz,
                        final Type type,
                        final Annotation[] annotations,
                        final MediaType mediaType,
                        final MultivaluedMap<String, Object> httpHeaders,
                        final OutputStream outputStream) {
        final PrintWriter pw = createWriter(outputStream);
        final JsonImpl sn = new JsonImpl(object);
        pw.println(sn.getDetail());
        pw.flush();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isReadable(final Class<?> clazz,
                              final Type type,
                              final Annotation[] annotations,
                              final MediaType mediaType) {
        return Failure.class.equals(clazz);
    }

    /** {@inheritDoc} */
    @Override
    public Failure readFrom(final Class<Failure> arg0,
                            final Type arg1,
                            final Annotation[] arg2,
                            final MediaType arg3,
                            final MultivaluedMap<String, String> arg4,
                            final InputStream arg5) throws IOException  {
        return new Failure(readJson(arg3, arg5));
    }
}
