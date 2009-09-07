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

import ccc.domain.LogEntry;
import ccc.domain.User;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.Repository;
import ccc.rest.dto.UserDto;
import ccc.serialization.JsonImpl;
import ccc.types.CommandType;
import ccc.types.EmailAddress;


/**
 * Command: create a new user.
 *
 * @author Civic Computing Ltd.
 */
public class CreateUserCommand {

    private final Repository      _repository;
    private final LogEntryRepository _audit;

    /**
     * Constructor.
     *
     * @param repository The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     */
    public CreateUserCommand(final Repository repository,
                             final LogEntryRepository audit) {
        _repository = repository;
        _audit = audit;
    }

    /**
     * Create new user.
     *
     * @param delta The properties for the new user.
     * @param actor The user who performed the command.
     * @param happenedOn When the command was performed.
     *
     * @return Persisted user.
     */
    public User execute(final User actor,
                        final Date happenedOn,
                        final UserDto delta) {
        final User user =
            new User(delta.getUsername(), delta.getPassword());
        user.email(new EmailAddress(delta.getEmail()));
        user.roles(delta.getRoles());
        user.addMetadata(delta.getMetadata());
        _repository.create(user);

        _audit.record(
            new LogEntry(
                actor,
                CommandType.USER_CREATE,
                happenedOn,
                user.id(),
                new JsonImpl(user).getDetail()));

        return user;
    }
}
