/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
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
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */

package ccc.api.jaxrs;

import java.io.UnsupportedEncodingException;

import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.ClientResponseFailure;

import ccc.api.exceptions.CCException;
import ccc.api.jaxrs.providers.RestExceptionMapper;
import ccc.commons.Reflection;



/**
 * Abstract base class for JAX-RS implementations of API services.
 *
 * @author Civic Computing Ltd.
 */
public abstract class JaxrsCollection {

    /**
     * Convert a runtime exception to a CC specific exception.
     *
     * @param e The runtime exception.
     *
     * @return The converted exception.
     */
    public RuntimeException convertException(final RuntimeException e) {
        if (e instanceof ClientResponseFailure) {
            final ClientResponseFailure ex = (ClientResponseFailure) e;
            try {
                final ClientResponse<byte[]> r = ex.getResponse();
                final String body = new String(r.getEntity(), "UTF-8");
                return
                    new RestExceptionMapper().fromResponse(body);
            } catch (final UnsupportedEncodingException ee) {
                throw new InternalError("Unsupported encoding.");
            }
        } else if (Reflection.isClass("javax.ejb.EJBException", e)) {
            if (e.getCause() instanceof CCException) {
                return (CCException) e.getCause();
            }
            return e;
        } else {
            return e;
        }
    }
}
