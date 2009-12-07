/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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

import ccc.domain.Alias;
import ccc.domain.CccCheckedException;
import ccc.domain.Resource;
import ccc.domain.User;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.ResourceRepository;
import ccc.types.CommandType;


/**
 * Command: updates an alias.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateAliasCommand
    extends
        UpdateResourceCommand<Void> {

    private final UUID _targetId;
    private final UUID _aliasId;

    /**
     * Constructor.
     *
     * @param repository The DAO used for CRUD operations, etc.
     * @param audit The audit log to record business actions.
     * @param targetId The new target for the alias.
     * @param aliasId The alias to update.
     */
    public UpdateAliasCommand(final ResourceRepository repository,
                              final LogEntryRepository audit,
                              final UUID targetId,
                              final UUID aliasId) {
        super(repository, audit, null);
        _targetId = targetId;
        _aliasId = aliasId;
    }


    /** {@inheritDoc} */
    @Override
    public Void doExecute(final User actor,
                          final Date happenedOn) throws CccCheckedException {

        final Alias alias = getRepository().find(Alias.class, _aliasId);
        alias.confirmLock(actor);

        final Resource target = getRepository().find(Resource.class, _targetId);
        alias.target(target);

        update(alias, actor, happenedOn);

        return null;
    }


    /** {@inheritDoc} */
    @Override
    protected CommandType getType() { return CommandType.ALIAS_UPDATE; }
}
