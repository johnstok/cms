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
import ccc.domain.Resource;
import ccc.domain.RevisionMetadata;
import ccc.domain.User;
import ccc.domain.WCAware;
import ccc.domain.WorkingCopyNotSupportedException;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.ResourceRepository;


/**
 * Command: apply the current working copy.
 *
 * @author Civic Computing Ltd.
 */
public class ApplyWorkingCopyCommand extends UpdateResourceCommand {


    /**
     * Constructor.
     *
     * @param repository The DAO used for CRUD operations, etc.
     * @param audit The audit log to record business actions.
     */
    public ApplyWorkingCopyCommand(final ResourceRepository repository,
                                   final LogEntryRepository audit) {
        super(repository, audit);
    }

    /**
     * Applies the current working copy to update a resource.
     *
     * @param id The resource's id.
     * @param comment The comment for the page edit.
     * @param isMajorEdit A boolean for major edit.
     * @param actor The user who performed the command.
     * @param happenedOn When the command was performed.
     *
     * @throws CccCheckedException If the command fails.
     */
    public void execute(final User actor,
                        final Date happenedOn,
                        final UUID id,
                        final String comment,
                        final boolean isMajorEdit) throws CccCheckedException {
        final Resource r = getDao().find(Resource.class, id);
        r.confirmLock(actor);

        if (r instanceof WCAware<?>) {
            final WCAware<?> wcAware = (WCAware<?>) r;
            final RevisionMetadata rm =
                new RevisionMetadata(happenedOn, actor, isMajorEdit, comment);
            wcAware.applyWorkingCopy(rm);
        } else {
            throw new WorkingCopyNotSupportedException(r);
        }

        update(r, actor, happenedOn);
    }
}
