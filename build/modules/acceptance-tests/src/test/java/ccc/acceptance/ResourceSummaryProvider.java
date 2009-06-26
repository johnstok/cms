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
package ccc.acceptance;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import ccc.api.ResourceSummary;


/**
 * A {@link MessageBodyWriter} a collection of resource summaries.
 * TODO: Remove this class - it is a duplicate.
 *
 * @author Civic Computing Ltd.
 */
@Provider
@Produces("text/html")
@Consumes("text/html")
public class ResourceSummaryProvider
    implements
        MessageBodyWriter<Collection<ResourceSummary>>,
        MessageBodyReader<Collection<ResourceSummary>> {

    @Context private HttpServletRequest _request;

    /** {@inheritDoc} */
    @Override
    public long getSize(final Collection<ResourceSummary> object,
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

        final boolean isWriteable =
            isCollectionOfType(ResourceSummary.class, type);

        return isWriteable;
    }

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
    public void writeTo(final Collection<ResourceSummary> object,
                        final Class<?> clazz,
                        final Type type,
                        final Annotation[] annotations,
                        final MediaType mediaType,
                        final MultivaluedMap<String, Object> httpHeaders,
                        final OutputStream outputStream) {

        final PrintWriter pw = new PrintWriter(outputStream);
        pw.println("<html>");
        for (final ResourceSummary rs : object) {
            pw.println(
                "<h1><a href=\""
                +_request.getContextPath()
                +"/resource/"
                +rs.getId()
                +"\">"
                +rs.getName()
                +"</a></h1>");
        }
        pw.println("</html>");
        pw.flush();
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
        return new ArrayList<ResourceSummary>();
    }

}
