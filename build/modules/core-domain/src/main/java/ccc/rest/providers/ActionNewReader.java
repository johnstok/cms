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

import ccc.rest.dto.ActionDto;
import ccc.serialization.Json;
import ccc.serialization.JsonKeys;
import ccc.types.CommandType;


/**
 * A reader for actions.
 *
 * @author Civic Computing Ltd.
 */
@Provider
@Consumes("application/json")
public class ActionNewReader
    extends
        AbstractProvider
    implements
        MessageBodyReader<ActionDto> {

    /** {@inheritDoc} */
    @Override
    public boolean isReadable(final Class<?> clazz,
                              final Type type,
                              final Annotation[] annotations,
                              final MediaType mediaType) {
        return ActionDto.class.equals(clazz);
    }

    /** {@inheritDoc} */
    @Override
    public ActionDto readFrom(final Class<ActionDto> clazz,
                              final Type type,
                              final Annotation[] annotations,
                              final MediaType mimetype,
                              final MultivaluedMap<String, String> httpHeaders,
                              final InputStream is) throws IOException {
        final Json json = readJson(mimetype, is);
        return new ActionDto(
            json.getId(JsonKeys.SUBJECT_ID),
            CommandType.valueOf(json.getString(JsonKeys.ACTION)),
            json.getDate(JsonKeys.EXECUTE_AFTER),
            json.getStringMap(JsonKeys.PARAMETERS));
    }
}
