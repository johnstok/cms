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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import ccc.rest.dto.PageDelta;
import ccc.rest.dto.PageNew;
import ccc.serialization.Json;
import ccc.serialization.JsonKeys;


/**
 * A reader for templates.
 *
 * @author Civic Computing Ltd.
 */
@Provider
@Consumes("application/json")
public class PageNewReader
    extends
        AbstractProvider
    implements
        MessageBodyReader<PageNew> {

    /** {@inheritDoc} */
    @Override
    public boolean isReadable(final Class<?> clazz,
                              final Type type,
                              final Annotation[] annotations,
                              final MediaType mediaType) {
        return PageNew.class.equals(clazz);
    }

    /** {@inheritDoc} */
    @Override
    public PageNew readFrom(final Class<PageNew> clazz,
                            final Type type,
                            final Annotation[] annotations,
                            final MediaType mimetype,
                            final MultivaluedMap<String, String> httpHeaders,
                            final InputStream is) throws IOException {
        final Json json = readJson(mimetype, is);
        final PageDelta d = new PageDelta(json.getJson(JsonKeys.DELTA));
        return new PageNew(
            json.getId(JsonKeys.PARENT_ID),
            d,
            json.getString(JsonKeys.NAME),
            json.getId(JsonKeys.TEMPLATE_ID),
            json.getString(JsonKeys.TITLE),
            json.getString(JsonKeys.COMMENT),
            json.getBool(JsonKeys.MAJOR_CHANGE).booleanValue());
    }
}
