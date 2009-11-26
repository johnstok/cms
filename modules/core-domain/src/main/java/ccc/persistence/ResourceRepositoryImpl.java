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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.EntityManager;

import ccc.domain.CCCException;
import ccc.domain.EntityNotFoundException;
import ccc.domain.File;
import ccc.domain.Folder;
import ccc.domain.HistoricalResource;
import ccc.domain.Page;
import ccc.domain.Resource;
import ccc.domain.Revision;
import ccc.domain.Template;
import ccc.types.DBC;
import ccc.types.PredefinedResourceNames;
import ccc.types.ResourceName;
import ccc.types.ResourcePath;


/**
 * A repository for resource objects.
 *
 * @author Civic Computing Ltd.
 */
class ResourceRepositoryImpl implements ResourceRepository {

    private final Repository _repository;

    /**
     * Constructor.
     *
     * @param repository The DAO used for persistence.
     */
    public ResourceRepositoryImpl(final Repository repository) {
        DBC.require().notNull(repository);
        _repository = repository;
    }


    /**
     * Constructor.
     *
     * @param em The JPA entity manager for this repository.
     */
    public ResourceRepositoryImpl(final EntityManager em) {
        this(new JpaRepository(em));
    }


    /** {@inheritDoc} */
    @Override
    public List<Resource> locked() {
        return list(QueryNames.LOCKED_RESOURCES, Resource.class);
    }


    /** {@inheritDoc} */
    @Override
    public Map<Integer, ? extends Revision<?>> history(final UUID resourceId)
    throws EntityNotFoundException {
        final Resource r = find(Resource.class, resourceId);
        return (r instanceof HistoricalResource<?, ?>)
            ? ((HistoricalResource<?, ?>) r).revisions()
            : new HashMap<Integer, Revision<?>>();
    }


    /** {@inheritDoc} */
    @Override
    public <T extends Resource> T find(final Class<T> type, final UUID id)
    throws EntityNotFoundException {
        return discardDeleted(_repository.find(type, id));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Resource lookup(final ResourcePath path)
    throws EntityNotFoundException {
        final Folder root = root(PredefinedResourceNames.CONTENT);
        try {
            return root.navigateTo(path);
        } catch (final CCCException e) { // TODO: Dodgy?
            throw new EntityNotFoundException(null);
        }
    }

    /** {@inheritDoc} */
    @Override
    public Resource lookupWithLegacyId(final String legacyId)
    throws EntityNotFoundException {
        return
            discardDeleted(
                _repository.find(
                    QueryNames.RESOURCE_BY_LEGACY_ID,
                    Resource.class,
                    legacyId));
    }

    /** {@inheritDoc} */
    @Override
    public List<Resource> lookupWithMetadataKey(final String key) {
        return
        discardDeleted(
            _repository.list(
                QueryNames.RESOURCE_BY_METADATA_KEY,
                Resource.class,
                key));
    }


    /** {@inheritDoc} */
    @Override
    public Folder root(final String name) throws EntityNotFoundException {
        return
            find(
                QueryNames.ROOT_BY_NAME,
                Folder.class,
                new ResourceName(name));
    }


    /** {@inheritDoc} */
    @Override
    public void create(final Resource newResource) {
        _repository.create(newResource);
    }


    /** {@inheritDoc} */
    @Override
    public List<File> images() {
        return list(QueryNames.ALL_IMAGES, File.class);
    }


    /** {@inheritDoc} */
    @Override
    public List<Folder> roots() {
        final List<Folder> roots = new ArrayList<Folder>();

        // Exclude the trash root.
        for (final Folder root : list(QueryNames.ROOTS, Folder.class)) {
            if (!PredefinedResourceNames.TRASH.equals(root.name().toString())) {
                roots.add(root);
            }
        }

        return roots;
    }


    /** {@inheritDoc} */
    @Override
    public List<Template> templates() {
        return list(QueryNames.ALL_TEMPLATES, Template.class);
    }


    /** {@inheritDoc} */
    @Override
    public List<File> files() {
        return list(QueryNames.ALL_FILES, File.class);
    }


    /** {@inheritDoc} */
    @Override
    public List<Page> pages() {
        return list(QueryNames.ALL_PAGES, Page.class);
    }


    /** {@inheritDoc} */
    @Override
    public Template template(final String name) throws EntityNotFoundException {
        return find(
            QueryNames.TEMPLATE_BY_NAME,
            Template.class,
            new ResourceName(name));
    }


    private <T extends Resource> List<T> list(final String queryName,
                                              final Class<T> resultType,
                                              final Object... params) {
        return discardDeleted(_repository.list(queryName, resultType, params));
    }


    private <T extends Resource> T find(final String queryName,
                                        final Class<T> resultType,
                                        final Object... params)
    throws EntityNotFoundException {
        return discardDeleted(_repository.find(queryName, resultType, params));
    }


    private <T extends Resource> T discardDeleted(final T resource)
    throws EntityNotFoundException {
        if (resource.isDeleted()) {
            throw new EntityNotFoundException(resource.id());
        }
        return resource;
    }


    private <T extends Resource> List<T> discardDeleted(final List<T> all) {
        final List<T> nondeleted = new ArrayList<T>();
        for (final T r : all) {
            if (!r.isDeleted()) { nondeleted.add(r); }
        }
        return nondeleted;
    }
}
