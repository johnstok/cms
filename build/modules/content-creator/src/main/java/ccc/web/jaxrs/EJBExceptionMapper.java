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
package ccc.web.jaxrs;

import javax.ejb.EJBException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.apache.log4j.Logger;

import ccc.api.exceptions.InternalError;
import ccc.api.exceptions.RestException;
import ccc.api.jaxrs.providers.RestExceptionMapper;


/**
 * An mapper for 'command failed' exceptions.
 *
 * @author Civic Computing Ltd.
 */
public class EJBExceptionMapper
    implements
        ExceptionMapper<EJBException> {
    private static final Logger LOG =
        Logger.getLogger(EJBExceptionMapper.class);

    /** {@inheritDoc} */
    @Override
    public Response toResponse(final EJBException e) {

        if (e.getCausedByException() instanceof RestException) {
            return
                new RestExceptionMapper().toResponse(
                    (RestException) e.getCausedByException());
        }

        final RestException re = new InternalError();
        LOG.warn(
            "Converted unexpected EJB exception to error: "
            + re.getFailure().getExceptionId(), e);
        return new RestExceptionMapper().toResponse(re);
    }
}
