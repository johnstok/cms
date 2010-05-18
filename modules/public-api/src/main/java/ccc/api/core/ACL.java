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

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import ccc.api.types.DBC;


/**
 * A DTO encapsulating an access-control list.
 *
 * @author Civic Computing Ltd.
 */
public final class ACL implements Serializable {

    private Set<Entry> _users = new HashSet<Entry>();
    private Set<Entry> _groups = new HashSet<Entry>();


    /**
     * Constructor.
     */
    public ACL() { super(); }


    /**
     * Accessor.
     *
     * @return Returns the users.
     */
    public Set<Entry> getUsers() {
        return _users;
    }


    /**
     * Mutator.
     *
     * @param users The users to set.
     *
     * @return Returns 'this' reference, to allow method chaining.
     */
    public ACL setUsers(final Collection<Entry> users) {
        DBC.require().notNull(users);
        _users = new HashSet<Entry>(users);
        return this;
    }


    /**
     * Accessor.
     *
     * @return Returns the groups.
     */
    public Set<Entry> getGroups() {
        return _groups;
    }


    /**
     * Mutator.
     *
     * @param groups The groups to set.
     *
     * @return Returns 'this' reference, to allow method chaining.
     */
    public ACL setGroups(final Collection<Entry> groups) {
        DBC.require().notNull(groups);
        _groups = new HashSet<Entry>(groups);
        return this;
    }


    public static class Entry implements Serializable {

        public UUID    _principal;
        public String  _name;
        public boolean _canRead;
        public boolean _canWrite;


        /**
         * Constructor.
         */
        public Entry() { super(); }


        /** {@inheritDoc} */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + (_canRead ? 1231 : 1237);
            result = prime * result + (_canWrite ? 1231 : 1237);
            result = prime * result + ((_name == null) ? 0 : _name.hashCode());
            result =
                prime
                * result
                + ((_principal == null) ? 0 : _principal.hashCode());
            return result;
        }


        /** {@inheritDoc} */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Entry other = (Entry) obj;
            if (_canRead != other._canRead) {
                return false;
            }
            if (_canWrite != other._canWrite) {
                return false;
            }
            if (_name == null) {
                if (other._name != null) {
                    return false;
                }
            } else if (!_name.equals(other._name)) {
                return false;
            }
            if (_principal == null) {
                if (other._principal != null) {
                    return false;
                }
            } else if (!_principal.equals(other._principal)) {
                return false;
            }
            return true;
        }


        /**
         * TODO: Add a description for this method.
         *
         * @return
         */
        @Deprecated
        // FIXME: _principal should be a reference to a user (with id & title).
        public String user() {
            return "/secure/users/"+_principal;
        }
    }
}
