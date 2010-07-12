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

import ccc.api.core.Group;
import ccc.api.types.DBC;
import ccc.api.types.Link;
import ccc.api.types.NormalisingEncoder;


/**
 * A security group.
 *
 * @author Civic Computing Ltd.
 */
public class GroupEntity
    extends
        Principal {

    private Set<String> _permissions = new HashSet<String>();


    /** Constructor: for persistence only. */
    protected GroupEntity() { super(); }


    /**
     * Constructor.
     *
     * @param name The group's name.
     * @param permissions The group's permissions.
     */
    public GroupEntity(final String name, final String... permissions) {
        setName(name);
        setPermissions(new HashSet<String>(Arrays.asList(permissions)));
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
    public Group createDto() {
        final Group dto = new Group();
        dto.setId(getId());
        dto.setName(getName());
        dto.setPermissions(getPermissions());
        dto.addLink(
            "self",
            new Link(ccc.api.core.ResourceIdentifiers.Group.ELEMENT)
            .build("id", getId().toString(), new NormalisingEncoder()));
        return dto;
    }


    /**
     * Map a list of groups to a list of group DTOs.
     *
     * @param groups The groups to map.
     * @return The corresponding DTOs.
     */
    public static List<Group> map(final Collection<GroupEntity> groups) {
        final List<Group> mapped = new ArrayList<Group>();
        for (final GroupEntity g : groups) {
            mapped.add(g.createDto());
        }
        return mapped;
    }


    /** {@inheritDoc} */
    @Override
    public boolean includes(final UserEntity user) {
        return (null==user) ? false : user.getGroups().contains(this);
    }
}
