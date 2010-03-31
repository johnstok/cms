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

import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import ccc.rest.UnauthorizedException;
import ccc.serialization.Json;


/**
 * A reader for unauthorized exceptions.
 *
 * @author Civic Computing Ltd.
 */
@Provider
@Consumes("application/json")
public class UnauthorizedExceptionReader
    extends
        AbstractProvider
    implements
        MessageBodyReader<UnauthorizedException> {

    /** {@inheritDoc} */
    @Override
    public boolean isReadable(final Class<?> clazz,
                              final Type type,
                              final Annotation[] annotations,
                              final MediaType mediaType) {
        return UnauthorizedException.class.equals(clazz);
    }

    /** {@inheritDoc} */
    @Override
    public UnauthorizedException readFrom(
                            final Class<UnauthorizedException> clazz,
                            final Type type,
                            final Annotation[] annotations,
                            final MediaType mimetype,
                            final MultivaluedMap<String, String> httpHeaders,
                            final InputStream is) throws IOException {
        final Json json = readJson(mimetype, is);
        return new UnauthorizedException(json);
    }
}
