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

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import ccc.api.UserDelta;


/**
 * A {@link MessageBodyReader} for user deltas.
 *
 * @author Civic Computing Ltd.
 */
@Provider
@Consumes("application/json")
public class UserDeltaReader
    extends
        AbstractProvider
    implements
        MessageBodyReader<UserDelta> {

    /** {@inheritDoc} */
    @Override
    public boolean isReadable(final Class<?> clazz,
                              final Type type,
                              final Annotation[] annotations,
                              final MediaType mediaType) {
        return UserDelta.class.equals(clazz);
    }

    /** {@inheritDoc} */
    @Override
    public UserDelta readFrom(final Class<UserDelta> arg0,
                              final Type arg1,
                              final Annotation[] arg2,
                              final MediaType arg3,
                              final MultivaluedMap<String, String> arg4,
                              final InputStream arg5) throws IOException {
        return new UserDelta(readJson(arg3, arg5));
    }
}
