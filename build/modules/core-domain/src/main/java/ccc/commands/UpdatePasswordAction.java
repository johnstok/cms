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

import static ccc.persistence.QueryNames.*;

import java.util.Date;
import java.util.UUID;

import ccc.domain.LogEntry;
import ccc.domain.Password;
import ccc.domain.User;
import ccc.persistence.AuditLog;
import ccc.persistence.Repository;
import ccc.types.CommandType;


/**
 * Command: update a user's password..
 *
 * @author Civic Computing Ltd.
 */
public class UpdatePasswordAction {

    private final Repository      _repository;
    private final AuditLog _audit;

    /**
     * Constructor.
     *
     * @param repository The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     */
    public UpdatePasswordAction(final Repository repository, final AuditLog audit) {
        _repository = repository;
        _audit = audit;
    }


    /**
     * Update a user's password.
     *
     * @param userId The user's id.
     * @param password The new password.
     * @param actor The user who performed the command.
     * @param happenedOn When the command was performed.
     */
    public void execute(final User actor,
                        final Date happenedOn,
                        final UUID userId,
                        final String password) {
        final Password p =
                _repository.find(PASSWORD_FOR_USER, Password.class, userId);
        p.password(password);

        final LogEntry le = new LogEntry(
            actor,
            CommandType.USER_CHANGE_PASSWORD,
            happenedOn,
            p.id(),
            "{}");
        _audit.record(le);
    }
}
