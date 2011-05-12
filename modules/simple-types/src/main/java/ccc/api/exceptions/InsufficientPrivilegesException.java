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
package ccc.api.exceptions;

import java.util.HashMap;
import java.util.UUID;

import ccc.api.types.CommandType;
import ccc.api.types.DBC;



/**
 * This exception indicates that a user attempted to perform an operation
 * without sufficient privileges.
 *
 * @author Civic Computing Ltd.
 */
public class InsufficientPrivilegesException
    extends
        CCException {

    private static final String ACTION = "action";
    private static final String USER   = "user";


    /** Constructor. */
    public InsufficientPrivilegesException() { super(); }


    /**
     * Constructor.
     *
     * @param action The action that was disallowed.
     * @param user The user attempting to perform the action.
     */
    public InsufficientPrivilegesException(final CommandType action,
                                           final UUID user) {
        super(
            "User "
                + user // NULL indicates anonymous access.
                + " may not perform action: "
                + DBC.require().notNull(action) + ".",
            null,
            new HashMap<String, String>() {{
                put(USER,   (null==user) ? null : user.toString());
                put(ACTION, action.toString());
            }});
    }


    /**
     * Accessor.
     *
     * @return Returns the action.
     */
    public CommandType getAction() {
        return CommandType.valueOf(getParam(ACTION));
    }


    /**
     * Accessor.
     *
     * @return Returns the user.
     */
    public UUID getUser() {
        final String user = getParam(USER);
        return (null==user) ? null : UUID.fromString(user);
    }
}
