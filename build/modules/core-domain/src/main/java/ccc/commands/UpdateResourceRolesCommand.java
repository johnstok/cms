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
package ccc.commands;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import ccc.domain.CccCheckedException;
import ccc.domain.EntityNotFoundException;
import ccc.domain.Group;
import ccc.domain.LogEntry;
import ccc.domain.Resource;
import ccc.domain.User;
import ccc.persistence.GroupRepository;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.ResourceRepository;
import ccc.persistence.UserRepository;
import ccc.rest.dto.AclDto;
import ccc.serialization.JsonImpl;
import ccc.types.CommandType;


/**
 * Command: update cache duration.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateResourceRolesCommand
    extends
        Command<Void> {

    private final UUID _id;
    private final AclDto _roles;
    private final GroupRepository _groups;


    /**
     * Constructor.
     *
     * @param repository The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     * @param id The id of the resource to update.
     * @param acl The new access control list.
     */
    public UpdateResourceRolesCommand(final ResourceRepository repository,
                                      final LogEntryRepository audit,
                                      final GroupRepository group,
                                      final UserRepository users,
                                      final UUID id,
                                      final AclDto acl) {
        super(repository, audit, users, null);
        _id = id;
        _roles = acl;
        _groups = group;
    }

    /** {@inheritDoc} */
    @Override
    public Void doExecute(final User actor,
                          final Date happenedOn) throws CccCheckedException {

        final Resource r = getRepository().find(Resource.class, _id);
        r.confirmLock(actor);

        lookupGroups(r);
        lookupUsers(r);

        final LogEntry le = new LogEntry(
            actor,
            CommandType.RESOURCE_CHANGE_ROLES,
            happenedOn,
            _id,
            new JsonImpl(r).getDetail());
        getAudit().record(le);

        return null;
    }


    private void lookupGroups(final Resource r) throws EntityNotFoundException {
        final Set<Group> groups = new HashSet<Group>();
        for (final UUID groupId : _roles.getGroups()) {
            groups.add(_groups.find(groupId));
        }
        r.roles(groups);
    }


    private void lookupUsers(final Resource r) throws EntityNotFoundException {
        final Set<User> users = new HashSet<User>();
        for (final UUID userId : _roles.getUsers()) {
            users.add(getUsers().find(userId));
        }
        r.setUserAcl(users);
    }


    /** {@inheritDoc} */
    @Override
    protected CommandType getType() {
        return CommandType.RESOURCE_CHANGE_ROLES;
    }
}
