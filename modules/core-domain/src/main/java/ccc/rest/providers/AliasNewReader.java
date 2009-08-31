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

import ccc.rest.AliasNew;
import ccc.serialization.Json;
import ccc.serialization.JsonKeys;


/**
 * A reader for aliases.
 *
 * @author Civic Computing Ltd.
 */
@Provider
@Consumes("application/json")
public class AliasNewReader
    extends
        AbstractProvider
    implements
        MessageBodyReader<AliasNew> {

    /** {@inheritDoc} */
    @Override
    public boolean isReadable(final Class<?> clazz,
                              final Type type,
                              final Annotation[] annotations,
                              final MediaType mediaType) {
        return AliasNew.class.equals(clazz);
    }

    /** {@inheritDoc} */
    @Override
    public AliasNew readFrom(final Class<AliasNew> clazz,
                              final Type type,
                              final Annotation[] annotations,
                              final MediaType mimetype,
                              final MultivaluedMap<String, String> httpHeaders,
                              final InputStream is) throws IOException {
        final Json json = readJson(mimetype, is);
        return new AliasNew(
            json.getId(JsonKeys.PARENT_ID),
            json.getString(JsonKeys.NAME),
            json.getId(JsonKeys.TARGET));
    }
}
