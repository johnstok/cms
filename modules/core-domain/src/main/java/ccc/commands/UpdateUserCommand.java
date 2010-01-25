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
 * Command: update a user.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateUserCommand
    extends
        Command<User> {

    private final UUID _userId;
    private final UserDto _delta;
    private final GroupRepository _groups;


    /**
     * Constructor.
     *
     * @param repository The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     * @param userId The id of the user to update.
     * @param delta The changes to apply.
     */
    public UpdateUserCommand(final UserRepository repository,
                             final LogEntryRepository audit,
                             final GroupRepository groups,
                             final UUID userId,
                             final UserDto delta) {
        super(null, audit, repository, null);
        _userId = userId;
        _delta = delta;
        _groups = groups;
    }

    /** {@inheritDoc} */
    @Override
    public User doExecute(final User actor,
                          final Date happenedOn) throws CccCheckedException {

        final User current = getUsers().find(_userId);

        // current.username(delta.getUsername().toString()); #571
        current.email(new EmailAddress(_delta.getEmail()));
        current.name(_delta.getEmail());
        current.clearGroups();
        for (final UUID groupId : _delta.getRoles()) {
            current.addRole(_groups.find(groupId));
        }
        current.clearMetadata();
        current.addMetadata(_delta.getMetadata());

        getAudit().record(
            new LogEntry(
                actor,
                getType(),
                happenedOn,
                _userId,
                new JsonImpl(current).getDetail()));

        return current;
    }


    /** {@inheritDoc} */
    @Override
    protected CommandType getType() { return CommandType.USER_UPDATE; }
}
