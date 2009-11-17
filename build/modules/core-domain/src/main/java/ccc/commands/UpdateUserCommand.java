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
public class UpdateUserCommand
    extends
        Command<User> {

    private final UUID _userId;
    private final UserDto _delta;


    /**
     * Constructor.
     *
     * @param repository The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     * @param userId The id of the user to update.
     * @param delta The changes to apply.
     */
    public UpdateUserCommand(final UserRepository repository,
                             final LogEntryRepository audit,
                             final UUID userId,
                             final UserDto delta) {
        super(null, audit, repository);
        _userId = userId;
        _delta = delta;
    }

    /** {@inheritDoc} */
    @Override
    public User doExecute(final User actor,
                          final Date happenedOn) throws CccCheckedException {

        final User current = getUsers().find(_userId);

        // current.username(delta.getUsername().toString()); #571
        current.email(new EmailAddress(_delta.getEmail()));
        current.name(_delta.getEmail());
        current.roles(_delta.getRoles());
        current.clearMetadata();
        current.addMetadata(_delta.getMetadata());

        getAudit().record(
            new LogEntry(
                actor,
                getType(),
                happenedOn,
                _userId,
                new JsonImpl(current).getDetail()));

        return current;
    }


    /** {@inheritDoc} */
    @Override
    protected CommandType getType() { return CommandType.USER_UPDATE; }
}
