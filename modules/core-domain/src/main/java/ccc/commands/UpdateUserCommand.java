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

import ccc.api.UserSummary;
import ccc.domain.LogEntry;
import ccc.domain.JsonImpl;
import ccc.domain.User;
import ccc.persistence.AuditLog;
import ccc.persistence.Repository;
import ccc.types.CommandType;
import ccc.types.EmailAddress;


/**
 * Command: update a user.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateUserCommand {


    private final Repository      _repository;
    private final AuditLog _audit;

    /**
     * Constructor.
     *
     * @param repository The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     */
    public UpdateUserCommand(final Repository repository, final AuditLog audit) {
        _repository = repository;
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
                        final UserSummary delta) {
        final User current = _repository.find(User.class, userId);
        // current.username(delta.getUsername().toString()); #571
        current.email(new EmailAddress(delta.getEmail()));
        current.roles(delta.getRoles());

        _audit.record(
            new LogEntry(
                actor,
                CommandType.USER_UPDATE,
                happenedOn,
                userId,
                new JsonImpl(current).getDetail()));

        return current;
    }
}
