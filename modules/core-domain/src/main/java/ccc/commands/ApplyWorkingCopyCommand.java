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
import ccc.types.CommandType;


/**
 * Command: apply the current working copy.
 *
 * @author Civic Computing Ltd.
 */
public class ApplyWorkingCopyCommand
    extends
        UpdateResourceCommand<Void> {

    private final UUID _id;
    private final String _comment;
    private final boolean _isMajorEdit;


    /**
     * Constructor.
     *
     * @param repository The DAO used for CRUD operations, etc.
     * @param audit The audit log to record business actions.
     * @param id The resource's id.
     * @param comment The comment for the page edit.
     * @param isMajorEdit A boolean for major edit.
     */
    public ApplyWorkingCopyCommand(final ResourceRepository repository,
                                   final LogEntryRepository audit,
                                   final UUID id,
                                   final String comment,
                                   final boolean isMajorEdit) {
        super(repository, audit, null);
        _id = id;
        _comment = comment;
        _isMajorEdit = isMajorEdit;
    }


    /** {@inheritDoc} */
    @Override
    public Void doExecute(final User actor,
                          final Date happenedOn) throws CccCheckedException {

        final Resource r = getRepository().find(Resource.class, _id);
        r.confirmLock(actor);

        if (r instanceof WCAware<?>) {
            final WCAware<?> wcAware = (WCAware<?>) r;
            final RevisionMetadata rm =
                new RevisionMetadata(happenedOn, actor, _isMajorEdit, _comment);
            wcAware.applyWorkingCopy(rm);
        } else {
            throw new WorkingCopyNotSupportedException(r);
        }

        update(r, actor, happenedOn);

        return null;
    }


    /** {@inheritDoc} */
    @Override
    protected CommandType getType() { return CommandType.RESOURCE_APPLY_WC; }
}
