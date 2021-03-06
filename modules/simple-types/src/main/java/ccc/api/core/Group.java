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
package ccc.api.core;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import ccc.api.types.DBC;


/**
 * DTO for a user group.
 *
 * @author Civic Computing Ltd.
 */
public class Group
    extends
        Res {

    private String _name;
    private UUID _id;
    private Set<String> _permissions = new HashSet<String>();


    /**
     * Constructor.
     */
    public Group() { super(); }


    /**
     * Accessor.
     *
     * @return Returns the name.
     */
    public final String getName() {
        return _name;
    }


    /**
     * Mutator.
     *
     * @param name The name to set.
     */
    public final void setName(final String name) {
        DBC.require().notEmpty(name);
        _name = name;
    }


    /**
     * Accessor.
     *
     * @return Returns the permissions.
     */
    public final Set<String> getPermissions() {
        return _permissions;
    }


    /**
     * Mutator.
     *
     * @param permissions The permissions to set.
     */
    public final void setPermissions(final Set<String> permissions) {
        DBC.require().notNull(permissions);
        _permissions = permissions;
    }


    /**
     * Accessor.
     *
     * @return Returns the ID.
     */
    public final UUID getId() {
        return _id;
    }


    /**
     * Mutator.
     *
     * @param id The ID to set.
     */
    public final void setId(final UUID id) {
        _id = id;
    }


    /**
     * Link..
     *
     * @return A link to this group.
     */
    public String self() { return getLink("self"); }



    /**
     * Property names for this class.
     *
     * @author Civic Computing Ltd.
     */
    public static final class Properties {
        private Properties() { super(); }

        /** NAME : String. */
        public static final String NAME = "name";
        /** ID : String. */
        public static final String ID   = "id";
    }
}
