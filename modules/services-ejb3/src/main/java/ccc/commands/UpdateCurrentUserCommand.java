/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
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

import ccc.api.core.User;
import ccc.api.exceptions.CCException;
import ccc.api.exceptions.UnauthorizedException;
import ccc.api.types.CommandType;
import ccc.api.types.EmailAddress;
import ccc.api.types.Password;
import ccc.domain.LogEntry;
import ccc.domain.UserEntity;
import ccc.persistence.IRepositoryFactory;
import ccc.plugins.s11n.json.JsonImpl;


/**
 * Command: update a user's own email and password.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateCurrentUserCommand
    extends
        Command<Void> {

    private final UUID _userId;
    private final User _delta;

    /**
     * Constructor.
     *
     * @param repoFactory The repository factory for this command.
     * @param userId The user's id.
     * @param delta The changes to apply.
     */
    public UpdateCurrentUserCommand(final IRepositoryFactory repoFactory,
                                    final UUID userId,
                                    final User delta) {
        super(repoFactory);
        _userId = userId;
        _delta = delta;
    }


    /** {@inheritDoc} */
    @Override
    public Void doExecute(final UserEntity actor,
                          final Date happenedOn) {

        final UserEntity current = getUsers().find(_userId);

        // TODO: Move to separate command
        if (null != _delta.getPassword()) {
            current.setPassword(_delta.getPassword());
            getAudit().record(
                new LogEntry(
                    actor,
                    CommandType.USER_CHANGE_PASSWORD,
                    happenedOn,
                    _userId,
                    "{}"));
        }

        current.setEmail(new EmailAddress(_delta.getEmail()));
        current.setName(_delta.getName());
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
    protected void validate() {
        if ((null!=_delta.getPassword()
                && !Password.isStrong(_delta.getPassword()))
            || null==_delta.getName()
            || null==_delta.getEmail()
            || !EmailAddress.isValidText(_delta.getEmail())) {
            // FIXME: Should be an InvalidException.
            throw new CCException("Bad input data.");
        }
    }


    /** {@inheritDoc} */
    @Override
    protected void authorize(final UserEntity actor) {
        if (!actor.getId().equals(_userId)) {
            throw new UnauthorizedException(_userId, actor.getId());
        }
    }


    /** {@inheritDoc} */
    @Override
    protected CommandType getType() { return CommandType.USER_UPDATE; }
}
