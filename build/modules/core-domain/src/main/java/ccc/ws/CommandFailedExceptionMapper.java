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
package ccc.ws;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

import ccc.commands.CommandFailedException;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class CommandFailedExceptionMapper
    implements
        ExceptionMapper<CommandFailedException> {

    /** {@inheritDoc} */
    @Override
    public Response toResponse(final CommandFailedException e) {
        return
            Response.status(Status.INTERNAL_SERVER_ERROR)
                    .type("application/json")
                    .entity(e.getFailure())
                    .build();
    }
}
