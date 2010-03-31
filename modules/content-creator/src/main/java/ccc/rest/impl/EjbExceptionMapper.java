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
package ccc.rest.impl;

import javax.ejb.EJBException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.apache.log4j.Logger;

import ccc.rest.RestException;
import ccc.rest.UnauthorizedException;
import ccc.rest.providers.CommandFailedExceptionMapper;
import ccc.rest.providers.UnauthorizedExceptionMapper;
import ccc.types.Failure;
import ccc.types.FailureCode;
import ccc.types.HttpStatusCode;


/**
 * An mapper for EJB exceptions.
 *
 * @author Civic Computing Ltd.
 */
public class EjbExceptionMapper
    implements
        ExceptionMapper<EJBException> {
    private static final Logger LOG =
        Logger.getLogger(EjbExceptionMapper.class);

    /** {@inheritDoc} */
    @Override
    public Response toResponse(final EJBException e) {

        final Exception cause = e.getCausedByException();

        if (cause instanceof UnauthorizedException) {
            return
                new UnauthorizedExceptionMapper()
                    .toResponse((UnauthorizedException) cause);

        } else if (cause instanceof RestException) {
            return
                new CommandFailedExceptionMapper()
                    .toResponse((RestException) cause);

        } else {
            final Failure f = new Failure(FailureCode.UNEXPECTED);
            LOG.warn(
                "EJBException invoking API via JAX-RS: "+f.getExceptionId(), e);
            return
                Response
                    .status(HttpStatusCode.ERROR)
                    .type("application/json")
                    .entity(f)
                    .build();
        }
    }
}
