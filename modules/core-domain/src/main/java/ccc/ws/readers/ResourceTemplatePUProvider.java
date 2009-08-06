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
package ccc.ws.readers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import ccc.api.Json;
import ccc.api.JsonKeys;
import ccc.api.rest.ResourceTemplatePU;
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
@Produces("application/json")
public class ResourceTemplatePUProvider
    implements
        MessageBodyReader<ResourceTemplatePU> {

    /** {@inheritDoc} */
    @Override
    public boolean isReadable(final Class<?> clazz,
                              final Type type,
                              final Annotation[] annotations,
                              final MediaType mediaType) {
        return ResourceTemplatePU.class.equals(clazz);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceTemplatePU readFrom(final Class<ResourceTemplatePU> arg0,
                             final Type arg1,
                             final Annotation[] arg2,
                             final MediaType arg3,
                             final MultivaluedMap<String, String> arg4,
                             final InputStream arg5) throws IOException, WebApplicationException {
        final String body = readString(arg3, arg5);
        final Json json = new Snapshot(body);
        return new ResourceTemplatePU(json.getId(JsonKeys.TEMPLATE_ID));
    }

    /**
     * TODO: Add a description for this method.
     *
     * @param arg3
     * @param arg5
     * @return
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
    private String readString(final MediaType arg3, final InputStream arg5) throws IOException, UnsupportedEncodingException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IO.copy(arg5, baos);
        final String charset = arg3.getParameters().get("charset");
        final String s = new String(baos.toByteArray(), (null==charset) ? "UTF8" : charset).trim();
        return s;
    }
}
