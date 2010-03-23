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
package ccc.rest;

import java.util.UUID;

import ccc.types.DBC;


/**
 * An exception indicating that access to an entity was denied.
 *
 * @author Civic Computing Ltd.
 */
public class UnauthorizedException
    extends
        Exception {

    private final UUID _target;
    private final UUID _user;


    /**
     * Constructor.
     *
     * @param target The entity that couldn't be accessed.
     * @param user   The user trying to access the entity.
     */
    public UnauthorizedException(final UUID target, final UUID user) {
        _target = DBC.require().notNull(target);
        _user   = user; // NULL indicates anonymous access.
    }


    /** {@inheritDoc} */
    @Override
    public String getMessage() {
        return
            "User " + _user
            + " isn't authorized to access entity " + _target + ".";
    }
}