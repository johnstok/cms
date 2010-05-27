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
import ccc.api.types.ResourceName;
import ccc.domain.AliasEntity;
import ccc.domain.ResourceEntity;
import ccc.domain.UserEntity;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.ResourceRepository;


/**
 * Command: create an alias.
 *
 * @author Civic Computing Ltd.
 */
class CreateAliasCommand
    extends
        CreateResourceCommand<AliasEntity> {

    private final UUID _parentFolder;
    private final UUID _targetId;
    private final ResourceName _title;


    /**
     * Constructor.
     *
     * @param repository The DAO used for CRUD operations, etc.
     * @param audit The audit log to record business actions.
     * @param parentFolder The folder in which the alias should be created.
     * @param targetId The alias' target resource.
     * @param title The alias' title.
     */
    public CreateAliasCommand(final ResourceRepository repository,
                              final LogEntryRepository audit,
                              final UUID parentFolder,
                              final UUID targetId,
                              final ResourceName title) {
        super(repository, audit);
        _parentFolder = parentFolder;
        _targetId = targetId;
        _title = title;
    }

    /** {@inheritDoc} */
    @Override
    public AliasEntity doExecute(final UserEntity actor,
                           final Date happenedOn) {
        final ResourceEntity target =
            getRepository().find(ResourceEntity.class, _targetId);
        if (target == null) {
            throw new RuntimeException("Target does not exists.");
        }
        final AliasEntity a = new AliasEntity(_title.toString(), target);

        create(actor, happenedOn, _parentFolder, a);

        return a;
    }


    /** {@inheritDoc} */
    @Override
    protected CommandType getType() { return CommandType.ALIAS_CREATE; }
}
