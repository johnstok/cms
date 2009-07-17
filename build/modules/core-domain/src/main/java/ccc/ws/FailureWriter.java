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

import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import ccc.api.Failure;
import ccc.domain.Snapshot;


/**
 * Writes failure objects for JAX-RS.
 * TODO: Set char encoding.
 *
 * @author Civic Computing Ltd.
 */
@Provider
@Produces("application/json")
public class FailureWriter
    implements
        MessageBodyWriter<Failure> {


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
        final Snapshot sn = new Snapshot(object);
        pw.println(sn.getDetail());
        pw.flush();
    }
}
