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

import ccc.api.core.UserDto;
import ccc.api.types.CommandType;
import ccc.api.types.EmailAddress;
import ccc.domain.LogEntry;
import ccc.domain.UserEntity;
import ccc.persistence.GroupRepository;
import ccc.persistence.IRepositoryFactory;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.UserRepository;
import ccc.plugins.s11n.json.JsonImpl;


/**
 * Command: create a new user.
 *
 * @author Civic Computing Ltd.
 */
public class CreateUserCommand {

    private final UserRepository     _repository;
    private final GroupRepository    _groups;
    private final LogEntryRepository _audit;

    /**
     * Constructor.
     *
     * @param repoFactory The repository factory for this command.
     */
    public CreateUserCommand(final IRepositoryFactory repoFactory) {
        _repository = repoFactory.createUserRepo();
        _audit = repoFactory.createLogEntryRepo();
        _groups = repoFactory.createGroupRepo();
    }

    /**
     * Create new user.
     *
     * @param delta The properties for the new user.
     * @param actor The user who performed the command.
     * @param happenedOn When the command was performed.
     *
     * @return Persisted user.
     */
    public UserEntity execute(final UserEntity actor,
                        final Date happenedOn,
                        final UserDto delta) {
        final UserEntity user =
            new UserEntity(delta.getUsername(), delta.getName(), delta.getPassword());
        user.setEmail(new EmailAddress(delta.getEmail()));
        for (final UUID groupId : delta.getGroups()) {
            user.addGroup(_groups.find(groupId));
        }
        user.addMetadata(delta.getMetadata());
        _repository.create(user);

        _audit.record(
            new LogEntry(
                actor,
                CommandType.USER_CREATE,
                happenedOn,
                user.getId(),
                new JsonImpl(user).getDetail()));

        return user;
    }
}
