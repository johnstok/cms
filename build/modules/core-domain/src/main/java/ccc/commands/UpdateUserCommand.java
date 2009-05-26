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
import ccc.api.UserDelta;
import ccc.commons.EmailAddress;
import ccc.domain.LogEntry;
import ccc.domain.Snapshot;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Dao;


/**
 * Command: update a user.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateUserCommand {


    private final Dao      _dao;
    private final AuditLog _audit;

    /**
     * Constructor.
     *
     * @param dao The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     */
    public UpdateUserCommand(final Dao dao, final AuditLog audit) {
        _dao = dao;
        _audit = audit;
    }

    /**
     * Update user.
     *
     * @param userId The id of the user to update.
     * @param delta The changes to apply.
     * @param actor The user who performed the command.
     * @param happenedOn When the command was performed.
     *
     * @return The updated user.
     */
    public User execute(final User actor,
                        final Date happenedOn,
                        final UUID userId,
                        final UserDelta delta) {
        final User current = _dao.find(User.class, userId);
        current.username(delta.getUsername().toString());
        current.email(new EmailAddress(delta.getEmail()));
        current.roles(delta.getRoles());

        _audit.record(
            new LogEntry(
                actor,
                CommandType.USER_UPDATE,
                happenedOn,
                userId,
                null,
                new Snapshot(delta).getDetail(),
                false));

        return current;
    }
}
