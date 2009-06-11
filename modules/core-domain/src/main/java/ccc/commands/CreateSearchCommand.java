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

import ccc.domain.RemoteExceptionSupport;
import ccc.domain.Search;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Dao;


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
     * @param dao The DAO used for CRUD operations, etc.
     * @param audit The audit log to record business actions.
     */
    public CreateSearchCommand(final Dao dao, final AuditLog audit) {
        super(dao, audit);
    }

    /**
     * Create a new search.
     *
     * @param parentFolder The folder in which the search will be created.
     * @param title The title of the search.
     * @param actor The user who performed the command.
     * @param happenedOn When the command was performed.
     *
     * @throws RemoteExceptionSupport If the command fails.
     *
     *  @return The new search.
     */
    public Search execute(final User actor,
                          final Date happenedOn,
                          final UUID parentFolder,
                          final String title) throws RemoteExceptionSupport {
        final Search s = new Search(title);

        create(actor, happenedOn, parentFolder, s);

        return s;
    }
}
