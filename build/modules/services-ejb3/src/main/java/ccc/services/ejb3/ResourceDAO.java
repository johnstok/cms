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

import static javax.ejb.TransactionAttributeType.*;

import java.util.List;
import java.util.UUID;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;

import ccc.domain.CCCException;
import ccc.domain.LogEntry;
import ccc.domain.Resource;
import ccc.domain.User;
import ccc.services.AuditLogLocal;
import ccc.services.QueryManagerLocal;
import ccc.services.ResourceDAOLocal;
import ccc.services.UserManagerLocal;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name="ResourceDAO")
@TransactionAttribute(REQUIRED)
@Local(ResourceDAOLocal.class)
public class ResourceDAO implements ResourceDAOLocal {

    @EJB(name="UserManager", beanInterface=UserManagerLocal.class)
    private UserManagerLocal _users;
    @EJB(name="QueryManager", beanInterface=QueryManagerLocal.class)
    private QueryManagerLocal _queries;
    @EJB(name="AuditLog", beanInterface=AuditLogLocal.class)
    private AuditLogLocal _audit;

    /** Constructor. */
    @SuppressWarnings("unused") private ResourceDAO() { /* NO-OP */ }

    /**
     * Constructor.
     *
     * @param userDAO UserManager service.
     * @param queryManager QueryManager service.
     * @param audit AuditLog service.
     */
    public ResourceDAO(final UserManagerLocal userDAO,
                       final QueryManagerLocal queryManager,
                       final AuditLogLocal audit) {
        _users = userDAO;
        _queries = queryManager;
        _audit = audit;
    }

    /** {@inheritDoc} */
    @Override
    public Resource lock(final String resourceId) {
        final Resource r = _queries.find(Resource.class, resourceId);
        if (r.isLocked()) {
            throw new CCCException("Resource is already locked.");
        }
        r.lock(_users.loggedInUser());
        _audit.recordLock(r);
        return r;
    }

    /** {@inheritDoc} */
    @Override
    public Resource unlock(final String resourceId) {
        final User loggedInUser = _users.loggedInUser();
        final Resource r = _queries.find(Resource.class, resourceId);
        // TODO: Test that r.isLocked() == true
        if (r.canUnlock(loggedInUser)) { // TODO: Fold if/else into r.unlock()?
            r.unlock();
        } else {
            throw new CCCException("User not allowed to unlock this resource.");
        }
        _audit.recordUnlock(r);
        return r;
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

    /** {@inheritDoc} */
    @Override
    public List<LogEntry> history(final String resourceId) {
        return
            _queries.list("resourceHistory",
                          LogEntry.class,
                          UUID.fromString(resourceId));
    }

    /** {@inheritDoc} */
    @Override
    public void updateTags(final String resourceId, final String tags) {
        final Resource r = _queries.find(Resource.class, resourceId);
        r.tags(tags);
    }
}
