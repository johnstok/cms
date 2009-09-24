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


/**
 * Command: create a new search.
 *
 * @author Civic Computing Ltd.
 */
public class CreateSearchCommand
    extends
        CreateResourceCommand {

    /**
     * Constructor.
     *
     * @param repository The DAO used for CRUD operations, etc.
     * @param audit The audit log to record business actions.
     */
    public CreateSearchCommand(final ResourceRepository repository,
                               final LogEntryRepository audit) {
        super(repository, audit);
    }

    /**
     * Create a new search.
     *
     * @param parentFolder The folder in which the search will be created.
     * @param title The title of the search.
     * @param actor The user who performed the command.
     * @param happenedOn When the command was performed.
     *
     * @throws CccCheckedException If the command fails.
     *
     *  @return The new search.
     */
    public Search execute(final User actor,
                          final Date happenedOn,
                          final UUID parentFolder,
                          final String title) throws CccCheckedException {
        final Search s = new Search(title);

        create(actor, happenedOn, parentFolder, s);

        return s;
    }
}
