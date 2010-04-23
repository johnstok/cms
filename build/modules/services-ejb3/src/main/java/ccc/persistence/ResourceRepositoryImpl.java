/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
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

import ccc.api.dto.ResourceCriteria;
import ccc.api.exceptions.EntityNotFoundException;
import ccc.api.types.DBC;
import ccc.api.types.PredefinedResourceNames;
import ccc.api.types.ResourceName;
import ccc.api.types.ResourceOrder;
import ccc.api.types.ResourcePath;
import ccc.api.types.ResourceType;
import ccc.api.types.SortOrder;
import ccc.domain.File;
import ccc.domain.Folder;
import ccc.domain.HistoricalResource;
import ccc.domain.Page;
import ccc.domain.Resource;
import ccc.domain.Revision;
import ccc.domain.Template;


/**
 * A repository for resource objects.
 *
 * @author Civic Computing Ltd.
 */
class ResourceRepositoryImpl implements ResourceRepository {

    private static final int MAX_RESULTS = 1000;
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
        } catch (final RuntimeException e) { // TODO: Dodgy?
            throw new EntityNotFoundException((UUID) null);
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
    public List<File> images(final UUID folderId,
        final int pageNo,
        final int pageSize)
    throws EntityNotFoundException {
        final Resource r = find(Resource.class, folderId);

        final StringBuffer query = new StringBuffer();
        query.append("FROM ccc.domain.File f WHERE f._publishedBy is not null "
            + " AND f._history[f._currentRev]._mimeType._primaryType = 'image' "
            + " AND f._parent = ? "
            + " order by upper(f._name) ASC");
        final List<Object> params = new ArrayList<Object>();
        params.add(r);

        return
        _repository.listDyn(
            query.toString(),
            File.class,
            pageNo,
            pageSize > MAX_RESULTS ? MAX_RESULTS : pageSize,
            params.toArray());
    }


    /** {@inheritDoc} */
    @Override
    public List<Folder> roots() {
        final List<Folder> roots = new ArrayList<Folder>();

        // Exclude the trash root.
        for (final Folder root : list(QueryNames.ROOTS, Folder.class)) {
            if (!PredefinedResourceNames.TRASH.equals(root.getName().toString())) {
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
            throw new EntityNotFoundException(resource.getId());
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


    /** {@inheritDoc} */
    @Override
    public List<Resource> list(final ResourceCriteria criteria,
                               final Folder f,
                               final String sort,
                               final SortOrder sortOrder,
                               final int pageNo,
                               final int pageSize) {

        final StringBuffer query = new StringBuffer();
        final List<Object> params = new ArrayList<Object>();

        if (null != sort && ("locked".equalsIgnoreCase(sort)
            || "published".equalsIgnoreCase(sort))) {
            query.append("select r from ccc.domain.Resource r "
                + "LEFT JOIN r._lockedBy LEFT JOIN r._publishedBy");
        } else {
            query.append("select r from ccc.domain.Resource r ");
        }

        appendCriteria(criteria, f, query, params);
        appendSorting(f, sort, sortOrder, query);

        return
        _repository.listDyn(
            query.toString(),
            Resource.class,
            pageNo,
            pageSize > MAX_RESULTS ? MAX_RESULTS : pageSize,
            params.toArray());
    }


    private void appendCriteria(final ResourceCriteria criteria,
                                final Folder f,
                                final StringBuffer query,
                                final List<Object> params) {

        if (null!=f) {
            query.append(" where r._parent = ?");
            params.add(f);
        }

        if (null!=criteria.getTag()) {
            query.append((params.size()>0) ? " and" : " where");
            query.append(" ? in elements(r._tags)");
            params.add(criteria.getTag());
        }

        if (null!=criteria.getChangedBefore()) {
            query.append((params.size()>0) ? " and" : " where");
            query.append(" ? > r._dateChanged");
            params.add(criteria.getChangedBefore());
        }

        if (null!=criteria.getChangedAfter()) {
            query.append((params.size()>0) ? " and" : " where");
            query.append(" ? < r._dateChanged");
            params.add(criteria.getChangedAfter());
        }

        if (null!=criteria.getMainmenu()) {
            if (criteria.getMainmenu().equalsIgnoreCase("true")) {
                query.append((params.size()>0) ? " and" : " where");
                query.append(" r._includeInMainMenu = true");
            } else if (criteria.getMainmenu().equalsIgnoreCase("false")) {
                query.append((params.size()>0) ? " and" : " where");
                query.append(" r._includeInMainMenu = false");
            }
        }
    }

    private void appendSorting(final Resource resource,
                               final String sort,
                               final SortOrder sortOrder,
                               final StringBuffer query) {

        boolean knownSort = false;
        if (null != sort) {
            knownSort = true;
            if ("title".equalsIgnoreCase(sort)) {
                query.append(" order by upper(r._title) ");
            } else if ("mm_include".equalsIgnoreCase(sort)) {
                query.append(" order by upper(r._includeInMainMenu) ");
            } else if ("locked".equalsIgnoreCase(sort)) {
                query.append(" order by upper(r._lockedBy._username) ");
            } else if ("published".equalsIgnoreCase(sort)) {
                query.append(" order by upper(r._publishedBy._username) ");
            } else if ("name".equalsIgnoreCase(sort)) {
                query.append(" order by upper(r._name) ");
            } else if ("type".equalsIgnoreCase(sort)) {
                query.append(" order by r.class ");
            } else {
                knownSort = false;
            }
            if (knownSort) {
                query.append(sortOrder.name());
            }
        }
        if (!knownSort
            && resource != null
            && resource.getType() == ResourceType.FOLDER) {
            final Folder f = resource.as(Folder.class);
            final ResourceOrder order = f.getSortOrder();
            switch (order){
                case DATE_CHANGED_ASC:
                    query.append(" order by r._dateChanged asc ");
                    break;
                case DATE_CHANGED_DESC:
                    query.append(" order by r._dateChanged desc ");
                    break;
                case DATE_CREATED_ASC:
                    query.append(" order by r._dateCreated asc ");
                    break;
                case DATE_CREATED_DESC:
                    query.append(" order by r._dateCreated desc ");
                    break;
                case NAME_ALPHANUM_ASC:
                    query.append(" order by r._name asc ");
                    break;
                case NAME_ALPHANUM_CI_ASC:
                    query.append(" order by upper(r._name) asc ");
                    break;
                case MANUAL:
                    query.append(" order by _parentIndex ");
                    break;
            }
        }
    }



    /** {@inheritDoc} */
    @Override
    public long imagesCount(final UUID folderId)
        throws EntityNotFoundException {
        final Resource r = find(Resource.class, folderId);
        return _repository.scalarLong("SELECT COUNT(f) FROM ccc.domain.File f "
        + " WHERE f._publishedBy is not null"
        + " AND f._history[f._currentRev]._mimeType._primaryType = 'image' "
        + " AND f._parent = ?", r);
    }


    /** {@inheritDoc} */
    @Override
    public long totalCount(final ResourceCriteria criteria, final Folder f) {
        final StringBuffer query = new StringBuffer();
        final List<Object> params = new ArrayList<Object>();
        query.append("SELECT COUNT(r) FROM ccc.domain.Resource r ");
        appendCriteria(criteria, f, query, params);
        return _repository.scalarLong(query.toString() ,params.toArray());
    }
}
