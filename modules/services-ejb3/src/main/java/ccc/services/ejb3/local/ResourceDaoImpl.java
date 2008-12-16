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
package ccc.services.ejb3.local;

import static javax.ejb.TransactionAttributeType.*;

import java.util.List;
import java.util.UUID;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;

import ccc.domain.CCCException;
import ccc.domain.Folder;
import ccc.domain.LogEntry;
import ccc.domain.Resource;
import ccc.domain.ResourceName;
import ccc.domain.Template;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.ResourceDao;
import ccc.services.UserManager;
import ccc.services.ejb3.support.BaseResourceDao;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name="ResourceDao")
@TransactionAttribute(REQUIRED)
@Local(ResourceDao.class)
public class ResourceDaoImpl extends BaseResourceDao implements ResourceDao {

    @EJB(name="UserManager")  private UserManager  _users;

    /** Constructor. */
    @SuppressWarnings("unused") public ResourceDaoImpl() { /* NO-OP */ }

    /**
     * Constructor.
     *
     * @param userDAO UserManager service.
     * @param audit AuditLog service.
     */
    public ResourceDaoImpl(final UserManager userDAO,
                       final AuditLog audit) {
        _users = userDAO;
        _audit = audit;
    }

    /** {@inheritDoc} */
    @Override
    public Resource lock(final String resourceId) {
        final Resource r = find(Resource.class, UUID.fromString(resourceId));
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
        final Resource r = find(Resource.class, resourceId);
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
            list("resourcesLockedByUser",
                 Resource.class,
                 _users.loggedInUser());
    }

    /** {@inheritDoc} */
    @Override
    public List<Resource> locked() {
        return list("lockedResources", Resource.class);
    }

    /** {@inheritDoc} */
    @Override
    public List<LogEntry> history(final String resourceId) {
        return
            list("resourceHistory",
                 LogEntry.class,
                 UUID.fromString(resourceId));
    }

    /** {@inheritDoc} */
    @Override
    public void updateTags(final String resourceId, final String tags) {
        final Resource r = find(Resource.class, resourceId);
        r.tags(tags);
    }

    /** {@inheritDoc} */
    @Override
    public Resource publish(final String resourceId) {
        final Resource r = find(Resource.class, resourceId);
        r.publish(_users.loggedInUser());
        _audit.recordPublish(r);
        return r;
    }

    /** {@inheritDoc} */
    @Override
    public Resource unpublish(final String resourceId) {
        final Resource r = find(Resource.class, resourceId);
        r.unpublish();
        _audit.recordUnpublish(r);
        return r;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateTemplateForResource(final UUID resourceId,
                                          final Template template) {
        // FIXME Don't check version!!!
        final Resource r = find(Resource.class, resourceId);
        r.template(template);
        _audit.recordChangeTemplate(r);
    }

    /** {@inheritDoc} */
    @Override
    public void move(final UUID resourceId, final UUID newParentId) {
        // FIXME Don't check version!!!
        final Resource resource = find(Resource.class, resourceId);
        final Folder newParent = find(Folder.class, newParentId);

        resource.parent().remove(resource);
        newParent.add(resource);

        _audit.recordMove(resource);
    }

    /** {@inheritDoc} */
    @Override
    public void rename(final UUID resourceId, final String name) {
        // FIXME Don't check version!!!
        final Resource resource = find(Resource.class, resourceId);
        resource.name(new ResourceName(name));
        _audit.recordRename(resource);
    }
}
