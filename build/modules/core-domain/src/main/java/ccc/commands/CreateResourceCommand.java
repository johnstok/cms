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
import ccc.persistence.ResourceRepository;
import ccc.serialization.JsonImpl;


/**
 * Abstract superclass for commands that create resources.
 *
 * @param <T> The result type of the command.
 *
 * @author Civic Computing Ltd.
 */
abstract class CreateResourceCommand<T> extends Command<T> {

    /**
     * Constructor.
     *
     * @param repository The ResourceDao used for CRUD operations, etc.
     * @param audit The audit log to record business actions.
     */
    public CreateResourceCommand(final ResourceRepository repository,
                                 final LogEntryRepository audit) {
        super(repository, audit);
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

        final Folder folder = getRepository().find(Folder.class, folderId);
        if (null==folder) {
            throw new CCCException("No folder exists with id: "+folderId);
        }
        folder.add(newResource);
        getRepository().create(newResource);

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

        final JsonImpl ss = new JsonImpl(resource);

        final LogEntry le =
            new LogEntry(
                actor,
                getType(),
                happenedOn,
                resource.id(),
                ss.getDetail());

        getAudit().record(le);
    }
}
