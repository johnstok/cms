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

import ccc.api.UserSummary;
import ccc.domain.LogEntry;
import ccc.domain.Password;
import ccc.domain.Snapshot;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Dao;
import ccc.types.CommandType;
import ccc.types.EmailAddress;


/**
 * Command: create a new user.
 *
 * @author Civic Computing Ltd.
 */
public class CreateUserCommand {

    private final Dao      _dao;
    private final AuditLog _audit;

    /**
     * Constructor.
     *
     * @param dao The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     */
    public CreateUserCommand(final Dao dao, final AuditLog audit) {
        _dao = dao;
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
        _dao.create(user);

        final Password defaultPassword =
            new Password(user, delta.getPassword());
        _dao.create(defaultPassword);

        _audit.record(
            new LogEntry(
                actor,
                CommandType.USER_CREATE,
                happenedOn,
                user.id(),
                new Snapshot(user).getDetail()));

        return user;
    }
}
