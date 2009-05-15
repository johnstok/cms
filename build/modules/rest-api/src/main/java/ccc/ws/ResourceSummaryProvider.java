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
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import ccc.api.ResourceSummary;


/**
 * TODO: Add Description for this type.
 * TODO: Set char encoding?
 * TODO: Use velocity?
 * TODO: eTags?
 *
 * @author Civic Computing Ltd.
 */
@Provider
@Produces("text/html")
public class ResourceSummaryProvider implements MessageBodyWriter<Collection<ResourceSummary>>{

    @Context HttpServletRequest _request;

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
                        final OutputStream outputStream) throws IOException, WebApplicationException {

        final PrintWriter pw = new PrintWriter(outputStream);
        pw.println("<html>");
        for (final ResourceSummary rs : object) {
            pw.println("<h1><a href=\""+_request.getContextPath()+"/resource/"+rs.getId()+"\">"+rs.getName()+"</a></h1>");
        }
        pw.println("</html>");
        pw.flush();
    }

}
