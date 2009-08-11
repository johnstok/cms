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

import ccc.domain.Folder;
import ccc.domain.Resource;
import ccc.domain.ResourceExistsException;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Dao;
import ccc.services.QueryNames;


/**
 * Command: create a root folder.
 *
 * @author Civic Computing Ltd.
 */
public class CreateRootCommand extends CreateResourceCommand {

    /**
     * Constructor.
     *
     * @param dao The DAO used for CRUD operations, etc.
     * @param audit The audit log to record business actions.
     */
    public CreateRootCommand(final Dao dao, final AuditLog audit) {
        super(dao, audit);
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
     * @throws ResourceExistsException If a resource with the same name already
     *  exists.
     */
    public void execute(final User actor,
                        final Date happenedOn,
                        final Folder folder) throws ResourceExistsException {
        final Resource possibleRoot = getDao().find(
            QueryNames.ROOT_BY_NAME, Resource.class, folder.name());
        if (null!=possibleRoot) {
            throw new ResourceExistsException(null, possibleRoot);
        }

        folder.dateCreated(happenedOn);
        folder.dateChanged(happenedOn);
        getDao().create(folder);

        audit(folder, actor, happenedOn);
    }
}
