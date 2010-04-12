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

import ccc.api.Security;
import ccc.api.types.DBC;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class SecurityImpl2 {

    private final Security _delegate;

    /**
     * Constructor.
     *
     * @param delegate
     */
    public SecurityImpl2(final Security delegate) {
        _delegate = DBC.require().notNull(delegate);
    }

    /**
     * @return
     * @see ccc.api.Security#isLoggedIn()
     */
    public Boolean isLoggedIn() {

        return _delegate.isLoggedIn();
    }

    /**
     * @param username
     * @param password
     * @return
     * @see ccc.api.Security#login(java.lang.String, java.lang.String)
     */
    public Boolean login(final String username, final String password) {

        return _delegate.login(username, password);
    }

    /**
     *
     * @see ccc.api.Security#logout()
     */
    public void logout() {

        _delegate.logout();
    }

    /**
     * @return
     * @see ccc.api.Security#readAllProperties()
     */
    public String readAllProperties() {

        return _delegate.readAllProperties();
    }



}
