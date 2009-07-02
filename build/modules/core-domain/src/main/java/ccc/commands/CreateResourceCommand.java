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

import ccc.api.CommandType;
import ccc.api.JsonKeys;
import ccc.domain.CCCException;
import ccc.domain.Folder;
import ccc.domain.LogEntry;
import ccc.domain.RemoteExceptionSupport;
import ccc.domain.Resource;
import ccc.domain.Snapshot;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Dao;


/**
 * Abstract superclass for commands that create resources.
 *
 * @author Civic Computing Ltd.
 */
public abstract class CreateResourceCommand {

    private final Dao      _dao;
    private final AuditLog _audit;

    /**
     * Constructor.
     *
     * @param dao The ResourceDao used for CRUD operations, etc.
     * @param audit The audit log to record business actions.
     */
    public CreateResourceCommand(final Dao dao, final AuditLog audit) {
        _dao = dao;
        _audit = audit;
    }

    /**
     * Create a resource in the specified folder.
     *
     * @param actor The user performing the action.
     * @param happenedOn The date the command was executed.
     * @param folderId The folder in which the resource will be created.
     * @param newResource The new resource.
     *
     * @throws RemoteExceptionSupport If the command fails.
     */
    protected void create(final User actor,
                          final Date happenedOn,
                          final UUID folderId,
                          final Resource newResource)
                                                throws RemoteExceptionSupport {
        newResource.dateCreated(happenedOn);

        final Folder folder = _dao.find(Folder.class, folderId);
        if (null==folder) {
            throw new CCCException("No folder exists with id: "+folderId);
        }
        folder.add(newResource);
        _dao.create(newResource);

        audit(newResource, actor, happenedOn);
    }


    protected void audit(final Resource resource,
                         final User actor,
                         final Date happenedOn) {

        CommandType type;
        switch (resource.type()) {
            case ALIAS:
                type = CommandType.ALIAS_CREATE;
                break;
            case FILE:
                type = CommandType.FILE_CREATE;
                break;
            case FOLDER:
                type = CommandType.FOLDER_CREATE;
                break;
            case PAGE:
                type = CommandType.PAGE_CREATE;
                break;
            case SEARCH:
                type = CommandType.SEARCH_CREATE;
                break;
            case TEMPLATE:
                type = CommandType.TEMPLATE_CREATE;
                break;
            default:
                throw new UnsupportedOperationException();
        }

        final Snapshot ss = new Snapshot(resource.createSnapshot());

        ss.set(JsonKeys.NAME, resource.name().toString());

        final User pBy = resource.publishedBy();
        ss.set(JsonKeys.PUBLISHED_BY, (null==pBy)?null:pBy.id().toString());

        final Resource parent = resource.parent();
        ss.set(JsonKeys.PARENT_ID, (null==parent)?null:parent.id().toString());

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
