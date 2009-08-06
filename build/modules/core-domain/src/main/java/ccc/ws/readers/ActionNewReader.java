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

import ccc.api.CommandType;
import ccc.api.Json;
import ccc.api.JsonKeys;
import ccc.api.rest.ActionNew;
import ccc.commons.IO;
import ccc.domain.Snapshot;


/**
 * A reader for folders.
 *
 * @author Civic Computing Ltd.
 */
@Provider
@Consumes("application/json")
public class ActionNewReader
    implements
        MessageBodyReader<ActionNew> {

    /** {@inheritDoc} */
    @Override
    public boolean isReadable(final Class<?> clazz,
                              final Type type,
                              final Annotation[] annotations,
                              final MediaType mediaType) {
        return ActionNew.class.equals(clazz);
    }

    /** {@inheritDoc} */
    @Override
    public ActionNew readFrom(final Class<ActionNew> clazz,
                              final Type type,
                              final Annotation[] annotations,
                              final MediaType mimetype,
                              final MultivaluedMap<String, String> httpHeaders,
                              final InputStream is) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IO.copy(is, baos);
        final String s = new String(baos.toByteArray());

        final Json json = new Snapshot(s);
        return new ActionNew(
            json.getId(JsonKeys.SUBJECT_ID),
            CommandType.valueOf(json.getString(JsonKeys.ACTION)),
            json.getLong(JsonKeys.EXECUTE_AFTER),
            json.getStringMap(JsonKeys.PARAMETERS));
    }
}
