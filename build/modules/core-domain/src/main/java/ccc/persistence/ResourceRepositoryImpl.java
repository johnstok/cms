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
package ccc.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import ccc.domain.CCCException;
import ccc.domain.Folder;
import ccc.domain.HistoricalResource;
import ccc.domain.Resource;
import ccc.domain.Revision;
import ccc.domain.User;
import ccc.types.ResourceName;
import ccc.types.ResourcePath;


/**
 * A repository for resource objects.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceRepositoryImpl implements ResourceRepository {

    private final Repository            _repository;

    /**
     * Constructor.
     *
     * @param repository The DAO used for persistence.
     */
    public ResourceRepositoryImpl(final Repository repository) {
        _repository = repository;
    }


    /** {@inheritDoc} */
    @Override
    public List<Resource> lockedByUser(final User actor) {
        return
            _repository.list(QueryNames.RESOURCES_LOCKED_BY_USER,
                      Resource.class,
                      actor);
    }


    /** {@inheritDoc} */
    @Override
    public List<Resource> locked() {
        return _repository.list(QueryNames.LOCKED_RESOURCES, Resource.class);
    }


    /** {@inheritDoc} */
    @Override
    public Map<Integer, ? extends Revision<?>> history(final UUID resourceId) {
        final Resource r = _repository.find(Resource.class, resourceId);
        return (r instanceof HistoricalResource<?, ?>)
            ? ((HistoricalResource<?, ?>) r).revisions()
            : new HashMap<Integer, Revision<?>>();
    }


    /** {@inheritDoc} */
    @Override
    public <T extends Resource> T find(final Class<T> type, final UUID id) {
        return _repository.find(type, id);
    }


    /** {@inheritDoc} */
    @Override
    public <T> List<T> list(final String queryName,
                            final Class<T> resultType,
                            final Object... params) {
        return _repository.list(queryName, resultType, params);
    }


    /** {@inheritDoc} */
    @Override
    public <T> T find(final String queryName,
                      final Class<T> resultType,
                      final Object... params) {
        return _repository.find(queryName, resultType, params);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Resource lookup(final String rootName, final ResourcePath path) {
        final Folder root =
            _repository.find(
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
        return _repository.find(
            QueryNames.RESOURCE_BY_LEGACY_ID, Resource.class, legacyId);
    }
}