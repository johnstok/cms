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
import java.util.UUID;

import ccc.domain.AccessPermission;
import ccc.domain.LogEntry;
import ccc.domain.Resource;
import ccc.domain.User;
import ccc.persistence.IRepositoryFactory;
import ccc.rest.dto.AclDto;
import ccc.rest.dto.AclDto.Entry;
import ccc.rest.exceptions.EntityNotFoundException;
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


    /**
     * Constructor.
     *
     * @@param repoFactory The repository factory for this command.
     * @param id The id of the resource to update.
     * @param acl The new access control list.
     */
    public UpdateResourceRolesCommand(final IRepositoryFactory repoFactory,
                                      final UUID id,
                                      final AclDto acl) {
        super(repoFactory);
        _id = id;
        _roles = acl;
    }

    /** {@inheritDoc} */
    @Override
    public Void doExecute(final User actor,
                          final Date happenedOn) {

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
        r.clearGroupAcl();
        for (final Entry e : _roles.getGroups()) {
            r.addGroupPermission(
                new AccessPermission(
                    e._canRead, e._canWrite, getGroups().find(e._principal)));
        }
    }


    private void lookupUsers(final Resource r) throws EntityNotFoundException {
        r.clearUserAcl();
        for (final Entry e : _roles.getUsers()) {
            r.addUserPermission(
                new AccessPermission(
                    e._canRead, e._canWrite, getUsers().find(e._principal)));
        }
    }


    /** {@inheritDoc} */
    @Override
    protected CommandType getType() {
        return CommandType.RESOURCE_CHANGE_ROLES;
    }
}
