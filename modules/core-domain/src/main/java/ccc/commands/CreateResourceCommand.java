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

import ccc.domain.CCCException;
import ccc.domain.Folder;
import ccc.domain.Resource;
import ccc.domain.ResourceExistsException;
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
     * @throws ResourceExistsException If a resource with the same name already
     *  exists.
     */
    protected void create(final User actor,
                          final Date happenedOn,
                          final UUID folderId,
                          final Resource newResource)
                                                throws ResourceExistsException {
        newResource.dateCreated(happenedOn);

        final Folder folder = _dao.find(Folder.class, folderId);
        if (null==folder) {
            throw new CCCException("No folder exists with id: "+folderId);
        }
        folder.add(newResource);
        _dao.create(newResource);
        _audit.recordCreate(
            newResource,
            actor,
            happenedOn);
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
