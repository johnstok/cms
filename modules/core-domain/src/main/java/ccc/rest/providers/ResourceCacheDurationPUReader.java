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
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import ccc.rest.dto.ResourceDto;
import ccc.serialization.Json;
import ccc.serialization.JsonKeys;
import ccc.types.Duration;


/**
 * A reader for cache duration partial updates.
 *
 * @author Civic Computing Ltd.
 */
@Provider
@Consumes("application/json")
public class ResourceCacheDurationPUReader
    extends
        AbstractProvider
    implements
        MessageBodyReader<ResourceDto> {

    /** {@inheritDoc} */
    @Override
    public boolean isReadable(final Class<?> clazz,
                              final Type type,
                              final Annotation[] annotations,
                              final MediaType mediaType) {
        return ResourceDto.class.equals(clazz);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceDto readFrom(
                             final Class<ResourceDto> arg0,
                             final Type arg1,
                             final Annotation[] arg2,
                             final MediaType arg3,
                             final MultivaluedMap<String, String> arg4,
                             final InputStream arg5) throws IOException {
        final Json json = readJson(arg3, arg5);
        final Json duration = json.getJson(JsonKeys.CACHE_DURATION);
        final Long revNo = json.getLong(JsonKeys.REVISION);
        final UUID templateId = json.getId(JsonKeys.TEMPLATE_ID);
        return new ResourceDto(
            (null==duration) ? null : new Duration(duration), revNo, templateId);
    }
}
