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

package ccc.domain;

import ccc.api.types.DBC;



/**
 * A CCC principal.
 *
 * @author Civic Computing Ltd.
 */
public abstract class Principal
    extends
        Entity {

    private String _name;


    /**
     * Constructor.
     */
    protected Principal() {
        setName(getId().toString());
    }


    /**
     * Accessor for the name property.
     *
     * @return The name.
     */
    public String getName() {
        return _name;
    }


    /**
     * Mutator for the name.
     *
     * @param name The name.
     */
    public void setName(final String name) {
        _name = DBC.require().notEmpty(name);
    }


    /**
     * Check if the specified user is included by this principal.
     *
     * @param user The user to check.
     *
     * @return True if the user is included; false otherwise.
     */
    public abstract boolean includes(final UserEntity user);
}
