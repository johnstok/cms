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
package ccc.ws.readers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import ccc.api.Json;
import ccc.api.JsonKeys;
import ccc.api.UserDelta;
import ccc.api.rest.UserNew;
import ccc.commons.IO;
import ccc.domain.Snapshot;


/**
 * A reader for users.
 *
 * @author Civic Computing Ltd.
 */
@Provider
@Consumes("application/json")
public class UserNewReader
    implements
        MessageBodyReader<UserNew> {

    /** {@inheritDoc} */
    @Override
    public boolean isReadable(final Class<?> clazz,
                              final Type type,
                              final Annotation[] annotations,
                              final MediaType mediaType) {
        return UserNew.class.equals(clazz);
    }

    /** {@inheritDoc} */
    @Override
    public UserNew readFrom(final Class<UserNew> clazz,
                            final Type type,
                            final Annotation[] annotations,
                            final MediaType mimetype,
                            final MultivaluedMap<String, String> httpHeaders,
                            final InputStream is) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IO.copy(is, baos);
        final String s = new String(baos.toByteArray());

        final Json json = new Snapshot(s);
        final UserDelta d = new UserDelta(json.getJson(JsonKeys.DELTA));
        return new UserNew(d, json.getString(JsonKeys.PASSWORD));
    }
}
