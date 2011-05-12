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
package ccc.api.exceptions;

import java.util.HashMap;
import java.util.UUID;

import ccc.api.types.DBC;


/**
 * An exception indicating that access to an entity was denied.
 *
 * @author Civic Computing Ltd.
 */
public class UnauthorizedException
    extends
        CCException {

    private static final String TARGET = "target";
    private static final String USER   = "user";


    /** Constructor. */
    public UnauthorizedException() { super(); }


    /**
     * Constructor.
     *
     * @param target The entity that couldn't be accessed.
     * @param user   The user trying to access the entity.
     */
    public UnauthorizedException(final UUID target, final UUID user) {
        super(
            "User "
                + user // NULL indicates anonymous access.
                + " isn't authorized to access entity "
                + DBC.require().notNull(target) + ".",
            null,
            new HashMap<String, String>() {{
                put(USER,   (null==user) ? null : user.toString());
                put(TARGET, target.toString());
            }});
    }


    /**
     * Accessor.
     *
     * @return Returns the target.
     */
    public UUID getTarget() {
        return UUID.fromString(getParam(TARGET));
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
