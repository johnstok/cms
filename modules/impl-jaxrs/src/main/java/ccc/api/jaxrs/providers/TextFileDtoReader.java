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
package ccc.api.jaxrs.providers;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import ccc.api.dto.TextFileDto;
import ccc.api.types.MimeType;
import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.JsonKeys;


/**
 * A reader for file DTOs.
 *
 * @author Civic Computing Ltd.
 */
@Provider
@Consumes("application/json")
public class TextFileDtoReader
    extends
        AbstractProvider
    implements
        MessageBodyReader<TextFileDto> {

    /** {@inheritDoc} */
    @Override
    public boolean isReadable(final Class<?> clazz,
                              final Type type,
                              final Annotation[] annotations,
                              final MediaType mediaType) {
        return TextFileDto.class.equals(clazz);
    }

    /** {@inheritDoc} */
    @Override
    public TextFileDto readFrom(final Class<TextFileDto> arg0,
                             final Type arg1,
                             final Annotation[] arg2,
                             final MediaType arg3,
                             final MultivaluedMap<String, String> arg4,
                             final InputStream arg5) throws IOException {
        final Json json = readJson(arg3, arg5);
        final TextFileDto d =
            new TextFileDto(
                json.getId(JsonKeys.PARENT_ID),
                json.getString(JsonKeys.NAME),
                new MimeType(json.getJson(JsonKeys.MIME_TYPE)),
                json.getBool(JsonKeys.MAJOR_CHANGE).booleanValue(),
                json.getString(JsonKeys.COMMENT),
                json.getString(JsonKeys.DATA)
            );
        return d;
    }
}