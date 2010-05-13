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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import ccc.api.temp.DurationSerializer;
import ccc.api.types.Duration;
import ccc.plugins.s11n.json.JsonImpl;


/**
 * A provider for UUIDs.
 *
 * @author Civic Computing Ltd.
 */
@Provider
@Consumes("application/json")
@Produces("application/json")
public class DurationProvider
    extends
        AbstractProvider
    implements
        MessageBodyReader<Duration>,
        MessageBodyWriter<Duration>{

    /** {@inheritDoc} */
    @Override
    public boolean isReadable(final Class<?> clazz,
                              final Type arg1,
                              final Annotation[] arg2,
                              final MediaType arg3) {
        return Duration.class.equals(clazz);
    }

    /** {@inheritDoc} */
    @Override
    public Duration readFrom(final Class<Duration> arg0,
                             final Type arg1,
                             final Annotation[] arg2,
                             final MediaType arg3,
                             final MultivaluedMap<String, String> arg4,
                             final InputStream arg5) throws IOException {
        final String s = readString(arg3, arg5);
        return new DurationSerializer().read(new JsonImpl(s));
    }

    /** {@inheritDoc} */
    @Override
    public long getSize(final Duration arg0,
                        final Class<?> arg1,
                        final Type arg2,
                        final Annotation[] arg3,
                        final MediaType arg4) {
        return -1;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isWriteable(final Class<?> arg0,
                               final Type arg1,
                               final Annotation[] arg2,
                               final MediaType arg3) {
        return Duration.class.equals(arg0);
    }

    /** {@inheritDoc} */
    @Override
    public void writeTo(final Duration arg0,
                        final Class<?> arg1,
                        final Type arg2,
                        final Annotation[] arg3,
                        final MediaType arg4,
                        final MultivaluedMap<String, Object> arg5,
                        final OutputStream outputStream) {
        final PrintWriter pw = createWriter(outputStream);
        final JsonImpl json = new JsonImpl();
        new DurationSerializer().write(json, arg0);
        pw.print(json.getDetail());
        pw.flush();
    }
}
