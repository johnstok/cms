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
package ccc.rest.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import ccc.serialization.Json;
import ccc.serialization.JsonKeys;
import ccc.serialization.Jsonable;
import ccc.types.DBC;


/**
 * A DTO encapsulating an access-control list.
 *
 * @author Civic Computing Ltd.
 */
public final class AclDto implements Jsonable, Serializable {

    private Set<UUID> _users = new HashSet<UUID>();
    private Set<UUID> _groups = new HashSet<UUID>();


    /**
     * Constructor.
     *
     * @param json The JSON representation of the ACL.
     */
    public AclDto(final Json json) { fromJson(json); }


    /**
     * Constructor.
     */
    public AclDto() { super(); }


    /**
     * Accessor.
     *
     * @return Returns the users.
     */
    public Set<UUID> getUsers() {
        return _users;
    }


    /**
     * Mutator.
     *
     * @param users The users to set.
     *
     * @return Returns 'this' reference, to allow method chaining.
     */
    public AclDto setUsers(final Collection<UUID> users) {
        DBC.require().notNull(users);
        _users = new HashSet<UUID>(users);
        return this;
    }


    /**
     * Accessor.
     *
     * @return Returns the groups.
     */
    public Set<UUID> getGroups() {
        return _groups;
    }


    /**
     * Mutator.
     *
     * @param groups The groups to set.
     *
     * @return Returns 'this' reference, to allow method chaining.
     */
    public AclDto setGroups(final Collection<UUID> groups) {
        DBC.require().notNull(groups);
        _groups = new HashSet<UUID>(groups);
        return this;
    }


    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        json.setStrings(JsonKeys.GROUPS, mapString(getGroups()));
        json.setStrings(JsonKeys.USERS, mapString(getUsers()));
    }


    private void fromJson(final Json json) {
        setGroups(mapUuid(json.getStrings(JsonKeys.GROUPS)));
        setUsers(mapUuid(json.getStrings(JsonKeys.USERS)));
    }


    // TODO Duplicated on UserDto.
    private Set<String> mapString(final Set<UUID> roles) {
        final Set<String> strings = new HashSet<String>();
        for (final UUID role : roles) {
            strings.add(role.toString());
        }
        return strings;
    }


 // TODO Duplicated on UserDto.
    private Collection<UUID> mapUuid(final Collection<String> s) {
        final List<UUID> uuids = new ArrayList<UUID>();
        for (final String string : s) {
            uuids.add(UUID.fromString(string));
        }
        return uuids;
    }
}
