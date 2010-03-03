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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ccc.rest.dto.GroupDto;
import ccc.serialization.Json;
import ccc.types.DBC;


/**
 * A security group.
 *
 * @author Civic Computing Ltd.
 */
public class Group
    extends
        Entity {

    private String _name;
    private Set<String> _permissions;


    /** Constructor: for persistence only. */
    protected Group() {
        setName(id().toString());
        setPermissions(new HashSet<String>());
    }


    /**
     * Constructor.
     *
     * @param name The group's name.
     * @param permissions The group's permissions.
     */
    public Group(final String name, final String... permissions) {
        setName(name);
        setPermissions(new HashSet<String>(Arrays.asList(permissions)));
    }


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
        return new HashSet<String>(_permissions);
    }


    /**
     * Mutator.
     *
     * @param permissions The permissions to set.
     */
    public final void setPermissions(final Set<String> permissions) {
        DBC.require().notNull(permissions);
        // TODO: Check for NULL & ZLS.
        _permissions = permissions;
    }


    /**
     * Query - determine whether this group has the specified permission.
     *
     * @param permission The permission name.
     *
     * @return True if the group has this permission; false otherwise.
     */
    public boolean hasPermission(final String permission) {
        return _permissions.contains(permission);
    }


    /**
     * Create a DTO for this group.
     *
     * @return A DTO representing this group.
     */
    public GroupDto createDto() {
        final GroupDto dto = new GroupDto();
        dto.setId(id());
        dto.setName(getName());
        dto.setPermissions(getPermissions());
        return dto;
    }


    /**
     * Map a list of groups to a list of group DTOs.
     *
     * @param groups The groups to map.
     * @return The corresponding DTOs.
     */
    public static Collection<GroupDto> map(final Collection<Group> groups) {
        final List<GroupDto> mapped = new ArrayList<GroupDto>();
        for (final Group g : groups) {
            mapped.add(g.createDto());
        }
        return mapped;
    }


    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        createDto().toJson(json);
    }
}
