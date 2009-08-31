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

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import ccc.rest.ResourceTemplatePU;
import ccc.serialization.Json;
import ccc.serialization.JsonKeys;


/**
 * A {@link MessageBodyReader} for resource template partial updates.
 *
 * @author Civic Computing Ltd.
 */
@Provider
@Consumes("application/json")
@Produces("application/json")
public class ResourceTemplatePUProvider
    extends
        AbstractProvider
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
                             final InputStream arg5) throws IOException {
        final Json json = readJson(arg3, arg5);
        return new ResourceTemplatePU(json.getId(JsonKeys.TEMPLATE_ID));
    }
}
