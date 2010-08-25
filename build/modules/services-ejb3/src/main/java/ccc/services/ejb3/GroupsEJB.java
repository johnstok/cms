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
package ccc.services.ejb3;

import static ccc.api.types.CommandType.*;
import static javax.ejb.TransactionAttributeType.*;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;

import ccc.api.core.Group;
import ccc.api.core.Groups;
import ccc.api.core.PagedCollection;
import ccc.api.types.Permission;
import ccc.domain.GroupEntity;
import ccc.domain.LogEntry;
import ccc.persistence.GroupRepository;
import ccc.plugins.PluginFactory;
import ccc.plugins.s11n.Serializers;


/**
 * EJB implementation of the {@link Groups} interface.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=Groups.NAME)
@TransactionAttribute(REQUIRED)
@Local(Groups.class)
@RolesAllowed({})
public class GroupsEJB
    extends
        AbstractEJB
    implements
        Groups {


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(Permission.GROUP_CREATE)
    public Group create(final Group comment) { // FIXME: Factor into a command.
        final GroupEntity g = new GroupEntity(comment.getName());
        g.setPermissions(comment.getPermissions());

        getRepoFactory().createGroupRepo().create(g);

        final Group result = g.createDto();

        final Serializers sFactory = new PluginFactory().serializers();
        final String data = sFactory.create(Group.class).write(result);

        getRepoFactory().createLogEntryRepo().record(
            new LogEntry(
                currentUser(),
                GROUP_CREATE,
                new Date(),
                g.getId(),
                data));

        return result;
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed(Permission.GROUP_READ)
    public Group retrieve(final UUID id) {
        return getRepoFactory().createGroupRepo().find(id).createDto();
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed(Permission.GROUP_READ)
    public PagedCollection<Group> query(
        final String name,
        final int pageNo,
        final int pageSize) {

        final GroupRepository gr = getRepoFactory().createGroupRepo();
        final long totalCount = gr.totalCount(name);

        final Collection<GroupEntity> groups = gr.list(name,
                                                       pageNo,
                                                       pageSize);

        final PagedCollection<Group> pGroups =
            new PagedCollection<Group>(
                totalCount, Group.class, GroupEntity.map(groups));
        pGroups.addLink(
            "self",
            ccc.api.core.ResourceIdentifiers.Group.COLLECTION
            + "?{-join|&|count,page,sort,order}");

        return pGroups;
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(Permission.GROUP_UPDATE)
    public Group update(final UUID id, final Group group) {
        final GroupEntity g = getRepoFactory().createGroupRepo().find(id);
        g.setName(group.getName());
        g.setPermissions(group.getPermissions());

        final Group result = g.createDto();

        final Serializers sFactory = new PluginFactory().serializers();
        final String data = sFactory.create(Group.class).write(result);

        getRepoFactory().createLogEntryRepo().record(
            new LogEntry(
                currentUser(),
                GROUP_UPDATE,
                new Date(),
                g.getId(),
                data));

        return result;
    }
}
