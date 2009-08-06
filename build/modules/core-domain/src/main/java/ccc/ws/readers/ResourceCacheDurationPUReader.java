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

import ccc.api.Duration;
import ccc.api.Json;
import ccc.api.JsonKeys;
import ccc.api.rest.ResourceCacheDurationPU;
import ccc.commons.IO;
import ccc.domain.Snapshot;


/**
 * A reader for cache duration partial updates.
 *
 * @author Civic Computing Ltd.
 */
@Provider
@Consumes("application/json")
public class ResourceCacheDurationPUReader
    implements
        MessageBodyReader<ResourceCacheDurationPU> {

    /** {@inheritDoc} */
    @Override
    public boolean isReadable(final Class<?> clazz,
                              final Type type,
                              final Annotation[] annotations,
                              final MediaType mediaType) {
        return ResourceCacheDurationPU.class.equals(clazz);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceCacheDurationPU readFrom(
                             final Class<ResourceCacheDurationPU> arg0,
                             final Type arg1,
                             final Annotation[] arg2,
                             final MediaType arg3,
                             final MultivaluedMap<String, String> arg4,
                             final InputStream arg5) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IO.copy(arg5, baos);
        final String s = new String(baos.toByteArray());

        final Json json = new Snapshot(s).getJson(JsonKeys.CACHE_DURATION);

        return new ResourceCacheDurationPU(
            (null==json) ? null : new Duration(json));
    }
}
