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

import ccc.domain.LogEntry;
import ccc.domain.User;
import ccc.persistence.IRepositoryFactory;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.UserRepository;
import ccc.plugins.s11n.json.JsonImpl;
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
     * @param repoFactory The repository factory for this command.
     */
    public UpdatePasswordAction(final IRepositoryFactory repoFactory) {
        _repository = repoFactory.createUserRepo();
        _audit = repoFactory.createLogEntryRepo();
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
                        final String password) {
        final User u =
                _repository.find(userId);
        u.setPassword(password);

        final LogEntry le = new LogEntry(
            actor,
            CommandType.USER_CHANGE_PASSWORD,
            happenedOn,
            u.getId(),
            new JsonImpl(u).getDetail());
        _audit.record(le);
    }
}
