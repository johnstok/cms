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
import ccc.domain.Page;
import ccc.domain.RevisionMetadata;
import ccc.domain.User;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.ResourceRepository;
import ccc.rest.dto.PageDelta;
import ccc.types.CommandType;


/**
 * Command: updates a page with the specified delta.
 *
 * @author Civic Computing Ltd.
 */
public class UpdatePageCommand
    extends
        UpdateResourceCommand<Void> {

    private final UUID      _id;
    private final PageDelta _delta;
    private final boolean   _isMajorEdit;
    private final String    _comment;

    /**
     * Constructor.
     *
     * @param repository The DAO used for CRUD operations, etc.
     * @param audit The audit log to record business actions.
     * @param id The id of the page to update.
     * @param delta The changes to the page.
     * @param comment Comment describing the change.
     * @param isMajorEdit Is this a major change.
     */
    public UpdatePageCommand(final ResourceRepository repository,
                             final LogEntryRepository audit,
                             final UUID id,
                             final PageDelta delta,
                             final String comment,
                             final boolean isMajorEdit) {
        super(repository, audit, null);
        _id = id;
        _delta = delta;
        _comment = comment;
        _isMajorEdit = isMajorEdit;
    }


    /** {@inheritDoc} */
    @Override
    public Void doExecute(final User actor,
                          final Date happenedOn) throws CccCheckedException {

        final Page page = getRepository().find(Page.class, _id);
        page.confirmLock(actor);

        final RevisionMetadata rm =
            new RevisionMetadata(happenedOn, actor, _isMajorEdit, _comment);

        page.setOrUpdateWorkingCopy(_delta);
        page.applyWorkingCopy(rm);

        update(page, actor, happenedOn);

        return null;
    }


    /** {@inheritDoc} */
    @Override
    protected CommandType getType() { return CommandType.PAGE_UPDATE; }
}
