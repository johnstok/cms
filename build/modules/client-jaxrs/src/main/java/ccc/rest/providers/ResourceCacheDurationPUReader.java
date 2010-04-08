/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
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

import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.JsonKeys;
import ccc.rest.dto.ResourceDto;
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
            (null==duration)
                ? null
                : new Duration(duration), revNo, templateId);
    }
}
