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

import ccc.domain.Resource;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Dao;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
class UpdateResourceCommand {


    protected final Dao      _dao;
    protected final AuditLog _audit;

    /**
     * Constructor.
     *
     * @param dao The ResourceDao used for CRUD operations, etc.
     * @param audit The audit log to record business actions.
     */
    public UpdateResourceCommand(final Dao dao, final AuditLog audit) {
        _dao = dao;
        _audit = audit;
    }

    /**
     * Record that a resource has been updated (generates a log entry).
     *
     * @param resource The resource that was updated.
     * @param comment The comment for the edit.
     * @param isMajorEdit The major edit boolean.
     * @param actor The actor who performed the update.
     * @param happenedOn The date the update took place.
     */
    protected void update(final Resource resource,
                       final String comment,
                       final boolean isMajorEdit,
                       final User actor,
                       final Date happenedOn) {
        resource.dateChanged(happenedOn);
        _audit.recordUpdate(
            resource,
            actor,
            resource.dateChanged(),
            comment,
            isMajorEdit);
    }
}
