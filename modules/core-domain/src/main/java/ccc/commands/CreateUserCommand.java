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

import ccc.domain.CccCheckedException;
import ccc.domain.LogEntry;
import ccc.domain.User;
import ccc.persistence.GroupRepository;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.UserRepository;
import ccc.rest.dto.UserDto;
import ccc.serialization.JsonImpl;
import ccc.types.CommandType;
import ccc.types.EmailAddress;


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
     * @param repository The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     */
    public CreateUserCommand(final UserRepository repository,
                             final GroupRepository groups,
                             final LogEntryRepository audit) {
        _repository = repository;
        _audit = audit;
        _groups = groups;
    }

    /**
     * Create new user.
     *
     * @param delta The properties for the new user.
     * @param actor The user who performed the command.
     * @param happenedOn When the command was performed.
     *
     * @throws CccCheckedException If the command fails.
     *
     * @return Persisted user.
     */
    public User execute(final User actor,
                        final Date happenedOn,
                        final UserDto delta) throws CccCheckedException {
        final User user =
            new User(delta.getUsername(), delta.getName(), delta.getPassword());
        user.setEmail(new EmailAddress(delta.getEmail()));
        for (final UUID groupId : delta.getRoles()) {
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
