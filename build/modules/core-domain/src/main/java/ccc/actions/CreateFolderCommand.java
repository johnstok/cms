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
package ccc.actions;

import java.util.Date;
import java.util.UUID;

import ccc.domain.Folder;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Dao;


/**
 * TODO: Add Description for this type.
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
     * Create the page.
     *
     * @param actor
     * @param happenedOn
     * @param parentFolder
     * @param name
     * @param title
     */
    public Folder execute(final User actor,
                          final Date happenedOn,
                          final UUID parentFolder,
                          final String name,
                          final String title) {
        final Folder f = new Folder(name);
        f.title((null==title)?name:title);

        create(actor, happenedOn, parentFolder, f);

        return f;
    }

}
