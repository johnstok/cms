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

import ccc.rest.dto.TextFileDelta;
import ccc.serialization.Json;
import ccc.serialization.JsonKeys;
import ccc.types.MimeType;


/**
 * A reader for file DTOs.
 *
 * @author Civic Computing Ltd.
 */
@Provider
@Consumes("application/json")
public class TextFileDeltaReader
    extends
        AbstractProvider
    implements
        MessageBodyReader<TextFileDelta> {

    /** {@inheritDoc} */
    @Override
    public boolean isReadable(final Class<?> clazz,
                              final Type type,
                              final Annotation[] annotations,
                              final MediaType mediaType) {
        return TextFileDelta.class.equals(clazz);
    }

    /** {@inheritDoc} */
    @Override
    public TextFileDelta readFrom(final Class<TextFileDelta> arg0,
                             final Type arg1,
                             final Annotation[] arg2,
                             final MediaType arg3,
                             final MultivaluedMap<String, String> arg4,
                             final InputStream arg5) throws IOException {
        final Json json = readJson(arg3, arg5);
        final TextFileDelta d =
            new TextFileDelta(
                json.getId(JsonKeys.ID),
                json.getString(JsonKeys.DATA),
                new MimeType(json.getJson(JsonKeys.MIME_TYPE)),
                json.getBool(JsonKeys.MAJOR_CHANGE).booleanValue(),
                json.getString(JsonKeys.COMMENT)
            );
        return d;
    }
}
