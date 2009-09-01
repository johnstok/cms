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
import java.util.UUID;

import ccc.domain.Action;
import ccc.domain.LogEntry;
import ccc.domain.User;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.Repository;
import ccc.serialization.JsonImpl;
import ccc.types.CommandType;


/**
 * Command: cancel an action.
 *
 * @author Civic Computing Ltd.
 */
public class CancelActionCommand {

    private final Repository _repository;
    private final LogEntryRepository _audit;

    /**
     * Constructor.
     *
     * @param repository The DAO used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     */
    public CancelActionCommand(final Repository repository, final LogEntryRepository audit) {
        _repository = repository;
        _audit = audit;
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
        final Action a = _repository.find(Action.class, actionId);
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
