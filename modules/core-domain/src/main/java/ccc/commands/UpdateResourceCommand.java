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

import ccc.api.CommandType;
import ccc.domain.LogEntry;
import ccc.domain.Resource;
import ccc.domain.Snapshot;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Dao;


/**
 * Abstract superclass for commands that update resources.
 *
 * @author Civic Computing Ltd.
 */
class UpdateResourceCommand {


    private final Dao      _dao;
    private final AuditLog _audit;


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
        audit(resource, happenedOn, actor);
    }


    private void audit(final Resource resource,
                       final Date happenedOn,
                       final User actor) {
        CommandType type;
        switch (resource.type()) {
            case ALIAS:
                type = CommandType.ALIAS_UPDATE;
                break;
            case FILE:
                type = CommandType.FILE_UPDATE;
                break;
            case FOLDER:
                type = CommandType.FOLDER_UPDATE;
                break;
            case PAGE:
                type = CommandType.PAGE_UPDATE;
                break;
            case TEMPLATE:
                type = CommandType.TEMPLATE_UPDATE;
                break;
            default:
                throw new UnsupportedOperationException();
        }

        final Snapshot ss = new Snapshot(resource.createSnapshot());

        final LogEntry le =
            new LogEntry(
                actor,
                type,
                happenedOn,
                resource.id(),
                ss.getDetail());
        _audit.record(le);
    }


    /**
     * Accessor.
     *
     * @return Returns the DAO.
     */
    protected Dao getDao() {
        return _dao;
    }


    /**
     * Accessor.
     *
     * @return Returns the audit logger.
     */
    protected AuditLog getAudit() {
        return _audit;
    }


}
