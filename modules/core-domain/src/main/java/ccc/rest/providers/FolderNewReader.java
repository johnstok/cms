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

import ccc.rest.dto.FolderDto;
import ccc.serialization.Json;
import ccc.serialization.JsonKeys;
import ccc.types.ResourceName;


/**
 * A reader for folders.
 *
 * @author Civic Computing Ltd.
 */
@Provider
@Consumes("application/json")
public class FolderNewReader
    extends
        AbstractProvider
    implements
        MessageBodyReader<FolderDto> {

    /** {@inheritDoc} */
    @Override
    public boolean isReadable(final Class<?> clazz,
                              final Type type,
                              final Annotation[] annotations,
                              final MediaType mediaType) {
        return FolderDto.class.equals(clazz);
    }

    /** {@inheritDoc} */
    @Override
    public FolderDto readFrom(final Class<FolderDto> clazz,
                              final Type type,
                              final Annotation[] annotations,
                              final MediaType mimetype,
                              final MultivaluedMap<String, String> httpHeaders,
                              final InputStream is) throws IOException {
        final Json json = readJson(mimetype, is);
        return new FolderDto(
            json.getId(JsonKeys.PARENT_ID),
            new ResourceName(json.getString(JsonKeys.NAME)));
    }
}
