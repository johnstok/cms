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

import ccc.rest.dto.RevisionDto;
import ccc.serialization.JsonImpl;


/**
 * A {@link MessageBodyReader} a collection of resource summaries.
 * TODO: Remove this class - it is a duplicate.
 *
 * @author Civic Computing Ltd.
 */
@Provider
@Consumes("application/json")
public class RevisionSummaryCollectionReader
    extends
        AbstractProvider
    implements
        MessageBodyReader<Collection<RevisionDto>> {

    /** {@inheritDoc} */
    @Override
    public boolean isReadable(final Class<?> clazz,
                              final Type type,
                              final Annotation[] annotations,
                              final MediaType mediaType) {
        return isCollectionOfType(RevisionDto.class, type);
    }

    /** {@inheritDoc} */
    @Override
    public Collection<RevisionDto> readFrom(
                                final Class<Collection<RevisionDto>> arg0,
                                final Type arg1,
                                final Annotation[] arg2,
                                final MediaType arg3,
                                final MultivaluedMap<String, String> arg4,
                                final InputStream arg5) throws IOException {
        try {
            final String s = readString(arg3, arg5);
            final JSONArray result = new JSONArray(s);
            final Collection<RevisionDto> rs =
                new ArrayList<RevisionDto>();
            for (int i=0; i<result.length(); i++) {
                final JSONObject o = result.getJSONObject(i);
                rs.add(new RevisionDto(new JsonImpl(o)));
            }
            return rs;
        } catch (final JSONException e) {
            throw new WebApplicationException(e);
        }
    }

}
