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
import ccc.persistence.UserRepository;
import ccc.rest.dto.UserDto;
import ccc.serialization.JsonImpl;
import ccc.types.CommandType;
import ccc.types.EmailAddress;


/**
 * Command: update a user's own email and password.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateCurrentUserCommand
    extends
        Command<Void> {

    private final UUID _userId;
    private final UserDto _delta;

    /**
     * Constructor.
     *
     * @param repository The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     * @param userId The user's id.
     * @param delta The changes to apply.
     */
    public UpdateCurrentUserCommand(final UserRepository repository,
                                    final LogEntryRepository audit,
                                    final UUID userId,
                                    final UserDto delta) {
        super(null, audit, repository, null);
        _userId = userId;
        _delta = delta;
    }


    /** {@inheritDoc} */
    @Override
    public Void doExecute(final User actor,
                          final Date happenedOn) throws CccCheckedException {

        final User current = getUsers().find(_userId);

        // TODO: Move to separate command
        if (null != _delta.getPassword()) {
            current.password(_delta.getPassword());
            getAudit().record(
                new LogEntry(
                    actor,
                    CommandType.USER_CHANGE_PASSWORD,
                    happenedOn,
                    _userId,
                    ""));
        }

        current.email(new EmailAddress(_delta.getEmail()));
        current.name(_delta.getName());
        current.clearMetadata();
        current.addMetadata(_delta.getMetadata());

        getAudit().record(
            new LogEntry(
                actor,
                getType(),
                happenedOn,
                _userId,
                new JsonImpl(current).getDetail()));

        return null;
    }


    /** {@inheritDoc} */
    @Override
    protected void validate() throws InvalidCommandException {
        if (null==_delta.getName()
            || null==_delta.getEmail()
            || !EmailAddress.isValidText(_delta.getEmail())) {
            throw new InvalidCommandException();
        }
    }


    /** {@inheritDoc} */
    @Override
    protected void authorize(final User actor)
                                    throws InsufficientPrivilegesException {
        if (!actor.id().equals(_userId)) {
            throw new InsufficientPrivilegesException(
                getType(), actor);
        }
    }


    /** {@inheritDoc} */
    @Override
    protected CommandType getType() { return CommandType.USER_UPDATE; }
}
