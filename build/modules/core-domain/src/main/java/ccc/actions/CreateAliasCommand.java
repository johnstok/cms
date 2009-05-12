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

import ccc.domain.Alias;
import ccc.domain.CCCException;
import ccc.domain.Resource;
import ccc.domain.ResourceExistsException;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Dao;


/**
 * TODO: Add Description for this type.
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
     * Create the alias.
     *
     * @param actor
     * @param happenedOn
     * @param parentFolder
     * @param targetId
     * @param title
     * @throws ResourceExistsException
     */
    public Alias execute(final User actor,
                            final Date happenedOn,
                            final UUID parentFolder,
                            final UUID targetId,
                            final String title) throws ResourceExistsException {
        final Resource target = _dao.find(Resource.class, targetId);
        if (target == null) {
            throw new CCCException("Target does not exists.");
        }
        final Alias a = new Alias(title, target);

        create(actor, happenedOn, parentFolder, a);

        return a;
    }
}
