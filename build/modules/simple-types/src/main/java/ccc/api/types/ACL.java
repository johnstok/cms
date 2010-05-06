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
package ccc.api.types;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.JsonKeys;
import ccc.plugins.s11n.Jsonable2;


/**
 * A DTO encapsulating an access-control list.
 *
 * @author Civic Computing Ltd.
 */
public final class ACL implements Jsonable2, Serializable {

    private Set<Entry> _users = new HashSet<Entry>();
    private Set<Entry> _groups = new HashSet<Entry>();


    /**
     * Constructor.
     *
     * @param json The JSON representation of the ACL.
     */
    public ACL(final Json json) { fromJson(json); }


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


    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        json.set(JsonKeys.GROUPS, getGroups());
        json.set(JsonKeys.USERS, getUsers());
    }


    /** {@inheritDoc} */
    @Override
    public void fromJson(final Json json) {
        setGroups(Entry.unmap(json.getCollection(JsonKeys.GROUPS)));
        setUsers(Entry.unmap(json.getCollection(JsonKeys.USERS)));
    }


    public static class Entry implements Jsonable2, Serializable {

        public UUID    _principal;
        public String  _name;
        public boolean _canRead;
        public boolean _canWrite;


        /**
         * Constructor.
         *
         * @param json The JSON representation of an ACL entry.
         */
        public Entry(final Json json) { fromJson(json); }


        /**
         * Constructor.
         */
        public Entry() { super(); }


        /**
         * Un-map from a JSON collection to an Entry collection.
         *
         * @param s The JSON collection.
         *
         * @return The corresponding ACL entry collection.
         */
        public static Collection<Entry> unmap(final Collection<Json> s) {
            final Set<Entry> uuids = new HashSet<Entry>();
            for (final Json json : s) {
                uuids.add(new Entry(json));
            }
            return uuids;
        }


        /** {@inheritDoc} */
        @Override
        public void fromJson(final Json json) {
            _principal = json.getId(JsonKeys.PRINCIPAL);
            _name      = json.getString(JsonKeys.NAME);
            _canRead   = json.getBool("can_read").booleanValue();
            _canWrite  = json.getBool("can_write").booleanValue();
        }


        /** {@inheritDoc} */
        @Override
        public void toJson(final Json json) {
            json.set(JsonKeys.PRINCIPAL, _principal);
            json.set(JsonKeys.NAME, _name);
            json.set("can_read", _canRead);
            json.set("can_write", _canWrite);
        }


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
