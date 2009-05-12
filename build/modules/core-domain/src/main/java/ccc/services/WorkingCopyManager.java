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

import java.util.UUID;

import ccc.commons.DBC;
import ccc.domain.LockMismatchException;
import ccc.domain.Resource;
import ccc.domain.Snapshot;
import ccc.domain.UnlockedException;
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
                                  final Snapshot workingCopy)
                               throws UnlockedException, LockMismatchException {
        final Resource r = _dao.findLocked(Resource.class, id, actor);
        r.workingCopy(workingCopy);
    }

    /** {@inheritDoc} */
    @Override
    public void clearWorkingCopy(final User actor,
                                 final UUID id)
                               throws UnlockedException, LockMismatchException {
        final Resource r = _dao.findLocked(Resource.class, id, actor);
        r.clearWorkingCopy();
    }
}
