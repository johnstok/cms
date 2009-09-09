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

import ccc.domain.CccCheckedException;
import ccc.domain.InsufficientPrivilegesException;
import ccc.domain.LogEntry;
import ccc.domain.User;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.Repository;
import ccc.serialization.JsonImpl;
import ccc.types.CommandType;
import ccc.types.EmailAddress;


/**
 * Command: update a user's own email and password.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateCurrentUserCommand {

    private final Repository      _repository;
    private final LogEntryRepository _audit;

    /**
     * Constructor.
     *
     * @param repository The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     */
    public UpdateCurrentUserCommand(final Repository repository,
                                    final LogEntryRepository audit) {
        _repository = repository;
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
     *
     * @throws CccCheckedException If the command fails.
     */
    public void execute(final User actor,
                        final Date happenedOn,
                        final UUID userId,
                        final String email,
                        final String password) throws CccCheckedException {

        if (!actor.id().equals(userId)) {
            throw new InsufficientPrivilegesException(
                CommandType.USER_UPDATE, actor);
        }

        final User current = _repository.find(User.class, userId);

        if (password != null) {
            current.password(password);
        }

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
