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

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import ccc.serialization.JsonImpl;
import ccc.types.Duration;


/**
 * A {@link MessageBodyReader} for durations.
 *
 * @author Civic Computing Ltd.
 */
@Provider
@Consumes("application/json")
public class DurationReader
    extends
        AbstractProvider
    implements
        MessageBodyReader<Duration> {

    /** {@inheritDoc} */
    @Override
    public boolean isReadable(final Class<?> clazz,
                              final Type type,
                              final Annotation[] annotations,
                              final MediaType mediaType) {
        return Duration.class.equals(clazz);
    }

    /** {@inheritDoc} */
    @Override
    public Duration readFrom(final Class<Duration> arg0,
                             final Type arg1,
                             final Annotation[] arg2,
                             final MediaType arg3,
                             final MultivaluedMap<String, String> arg4,
                             final InputStream arg5) throws IOException {
        final String s = readString(arg3, arg5);

        if (0==s.trim().length()) {
            return null;
        }

        final JsonImpl sn = new JsonImpl(s);
        final Duration d = new Duration(sn);

        return d;
    }
}
