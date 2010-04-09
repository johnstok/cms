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
import ccc.domain.LogEntry;
import ccc.domain.Resource;
import ccc.domain.User;
import ccc.persistence.IRepositoryFactory;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.ResourceRepository;
import ccc.plugins.s11n.json.JsonImpl;


/**
 * Command: include/exclude a resource in the main menu.
 *
 * @author Civic Computing Ltd.
 */
public class IncludeInMainMenuCommand {

    private final ResourceRepository _repository;
    private final LogEntryRepository _audit;


    /**
     * Constructor.
     *
     * @param repoFactory The repository factory for this command.
     */
    public IncludeInMainMenuCommand(final IRepositoryFactory repoFactory) {
        _repository = repoFactory.createResourceRepository();
        _audit = repoFactory.createLogEntryRepo();
    }


    /**
     * Constructor.
     */
    public IncludeInMainMenuCommand(final ResourceRepository repository,
                                  final LogEntryRepository audit) {
        _repository = repository;
        _audit = audit;
    }

    /**
     * Specify whether this resource should be included in the main menu.
     *
     * @param id The id of the resource to change.
     * @param b True if the resource should be included; false otherwise.
     * @param actor The user who performed the command.
     * @param happenedOn When the command was performed.
     */
    public void execute(final User actor,
                        final Date happenedOn,
                        final UUID id,
                        final boolean b) {
        final Resource r = _repository.find(Resource.class, id);
        r.confirmLock(actor);

        r.setIncludedInMainMenu(b);

        final CommandType command =
            (b) ? CommandType.RESOURCE_INCLUDE_IN_MM
                : CommandType.RESOURCE_REMOVE_FROM_MM;

        final LogEntry le =
            new LogEntry(
                actor, command, happenedOn, id, new JsonImpl(r).getDetail());
        _audit.record(le);
    }
}
