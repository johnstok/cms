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

import ccc.rest.dto.FolderDelta;
import ccc.serialization.Json;
import ccc.serialization.JsonKeys;


/**
 * A reader for folder deltas.
 *
 * @author Civic Computing Ltd.
 */
@Provider
@Consumes("application/json")
public class FolderDeltaReader
    extends
        AbstractProvider
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
                              final InputStream arg5) throws IOException {
        final Json json = readJson(arg3, arg5);
        final FolderDelta d =
            new FolderDelta(
                json.getString(JsonKeys.SORT_ORDER),
                json.getId(JsonKeys.INDEX_PAGE_ID),
                json.getStrings(JsonKeys.SORT_LIST)
            );
        return d;
    }
}
