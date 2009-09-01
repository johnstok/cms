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
import ccc.domain.Password;
import ccc.domain.User;
import ccc.persistence.AuditLog;
import ccc.persistence.Repository;
import ccc.rest.dto.UserSummary;
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
    private final AuditLog _audit;

    /**
     * Constructor.
     *
     * @param repository The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     */
    public CreateUserCommand(final Repository repository, final AuditLog audit) {
        _repository = repository;
        _audit = audit;
    }

    /**
     * Create new user.
     *
     * @param delta The properties for the new user.
     * @param password The password to be used for the user.
     * @param actor The user who performed the command.
     * @param happenedOn When the command was performed.
     *
     * @return Persisted user.
     */
    public User execute(final User actor,
                        final Date happenedOn,
                        final UserSummary delta) {
        final User user = new User(delta.getUsername().toString());
        user.email(new EmailAddress(delta.getEmail()));
        user.roles(delta.getRoles());
        user.addMetadata(delta.getMetadata());
        _repository.create(user);

        final Password defaultPassword =
            new Password(user, delta.getPassword());
        _repository.create(defaultPassword);

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
