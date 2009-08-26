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

import ccc.domain.Alias;
import ccc.domain.CCCException;
import ccc.domain.CccCheckedException;
import ccc.domain.Resource;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Dao;


/**
 * Command: create an alias.
 *
 * @author Civic Computing Ltd.
 */
public class CreateAliasCommand
    extends
        CreateResourceCommand {

    /**
     * Constructor.
     *
     * @param dao The DAO used for CRUD operations, etc.
     * @param audit The audit log to record business actions.
     */
    public CreateAliasCommand(final Dao dao, final AuditLog audit) {
        super(dao, audit);
    }

    /**
     * Create an alias.
     *
     * @param parentFolder The folder in which the alias should be created.
     * @param targetId The alias' target resource.
     * @param title The alias' title.
     * @param actor The user who performed the command.
     * @param happenedOn When the command was performed.
     *
     * @throws CccCheckedException If the command fails.
     *
     *  @return The new alias.
     */
    public Alias execute(final User actor,
                         final Date happenedOn,
                         final UUID parentFolder,
                         final UUID targetId,
                         final String title) throws CccCheckedException {
        final Resource target = getDao().find(Resource.class, targetId);
        if (target == null) {
            throw new CCCException("Target does not exists.");
        }
        final Alias a = new Alias(title, target);

        create(actor, happenedOn, parentFolder, a);

        return a;
    }
}
