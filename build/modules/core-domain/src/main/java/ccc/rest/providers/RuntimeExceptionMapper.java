/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
