/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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
import ccc.domain.Resource;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Dao;


/**
 * Command: updates an alias.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateAliasCommand extends UpdateResourceCommand {

    /**
     * Constructor.
     *
     * @param dao The DAO used for CRUD operations, etc.
     * @param audit The audit log to record business actions.
     */
    public UpdateAliasCommand(final Dao dao, final AuditLog audit) {
        super(dao, audit);
    }


    /**
     * Perform the update.
     *
     * @param actor
     * @param happenedOn
     * @param targetId
     * @param aliasId
     */
    public void execute(final User actor,
                            final Date happenedOn,
                            final UUID targetId,
                            final UUID aliasId) {
        final Alias alias = _dao.find(Alias.class, aliasId);
        alias.confirmLock(actor);

        final Resource target = _dao.find(Resource.class, targetId);
        alias.target(target);

        update(alias, null, false, actor, happenedOn);
    }
}
