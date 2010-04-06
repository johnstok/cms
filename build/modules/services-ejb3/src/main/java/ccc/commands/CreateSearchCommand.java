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

import ccc.domain.Search;
import ccc.domain.User;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.ResourceRepository;
import ccc.types.CommandType;


/**
 * Command: create a new search.
 *
 * @author Civic Computing Ltd.
 */
class CreateSearchCommand
    extends
        CreateResourceCommand<Search> {

    private final UUID _parentFolder;
    private final String _title;

    /**
     * Constructor.
     *
     * @param repository The DAO used for CRUD operations, etc.
     * @param audit The audit log to record business actions.
     * @param parentFolder The folder in which the search will be created.
     * @param title The title of the search.
     */
    public CreateSearchCommand(final ResourceRepository repository,
                               final LogEntryRepository audit,
                               final UUID parentFolder,
                               final String title) {
        super(repository, audit);
        _parentFolder = parentFolder;
        _title = title;
    }

    /** {@inheritDoc} */
    @Override
    public Search doExecute(final User actor,
                          final Date happenedOn) {
        final Search s = new Search(_title);

        create(actor, happenedOn, _parentFolder, s);

        return s;
    }


    /** {@inheritDoc} */
    @Override
    protected CommandType getType() { return CommandType.SEARCH_CREATE; }
}
