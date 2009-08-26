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

import static ccc.services.QueryNames.*;

import java.util.Date;
import java.util.UUID;

import ccc.domain.InsufficientPrivilegesException;
import ccc.domain.LogEntry;
import ccc.domain.Password;
import ccc.domain.JsonImpl;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Dao;
import ccc.types.CommandType;
import ccc.types.EmailAddress;


/**
 * Command: update a user's own email and password.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateCurrentUserAction {

    private final Dao      _dao;
    private final AuditLog _audit;

    /**
     * Constructor.
     *
     * @param dao The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     */
    public UpdateCurrentUserAction(final Dao dao, final AuditLog audit) {
        _dao = dao;
        _audit = audit;
    }


    /**
     * Update a user's email and password.
     *
     * @param userId The user's id.
     * @param email The new email.
     * @param password The new password.
     * @param actor The user who performed the command.
     * @param happenedOn When the command was performed.
     * @throws InsufficientPrivilegesException
     */
    public void execute(final User actor,
                        final Date happenedOn,
                        final UUID userId,
                        final String email,
                        final String password) throws InsufficientPrivilegesException {
        if (!actor.id().equals(userId)) {
            throw new InsufficientPrivilegesException(CommandType.USER_UPDATE, actor);
        }

        if (password != null) {
            final Password p =
                _dao.find(PASSWORD_FOR_USER, Password.class, userId);
            p.password(password);
        }

        final User current = _dao.find(User.class, userId);
        current.email(new EmailAddress(email));

        _audit.record(
            new LogEntry(
                actor,
                CommandType.USER_UPDATE,
                happenedOn,
                userId,
                new JsonImpl(current).getDetail()));
    }
}
