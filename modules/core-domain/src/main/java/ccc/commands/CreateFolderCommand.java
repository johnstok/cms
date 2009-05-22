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

import ccc.domain.Folder;
import ccc.domain.ResourceExistsException;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Dao;


/**
 * Command: create a new folder.
 *
 * @author Civic Computing Ltd.
 */
public class CreateFolderCommand extends CreateResourceCommand {

    /**
     * Constructor.
     *
     * @param dao The DAO used for CRUD operations, etc.
     * @param audit The audit log to record business actions.
     */
    public CreateFolderCommand(final Dao dao, final AuditLog audit) {
        super(dao, audit);
    }


    /**
     * Create a folder.
     *
     * @param parentFolder The folder in which the new folder will be created.
     * @param name The name of the new folder.
     * @param title The title of the new folder.
     * @param actor The user who performed the command.
     * @param happenedOn When the command was performed.
     *
     * @throws ResourceExistsException If a resource with the same name already
     *  exists.
     *
     *  @return The new folder.
     */
    public Folder execute(final User actor,
                          final Date happenedOn,
                          final UUID parentFolder,
                          final String name,
                          final String title) throws ResourceExistsException {
        final Folder f = new Folder(name);
        f.title((null==title)?name:title);

        create(actor, happenedOn, parentFolder, f);

        return f;
    }
}
