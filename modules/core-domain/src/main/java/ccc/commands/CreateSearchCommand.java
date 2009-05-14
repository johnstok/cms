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

import ccc.domain.ResourceExistsException;
import ccc.domain.Search;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Dao;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class CreateSearchCommand
    extends
        CreateResourceCommand {

    /**
     * Constructor.
     *
     * @param dao The DAO used for CRUD operations, etc.
     * @param audit The audit log to record business actions.
     */
    public CreateSearchCommand(final Dao dao, final AuditLog audit) {
        super(dao, audit);
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param actor
     * @param happenedOn
     * @param parentFolder
     * @param title
     * @return
     * @throws ResourceExistsException
     */
    public Search execute(final User actor,
                          final Date happenedOn,
                          final UUID parentFolder,
                          final String title) throws ResourceExistsException {
        final Search s = new Search(title);

        create(actor, happenedOn, parentFolder, s);

        return s;
    }
}
