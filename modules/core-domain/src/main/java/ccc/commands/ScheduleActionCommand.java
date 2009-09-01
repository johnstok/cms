/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
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
import ccc.persistence.LogEntryRepository;
import ccc.persistence.Repository;
import ccc.serialization.JsonImpl;
import ccc.types.CommandType;


/**
 * Command: schedule an action.
 *
 * @author Civic Computing Ltd.
 */
public class ScheduleActionCommand {

    private final Repository _repository;
    private final LogEntryRepository _audit;

    /**
     * Constructor.
     *
     * @param repository The DAO used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     */
    public ScheduleActionCommand(final Repository repository, final LogEntryRepository audit) {
        _repository = repository;
        _audit = audit;
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
                action.id(),
                new JsonImpl(action).getDetail()));
    }
}
