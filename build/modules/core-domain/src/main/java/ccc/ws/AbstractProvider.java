/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */

package ccc.ws;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import ccc.commons.IO;
import ccc.domain.JsonImpl;
import ccc.serialization.Json;


/**
 * Abstract class providing helper methods for JAX-RS readers & writers.
 *
 * @author Civic Computing Ltd.
 */
public class AbstractProvider {

    /**
     * Determine if a type is a collection of the specified class.
     *
     * @param clazz The parameterized type of the collection.
     * @param type The type to check.
     * @return True if 'type' is a collection of type 'clazz', false otherwise.
     */
    protected boolean isCollectionOfType(final Class<?> clazz,
                                         final Type type) {
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

    /**
     * Determine if a type is a collection of the specified class.
     *
     * @param clazz The parameterized type of the collection.
     * @param type The type to check.
     * @return True if 'type' is a collection of type 'clazz', false otherwise.
     */
    protected boolean isMapOfType(final Class<?> clazz, final Type type) {
        if (type instanceof ParameterizedType) {
            final ParameterizedType pType = (ParameterizedType) type;
            if (Map.class.isAssignableFrom((Class<?>) pType.getRawType())
                && pType.getActualTypeArguments()[0].equals(clazz)
                && pType.getActualTypeArguments()[1].equals(clazz)) {
                return true;
            }
            return false;

        }
        return false;
    }


    protected String readString(final MediaType mediaType,
                                final InputStream entityStream)
    throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IO.copy(entityStream, baos);
        final String charset = mediaType.getParameters().get("charset");
        final String s =
            new String(
                baos.toByteArray(),
                (null==charset) ? "UTF8" : charset).trim();
        return s;
    }


    protected Json readJson(final MediaType mediaType,
                            final InputStream entityStream) throws IOException {
        final String body = readString(mediaType, entityStream);
        final Json json = new JsonImpl(body);
        return json;
    }
}
