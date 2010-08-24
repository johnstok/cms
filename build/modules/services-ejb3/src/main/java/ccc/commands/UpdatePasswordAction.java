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

import ccc.api.types.CommandType;
import ccc.domain.UserEntity;
import ccc.persistence.IRepositoryFactory;


/**
 * Command: update a user's password..
 *
 * @author Civic Computing Ltd.
 */
public class UpdatePasswordAction
    extends
        Command<Void> {

    private final UUID _userId;
    private final String _password;


    /**
     * Constructor.
     *
     * @param repoFactory The repository factory for this command.
     * @param userId The user's id.
     * @param password The new password.
     */
    public UpdatePasswordAction(final IRepositoryFactory repoFactory,
                                final UUID userId,
                                final String password) {
        super(repoFactory);
        _userId = userId;
        _password = password;
    }


    /** {@inheritDoc} */
    @Override
    protected Void doExecute(final UserEntity actor, final Date happenedOn) {
        final UserEntity u = getUsers().find(_userId);
        u.setPassword(_password);

        auditUserCommand(actor, happenedOn, u);

        return null;
    }


    /** {@inheritDoc} */
    @Override
    protected CommandType getType() { return CommandType.USER_CHANGE_PASSWORD; }
}
