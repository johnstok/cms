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
import java.util.ArrayList;
import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ccc.rest.dto.GroupDto;
import ccc.serialization.JsonImpl;


/**
 * A {@link MessageBodyReader} for a collection of group DTOs.
 *
 * @author Civic Computing Ltd.
 */
@Provider
@Consumes("application/json")
public class GroupCollectionReader
    extends
        AbstractProvider
    implements
        MessageBodyReader<Collection<GroupDto>> {

    /** {@inheritDoc} */
    @Override
    public boolean isReadable(final Class<?> clazz,
                              final Type type,
                              final Annotation[] annotations,
                              final MediaType mediaType) {
        return isCollectionOfType(GroupDto.class, type);
    }

    /** {@inheritDoc} */
    @Override
    public Collection<GroupDto> readFrom(
                                final Class<Collection<GroupDto>> arg0,
                                final Type arg1,
                                final Annotation[] arg2,
                                final MediaType arg3,
                                final MultivaluedMap<String, String> arg4,
                                final InputStream arg5) throws IOException {
        try {
            final String s = readString(arg3, arg5);
            final JSONArray result = new JSONArray(s);
            final Collection<GroupDto> rs =
                new ArrayList<GroupDto>();
            for (int i=0; i<result.length(); i++) {
                final JSONObject o = result.getJSONObject(i);
                rs.add(new GroupDto(new JsonImpl(o)));
            }
            return rs;
        } catch (final JSONException e) {
            throw new WebApplicationException(e);
        }
    }
}
