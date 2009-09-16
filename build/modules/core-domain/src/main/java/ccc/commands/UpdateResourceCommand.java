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

import ccc.domain.LogEntry;
import ccc.domain.Resource;
import ccc.domain.User;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.Repository;
import ccc.serialization.JsonImpl;
import ccc.types.CommandType;


/**
 * Abstract superclass for commands that update resources.
 *
 * @author Civic Computing Ltd.
 */
class UpdateResourceCommand {


    private final Repository      _repository;
    private final LogEntryRepository _audit;


    /**
     * Constructor.
     *
     * @param repository The ResourceDao used for CRUD operations, etc.
     * @param audit The audit log to record business actions.
     */
    public UpdateResourceCommand(final Repository repository,
                                 final LogEntryRepository audit) {
        _repository = repository;
        _audit = audit;
    }


    /**
     * Record that a resource has been updated (generates a log entry).
     *
     * @param resource The resource that was updated.
     * @param actor The actor who performed the update.
     * @param happenedOn The date the update took place.
     */
    protected void update(final Resource resource,
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

        final JsonImpl ss = new JsonImpl(resource);

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
    protected Repository getDao() {
        return _repository;
    }


    /**
     * Accessor.
     *
     * @return Returns the audit logger.
     */
    protected LogEntryRepository getAudit() {
        return _audit;
    }


}
