/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.ws;

import java.io.ByteArrayOutputStream;
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

import ccc.commons.IO;


/**
 * A {@link MessageBodyWriter} a collection of resource summaries.
 * TODO: Remove this class - it is a duplicate.
 *
 * @author Civic Computing Ltd.
 */
@Provider
@Consumes("application/json")
@Produces("application/json")
public class BooleanProvider
    implements
        MessageBodyReader<Boolean>,
        MessageBodyWriter<Boolean> {

    /** {@inheritDoc} */
    @Override
    public boolean isReadable(final Class<?> clazz,
                              final Type type,
                              final Annotation[] annotations,
                              final MediaType mediaType) {
        return Boolean.class.equals(clazz);
    }

    /** {@inheritDoc} */
    @Override
    public Boolean readFrom(final Class<Boolean> arg0,
                             final Type arg1,
                             final Annotation[] arg2,
                             final MediaType arg3,
                             final MultivaluedMap<String, String> arg4,
                             final InputStream arg5) throws IOException, WebApplicationException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IO.copy(arg5, baos);
        final String s = new String(baos.toByteArray());

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
    public boolean isWriteable(final Class<?> arg0,
                               final Type arg1,
                               final Annotation[] arg2,
                               final MediaType arg3) {
        return Boolean.class.equals(arg0);
    }

    /** {@inheritDoc} */
    @Override
    public void writeTo(final Boolean arg0,
                        final Class<?> arg1,
                        final Type arg2,
                        final Annotation[] arg3,
                        final MediaType arg4,
                        final MultivaluedMap<String, Object> arg5,
                        final OutputStream outputStream) throws IOException, WebApplicationException {
        final PrintWriter pw = new PrintWriter(outputStream);
        pw.println(arg0);
        pw.flush();
    }
}
