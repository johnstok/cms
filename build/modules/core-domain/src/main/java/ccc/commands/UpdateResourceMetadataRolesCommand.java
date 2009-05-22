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
import java.util.Map;
import java.util.UUID;

import ccc.domain.LockMismatchException;
import ccc.domain.Resource;
import ccc.domain.UnlockedException;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Dao;


/**
 * Command: update resource metadata.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateResourceMetadataRolesCommand {

    private final Dao      _dao;
    private final AuditLog _audit;

    /**
     * Constructor.
     *
     * @param dao The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     */
    public UpdateResourceMetadataRolesCommand(final Dao dao,
                                              final AuditLog audit) {
        _dao = dao;
        _audit = audit;
    }

    /**
     * Update metadata of the resource.
     *
     * @param id The resource to update.
     * @param metadata The new metadata to set.
     * @param actor The user who performed the command.
     * @param happenedOn When the command was performed.
     *
     * @throws LockMismatchException If the resource is locked by another user.
     * @throws UnlockedException If the resource is unlocked.
     */
    public void execute(final User actor,
                        final Date happenedOn,
                        final UUID id,
                        final Map<String, String> metadata)
                               throws UnlockedException, LockMismatchException {
        final Resource r = _dao.find(Resource.class, id);
        r.confirmLock(actor);

        r.clearMetadata();
        for (final Map.Entry<String, String> metadatum: metadata.entrySet()) {
            r.addMetadatum(metadatum.getKey(), metadatum.getValue());
        }

        _audit.recordUpdateMetadata(r, actor, happenedOn);
    }

}
