/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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

import org.jboss.resteasy.client.ClientResponseFailure;

import ccc.api.core.Security;
import ccc.api.types.DBC;


/**
 * JAX-RS implementation of the security API.
 *
 * @author Civic Computing Ltd.
 */
public class SecurityImpl2
    extends
        JaxrsCollection
    implements
        Security {

    private final Security _delegate;


    /**
     * Constructor.
     *
     * @param delegate
     */
    public SecurityImpl2(final Security delegate) {
        _delegate = DBC.require().notNull(delegate);
    }


    /** {@inheritDoc} */
    @Override
    public Boolean isLoggedIn() {
        try {
            return _delegate.isLoggedIn();
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public Boolean login(final String username, final String password) {
        try {
            return _delegate.login(username, password);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void logout() {
        try {
            _delegate.logout();
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public String readAllProperties() {
        try {
            return _delegate.readAllProperties();
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }
}
