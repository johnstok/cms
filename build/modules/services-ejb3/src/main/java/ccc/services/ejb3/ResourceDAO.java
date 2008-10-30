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
package ccc.services.ejb3;

import java.util.List;

import ccc.domain.CCCException;
import ccc.domain.CreatorRoles;
import ccc.domain.Resource;
import ccc.domain.User;
import ccc.services.QueryManagerLocal;
import ccc.services.ResourceDAOLocal;
import ccc.services.UserManagerLocal;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceDAO implements ResourceDAOLocal {

    private UserManagerLocal _users;
    private QueryManagerLocal _queries;

    /**
     * Constructor.
     *
     * @param queryManager
     * @param userDAO
     */
    public ResourceDAO(final UserManagerLocal userDAO,
                       final QueryManagerLocal queryManager) {
        _users = userDAO;
        _queries = queryManager;
    }

    /** {@inheritDoc} */
    @Override
    public void lock(final Resource r) {
        if (r.isLocked()) {
            throw new CCCException("Resource is already locked.");
        }
        r.lock(_users.loggedInUser());
    }

    /** {@inheritDoc} */
    @Override
    public void unlock(final Resource r) {
        final User loggedInUser = _users.loggedInUser();
        if (canUnlock(r, loggedInUser)) {
            r.unlock();
        } else {
            throw new CCCException("User not allowed to unlock this resource.");
        }
    }


    /** {@inheritDoc} */
    @Override
    public List<Resource> lockedByCurrentUser() {
        return
        _queries.list("resourcesLockedByUser",
                      Resource.class,
                      _users.loggedInUser());
    }

    /** {@inheritDoc} */
    @Override
    public List<Resource> locked() {
        return _queries.list("lockedResources", Resource.class);
    }

    /* TODO: Move to resource class? */
    private boolean canUnlock(final Resource r, final User loggedInUser) {
        return loggedInUser.equals(r.lockedBy())
        || loggedInUser.hasRole(CreatorRoles.ADMINISTRATOR);
    }
}
