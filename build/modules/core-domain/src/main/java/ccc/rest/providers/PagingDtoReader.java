/*-----------------------------------------------------------------------------
 * Copyright (c) 2010 Civic Computing Ltd.
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

import ccc.rest.dto.PagingDto;
import ccc.serialization.Json;


/**
 * A reader for paging controls.
 *
 * @author Civic Computing Ltd.
 */
@Provider
@Consumes("application/json")
public class PagingDtoReader extends
AbstractProvider
implements
    MessageBodyReader<PagingDto> {

    /** {@inheritDoc} */
    @Override
    public boolean isReadable(final Class<?> clazz,
                              final Type type,
                              final Annotation[] annotations,
                              final MediaType mediaType) {
        return PagingDto.class.equals(clazz);
    }

    /** {@inheritDoc} */
    @Override
    public PagingDto readFrom(final Class<PagingDto> clazz,
                            final Type type,
                            final Annotation[] annotations,
                            final MediaType mimetype,
                            final MultivaluedMap<String, String> httpHeaders,
                            final InputStream is) throws IOException {
        final Json json = readJson(mimetype, is);
        final PagingDto dto = new PagingDto(json);
        return dto;
    }

}
