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

import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import ccc.api.core.ACL;
import ccc.api.core.ACL.Entry;
import ccc.api.exceptions.UnauthorizedException;
import ccc.api.types.CommandType;
import ccc.domain.AccessPermission;
import ccc.domain.GroupEntity;
import ccc.domain.ResourceEntity;
import ccc.domain.UserEntity;
import ccc.messaging.Producer;
import ccc.persistence.IRepositoryFactory;


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
    private final ResourceEntity _r;
    private final Producer       _producer;

    /**
     * Constructor.
     *
     * @@param repoFactory The repository factory for this command.
     * @param id The id of the resource to update.
     * @param acl The new access control list.
     */
    public UpdateResourceAclCommand(final IRepositoryFactory repoFactory,
                                    final Producer producer,
                                      final UUID id,
                                      final ACL acl) {
        super(repoFactory);
        _producer  = producer;
        _id = id;
        _acl = acl;
        _r = getRepository().find(ResourceEntity.class, _id);
    }

    /** {@inheritDoc} */
    @Override
    public Void doExecute(final UserEntity actor,
                          final Date happenedOn) {

        _r.confirmLock(actor);

        lookupGroups();
        lookupUsers();

        auditResourceCommand(actor, happenedOn, _r);

        _producer.broadcastMessage(
            CommandType.SEARCH_INDEX_RESOURCE,
            Collections.singletonMap("resource", _r.getId().toString()));

        return null;
    }


    private void lookupGroups() {
        _r.clearGroupAcl();
        for (final Entry e : _acl.getGroups()) {
            _r.addGroupPermission(
                new AccessPermission(
                    e.isReadable(),
                    e.isWriteable(),
                    getGroups().find(e.getPrincipal())));
        }
    }


    private void lookupUsers() {
        _r.clearUserAcl();
        for (final Entry e : _acl.getUsers()) {
            _r.addUserPermission(
                new AccessPermission(
                    e.isReadable(),
                    e.isWriteable(),
                    getUsers().find(e.getPrincipal())));
        }
    }


    @Override
    protected void authorize(final UserEntity actor) {
        if (!_r.isWriteableBy(actor)){
            final Set<GroupEntity> groups = actor.getGroups();
            for (final GroupEntity group : groups) {
                if (group.getName().equals("ADMINISTRATOR")) {
                    return;
                }
            }
            throw new UnauthorizedException(_id, actor.getId());
        }
    }


    /** {@inheritDoc} */
    @Override
    protected CommandType getType() {
        return CommandType.RESOURCE_CHANGE_ROLES;
    }
}
