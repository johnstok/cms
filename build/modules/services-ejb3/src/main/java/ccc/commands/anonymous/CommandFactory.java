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
package ccc.commands.anonymous;

import ccc.api.types.CommandType;
import ccc.persistence.IRepositoryFactory;


/**
 * Factory for anonymous commands.
 *
 * @author Civic Computing Ltd.
 */
public class CommandFactory {

    private final IRepositoryFactory _repoFactory;


    /**
     * Constructor.
     *
     * @param repoFactory The repository factory to create repositories.
     */
    public CommandFactory(final IRepositoryFactory repoFactory) {
        _repoFactory = repoFactory;
    }


    /**
     * Create a command of the specified type.
     *
     * @param type The type of command to create.
     *
     * @return An instance of the specified command type.
     */
    public AnonymousCommand createCommand(final CommandType type) {
        switch (type) {

            case SEARCH_INDEX_ALL:
                return
                    new IndexAllCommand(
                        _repoFactory.createResourceRepository(),
                        _repoFactory.createDataRepository(),
                        _repoFactory.createSettingsRepository());

            default:
                throw new RuntimeException(
                    "No anonymous command available for type: "+type);
        }
    }
}
