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

import org.apache.log4j.Logger;

import ccc.types.Failure;
import ccc.types.FailureCode;
import ccc.types.HttpStatusCode;


/**
 * An mapper for runtime exceptions.
 *
 * @author Civic Computing Ltd.
 */
public class RuntimeExceptionMapper
    implements
        ExceptionMapper<RuntimeException> {
    private static final Logger LOG =
        Logger.getLogger(RuntimeExceptionMapper.class);

    /** {@inheritDoc} */
    @Override
    public Response toResponse(final RuntimeException e) {

        if (e instanceof org.jboss.resteasy.spi.Failure) {
            final org.jboss.resteasy.spi.Failure restEasyFailure =
                (org.jboss.resteasy.spi.Failure) e;
            final Response failureResponse = restEasyFailure.getResponse();
            if (null!=failureResponse) { return failureResponse; }
        }

        final Failure f = new Failure(FailureCode.UNEXPECTED);
        LOG.warn("Error invoking API via JAX-RS: "+f.getExceptionId(), e);

        return
            Response
                .status(HttpStatusCode.ERROR)
                .type("application/json")
                .entity(f)
                .build();
    }
}
