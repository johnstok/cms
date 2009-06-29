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

import ccc.api.CommandType;
import ccc.domain.Action;
import ccc.domain.LogEntry;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Dao;


/**
 * Command: cancel an action.
 *
 * @author Civic Computing Ltd.
 */
public class CancelActionCommand {

    private final Dao _dao;
    private final AuditLog _audit;

    /**
     * Constructor.
     *
     * @param dao The DAO used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     */
    public CancelActionCommand(final Dao dao, final AuditLog audit) {
        _dao = dao;
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
        _dao.find(Action.class, actionId).cancel();

        _audit.record(
            new LogEntry(
                actor,
                CommandType.ACTION_CANCEL,
                happenedOn,
                actionId,
                "{}"));
    }
}
