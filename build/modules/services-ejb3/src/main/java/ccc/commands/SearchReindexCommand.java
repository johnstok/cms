/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.commands;

import java.util.Date;

import ccc.api.types.CommandType;
import ccc.api.types.DBC;
import ccc.api.types.Permission;
import ccc.domain.UserEntity;
import ccc.messaging.Producer;
import ccc.persistence.IRepositoryFactory;


/**
 * Command: invokes a search re-index on all nodes.
 *
 * @author Civic Computing Ltd.
 */
public class SearchReindexCommand
    extends
        Command<Void> {

    private final Producer _producer;


    /**
     * Constructor.
     *
     * @param repoFactory The repository factory for this command.
     * @param producer    The messaging producer for this command.
     */
    public SearchReindexCommand(final IRepositoryFactory repoFactory,
                                final Producer producer) {
        super(repoFactory);
        _producer = DBC.require().notNull(producer);
    }


    /** {@inheritDoc} */
    @Override
    protected Void doExecute(final UserEntity actor, final Date happenedOn) {
        _producer.broadcastMessage(CommandType.SEARCH_INDEX_ALL);
        return null;
    }


    /** {@inheritDoc} */
    @Override
    protected void authorize(final UserEntity actor) {
        checkPermission(actor, Permission.SEARCH_REINDEX);
    }


    /** {@inheritDoc} */
    @Override
    protected CommandType getType() { return CommandType.SEARCH_INDEX_ALL; }
}
