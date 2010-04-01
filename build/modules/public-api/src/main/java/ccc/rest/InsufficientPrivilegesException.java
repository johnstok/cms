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
package ccc.rest;

import java.util.UUID;

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
        RestException {

    private final CommandType _action;
    private final UUID _user;

    /**
     * Constructor.
     *
     * @param action The action that was disallowed.
     * @param user The user attempting to perform the action.
     */
    public InsufficientPrivilegesException(final CommandType action,
                                           final UUID user) {
        super(new Failure(FailureCode.PRIVILEGES));
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
            + _user
            + " may not perform action: "
            + _action;
    }


    /**
     * Accessor.
     *
     * @return Returns the action.
     */
    public CommandType getAction() { return _action; }


    /**
     * Accessor.
     *
     * @return Returns the user.
     */
    public UUID getUser() { return _user; }
}
