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

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import ccc.api.TemplateDelta;
import ccc.api.rest.TemplateNew;
import ccc.serialization.Json;
import ccc.serialization.JsonKeys;
import ccc.ws.AbstractProvider;


/**
 * A reader for templates.
 *
 * @author Civic Computing Ltd.
 */
@Provider
@Consumes("application/json")
public class TemplateNewReader
    extends
        AbstractProvider
    implements
        MessageBodyReader<TemplateNew> {

    /** {@inheritDoc} */
    @Override
    public boolean isReadable(final Class<?> clazz,
                              final Type type,
                              final Annotation[] annotations,
                              final MediaType mediaType) {
        return TemplateNew.class.equals(clazz);
    }

    /** {@inheritDoc} */
    @Override
    public TemplateNew readFrom(final Class<TemplateNew> clazz,
                            final Type type,
                            final Annotation[] annotations,
                            final MediaType mimetype,
                            final MultivaluedMap<String, String> httpHeaders,
                            final InputStream is) throws IOException {
        final Json json = readJson(mimetype, is);
        final TemplateDelta d = new TemplateDelta(json.getJson(JsonKeys.DELTA));
        return new TemplateNew(
            json.getId(JsonKeys.PARENT_ID),
            d,
            json.getString(JsonKeys.TITLE),
            json.getString(JsonKeys.DESCRIPTION),
            json.getString(JsonKeys.NAME));
    }
}
