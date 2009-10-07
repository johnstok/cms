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
import ccc.domain.LogEntry;
import ccc.domain.User;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.UserRepository;
import ccc.rest.dto.UserDto;
import ccc.serialization.JsonImpl;
import ccc.types.CommandType;
import ccc.types.EmailAddress;


/**
 * Command: update a user.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateUserCommand {


    private final UserRepository     _repository;
    private final LogEntryRepository _audit;

    /**
     * Constructor.
     *
     * @param repository The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     */
    public UpdateUserCommand(final UserRepository repository,
                             final LogEntryRepository audit) {
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
     * @throws CccCheckedException If the command fails.
     *
     * @return The updated user.
     */
    public User execute(final User actor,
                        final Date happenedOn,
                        final UUID userId,
                        final UserDto delta) throws CccCheckedException {

        final User current = _repository.find(userId);

        // current.username(delta.getUsername().toString()); #571
        current.email(new EmailAddress(delta.getEmail()));
        current.roles(delta.getRoles());
        current.clearMetadata();
        current.addMetadata(delta.getMetadata());

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
