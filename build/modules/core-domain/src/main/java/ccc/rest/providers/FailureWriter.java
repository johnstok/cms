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

import ccc.domain.Failure;
import ccc.rest.providers.AbstractProvider;
import ccc.serialization.JsonImpl;


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
        final PrintWriter pw = new PrintWriter(outputStream);
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
