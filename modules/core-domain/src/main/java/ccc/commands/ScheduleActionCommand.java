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
import ccc.domain.JsonImpl;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Dao;
import ccc.types.CommandType;


/**
 * Command: schedule an action.
 *
 * @author Civic Computing Ltd.
 */
public class ScheduleActionCommand {

    private final Dao _dao;
    private final AuditLog _audit;

    /**
     * Constructor.
     *
     * @param dao The DAO used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     */
    public ScheduleActionCommand(final Dao dao, final AuditLog audit) {
        _dao = dao;
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
        _dao.create(action);

        _audit.record(
            new LogEntry(
                actor,
                CommandType.ACTION_CREATE,
                happenedOn,
                action.id(),
                new JsonImpl(action).getDetail()));
    }
}
