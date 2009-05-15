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

import ccc.domain.LockMismatchException;
import ccc.domain.Resource;
import ccc.domain.UnlockedException;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Dao;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class ApplyWorkingCopyCommand extends UpdateResourceCommand {


    /**
     * Constructor.
     *
     * @param dao The DAO used for CRUD operations, etc.
     * @param audit The audit log to record business actions.
     */
    public ApplyWorkingCopyCommand(final Dao dao, final AuditLog audit) {
        super(dao, audit);
    }

    /**
     * Applies the current working copy to update a resource.
     *
     * @param id The resource's id.
     * @param comment The comment for the page edit.
     * @param isMajorEdit A boolean for major edit.
     * @param actor The actor that performed the update.
     * @param happenedOn The date the update took place.
     * @throws LockMismatchException
     * @throws UnlockedException
     */
    public void execute(final User actor,
                        final Date happenedOn,
                        final UUID id,
                        final String comment,
                        final boolean isMajorEdit)
                               throws UnlockedException, LockMismatchException {
        final Resource r = _dao.find(Resource.class, id);
        r.confirmLock(actor);

        r.applySnapshot(r.workingCopy());
        r.clearWorkingCopy();

// FIXME: We don't validate the page against its definition
//        final Template template = page.computeTemplate(null);
//
//        if (template != null) {
//            validateFieldsForPage(page.paragraphs(), template.definition());
//        }

        update(r, comment, isMajorEdit, actor, happenedOn);
    }
}
