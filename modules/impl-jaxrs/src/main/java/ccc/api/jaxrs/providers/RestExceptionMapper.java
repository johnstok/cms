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

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import ccc.api.core.Failure;
import ccc.api.exceptions.CCException;
import ccc.api.exceptions.ConflictException;
import ccc.api.exceptions.EntityNotFoundException;
import ccc.api.exceptions.InvalidException;
import ccc.api.exceptions.UnauthorizedException;
import ccc.api.types.HttpStatusCode;
import ccc.api.types.MimeType;
import ccc.plugins.s11n.InvalidSnapshotException;
import ccc.plugins.s11n.json.FailureSerializer;
import ccc.plugins.s11n.json.JsonImpl;


/**
 * An mapper for 'command failed' exceptions.
 *
 * @author Civic Computing Ltd.
 */
public class RestExceptionMapper
    implements
        ExceptionMapper<CCException> {

    /** {@inheritDoc} */
    @Override
    public Response toResponse(final CCException e) {
        return toResponse(e, MimeType.JSON);
    }

    /**
     * Convert an exception to a JAX-RS response.
     *
     * @param e The exception to convert.
     * @param responseType The mime type for the response.
     *
     * @return The corresponding JAX-RS response.
     */
    public Response toResponse(final CCException e,
                               final MimeType responseType) {

        int statusCode = HttpStatusCode.ERROR;

        if (e instanceof UnauthorizedException) {
            statusCode = HttpStatusCode.UNAUTHORIZED;

        } else if (e instanceof ConflictException) {
            statusCode = HttpStatusCode.CONFLICT;

        } else if (e instanceof EntityNotFoundException) {
            statusCode = HttpStatusCode.NOT_FOUND;

        } else if (e instanceof InvalidException) {
            statusCode = HttpStatusCode.BAD_REQUEST;

        }

        return
            Response.status(statusCode)
                .type(responseType.toString())
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
    public <T extends CCException> T fromResponse(final String body) {

        Failure f;
        try {
            f = new FailureSerializer().read(new JsonImpl(body));
        } catch (final InvalidSnapshotException e) {
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
}
