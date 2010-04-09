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

import ccc.api.dto.TemplateDelta;
import ccc.api.dto.TemplateDto;
import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.JsonKeys;


/**
 * A reader for templates.
 *
 * @author Civic Computing Ltd.
 */
@Provider
@Consumes("application/json")
public class TemplateNewReader
    extends
        AbstractProvider
    implements
        MessageBodyReader<TemplateDto> {

    /** {@inheritDoc} */
    @Override
    public boolean isReadable(final Class<?> clazz,
                              final Type type,
                              final Annotation[] annotations,
                              final MediaType mediaType) {
        return TemplateDto.class.equals(clazz);
    }

    /** {@inheritDoc} */
    @Override
    public TemplateDto readFrom(final Class<TemplateDto> clazz,
                            final Type type,
                            final Annotation[] annotations,
                            final MediaType mimetype,
                            final MultivaluedMap<String, String> httpHeaders,
                            final InputStream is) throws IOException {
        final Json json = readJson(mimetype, is);
        final TemplateDelta d = new TemplateDelta(json.getJson(JsonKeys.DELTA));
        return new TemplateDto(
            json.getId(JsonKeys.PARENT_ID),
            d,
            json.getString(JsonKeys.TITLE),
            json.getString(JsonKeys.DESCRIPTION),
            json.getString(JsonKeys.NAME));
    }
}
