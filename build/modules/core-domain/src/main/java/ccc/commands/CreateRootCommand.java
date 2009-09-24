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

import ccc.domain.CccCheckedException;
import ccc.domain.EntityNotFoundException;
import ccc.domain.Folder;
import ccc.domain.Resource;
import ccc.domain.ResourceExistsException;
import ccc.domain.User;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.QueryNames;
import ccc.persistence.ResourceRepository;


/**
 * Command: create a root folder.
 *
 * @author Civic Computing Ltd.
 */
public class CreateRootCommand extends CreateResourceCommand {

    /**
     * Constructor.
     *
     * @param repository The DAO used for CRUD operations, etc.
     * @param audit The audit log to record business actions.
     */
    public CreateRootCommand(final ResourceRepository repository,
                             final LogEntryRepository audit) {
        super(repository, audit);
    }


    /**
     * Create a new root folder.
     * <p>The name is checked against existing root folders in order to prevent
     * conflicts.
     *
     * @param folder The root folder to create.
     * @param actor The user who performed the command.
     * @param happenedOn When the command was performed.
     *
     * @throws CccCheckedException If the command fails.
     */
    public void execute(final User actor,
                        final Date happenedOn,
                        final Folder folder) throws CccCheckedException {
        try {
            final Resource possibleRoot = getDao().find(
                QueryNames.ROOT_BY_NAME, Resource.class, folder.name());
            throw new ResourceExistsException(null, possibleRoot);

        } catch (final EntityNotFoundException e) {
            folder.dateCreated(happenedOn);
            folder.dateChanged(happenedOn);
            getDao().create(folder);

            audit(folder, actor, happenedOn);
        }
    }
}
