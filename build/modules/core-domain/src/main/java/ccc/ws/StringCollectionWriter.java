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
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.json.JSONArray;
import org.json.JSONException;

import ccc.commons.IO;


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
@Consumes("application/json")
public class StringCollectionWriter
    implements
        MessageBodyWriter<Collection<String>>,
        MessageBodyReader<Collection<String>> {


    /** {@inheritDoc} */
    @Override
    public long getSize(final Collection<String> object,
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
        final boolean isWriteable = isCollectionOfType(String.class, type);
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
            if (Collection.class.isAssignableFrom((Class<?>) pType.getRawType())
                && pType.getActualTypeArguments()[0].equals(clazz)) {
                return true;
            }
            return false;

        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public void writeTo(final Collection<String> object,
                        final Class<?> clazz,
                        final Type type,
                        final Annotation[] annotations,
                        final MediaType mediaType,
                        final MultivaluedMap<String, Object> httpHeaders,
                        final OutputStream outputStream) {

        final PrintWriter pw = new PrintWriter(outputStream);
        pw.println("[");
        for (final String rs : object) {
            pw.println("\n\""+rs+"\","); // FIXME: Escaping & trailing comma!
        }
        pw.println("]");
        pw.flush();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isReadable(final Class<?> arg0,
                              final Type type,
                              final Annotation[] arg2,
                              final MediaType arg3) {
        final boolean isReadable = isCollectionOfType(String.class, type);
        return isReadable;
    }

    /** {@inheritDoc} */
    @Override
    public Collection<String> readFrom(final Class<Collection<String>> arg0,
                                       final Type arg1,
                                       final Annotation[] arg2,
                                       final MediaType arg3,
                                       final MultivaluedMap<String, String> arg4,
                                       final InputStream arg5) throws IOException, WebApplicationException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IO.copy(arg5, baos);
        final String s = new String(baos.toByteArray());

        try {
            final JSONArray a = new JSONArray(s);
            final Collection<String> strings = new ArrayList<String>();
            for (int i=0; i<a.length(); i++) {
                strings.add((String) a.get(i));
            }
            return strings;
        } catch (final JSONException e) {
            throw new WebApplicationException(e);
        }
    }
}
