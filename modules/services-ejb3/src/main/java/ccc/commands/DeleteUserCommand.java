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
 * Revision      $Rev: 3378 $
 * Modified by   $Author: petteri $
 * Modified on   $Date: 2011-01-14 15:59:04 +0000 (Fri, 14 Jan 2011) $
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.commands;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import ccc.api.core.User;
import ccc.api.types.CommandType;
import ccc.api.types.EmailAddress;
import ccc.domain.LogEntry;
import ccc.domain.UserEntity;
import ccc.persistence.IRepositoryFactory;
import ccc.plugins.s11n.json.JsonImpl;


/**
 * Command: delete a user.
 *
 * @author Civic Computing Ltd.
 */
public class DeleteUserCommand
    extends
        Command<UserEntity> {

    private final UUID _userId;


    /**
     * Constructor.
     *
     * @param repoFactory The repository factory for this command.
     * @param userId The id of the user to delete.
     */
    public DeleteUserCommand(final IRepositoryFactory repoFactory,
                             final UUID userId) {
        super(repoFactory);
        _userId = userId;
    }

    /** {@inheritDoc} */
    @Override
    public UserEntity doExecute(final UserEntity actor,
                          final Date happenedOn) {

        final UserEntity current = getUsers().find(_userId);
        
        Map<String, String> meta = current.getMetadata();
        meta.put("deleted", "true");
        current.addMetadata(meta);

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
    protected CommandType getType() { return CommandType.USER_DELETE; }
}
