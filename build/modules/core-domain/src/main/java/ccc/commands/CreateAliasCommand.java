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

import ccc.domain.Alias;
import ccc.domain.CCCException;
import ccc.domain.CccCheckedException;
import ccc.domain.Resource;
import ccc.domain.User;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.ResourceRepository;
import ccc.types.CommandType;
import ccc.types.ResourceName;


/**
 * Command: create an alias.
 *
 * @author Civic Computing Ltd.
 */
class CreateAliasCommand
    extends
        CreateResourceCommand<Alias> {

    private final UUID _parentFolder;
    private final UUID _targetId;
    private final ResourceName _title;


    /**
     * Constructor.
     *
     * @param repository The DAO used for CRUD operations, etc.
     * @param audit The audit log to record business actions.
     * @param parentFolder The folder in which the alias should be created.
     * @param targetId The alias' target resource.
     * @param title The alias' title.
     */
    public CreateAliasCommand(final ResourceRepository repository,
                              final LogEntryRepository audit,
                              final UUID parentFolder,
                              final UUID targetId,
                              final ResourceName title) {
        super(repository, audit);
        _parentFolder = parentFolder;
        _targetId = targetId;
        _title = title;
    }

    /** {@inheritDoc} */
    @Override
    public Alias doExecute(final User actor,
                           final Date happenedOn) throws CccCheckedException {
        final Resource target = getRepository().find(Resource.class, _targetId);
        if (target == null) {
            throw new CCCException("Target does not exists.");
        }
        final Alias a = new Alias(_title.toString(), target);

        create(actor, happenedOn, _parentFolder, a);

        return a;
    }


    /** {@inheritDoc} */
    @Override
    protected CommandType getType() { return CommandType.ALIAS_CREATE; }
}
