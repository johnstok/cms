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

import ccc.api.core.ACL;
import ccc.api.core.ACL.Entry;
import ccc.api.types.CommandType;
import ccc.domain.AccessPermission;
import ccc.domain.LogEntry;
import ccc.domain.ResourceEntity;
import ccc.domain.UserEntity;
import ccc.persistence.IRepositoryFactory;
import ccc.plugins.s11n.json.JsonImpl;


/**
 * Command: update cache duration.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateResourceAclCommand
    extends
        Command<Void> {

    private final UUID _id;
    private final ACL _acl;


    /**
     * Constructor.
     *
     * @@param repoFactory The repository factory for this command.
     * @param id The id of the resource to update.
     * @param acl The new access control list.
     */
    public UpdateResourceAclCommand(final IRepositoryFactory repoFactory,
                                      final UUID id,
                                      final ACL acl) {
        super(repoFactory);
        _id = id;
        _acl = acl;
    }

    /** {@inheritDoc} */
    @Override
    public Void doExecute(final UserEntity actor,
                          final Date happenedOn) {

        final ResourceEntity r = getRepository().find(ResourceEntity.class, _id);
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


    private void lookupGroups(final ResourceEntity r) {
        r.clearGroupAcl();
        for (final Entry e : _acl.getGroups()) {
            r.addGroupPermission(
                new AccessPermission(
                    e._canRead, e._canWrite, getGroups().find(e._principal)));
        }
    }


    private void lookupUsers(final ResourceEntity r) {
        r.clearUserAcl();
        for (final Entry e : _acl.getUsers()) {
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
