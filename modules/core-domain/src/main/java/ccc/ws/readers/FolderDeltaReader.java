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
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import ccc.api.Json;
import ccc.api.JsonKeys;
import ccc.api.rest.FolderDelta;
import ccc.commons.IO;
import ccc.domain.Snapshot;


/**
 * A reader for folder deltas.
 * TODO: Remove this class - it is a duplicate.
 *
 * @author Civic Computing Ltd.
 */
@Provider
@Consumes("application/json")
public class FolderDeltaReader
    implements
        MessageBodyReader<FolderDelta> {

    /** {@inheritDoc} */
    @Override
    public boolean isReadable(final Class<?> clazz,
                              final Type type,
                              final Annotation[] annotations,
                              final MediaType mediaType) {
        return FolderDelta.class.equals(clazz);
    }

    /** {@inheritDoc} */
    @Override
    public FolderDelta readFrom(final Class<FolderDelta> arg0,
                              final Type arg1,
                              final Annotation[] arg2,
                              final MediaType arg3,
                              final MultivaluedMap<String, String> arg4,
                              final InputStream arg5) throws IOException, WebApplicationException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IO.copy(arg5, baos);
        final String s = new String(baos.toByteArray());
        final Json json = new Snapshot(s);
        final FolderDelta d =
            new FolderDelta(
                json.getString(JsonKeys.SORT_ORDER),
                json.getId(JsonKeys.INDEX_PAGE_ID),
                json.getStrings(JsonKeys.SORT_LIST)
            );
        return d;
    }
}
