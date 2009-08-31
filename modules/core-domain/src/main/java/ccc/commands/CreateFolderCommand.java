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
import ccc.domain.CccCheckedException;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Repository;


/**
 * Command: create a new folder.
 *
 * @author Civic Computing Ltd.
 */
public class CreateFolderCommand extends CreateResourceCommand {

    /**
     * Constructor.
     *
     * @param repository The DAO used for CRUD operations, etc.
     * @param audit The audit log to record business actions.
     */
    public CreateFolderCommand(final Repository repository, final AuditLog audit) {
        super(repository, audit);
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
     * @throws CccCheckedException If the command fails.
     *
     *  @return The new folder.
     */
    public Folder execute(final User actor,
                          final Date happenedOn,
                          final UUID parentFolder,
                          final String name,
                          final String title) throws CccCheckedException {
        final Folder f = new Folder(name);
        f.title((null==title)?name:title);

        create(actor, happenedOn, parentFolder, f);

        return f;
    }
}
