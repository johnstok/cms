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
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import ccc.api.Jsonable;
import ccc.domain.Snapshot;


/**
 * A {@link MessageBodyWriter} a collection of resource summaries.
 * TODO: Set char encoding?
 * TODO: Use velocity?
 * TODO: eTags?
 *
 * @author Civic Computing Ltd.
 */
@Provider
@Produces("application/json")
public class JsonableCollectionWriter
    implements
        MessageBodyWriter<Collection<Jsonable>> {


    /** {@inheritDoc} */
    @Override
    public long getSize(final Collection<Jsonable> object,
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
        final boolean isWriteable = isCollectionOfType(Jsonable.class, type);
        return isWriteable;
    }

    /**
     * Determine if a type is a collection of the specified class.
     *
     * @param clazz The parameterized type of the collection.
     * @param type The type to check.
     * @return True if 'type' is a collection of type 'clazz', false otherwise.
     */
    boolean isCollectionOfType(final Class<?> clazz, final Type type) {
        if (type instanceof ParameterizedType) {

            final ParameterizedType pType = (ParameterizedType) type;
            final Class<?> typeArg =
                (Class<?>) pType.getActualTypeArguments()[0];

            if (Collection.class.isAssignableFrom((Class<?>) pType.getRawType())
                && clazz.isAssignableFrom(typeArg)) {
                return true;
            }

            return false;
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public void writeTo(final Collection<Jsonable> object,
                        final Class<?> clazz,
                        final Type type,
                        final Annotation[] annotations,
                        final MediaType mediaType,
                        final MultivaluedMap<String, Object> httpHeaders,
                        final OutputStream outputStream) {
        final PrintWriter pw = new PrintWriter(outputStream);
        pw.println("[\n");
        int a = 0;
        for (final Jsonable rs : object) {
            a++;
            if (a == object.size()) {
                pw.println(new Snapshot(rs).getDetail()+"\n");
            } else {
                pw.println(new Snapshot(rs).getDetail()+",\n");
            }
        }
        pw.println("\n]");
        pw.flush();
    }
}
