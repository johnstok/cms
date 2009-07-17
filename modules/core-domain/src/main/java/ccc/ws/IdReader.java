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
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import ccc.api.ID;
import ccc.commons.IO;


/**
 * A {@link MessageBodyWriter} a collection of resource summaries.
 *
 * @author Civic Computing Ltd.
 */
@Provider
@Consumes({"application/json"})
public class IdReader
    implements
        MessageBodyReader<ID> {

    /** {@inheritDoc} */
    @Override
    public boolean isReadable(final Class<?> clazz,
                              final Type type,
                              final Annotation[] annotations,
                              final MediaType mediaType) {
        return ID.class.equals(clazz);
    }

    /** {@inheritDoc} */
    @Override
    public ID readFrom(final Class<ID> arg0,
                       final Type arg1,
                       final Annotation[] arg2,
                       final MediaType arg3,
                       final MultivaluedMap<String, String> arg4,
                       final InputStream arg5) throws IOException, WebApplicationException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IO.copy(arg5, baos);
        final String s = new String(baos.toByteArray());

        if (0==s.trim().length()) {
            return null;
        }

        final ID id = new ID(s);

        return id;
    }
}