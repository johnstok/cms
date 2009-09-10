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
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.json.JSONArray;
import org.json.JSONException;


/**
 * A provider for a collection of strings.
 *
 * @author Civic Computing Ltd.
 */
@Provider
@Produces("application/json")
@Consumes("application/json")
public class StringCollectionWriter
    extends
        AbstractProvider
    implements
        MessageBodyWriter<Collection<String>>,
        MessageBodyReader<Collection<String>> {


    /** {@inheritDoc} */
    @Override
    public long getSize(final Collection<String> object,
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
        final boolean isWriteable = isCollectionOfType(String.class, type);
        return isWriteable;
    }

    /** {@inheritDoc} */
    @Override
    public void writeTo(final Collection<String> object,
                        final Class<?> clazz,
                        final Type type,
                        final Annotation[] annotations,
                        final MediaType mediaType,
                        final MultivaluedMap<String, Object> httpHeaders,
                        final OutputStream outputStream) {

        final PrintWriter pw = new PrintWriter(outputStream);
        pw.println("[");
        int a = 0;
        for (String rs : object) {
            try {
                rs = URLEncoder.encode(rs, "UTF-8");
            } catch (final UnsupportedEncodingException e) {
                // FIXME
            }

            a++;
            if (a == object.size()) {
                pw.println("\n\""+rs+"\"");
            } else {
                pw.println("\n\""+rs+"\",");
            }
        }
        pw.println("]");
        pw.flush();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isReadable(final Class<?> arg0,
                              final Type type,
                              final Annotation[] arg2,
                              final MediaType arg3) {
        final boolean isReadable = isCollectionOfType(String.class, type);
        return isReadable;
    }

    /** {@inheritDoc} */
    @Override
    public Collection<String> readFrom(
                                    final Class<Collection<String>> arg0,
                                    final Type arg1,
                                    final Annotation[] arg2,
                                    final MediaType arg3,
                                    final MultivaluedMap<String, String> arg4,
                                    final InputStream arg5) throws IOException {
        final String s = readString(arg3, arg5);

        try {
            final JSONArray a = new JSONArray(s);
            final Collection<String> strings = new ArrayList<String>();
            for (int i=0; i<a.length(); i++) {
                strings.add((String) a.get(i));
            }
            return strings;
        } catch (final JSONException e) {
            throw new WebApplicationException(e);
        }
    }
}
