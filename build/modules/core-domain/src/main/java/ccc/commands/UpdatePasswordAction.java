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
import ccc.persistence.LogEntryRepository;
import ccc.persistence.UserRepository;
import ccc.serialization.JsonImpl;
import ccc.types.CommandType;


/**
 * Command: update a user's password..
 *
 * @author Civic Computing Ltd.
 */
public class UpdatePasswordAction {

    private final UserRepository     _repository;
    private final LogEntryRepository _audit;

    /**
     * Constructor.
     *
     * @param repository The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     */
    public UpdatePasswordAction(final UserRepository repository,
                                final LogEntryRepository audit) {
        _repository = repository;
        _audit = audit;
    }


    /**
     * Update a user's password.
     *
     * @param userId The user's id.
     * @param password The new password.
     * @param actor The user who performed the command.
     * @param happenedOn When the command was performed.
     *
     * @throws CccCheckedException If the command fails.
     */
    public void execute(final User actor,
                        final Date happenedOn,
                        final UUID userId,
                        final String password) throws CccCheckedException {
        final User u =
                _repository.find(userId);
        u.password(password);

        final LogEntry le = new LogEntry(
            actor,
            CommandType.USER_CHANGE_PASSWORD,
            happenedOn,
            u.id(),
            new JsonImpl(u).getDetail());
        _audit.record(le);
    }
}
