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
package ccc.domain;

import ccc.rest.RestException;
import ccc.types.CommandType;
import ccc.types.DBC;
import ccc.types.Failure;
import ccc.types.FailureCode;



/**
 * This exception indicates that a user attempted to perform an operation
 * without sufficient privileges.
 *
 * @author Civic Computing Ltd.
 */
public class InsufficientPrivilegesException
    extends
        CccCheckedException {

    private final CommandType _action;
    private final User _user;

    /**
     * Constructor.
     *
     * @param action The action that was disallowed.
     * @param user The user attempting to perform the action.
     */
    public InsufficientPrivilegesException(final CommandType action,
                                           final User user) {
        DBC.require().notNull(action);
        DBC.require().notNull(user);
        _action = action;
        _user = user;
    }

    /** {@inheritDoc} */
    @Override
    public String getMessage() {
        return
            "User "
            + _user.username()
            + _user.roles()
            + " may not perform action: "
            + _action;
    }

    /** {@inheritDoc} */
    @Override
    public RestException toRemoteException() {
        return new RestException(new Failure(FailureCode.PRIVILEGES));
    }
}
