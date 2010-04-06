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

import ccc.domain.Action;
import ccc.domain.LogEntry;
import ccc.domain.User;
import ccc.persistence.ActionRepository;
import ccc.persistence.IRepositoryFactory;
import ccc.persistence.LogEntryRepository;
import ccc.serialization.JsonImpl;
import ccc.types.CommandType;


/**
 * Command: schedule an action.
 *
 * @author Civic Computing Ltd.
 */
public class ScheduleActionCommand {

    private final ActionRepository _repository;
    private final LogEntryRepository _audit;

    /**
     * Constructor.
     *
     * @param repoFactory The repository factory for this command.
     */
    public ScheduleActionCommand(final IRepositoryFactory repoFactory) {
        _repository = repoFactory.createActionRepository();
        _audit = repoFactory.createLogEntryRepo();
    }


    /**
     * Schedule an action.
     *
     * @param action The action to schedule.
     * @param actor The user that executed the command.
     * @param happenedOn The date the command was executed.
     */
    public void execute(final User actor,
                        final Date happenedOn,
                        final Action action) {
        _repository.create(action);

        _audit.record(
            new LogEntry(
                actor,
                CommandType.ACTION_CREATE,
                happenedOn,
                action.getId(),
                new JsonImpl(action).getDetail()));
    }
}
