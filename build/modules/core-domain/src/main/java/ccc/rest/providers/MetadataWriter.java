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
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.json.JSONException;
import org.json.JSONObject;

import ccc.serialization.JsonImpl;



/**
 * A provider for string maps.
 *
 * @author Civic Computing Ltd.
 */
@Provider
@Produces("application/json")
public class MetadataWriter
    extends
        AbstractProvider
    implements
        MessageBodyWriter<Map<String, String>>,
        MessageBodyReader<Map<String, String>> {


    /** {@inheritDoc} */
    @Override
    public long getSize(final Map<String, String> object,
                        final Class<?> clazz,
                        final Type type,
                        final Annotation[] annotations,
                        final MediaType mediaType) {
        return -1;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isWriteable(final Class<?> clazz,
                               final Type type,
                               final Annotation[] annotations,
                               final MediaType mediaType) {
        final boolean isWriteable = isMapOfType(String.class, type);
        return isWriteable;
    }

    /** {@inheritDoc} */
    @Override
    public void writeTo(final Map<String, String> object,
                        final Class<?> clazz,
                        final Type type,
                        final Annotation[] annotations,
                        final MediaType mediaType,
                        final MultivaluedMap<String, Object> httpHeaders,
                        final OutputStream outputStream) {
        final PrintWriter pw = new PrintWriter(outputStream);
        pw.println(new JsonImpl(object).getDetail());
        pw.flush();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isReadable(final Class<?> arg0,
                              final Type type,
                              final Annotation[] arg2,
                              final MediaType arg3) {
        final boolean isReader = isMapOfType(String.class, type);
        return isReader;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, String> readFrom(
                                    final Class<Map<String, String>> arg0,
                                    final Type arg1,
                                    final Annotation[] arg2,
                                    final MediaType arg3,
                                    final MultivaluedMap<String, String> arg4,
                                    final InputStream arg5) throws IOException {
        try {
            final JSONObject o = new JSONObject(readString(arg3, arg5));
            final Map<String, String> stringMap = new HashMap<String, String>();
            for (final Iterator<String> i = o.keys(); i.hasNext();) {
                final String mapKey = i.next();
                final String mapValue = (String) o.get(mapKey);
                stringMap.put(mapKey, mapValue);
            }
            return stringMap;
        } catch (final JSONException e) {
            throw new WebApplicationException(e);
        }
    }
}
