/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
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

import ccc.domain.CccCheckedException;
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
public class CreateSearchCommand
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
                          final Date happenedOn) throws CccCheckedException {
        final Search s = new Search(_title);

        create(actor, happenedOn, _parentFolder, s);

        return s;
    }


    /** {@inheritDoc} */
    @Override
    protected CommandType getType() { return CommandType.SEARCH_CREATE; }
}
