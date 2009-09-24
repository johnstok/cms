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
import ccc.domain.CccCheckedException;
import ccc.domain.LogEntry;
import ccc.domain.User;
import ccc.persistence.ActionRepository;
import ccc.persistence.LogEntryRepository;
import ccc.serialization.JsonImpl;
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
     * @param repository The DAO used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     */
    public CancelActionCommand(final ActionRepository repository,
                               final LogEntryRepository audit) {
        _repository = repository;
        _audit = audit;
    }


    /**
     * Cancel an action.
     *
     * @param actionId The id of the action to cancel.
     * @param actor The user who performed the command.
     * @param happenedOn When the command was performed.
     *
     * @throws CccCheckedException If the command fails.
     */
    public void execute(final User actor,
                        final Date happenedOn,
                        final UUID actionId) throws CccCheckedException {
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
