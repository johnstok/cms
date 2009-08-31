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

import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import ccc.serialization.JsonImpl;
import ccc.serialization.Jsonable;


/**
 * A {@link MessageBodyWriter} for jsonable objects.
 *
 * @author Civic Computing Ltd.
 */
@Provider
@Produces("application/json")
public class JsonableWriter
    implements
        MessageBodyWriter<Jsonable> {


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
        final PrintWriter pw = new PrintWriter(outputStream);
        pw.println(new JsonImpl(object).getDetail());
        pw.flush();
    }
}
