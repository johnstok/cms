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

import ccc.api.UserSummary;
import ccc.domain.JsonImpl;


/**
 * A {@link MessageBodyReader} for a collection of user summaries.
 *
 * @author Civic Computing Ltd.
 */
@Provider
@Consumes("application/json")
public class UserSummaryCollectionReader
    extends
        AbstractProvider
    implements
        MessageBodyReader<Collection<UserSummary>> {

    /** {@inheritDoc} */
    @Override
    public boolean isReadable(final Class<?> clazz,
                              final Type type,
                              final Annotation[] annotations,
                              final MediaType mediaType) {
        return isCollectionOfType(UserSummary.class, type);
    }

    /** {@inheritDoc} */
    @Override
    public Collection<UserSummary> readFrom(
                                    final Class<Collection<UserSummary>> arg0,
                                    final Type arg1,
                                    final Annotation[] arg2,
                                    final MediaType arg3,
                                    final MultivaluedMap<String, String> arg4,
                                    final InputStream arg5) throws IOException {
        try {
            final JSONArray result = new JSONArray(readString(arg3, arg5));
            final Collection<UserSummary> us = new ArrayList<UserSummary>();
            for (int i=0; i<result.length(); i++) {
                final JSONObject o = result.getJSONObject(i);
                us.add(new UserSummary(new JsonImpl(o)));
            }
            return us;
        } catch (final JSONException e) {
            throw new WebApplicationException(e);
        }
    }

}
