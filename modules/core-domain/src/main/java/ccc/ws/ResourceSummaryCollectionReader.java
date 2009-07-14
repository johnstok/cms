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
import java.util.Date;

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

import ccc.api.ID;
import ccc.api.JsonKeys;
import ccc.api.ResourceSummary;
import ccc.api.ResourceType;
import ccc.api.Username;
import ccc.commons.IO;


/**
 * A {@link MessageBodyWriter} a collection of resource summaries.
 * TODO: Remove this class - it is a duplicate.
 *
 * @author Civic Computing Ltd.
 */
@Provider
@Consumes("application/json")
public class ResourceSummaryCollectionReader
    implements
        MessageBodyReader<Collection<ResourceSummary>> {

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
            isCollectionOfType(ResourceSummary.class, type);

        return isReadable;
    }

    /** {@inheritDoc} */
    @Override
    public Collection<ResourceSummary> readFrom(final Class<Collection<ResourceSummary>> arg0,
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
            final Collection<ResourceSummary> rs = new ArrayList<ResourceSummary>();
            for (int i=0; i<result.length(); i++) {
                final JSONObject o = result.getJSONObject(i);
                rs.add(
                    new ResourceSummary(
                        (o.isNull(JsonKeys.ID)) ? null : new ID((String) o.get(JsonKeys.ID)),
                        (o.isNull(JsonKeys.PARENT_ID)) ? null : new ID((String) o.get(JsonKeys.PARENT_ID)),
                        (o.isNull(JsonKeys.NAME)) ? null : (String) o.get(JsonKeys.NAME),
                        (o.isNull(JsonKeys.PUBLISHED_BY)) ? null : new Username((String) o.get(JsonKeys.PUBLISHED_BY)),
                        (o.isNull(JsonKeys.TITLE)) ? null : (String)  o.get(JsonKeys.TITLE),
                        (o.isNull(JsonKeys.LOCKED_BY)) ? null : new Username((String) o.get(JsonKeys.LOCKED_BY)),
                        (o.isNull(JsonKeys.TYPE)) ? null : ResourceType.valueOf((String) o.get(JsonKeys.TYPE)),
                        o.getInt(JsonKeys.CHILD_COUNT),
                        o.getInt(JsonKeys.FOLDER_COUNT),
                        o.getBoolean(JsonKeys.INCLUDE_IN_MAIN_MENU),
                        (o.isNull(JsonKeys.SORT_ORDER)) ? null : (String) o.get(JsonKeys.SORT_ORDER),
                        o.getBoolean(JsonKeys.HAS_WORKING_COPY),
                        new Date(o.getLong(JsonKeys.DATE_CREATED)),
                        new Date(o.getLong(JsonKeys.DATE_CHANGED)),
                        (o.isNull(JsonKeys.TEMPLATE_ID)) ? null : new ID((String) o.get(JsonKeys.TEMPLATE_ID)),
                        (o.isNull(JsonKeys.TAGS)) ? null : (String) o.get(JsonKeys.TAGS),
                        (o.isNull(JsonKeys.ABSOLUTE_PATH)) ? null : (String) o.get(JsonKeys.ABSOLUTE_PATH),
                        (o.isNull(JsonKeys.INDEX_PAGE_ID)) ? null : new ID((String) o.get(JsonKeys.INDEX_PAGE_ID)),
                        (o.isNull(JsonKeys.DESCRIPTION)) ? null : (String) o.get(JsonKeys.DESCRIPTION)
                    )
                );
            }
            return rs;
        } catch (final JSONException e) {
            throw new WebApplicationException(e);
        }
    }

}
