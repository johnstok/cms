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

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import ccc.domain.CccCheckedException;
import ccc.domain.LogEntry;
import ccc.domain.Resource;
import ccc.domain.User;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.ResourceRepository;
import ccc.serialization.JsonImpl;
import ccc.types.CommandType;


/**
 * Command: update cache duration.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateResourceRolesCommand
    extends
        Command<Void> {

    private final UUID _id;
    private final Collection<String> _roles;


    /**
     * Constructor.
     *
     * @param repository The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     * @param id The id of the resource to update.
     * @param roles The new roles.
     */
    public UpdateResourceRolesCommand(final ResourceRepository repository,
                                      final LogEntryRepository audit,
                                      final UUID id,
                                      final Collection<String> roles) {
        super(repository, audit, null, null);
        _id = id;
        _roles = roles;
    }

    /** {@inheritDoc} */
    @Override
    public Void doExecute(final User actor,
                          final Date happenedOn) throws CccCheckedException {

        final Resource r = getRepository().find(Resource.class, _id);
        r.confirmLock(actor);

        r.roles(_roles);

        final LogEntry le = new LogEntry(
            actor,
            CommandType.RESOURCE_CHANGE_ROLES,
            happenedOn,
            _id,
            new JsonImpl(r).getDetail());
        getAudit().record(le);

        return null;
    }


    /** {@inheritDoc} */
    @Override
    protected CommandType getType() {
        return CommandType.RESOURCE_CHANGE_ROLES;
    }
}
