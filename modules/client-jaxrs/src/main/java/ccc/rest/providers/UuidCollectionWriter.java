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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import ccc.serialization.Json;
import ccc.serialization.JsonImpl;
import ccc.serialization.JsonKeys;


/**
 * A provider for a collection of UUIDs.
 *
 * @author Civic Computing Ltd.
 */
@Provider
@Produces("application/json")
@Consumes("application/json")
public class UuidCollectionWriter
    extends
        AbstractProvider
    implements
        MessageBodyWriter<Collection<UUID>>,
        MessageBodyReader<Collection<UUID>> {


    /** {@inheritDoc} */
    @Override
    public long getSize(final Collection<UUID> object,
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
        final boolean isWriteable = isCollectionOfType(UUID.class, type);
        return isWriteable;
    }

    /** {@inheritDoc} */
    @Override
    public void writeTo(final Collection<UUID> object,
                        final Class<?> clazz,
                        final Type type,
                        final Annotation[] annotations,
                        final MediaType mediaType,
                        final MultivaluedMap<String, Object> httpHeaders,
                        final OutputStream outputStream) {

        final List<String> strings = new ArrayList<String>();
        for (final UUID uuid : object) {
            strings.add(uuid.toString());
        }
        final JsonImpl json = new JsonImpl();
        json.setStrings(JsonKeys.ELEMENTS, strings);

        final PrintWriter pw = new PrintWriter(outputStream);
        pw.println(json.getDetail());
        pw.flush();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isReadable(final Class<?> arg0,
                              final Type type,
                              final Annotation[] arg2,
                              final MediaType arg3) {
        final boolean isReadable = isCollectionOfType(UUID.class, type);
        return isReadable;
    }

    /** {@inheritDoc} */
    @Override
    public Collection<UUID> readFrom(
                                    final Class<Collection<UUID>> arg0,
                                    final Type arg1,
                                    final Annotation[] arg2,
                                    final MediaType arg3,
                                    final MultivaluedMap<String, String> arg4,
                                    final InputStream arg5) throws IOException {
        final Json json = readJson(arg3, arg5);
        final List<UUID> uuids = new ArrayList<UUID>();

        for (final String s : json.getStrings(JsonKeys.ELEMENTS)) {
            uuids.add(UUID.fromString(s));
        }

        return uuids;
    }
}
