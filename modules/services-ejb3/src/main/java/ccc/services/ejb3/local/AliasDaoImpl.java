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
package ccc.services.ejb3.local;

import java.util.Date;
import java.util.UUID;

import ccc.domain.Alias;
import ccc.domain.Resource;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.ResourceDao;


/**
 * Command: updates an alias.
 *
 * @author Civic Computing Ltd.
 */
public class AliasDaoImpl {

    private final ResourceDao _dao;
    private final AuditLog    _audit;

    /**
     * Constructor.
     *
     * @param dao The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     */
    public AliasDaoImpl(final ResourceDao dao, final AuditLog audit) {
        _dao = dao;
        _audit = audit;
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
        final Resource target = _dao.find(Resource.class, targetId);
        final Alias alias = _dao.findLocked(Alias.class, aliasId, actor);

        alias.target(target);
        alias.dateChanged(happenedOn);

        _audit.recordUpdate(alias, actor, happenedOn);
    }
}
