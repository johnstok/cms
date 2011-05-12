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

import ccc.api.exceptions.UnauthorizedException;
import ccc.api.types.CommandType;
import ccc.domain.ResourceEntity;
import ccc.domain.UserEntity;
import ccc.persistence.IRepositoryFactory;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.ResourceRepository;


/**
 * Command: include/exclude a resource in the main menu.
 *
 * @author Civic Computing Ltd.
 */
public class IncludeInMainMenuCommand
    extends
        Command<Void> {

    private final UUID    _id;
    private final boolean _include;
    private final ResourceEntity _r;

    /**
     * Constructor.
     *
     * @param repoFactory The repository factory for this command.
     * @param id The id of the resource to change.
     * @param b True if the resource should be included; false otherwise.
     */
    public IncludeInMainMenuCommand(final IRepositoryFactory repoFactory,
                                    final UUID id,
                                    final boolean b) {
        super(repoFactory);
        _id = id;
        _include = b;
        _r = getRepository().find(ResourceEntity.class, _id);
    }


    /**
     * Constructor.
     *
     * @param repository The resource repository.
     * @param audit      The audit log repository.
     * @param id The id of the resource to change.
     * @param b True if the resource should be included; false otherwise.
     */
    public IncludeInMainMenuCommand(final ResourceRepository repository,
                                    final LogEntryRepository audit,
                                    final UUID id,
                                    final boolean b) {
        super(repository, audit, null, null);
        _id = id;
        _include = b;
        _r = getRepository().find(ResourceEntity.class, _id);
    }


    /** {@inheritDoc} */
    @Override
    protected Void doExecute(final UserEntity actor, final Date happenedOn) {
        _r.confirmLock(actor);

        _r.setIncludedInMainMenu(_include);

        auditResourceCommand(actor, happenedOn, _r);

        return null;
    }


    @Override
    protected void authorize(final UserEntity actor) {
        if (!_r.isWriteableBy(actor)) {
            throw new UnauthorizedException(_id, actor.getId());
        }
    }


    /** {@inheritDoc} */
    @Override
    protected CommandType getType() {
        return
            (_include) ? CommandType.RESOURCE_INCLUDE_IN_MM
                       : CommandType.RESOURCE_REMOVE_FROM_MM;
    }
}
