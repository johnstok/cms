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

import ccc.api.core.ResourceCriteria;
import ccc.api.exceptions.EntityNotFoundException;
import ccc.api.types.DBC;
import ccc.api.types.PredefinedResourceNames;
import ccc.api.types.ResourceName;
import ccc.api.types.ResourceOrder;
import ccc.api.types.ResourcePath;
import ccc.api.types.ResourceType;
import ccc.api.types.SortOrder;
import ccc.domain.FileEntity;
import ccc.domain.FolderEntity;
import ccc.domain.HistoricalResource;
import ccc.domain.PageEntity;
import ccc.domain.ResourceEntity;
import ccc.domain.RevisionEntity;
import ccc.domain.TemplateEntity;


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
    public List<ResourceEntity> locked() {
        return list(QueryNames.LOCKED_RESOURCES, ResourceEntity.class);
    }


    /** {@inheritDoc} */
    @Override
    public Map<Integer, ? extends RevisionEntity<?>> history(final UUID resourceId)
    throws EntityNotFoundException {
        final ResourceEntity r = find(ResourceEntity.class, resourceId);
        return (r instanceof HistoricalResource<?, ?>)
            ? ((HistoricalResource<?, ?>) r).revisions()
            : new HashMap<Integer, RevisionEntity<?>>();
    }


    /** {@inheritDoc} */
    @Override
    public <T extends ResourceEntity> T find(final Class<T> type, final UUID id)
    throws EntityNotFoundException {
        return discardDeleted(_repository.find(type, id));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceEntity lookup(final ResourcePath path)
    throws EntityNotFoundException {
        final FolderEntity root = root(PredefinedResourceNames.CONTENT);
        try {
            return root.navigateTo(path);
        } catch (final RuntimeException e) { // TODO: Dodgy?
            throw new EntityNotFoundException((UUID) null);
        }
    }

    /** {@inheritDoc} */
    @Override
    public ResourceEntity lookupWithLegacyId(final String legacyId)
    throws EntityNotFoundException {
        return
            discardDeleted(
                _repository.find(
                    QueryNames.RESOURCE_BY_LEGACY_ID,
                    ResourceEntity.class,
                    legacyId));
    }

    /** {@inheritDoc} */
    @Override
    public List<ResourceEntity> lookupWithMetadataKey(final String key) {
        return
        discardDeleted(
            _repository.list(
                QueryNames.RESOURCE_BY_METADATA_KEY,
                ResourceEntity.class,
                key));
    }

    /** {@inheritDoc} */
    @Override
    public FolderEntity root(final String name) throws EntityNotFoundException {
        return
            find(
                QueryNames.ROOT_BY_NAME,
                FolderEntity.class,
                new ResourceName(name));
    }


    /** {@inheritDoc} */
    @Override
    public void create(final ResourceEntity newResource) {
        _repository.create(newResource);
    }


    /** {@inheritDoc} */
    @Override
    public List<FileEntity> images(final UUID folderId,
        final int pageNo,
        final int pageSize)
    throws EntityNotFoundException {
        final ResourceEntity r = find(ResourceEntity.class, folderId);

        final StringBuffer query = new StringBuffer();
        query.append("FROM ccc.domain.FileEntity f WHERE f._publishedBy is not null "
            + " AND f._history[f._currentRev]._mimeType._primaryType = 'image' "
            + " AND f._parent = :parent "
            + " order by upper(f._name) ASC");
        final Map<String,Object> params = new HashMap<String,Object>();
        params.put("parent", r);

        return
        _repository.listDyn(
            query.toString(),
            FileEntity.class,
            pageNo,
            pageSize > MAX_RESULTS ? MAX_RESULTS : pageSize,
            params);
    }


    /** {@inheritDoc} */
    @Override
    public List<FolderEntity> roots() {
        final List<FolderEntity> roots = new ArrayList<FolderEntity>();

        // Exclude the trash root.
        for (final FolderEntity root : list(QueryNames.ROOTS, FolderEntity.class)) {
            if (!PredefinedResourceNames.TRASH.equals(root.getName().toString())) {
                roots.add(root);
            }
        }

        return roots;
    }


    /** {@inheritDoc} */
    @Override
    public List<TemplateEntity> templates() {
        return list(QueryNames.ALL_TEMPLATES, TemplateEntity.class);
    }


    /** {@inheritDoc} */
    @Override
    public List<FileEntity> files() {
        return list(QueryNames.ALL_FILES, FileEntity.class);
    }


    /** {@inheritDoc} */
    @Override
    public List<PageEntity> pages() {
        return list(QueryNames.ALL_PAGES, PageEntity.class);
    }


    /** {@inheritDoc} */
    @Override
    public TemplateEntity template(final String name) throws EntityNotFoundException {
        return find(
            QueryNames.TEMPLATE_BY_NAME,
            TemplateEntity.class,
            new ResourceName(name));
    }


    private <T extends ResourceEntity> List<T> list(final String queryName,
                                              final Class<T> resultType,
                                              final Object... params) {
        return discardDeleted(_repository.list(queryName, resultType, params));
    }


    private <T extends ResourceEntity> T find(final String queryName,
                                        final Class<T> resultType,
                                        final Object... params)
    throws EntityNotFoundException {
        return discardDeleted(_repository.find(queryName, resultType, params));
    }


    private <T extends ResourceEntity> T discardDeleted(final T resource)
    throws EntityNotFoundException {
        if (resource.isDeleted()) {
            throw new EntityNotFoundException(resource.getId());
        }
        return resource;
    }


    private <T extends ResourceEntity> List<T> discardDeleted(final List<T> all) {
        final List<T> nondeleted = new ArrayList<T>();
        for (final T r : all) {
            if (!r.isDeleted()) { nondeleted.add(r); }
        }
        return nondeleted;
    }


    /** {@inheritDoc} */
    @Override
    public List<ResourceEntity> list(final ResourceCriteria criteria,
                               final FolderEntity f,
                               final String sort,
                               final SortOrder sortOrder,
                               final int pageNo,
                               final int pageSize) {

        final StringBuffer query = new StringBuffer();
        final Map<String, Object> params = new HashMap<String, Object>();

        if (null != sort && ("locked".equalsIgnoreCase(sort)
            || "published".equalsIgnoreCase(sort))) {
            query.append("select r from ccc.domain.ResourceEntity r "
                + "LEFT JOIN r._lockedBy LEFT JOIN r._publishedBy");
        } else {
            query.append("select r from ccc.domain.ResourceEntity r ");
        }

        appendCriteria(criteria, f, query, params);
        appendSorting(f, sort, sortOrder, query);

        return
        _repository.listDyn(
            query.toString(),
            ResourceEntity.class,
            pageNo,
            pageSize > MAX_RESULTS ? MAX_RESULTS : pageSize,
            params);
    }


    private void appendCriteria(final ResourceCriteria criteria,
                                final FolderEntity f,
                                final StringBuffer query,
                                final Map<String, Object> params) {

        if (null!=f) {
            query.append(" where r._parent = :parent");
            params.put("parent", f);
        }

        if (null!=criteria.getTag()) {
            query.append((params.size()>0) ? " and" : " where");
            query.append(" :tag in elements(r._tags)");
            params.put("tag", criteria.getTag());
        }

        if (null!=criteria.getChangedBefore()) {
            query.append((params.size()>0) ? " and" : " where");
            query.append(" :dateChangedBefore > r._dateChanged");
            params.put("dateChangedBefore", criteria.getChangedBefore());
        }

        if (null!=criteria.getChangedAfter()) {
            query.append((params.size()>0) ? " and" : " where");
            query.append(" :dateChangedAfter < r._dateChanged");
            params.put("dateChangedAfter", criteria.getChangedAfter());
        }

        if (null!=criteria.getMainmenu()) {
            boolean knownCriteria = false;
            if (criteria.getMainmenu().equalsIgnoreCase("true")) {
                knownCriteria = true;
                params.put("includeInMainMenu", Boolean.TRUE);
            } else if (criteria.getMainmenu().equalsIgnoreCase("false")) {
                knownCriteria = true;
                params.put("includeInMainMenu", Boolean.FALSE);
            }
            if (knownCriteria) {
                query.append((params.size()>0) ? " and" : " where");
                query.append(" r._includeInMainMenu = :includeInMainMenu");
            }
        }

    }

    private void appendSorting(final ResourceEntity resource,
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
            final FolderEntity f = resource.as(FolderEntity.class);
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
        final ResourceEntity r = find(ResourceEntity.class, folderId);
        final Map<String,Object> params = new HashMap<String,Object>();
        params.put("parent", r);
        return _repository.scalarLong("SELECT COUNT(f) FROM ccc.domain.FileEntity f "
        + " WHERE f._publishedBy is not null"
        + " AND f._history[f._currentRev]._mimeType._primaryType = 'image' "
        + " AND f._parent = :parent", params);
    }


    /** {@inheritDoc} */
    @Override
    public long totalCount(final ResourceCriteria criteria, final FolderEntity f) {
        final StringBuffer query = new StringBuffer();
        final Map<String,Object> params = new HashMap<String,Object>();
        query.append("SELECT COUNT(r) FROM ccc.domain.ResourceEntity r ");
        appendCriteria(criteria, f, query, params);
        return _repository.scalarLong(query.toString(), params);
    }
}
