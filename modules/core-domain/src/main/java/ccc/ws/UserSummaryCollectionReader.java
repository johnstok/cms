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
package ccc.ws;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ccc.api.UserSummary;
import ccc.commons.IO;
import ccc.domain.Snapshot;


/**
 * A {@link MessageBodyWriter} a collection of resource summaries.
 * TODO: Remove this class - it is a duplicate.
 *
 * @author Civic Computing Ltd.
 */
@Provider
@Consumes("application/json")
public class UserSummaryCollectionReader
    implements
        MessageBodyReader<Collection<UserSummary>> {

    /**
     * Determine if a type is a collection of the specified class.
     *
     * @param clazz The parameterized type of the collection.
     * @param type The type to check.
     * @return True if 'type' is a collection of type 'clazz', false otherwise.
     */
    boolean isCollectionOfType(final Class<?> clazz, final Type type) {
        if (type instanceof ParameterizedType) {
            final ParameterizedType pType = (ParameterizedType) type;
            if (Collection.class.isAssignableFrom((Class<?>) pType.getRawType())
                && pType.getActualTypeArguments()[0].equals(clazz)) {
                return true;
            }
            return false;

        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isReadable(final Class<?> clazz,
                              final Type type,
                              final Annotation[] annotations,
                              final MediaType mediaType) {
        final boolean isReadable =
            isCollectionOfType(UserSummary.class, type);

        return isReadable;
    }

    /** {@inheritDoc} */
    @Override
    public Collection<UserSummary> readFrom(final Class<Collection<UserSummary>> arg0,
                                                final Type arg1,
                                                final Annotation[] arg2,
                                                final MediaType arg3,
                                                final MultivaluedMap<String, String> arg4,
                                                final InputStream arg5) throws IOException, WebApplicationException {
        try {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            IO.copy(arg5, baos);
            final String s = new String(baos.toByteArray());
            final JSONArray result = new JSONArray(s);
            final Collection<UserSummary> us = new ArrayList<UserSummary>();
            for (int i=0; i<result.length(); i++) {
                final JSONObject o = result.getJSONObject(i);
                us.add(new UserSummary(new Snapshot(o)));
            }
            return us;
        } catch (final JSONException e) {
            throw new WebApplicationException(e);
        }
    }

}
