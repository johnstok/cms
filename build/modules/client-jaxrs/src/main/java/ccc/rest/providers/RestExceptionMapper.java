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
package ccc.rest.providers;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import ccc.plugins.s11n.json.JsonImpl;
import ccc.rest.exceptions.ConflictException;
import ccc.rest.exceptions.EntityNotFoundException;
import ccc.rest.exceptions.InternalError;
import ccc.rest.exceptions.InvalidException;
import ccc.rest.exceptions.RestException;
import ccc.rest.exceptions.UnauthorizedException;
import ccc.types.HttpStatusCode;


/**
 * An mapper for 'command failed' exceptions.
 *
 * @author Civic Computing Ltd.
 */
public class RestExceptionMapper
    implements
        ExceptionMapper<RestException> {

    /** {@inheritDoc} */
    @Override
    public Response toResponse(final RestException e) {

        int statusCode = HttpStatusCode.ERROR;

        if (e instanceof UnauthorizedException) {
            statusCode = HttpStatusCode.UNAUTHORIZED;

        } else if (e instanceof ConflictException) {
            statusCode = HttpStatusCode.CONFLICT;

        } else if (e instanceof EntityNotFoundException) {
            statusCode = HttpStatusCode.NOT_FOUND;

        } else if (e instanceof InvalidException) {
            statusCode = HttpStatusCode.BAD_REQUEST;

        } else if (e instanceof InternalError) {
            statusCode = HttpStatusCode.ERROR;

        }

        return
            Response.status(statusCode)
                    .type("application/json")
                    .entity(e)
                    .build();
    }

    /**
     * Map from a response to the corresponding exception.
     *
     * @param <T> The type of exception expected.
     * @param statusCode The HTTP status code.
     * @param body The HTTP response body.
     *
     * @return The corresponding exception.
     */
    @SuppressWarnings("unchecked")
    public <T extends RestException> T fromResponse(final int statusCode,
                                                    final String body) {
        switch (statusCode) {

            case HttpStatusCode.UNAUTHORIZED:
                return (T) new UnauthorizedException(new JsonImpl(body));

            case HttpStatusCode.CONFLICT:
                return (T) new ConflictException(new JsonImpl(body));

            case HttpStatusCode.ERROR:
                return (T) new InternalError(new JsonImpl(body));

            case HttpStatusCode.NOT_FOUND:
                return (T) new EntityNotFoundException(new JsonImpl(body));

            case HttpStatusCode.BAD_REQUEST:
                return (T) new InvalidException(new JsonImpl(body));

            default:
                return (T) new InternalError(new JsonImpl(body));

        }
    }
}
