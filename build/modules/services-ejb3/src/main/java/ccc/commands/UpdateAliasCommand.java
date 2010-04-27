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

import ccc.api.types.CommandType;
import ccc.domain.AliasEntity;
import ccc.domain.ResourceEntity;
import ccc.domain.UserEntity;
import ccc.persistence.IRepositoryFactory;


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
     * @param repoFactory The repository factory for this command.
     * @param targetId The new target for the alias.
     * @param aliasId The alias to update.
     */
    public UpdateAliasCommand(final IRepositoryFactory repoFactory,
                              final UUID targetId,
                              final UUID aliasId) {
        super(repoFactory);
        _targetId = targetId;
        _aliasId = aliasId;
    }


    /** {@inheritDoc} */
    @Override
    public Void doExecute(final UserEntity actor,
                          final Date happenedOn) {

        final AliasEntity alias = getRepository().find(AliasEntity.class, _aliasId);
        alias.confirmLock(actor);

        final ResourceEntity target = getRepository().find(ResourceEntity.class, _targetId);
        alias.target(target);

        update(alias, actor, happenedOn);

        return null;
    }


    /** {@inheritDoc} */
    @Override
    protected CommandType getType() { return CommandType.ALIAS_UPDATE; }
}
