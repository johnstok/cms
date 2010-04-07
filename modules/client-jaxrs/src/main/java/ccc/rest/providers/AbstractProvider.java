/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
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
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */

package ccc.rest.providers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import ccc.commons.IO;
import ccc.serialization.Json;
import ccc.serialization.JsonImpl;


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


    /**
     * Read the request body into a string.
     *
     * @param mediaType The content type of the request.
     * @param entityStream The request input stream.
     *
     * @return The body, as a string.
     *
     * @throws IOException If reading from the input stream fails.
     */
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


    /**
     * Read the request body into a JSON object.
     *
     * @param mediaType The content type of the request.
     * @param entityStream The request input stream.
     *
     * @return The request body, as a JSON object.
     *
     * @throws IOException If reading from the input stream fails.
     */
    protected Json readJson(final MediaType mediaType,
                            final InputStream entityStream) throws IOException {
        final String body = readString(mediaType, entityStream);
        final Json json = new JsonImpl(body);
        return json;
    }


    /**
     * Create a UTF-8 print-writer.
     *
     * @param outputStream The output stream the writer will write to.
     *
     * @return The new print-writer.
     */
    protected PrintWriter createWriter(final OutputStream outputStream) {
        try {
            final Writer osw = new OutputStreamWriter(outputStream, "UTF-8");
            return new PrintWriter(osw);
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 encoding unsupported.", e);
        }
    }
}
