/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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
import ccc.domain.CccCheckedException;
import ccc.domain.Resource;
import ccc.domain.User;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.Repository;


/**
 * Command: updates an alias.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateAliasCommand extends UpdateResourceCommand {

    /**
     * Constructor.
     *
     * @param repository The DAO used for CRUD operations, etc.
     * @param audit The audit log to record business actions.
     */
    public UpdateAliasCommand(final Repository repository,
                              final LogEntryRepository audit) {
        super(repository, audit);
    }


    /**
     * Perform the update.
     *
     * @param targetId The new target for the alias.
     * @param aliasId The alias to update.
     * @param actor The user who performed the command.
     * @param happenedOn When the command was performed.
     *
     * @throws CccCheckedException If the command fails.
     */
    public void execute(final User actor,
                        final Date happenedOn,
                        final UUID targetId,
                        final UUID aliasId) throws CccCheckedException {
        final Alias alias = getDao().find(Alias.class, aliasId);
        alias.confirmLock(actor);

        final Resource target = getDao().find(Resource.class, targetId);
        alias.target(target);

        update(alias, null, false, actor, happenedOn);
    }
}
