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
import ccc.api.rest.UserPasswordPU;
import ccc.ws.AbstractProvider;


/**
 * A reader for folders.
 *
 * @author Civic Computing Ltd.
 */
@Provider
@Consumes("application/json")
public class UserPasswordPUReader
    extends
        AbstractProvider
    implements
        MessageBodyReader<UserPasswordPU> {

    /** {@inheritDoc} */
    @Override
    public boolean isReadable(final Class<?> clazz,
                              final Type type,
                              final Annotation[] annotations,
                              final MediaType mediaType) {
        return UserPasswordPU.class.equals(clazz);
    }

    /** {@inheritDoc} */
    @Override
    public UserPasswordPU readFrom(final Class<UserPasswordPU> clazz,
                              final Type type,
                              final Annotation[] annotations,
                              final MediaType mimetype,
                              final MultivaluedMap<String, String> httpHeaders,
                              final InputStream is) throws IOException {
        final Json json = readJson(mimetype, is);
        return new UserPasswordPU(json.getString(JsonKeys.PASSWORD));
    }
}
