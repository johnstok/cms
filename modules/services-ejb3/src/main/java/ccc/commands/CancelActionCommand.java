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

import ccc.domain.Action;
import ccc.domain.LogEntry;
import ccc.domain.User;
import ccc.persistence.ActionRepository;
import ccc.persistence.IRepositoryFactory;
import ccc.persistence.LogEntryRepository;
import ccc.plugins.s11n.json.JsonImpl;
import ccc.types.CommandType;


/**
 * Command: cancel an action.
 *
 * @author Civic Computing Ltd.
 */
public class CancelActionCommand {

    private final ActionRepository _repository;
    private final LogEntryRepository _audit;

    /**
     * Constructor.
     *
     * @param repoFactory The repository factory for this command.
     */
    public CancelActionCommand(final IRepositoryFactory repoFactory) {
        _repository = repoFactory.createActionRepository();
        _audit = repoFactory.createLogEntryRepo();
    }


    /**
     * Cancel an action.
     *
     * @param actionId The id of the action to cancel.
     * @param actor The user who performed the command.
     * @param happenedOn When the command was performed.
     */
    public void execute(final User actor,
                        final Date happenedOn,
                        final UUID actionId) {
        final Action a = _repository.find(actionId);
        a.cancel();

        _audit.record(
            new LogEntry(
                actor,
                CommandType.ACTION_CANCEL,
                happenedOn,
                actionId,
                new JsonImpl(a).getDetail()));
    }
}
