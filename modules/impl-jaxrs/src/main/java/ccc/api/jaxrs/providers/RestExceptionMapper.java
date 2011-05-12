/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.api.jaxrs.providers;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Variant;
import javax.ws.rs.ext.ExceptionMapper;

import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.core.Headers;

import ccc.api.core.Failure;
import ccc.api.exceptions.CCException;
import ccc.api.exceptions.ConflictException;
import ccc.api.exceptions.InvalidException;
import ccc.api.exceptions.UnauthorizedException;
import ccc.api.types.HttpStatusCode;
import ccc.plugins.PluginFactory;
import ccc.plugins.s11n.S11nException;
import ccc.plugins.s11n.Serializers;


/**
 * An mapper for 'command failed' exceptions.
 *
 * @author Civic Computing Ltd.
 */
@Provider
public class RestExceptionMapper
    implements
        ExceptionMapper<CCException> {

    @Context private Request _request;


    /**
     * Constructor.
     */
    public RestExceptionMapper() { super(); }


    /**
     * Constructor.
     *
     * @param request The JAX-RS request for the mapper.
     */
    public RestExceptionMapper(@Context final Request request) {
        _request = request;
    }


    /** {@inheritDoc} */
    @Override
    public Response toResponse(final CCException e) {

        final List<Variant> variants = new ArrayList<Variant>();
        variants.add(new Variant(MediaType.TEXT_HTML_TYPE,        null, null));
        variants.add(new Variant(MediaType.APPLICATION_JSON_TYPE, null, null));

        final Variant v = _request.selectVariant(variants);

        int statusCode = HttpStatusCode.ERROR;

        if (e instanceof UnauthorizedException) {
            statusCode = HttpStatusCode.UNAUTHORIZED;

        } else if (e instanceof ConflictException) {
            statusCode = HttpStatusCode.CONFLICT;

        } else if (e instanceof InvalidException) {
            statusCode = HttpStatusCode.BAD_REQUEST;

        }

        return
            Response
                .status(statusCode)
                .type(v.getMediaType())
                .entity(e.getFailure())
                .build();
    }


    /**
     * Map from a response to the corresponding exception.
     *
     * @param <T> The type of exception expected.
     * @param body The HTTP response body.
     *
     * @return The corresponding exception.
     */
    @SuppressWarnings("unchecked")
    public static <T extends CCException> T fromResponse(final String body) {

        Failure f;
        try {
            final Serializers sFactory = new PluginFactory().serializers();
            f = sFactory.create(Failure.class).read(body);
        } catch (final S11nException e) {
            throw new CCException(body);
        }

        try {
            final T ex = (T) Class.forName(f.getCode()).newInstance();
            ex.fillInStackTrace(); // Removes reflection stack entries.
            ex.setId(f.getId());
            ex.setParams(f.getParams());
            return ex;

        } catch (final InstantiationException e) {
            throw new CCException(e.getMessage());
        } catch (final IllegalAccessException e) {
            throw new CCException(e.getMessage());
        } catch (final ClassNotFoundException e) {
            throw new CCException(e.getMessage());
        }
    }


    /**
     * Map from a response to the corresponding exception.
     *
     * @param <T> The type of exception expected.
     * @param is  The HTTP response body.
     * @param mt  The media type of the response.
     *
     * @return The corresponding exception.
     */
    @SuppressWarnings("unchecked")
    public static <T extends CCException> T fromResponse(final InputStream is,
                                                         final MediaType mt) {

        try {
            final S11nProvider<Failure> s11n = new S11nProvider<Failure>();
            final Failure f =
                s11n.readFrom(
                    Failure.class,
                    Failure.class,
                    new Annotation[] {},
                    mt,
                    new Headers<String>(),
                    is);

            try {
                final T ex = (T) Class.forName(f.getCode()).newInstance();
                ex.fillInStackTrace(); // Removes reflection stack entries.
                ex.setId(f.getId());
                ex.setParams(f.getParams());
                return ex;

            } catch (final InstantiationException e) {
                throw new CCException(e.getMessage());
            } catch (final IllegalAccessException e) {
                throw new CCException(e.getMessage());
            } catch (final ClassNotFoundException e) {
                throw new CCException(e.getMessage());
            }
        } catch (final IOException e) {
            throw new CCException(e.getMessage());
        }
    }
}
