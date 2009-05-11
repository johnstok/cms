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
package ccc.services;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import ccc.domain.CCCException;
import ccc.domain.Folder;
import ccc.domain.LogEntry;
import ccc.domain.Resource;
import ccc.domain.ResourceName;
import ccc.domain.ResourcePath;
import ccc.domain.Template;
import ccc.domain.User;
import ccc.services.api.Duration;


/**
 * EJB Implementation of the {@link ResourceDao} interface.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceDaoImpl implements ResourceDao {

    private final AuditLog       _audit;
    private final Dao            _dao;

    /**
     * Constructor.
     *
     * @param audit AuditLog service.
     * @param dao The DAO used for persistence.
     */
    public ResourceDaoImpl(final AuditLog audit,
                           final Dao dao) {
        _audit = audit;
        _dao = dao;
    }





    /** {@inheritDoc} */
    @Override
    public void createRoot(final User actor, final Folder folder) {
        final Resource possibleRoot =
            _dao.find(QueryNames.ROOT_BY_NAME, Resource.class, folder.name());
        if (null!=possibleRoot) {
            throw new CCCException("Root exists with name: "+folder.name());
        }
        _dao.create(folder);
        _audit.recordCreate(
            folder,
            actor,
            folder.dateCreated());
    }


    /** {@inheritDoc} */
    @Override
    public Resource lock(final User actor,
                         final Date happenedOn,
                         final UUID resourceId) {
        final Resource r = _dao.find(Resource.class, resourceId);
        final User u = actor;
        r.lock(u);
        _audit.recordLock(r, u, happenedOn);
        return r;
    }


    /** {@inheritDoc} */
    @Override
    public Resource unlock(final User actor,
                           final Date happenedOn,
                           final UUID resourceId) {
        final User loggedInUser = actor;
        final Resource r = _dao.find(Resource.class, resourceId);
        r.unlock(loggedInUser);
        _audit.recordUnlock(r, loggedInUser, happenedOn);
        return r;
    }


    /** {@inheritDoc} */
    @Override
    public List<Resource> lockedByCurrentUser(final User actor) {
        return
            _dao.list(QueryNames.RESOURCES_LOCKED_BY_USER,
                      Resource.class,
                      actor);
    }


    /** {@inheritDoc} */
    @Override
    public List<Resource> locked() {
        return _dao.list(QueryNames.LOCKED_RESOURCES, Resource.class);
    }


    /** {@inheritDoc} */
    @Override
    public List<LogEntry> history(final UUID resourceId) {
        return
            _dao.list(QueryNames.RESOURCE_HISTORY, LogEntry.class, resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public void updateTags(final User actor,
                           final Date happenedOn,
                           final UUID resourceId,
                           final String tags) {
        final User loggedInUser = actor;
        final Resource r = findLocked(Resource.class, resourceId, loggedInUser);
        r.tags(tags);
        _audit.recordUpdateTags(r, loggedInUser,  happenedOn);
    }


    /** {@inheritDoc} */
    @Override
    public Resource publish(final User actor,
                            final Date happenedOn,
                            final UUID resourceId) {
        final User u = actor;
        final Resource r = findLocked(Resource.class, resourceId, u);
        r.publish(u);
        r.dateChanged(happenedOn);
        _audit.recordPublish(r, u, r.dateChanged());
        return r;
    }

    /** {@inheritDoc} */
    @Override
    public Resource publish(final UUID resourceId,
                            final UUID userId,
                            final Date publishedOn) {
        final User publishedBy = _dao.find(User.class, userId);
        final Resource r =
            findLocked(Resource.class, resourceId, publishedBy);
        r.publish(publishedBy);
        r.dateChanged(publishedOn);
        _audit.recordPublish(r, publishedBy, publishedOn);
        return r;
    }


    /** {@inheritDoc} */
    @Override
    public Resource unpublish(final User actor,
                              final Date happenedOn,
                              final UUID resourceId) {
        final User u = actor;
        final Resource r = findLocked(Resource.class, resourceId, u);
        r.unpublish();
        _audit.recordUnpublish(r, u, happenedOn);
        return r;
    }


    /** {@inheritDoc} */
    @Override
    public Resource unpublish(final UUID resourceId,
                              final UUID actor,
                              final Date happendedOn) {
        final User u = _dao.find(User.class, actor);
        final Resource r =
            findLocked(Resource.class, resourceId, u);
        r.unpublish();
        _audit.recordUnpublish(r, u, happendedOn);
        return r;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void updateTemplateForResource(final User actor,
                                          final Date happenedOn,
                                          final UUID resourceId,
                                          final UUID templateId) {
        final Resource r = findLocked(Resource.class, resourceId, actor);
        final Template t =
            (null==templateId)
                ? null
                : find(Template.class, templateId);

        r.template(t);

        _audit.recordChangeTemplate(r, actor, happenedOn);
    }


    /** {@inheritDoc} */
    @Override
    public void move(final User actor,
                     final Date happenedOn,
                     final UUID resourceId,
                     final UUID newParentId) {
        // TODO: Check new parent doesn't contain resource with same name!

        final User u = actor;
        final Resource resource = findLocked(Resource.class, resourceId, u);
        final Folder newParent = find(Folder.class, newParentId);

        resource.parent().remove(resource);
        newParent.add(resource);

        _audit.recordMove(resource, u, happenedOn);
    }


    /** {@inheritDoc} */
    @Override
    public void rename(final User actor,
                       final Date happenedOn,
                       final UUID resourceId,
                       final String name) {
        // TODO: Check parent doesn't contain resource with new name!
        final User u = actor;
        final Resource resource = findLocked(Resource.class, resourceId, u);
        resource.name(new ResourceName(name));
        _audit.recordRename(resource, u, happenedOn);
    }


    /** {@inheritDoc} */
    @Override
    public <T extends Resource> T find(final Class<T> type, final UUID id) {
        return _dao.find(type, id);
    }


    /** {@inheritDoc} */
    @Override
    public <T extends Resource> T findLocked(final Class<T> type,
                                              final UUID id,
                                              final User lockedBy) {
        final T r = _dao.find(type, id);
        r.confirmLock(lockedBy);
        return r;
    }


    /** {@inheritDoc} */
    @Override
    public <T> List<T> list(final String queryName,
                            final Class<T> resultType,
                            final Object... params) {
        return _dao.list(queryName, resultType, params);
    }


    /** {@inheritDoc} */
    @Override
    public <T> T find(final String queryName,
                      final Class<T> resultType,
                      final Object... params) {
        return _dao.find(queryName, resultType, params);
    }


    /** {@inheritDoc} */
    @Override
    public void includeInMainMenu(final User actor,
                                  final Date happenedOn,
                                  final UUID id,
                                  final boolean b) {
        final Resource r = findLocked(Resource.class, id, actor);
        final User u = actor;
        r.includeInMainMenu(b);
        if (b) {
            _audit.recordIncludeInMainMenu(r, u, happenedOn);
        } else {
            _audit.recordRemoveFromMainMenu(r, u, happenedOn);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void updateMetadata(final User actor,
                               final Date happenedOn,
                               final UUID id,
                               final Map<String,
                               String> metadata) {
        final Resource r = findLocked(Resource.class, id, actor);
        final User u = actor;
        r.clearMetadata();
        for (final String key : metadata.keySet()) {
            r.addMetadatum(key, metadata.get(key));
        }
        _audit.recordUpdateMetadata(r, u, happenedOn);
    }

    /** {@inheritDoc} */
    @Override
    public void changeRoles(final User actor,
                            final Date happenedOn,
                            final UUID id,
                            final Collection<String> roles) {
        final Resource r = findLocked(Resource.class, id, actor);
        final User u = actor;
        r.roles(roles);
        _audit.recordChangeRoles(r, u, happenedOn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Resource lookup(final String rootName, final ResourcePath path) {
        final Folder root =
            _dao.find(
                QueryNames.ROOT_BY_NAME,
                Folder.class,
                new ResourceName(rootName));

        if (null==root) {
            return null;
        }

        try {
            return root.navigateTo(path);
        } catch (final CCCException e) {
            return null;
        }
    }

    /** {@inheritDoc} */
    @Override
    public Resource lookupWithLegacyId(final String legacyId) {
        return _dao.find(
            QueryNames.RESOURCE_BY_LEGACY_ID, Resource.class, legacyId);
    }


    /** {@inheritDoc} */
    @Override
    public void updateCache(final User actor,
                            final Date happenedOn,
                            final UUID resourceId,
                            final Duration duration) {
        final User loggedInUser = actor;
        final Resource r = findLocked(Resource.class, resourceId, actor);
        if (duration == null) {
            r.cache(null);
        } else {
            r.cache(duration);
        }
        _audit.recordUpdateCache(r, loggedInUser, happenedOn);
    }
}
