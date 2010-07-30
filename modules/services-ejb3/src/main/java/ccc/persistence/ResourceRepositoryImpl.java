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
import java.util.Set;
import java.util.UUID;

import javax.persistence.EntityManager;

import ccc.api.core.PageCriteria;
import ccc.api.core.ResourceCriteria;
import ccc.api.exceptions.EntityNotFoundException;
import ccc.api.types.DBC;
import ccc.api.types.Paragraph;
import ccc.api.types.PredefinedResourceNames;
import ccc.api.types.ResourceName;
import ccc.api.types.ResourcePath;
import ccc.api.types.SortOrder;
import ccc.commons.Exceptions;
import ccc.domain.AliasEntity;
import ccc.domain.FileEntity;
import ccc.domain.FolderEntity;
import ccc.domain.HistoricalResource;
import ccc.domain.PageEntity;
import ccc.domain.ResourceEntity;
import ccc.domain.RevisionEntity;
import ccc.domain.Search;
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
    public Map<Integer, ? extends RevisionEntity<?>> history(
        final UUID resourceId) {
        final ResourceEntity r = find(ResourceEntity.class, resourceId);
        return (r instanceof HistoricalResource<?, ?>)
            ? ((HistoricalResource<?, ?>) r).revisions()
            : new HashMap<Integer, RevisionEntity<?>>();
    }


    /** {@inheritDoc} */
    @Override
    public <T extends ResourceEntity> T find(final Class<T> type,
                                             final UUID id) {
        return discardDeleted(_repository.find(type, id));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceEntity lookup(final ResourcePath path) {
        final FolderEntity root = root(PredefinedResourceNames.CONTENT);
        try {
            return root.navigateTo(path);
        } catch (final RuntimeException e) { // TODO: Dodgy?
            throw new EntityNotFoundException((UUID) null);
        }
    }

    /** {@inheritDoc} */
    @Override
    public ResourceEntity lookupWithLegacyId(final String legacyId) {
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
    public FolderEntity root(final String name) {
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
        final int pageSize) {
        final ResourceEntity r = find(ResourceEntity.class, folderId);

        final StringBuffer query = new StringBuffer();
        query.append("FROM ccc.domain.FileEntity f "
            + " WHERE f._publishedBy is not null "
            + " AND f._history[f._currentRev]._mimeType._primaryType = 'image' "
            + " AND f._parent = :parent "
            + " order by upper(f._name) ASC");
        final Map<String, Object> params = new HashMap<String, Object>();
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
        final List<FolderEntity> roots =
            new ArrayList<FolderEntity>();
        final List<FolderEntity> allRoots =
            list(QueryNames.ROOTS, FolderEntity.class);

        // Exclude the trash root.
        for (final FolderEntity root : allRoots) {
            if (!PredefinedResourceNames.TRASH.equals(
                    root.getName().toString())) {
                roots.add(root);
            }
        }

        return roots;
    }


    /** {@inheritDoc} */
    @Override
    public List<TemplateEntity> templates(final int pageNo,
                                          final int pageSize) {
        final Map<String, Object> params = new HashMap<String, Object>();
        final StringBuffer query = new StringBuffer();
        query.append("from ");
        query.append(TemplateEntity.class.getName());
        query.append(" where ");
        query.append(" _deleted = :deleted");
        params.put("deleted", Boolean.FALSE);

        return _repository.listDyn(
            query.toString(),
            TemplateEntity.class,
            pageNo,
            pageSize,
            params);
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
    public TemplateEntity template(final String name) {
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
                                        final Object... params) {
        return discardDeleted(_repository.find(queryName, resultType, params));
    }


    private <T extends ResourceEntity> T discardDeleted(final T resource) {
        if (resource.isDeleted()) {
            throw new EntityNotFoundException(resource.getId());
        }
        return resource;
    }


    private <T extends ResourceEntity> List<T> discardDeleted(
                                                            final List<T> all) {
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

        query.append(
            "SELECT r FROM ccc.domain.ResourceEntity r"
            + " LEFT JOIN r._lockedBy"
            + " LEFT JOIN r._publishedBy");

        appendMetaConditions(criteria.getMetadata(), query, params);
        appendCriteria(criteria, f, query, params);
        appendSorting(sort, sortOrder, query);

        return
            _repository.listDyn(
                query.toString(),
                ResourceEntity.class,
                pageNo,
                pageSize > MAX_RESULTS ? MAX_RESULTS : pageSize,
                params);
    }


    /** {@inheritDoc} */
    @Override
    public List<PageEntity> list(final PageCriteria criteria,
                                 final int pageNo,
                                 final int pageSize) {

        final StringBuffer query = new StringBuffer();
        final Map<String, Object> params = new HashMap<String, Object>();

        criteria.setType(null);

        query.append(
            "SELECT r"
            + " FROM ccc.domain.PageRevision p"
            + " INNER JOIN p._page AS r");

        appendParaConditions(criteria.getParas(), query, params);

        appendMetaConditions(criteria.getMetadata(), query, params);

        appendCriteria(
            criteria,
            (null==criteria.getParent())
                ? null : find(FolderEntity.class, criteria.getParent()),
            query,
            params);

        query.append((params.size()>0) ? " AND" : " WHERE");
        query.append(" r._currentRev=p._revNo");

        appendSorting(
            criteria.getSortField(),
            criteria.getSortOrder(),
            query);

        return
            _repository.listDyn(
                query.toString(),
                PageEntity.class,
                pageNo,
                pageSize > MAX_RESULTS ? MAX_RESULTS : pageSize,
                params);
    }


    /** {@inheritDoc} */
    @Override
    public long totalCount(final PageCriteria criteria) {

        final StringBuffer query = new StringBuffer();
        final Map<String, Object> params = new HashMap<String, Object>();

        criteria.setType(null);

        query.append(
            "SELECT COUNT(r)"
            + " FROM ccc.domain.PageRevision p"
            + " INNER JOIN p._page AS r");

        appendParaConditions(criteria.getParas(), query, params);

        appendMetaConditions(criteria.getMetadata(), query, params);

        appendCriteria(
            criteria,
            (null==criteria.getParent())
                ? null : find(FolderEntity.class, criteria.getParent()),
            query,
            params);

        query.append((params.size()>0) ? " AND" : " WHERE");
        query.append(" r._currentRev=p._revNo");

        return _repository.scalarLong(query.toString(), params);
    }


    private void appendMetaConditions(final Map<String, String> metadata,
                                      final StringBuffer query,
                                      final Map<String, Object> params) {
        for (final Map.Entry<String, String> d : metadata.entrySet()) {
            query.append((params.size()>0) ? " AND" : " WHERE");
            query.append(
                " r._metadata['"+d.getKey()+"'] LIKE "+":m_"+d.getKey());
            params.put("m_"+d.getKey(), d.getValue());
        }
    }


    private void appendParaConditions(final Set<Paragraph> paras,
                                      final StringBuffer query,
                                      final Map<String, Object> params) {
        for (final Paragraph p : paras) {
            query.append(", IN (p._content) p_"+p.getName());
        }
        for (final Paragraph p : paras) {
            query.append((params.size()>0) ? " AND" : " WHERE");
            query.append(" (p_"+p.getName()+"._name='"+p.getName());
            switch (p.getType()) {
                case BOOLEAN:
                    query.append(
                        "' AND p_"+p.getName()
                        +"._boolean = :p_"+p.getName()+")");
                    params.put("p_"+p.getName(), p.getBoolean());
                    break;
                case DATE:
                    query.append(
                        "' AND p_"+p.getName()
                        +"._date = :p_"+p.getName()+")");
                    params.put("p_"+p.getName(), p.getDate());
                    break;
                case TEXT:
                    query.append(
                        "' AND p_"+p.getName()
                        +"._text LIKE :p_"+p.getName()+")");
                    params.put("p_"+p.getName(), p.getText());
                    break;
                case NUMBER:
                    query.append(
                        "' AND p_"+p.getName()
                        +"._text = :p_"+p.getName()+")");
                    params.put("p_"+p.getName(), p.getText());
                    break;
                default:
                    throw new RuntimeException(
                        "Unsupported paragraphe type: "+p.getType());
            }
        }
    }


    private void appendCriteria(final ResourceCriteria criteria,
                                final FolderEntity f,
                                final StringBuffer query,
                                final Map<String, Object> params) {

        if (null!=f) {
            query.append((params.size()>0) ? " AND" : " WHERE");
            query.append(" r._parent = :parent");
            params.put("parent", f);
        }

        if (null!=criteria.getTag()) {
            query.append((params.size()>0) ? " AND" : " WHERE");
            query.append(" :tag IN elements(r._tags)");
            params.put("tag", criteria.getTag());
        }

        if (null!=criteria.getChangedBefore()) {
            query.append((params.size()>0) ? " AND" : " WHERE");
            query.append(" :dateChangedBefore > r._dateChanged");
            params.put("dateChangedBefore", criteria.getChangedBefore());
        }

        if (null!=criteria.getChangedAfter()) {
            query.append((params.size()>0) ? " AND" : " WHERE");
            query.append(" :dateChangedAfter < r._dateChanged");
            params.put("dateChangedAfter", criteria.getChangedAfter());
        }

        if (null!=criteria.getMainmenu()) {
            query.append((params.size()>0) ? " AND" : " WHERE");
            query.append(" r._includeInMainMenu = :includeInMainMenu");
            params.put("includeInMainMenu", criteria.getMainmenu());
        }


        if (null!=criteria.getPublished()) {
            query.append((params.size()>0) ? " AND" : " WHERE");
            if (criteria.getPublished().booleanValue()) {
                query.append(" r._publishedBy is not null");
            } else {
                query.append(" r._publishedBy is null");
            }
        }

        if (null!=criteria.getLocked()) {
            query.append((params.size()>0) ? " AND" : " WHERE");
            if (criteria.getLocked().booleanValue()) {
                query.append(" r._lockedBy is not null");
            } else {
                query.append(" r._lockedBy is null");
            }
        }

        query.append((params.size()>0) ? " AND" : " WHERE");
        query.append(" r._deleted = :deleted");
        params.put("deleted", Boolean.FALSE);

        if (null!=criteria.getType()) {
            // prepared statement wont work here
            try {
                switch (criteria.getType()) {
                    case FOLDER:
                        query.append((params.size()>0) ? " AND" : " WHERE");
                        query.append(" r.class = ");
                        query.append(FolderEntity.class.getName());
                        break;
                    case SEARCH:
                        query.append((params.size()>0) ? " AND" : " WHERE");
                        query.append(" r.class = ");
                        query.append(Search.class.getName());
                        break;
                    case PAGE:
                        query.append((params.size()>0) ? " AND" : " WHERE");
                        query.append(" r.class = ");
                        query.append(PageEntity.class.getName());
                        break;
                    case TEMPLATE:
                        query.append((params.size()>0) ? " AND" : " WHERE");
                        query.append(" r.class = ");
                        query.append(TemplateEntity.class.getName());
                        break;
                    case ALIAS:
                        query.append((params.size()>0) ? " AND" : " WHERE");
                        query.append(" r.class = ");
                        query.append(AliasEntity.class.getName());
                        break;
                    case FILE:
                        query.append((params.size()>0) ? " AND" : " WHERE");
                        query.append(" r.class = ");
                        query.append(FileEntity.class.getName());
                        break;
                    default:
                        break; // Append nothing.
                }
            } catch (final RuntimeException e) {
                Exceptions.swallow(e); // Ignore bad data.
            }
        }
    }

    private void appendSorting(final String sort,
                               final SortOrder sortOrder,
                               final StringBuffer query) {

        if (null != sort) {
            boolean knownSort = true;
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
            } else if ("manual".equalsIgnoreCase(sort)) {
                query.append(" order by r._parentIndex ");
            } else if ("date_changed".equalsIgnoreCase(sort)) {
                query.append(" order by r._dateChanged ");
            } else if ("date_created".equalsIgnoreCase(sort)) {
                query.append(" order by r._dateCreated ");
            } else {
                knownSort = false;
            }
            if (knownSort) {
                query.append(sortOrder.name());
            }
        }
    }



    /** {@inheritDoc} */
    @Override
    public long imagesCount(final UUID folderId) {
        final ResourceEntity r = find(ResourceEntity.class, folderId);
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("parent", r);
        return _repository.scalarLong("SELECT COUNT(f) "
        + "FROM ccc.domain.FileEntity f "
        + " WHERE f._publishedBy is not null"
        + " AND f._history[f._currentRev]._mimeType._primaryType = 'image' "
        + " AND f._parent = :parent", params);
    }


    /** {@inheritDoc} */
    @Override
    public long totalCount(final ResourceCriteria criteria,
                           final FolderEntity f) {
        final StringBuffer query = new StringBuffer();
        final Map<String, Object> params = new HashMap<String, Object>();
        query.append("SELECT COUNT(r) FROM ccc.domain.ResourceEntity r "
            + " LEFT JOIN r._lockedBy LEFT JOIN r._publishedBy");
        appendMetaConditions(criteria.getMetadata(), query, params);
        appendCriteria(criteria, f, query, params);
        return _repository.scalarLong(query.toString(), params);
    }


    /** {@inheritDoc} */
    @Override
    public long templateCount() {
        final StringBuffer query = new StringBuffer();
        final Map<String, Object> params = new HashMap<String, Object>();
        query.append("SELECT COUNT(t) FROM ");
        query.append(TemplateEntity.class.getName());
        query.append(" t ");

        return _repository.scalarLong(query.toString(), params);
    }

}
