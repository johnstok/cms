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
import ccc.domain.CccCheckedException;
import ccc.domain.Folder;
import ccc.domain.LogEntry;
import ccc.domain.Resource;
import ccc.domain.User;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.Repository;
import ccc.serialization.JsonImpl;
import ccc.types.CommandType;


/**
 * Abstract superclass for commands that create resources.
 *
 * @author Civic Computing Ltd.
 */
public abstract class CreateResourceCommand {

    private final Repository      _repository;
    private final LogEntryRepository _audit;

    /**
     * Constructor.
     *
     * @param repository The ResourceDao used for CRUD operations, etc.
     * @param audit The audit log to record business actions.
     */
    public CreateResourceCommand(final Repository repository,
                                 final LogEntryRepository audit) {
        _repository = repository;
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
     * @throws CccCheckedException If the command fails.
     */
    protected void create(final User actor,
                          final Date happenedOn,
                          final UUID folderId,
                          final Resource newResource)
                                                throws CccCheckedException {
        newResource.dateCreated(happenedOn);
        newResource.dateChanged(happenedOn);

        final Folder folder = _repository.find(Folder.class, folderId);
        if (null==folder) {
            throw new CCCException("No folder exists with id: "+folderId);
        }
        folder.add(newResource);
        _repository.create(newResource);

        audit(newResource, actor, happenedOn);
    }


    /**
     * Audit the creation of a resource.
     *
     * @param resource The newly created resource.
     * @param actor The actor performing the command.
     * @param happenedOn When the command was performed.
     */
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
