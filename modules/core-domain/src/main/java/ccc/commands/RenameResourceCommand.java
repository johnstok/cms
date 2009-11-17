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

import ccc.domain.CccCheckedException;
import ccc.domain.LogEntry;
import ccc.domain.Resource;
import ccc.domain.ResourceExistsException;
import ccc.domain.User;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.ResourceRepository;
import ccc.serialization.JsonImpl;
import ccc.types.CommandType;
import ccc.types.ResourceName;


/**
 * Command: rename a resource.
 *
 * @author Civic Computing Ltd.
 */
public class RenameResourceCommand
    extends
        Command<Void> {

    private final UUID   _resourceId;
    private final String _name;

    /**
     * Constructor.
     *
     * @param repository The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     * @param resourceId The resource to rename.
     * @param name The new name for the resource.
     */
    public RenameResourceCommand(final ResourceRepository repository,
                                 final LogEntryRepository audit,
                                 final UUID resourceId,
                                 final String name) {
        super(repository, audit, null, null);
        _resourceId = resourceId;
        _name = name;
    }


    /** {@inheritDoc} */
    @Override
    public Void doExecute(final User actor,
                          final Date happenedOn) throws CccCheckedException {

        final Resource resource =
            getRepository().find(Resource.class, _resourceId);
        resource.confirmLock(actor);

        final ResourceName newName = new ResourceName(_name);
        final Resource existingResource =
            resource.parent().entryWithName(newName);
        if (null!=existingResource) {
            throw new ResourceExistsException(
                resource.parent(), existingResource);
        }

        resource.name(new ResourceName(_name));

        final LogEntry le =
            new LogEntry(
                actor,
                getType(),
                happenedOn,
                resource.id(),
                new JsonImpl(resource).getDetail());
        getAudit().record(le);

        return null;
    }


    /** {@inheritDoc} */
    @Override
    protected CommandType getType() { return CommandType.RESOURCE_RENAME; }
}
