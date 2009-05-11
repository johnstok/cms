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
package ccc.services;

import java.util.Date;
import java.util.UUID;

import ccc.commons.DBC;
import ccc.domain.Resource;
import ccc.domain.Snapshot;
import ccc.domain.User;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class WorkingCopyManager implements IWorkingCopyManager {

    private ResourceDao _dao;

    /**
     * Constructor.
     *
     * @param dao The ResourceDao used for CRUD operations, etc.
     */
    public WorkingCopyManager(final ResourceDao dao) {
        DBC.require().notNull(dao);
        _dao = dao;
    }

    /** {@inheritDoc} */
    @Override
    public void updateWorkingCopy(final User actor,
                                  final UUID id,
                                  final Snapshot workingCopy) {
        final Resource r = _dao.findLocked(Resource.class, id, actor);
        r.workingCopy(workingCopy);
    }

    /** {@inheritDoc} */
    @Override
    public void clearWorkingCopy(final User actor,
                                 final UUID id) {
        final Resource r = _dao.findLocked(Resource.class, id, actor);
        r.clearWorkingCopy();
    }

    /** {@inheritDoc} */
    @Override
    public void applyWorkingCopy(final UUID id,
                                 final String comment,
                                 final boolean isMajorEdit,
                                 final User actor,
                                 final Date happenedOn) {
        final Resource r = _dao.findLocked(Resource.class, id, actor);
        r.applySnapshot(r.workingCopy());
        r.clearWorkingCopy();

        // TODO: Move to page class?
//        final Template template = page.computeTemplate(null);
//
//        if (template != null) {
//            validateFieldsForPage(page.paragraphs(), template.definition());
//        }
        _dao.update(r, comment, isMajorEdit, actor, happenedOn);
    }

    /** {@inheritDoc} */
    @Override
    public void applyWorkingCopy(final UUID id, final User actor) {
        final Resource r = _dao.findLocked(Resource.class, id, actor);
        r.applySnapshot(r.workingCopy());
        r.clearWorkingCopy();
        _dao.update(actor, r);
    }
}
