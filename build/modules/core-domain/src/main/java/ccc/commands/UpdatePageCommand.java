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
import ccc.persistence.Repository;
import ccc.rest.dto.PageDelta;


/**
 * Command: updates a page with the specified delta.
 *
 * @author Civic Computing Ltd.
 */
public class UpdatePageCommand extends UpdateResourceCommand{

    /**
     * Constructor.
     *
     * @param repository The DAO used for CRUD operations, etc.
     * @param audit The audit log to record business actions.
     */
    public UpdatePageCommand(final Repository repository,
                             final LogEntryRepository audit) {
        super(repository, audit);
    }


    /**
     * Update a page.
     *
     * @param id The id of the page to update.
     * @param delta The changes to the page.
     * @param comment Comment describing the change.
     * @param isMajorEdit Is this a major change.
     * @param actor The user who performed the command.
     * @param happenedOn When the command was performed.
     *
     * @throws CccCheckedException If the command fails.
     */
    public void execute(final User actor,
                        final Date happenedOn,
                        final UUID id,
                        final PageDelta delta,
                        final String comment,
                        final boolean isMajorEdit) throws CccCheckedException {

        final Page page = getDao().find(Page.class, id);
        page.confirmLock(actor);

        final RevisionMetadata rm =
            new RevisionMetadata(happenedOn, actor, isMajorEdit, comment);

        page.setOrUpdateWorkingCopy(delta);
        page.applyWorkingCopy(rm);

        update(page, comment, isMajorEdit, actor, happenedOn);
    }
}
