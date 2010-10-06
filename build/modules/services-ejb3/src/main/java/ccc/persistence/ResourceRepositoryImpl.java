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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.EntityManager;

import ccc.api.core.PageCriteria;
import ccc.api.core.ResourceCriteria;
import ccc.api.core.ResourceSummary;
import ccc.api.exceptions.CCException;
import ccc.api.types.DBC;
import ccc.api.types.Paragraph;
import ccc.api.types.PredefinedResourceNames;
import ccc.api.types.Range;
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
import ccc.domain.WorkingCopySupport;


/**
 * A repository for resource objects.
 *
 * @author Civic Computing Ltd.
 */
class ResourceRepositoryImpl implements ResourceRepository {

    private static final int MAX_RESULTS = 2000;
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


    /** {@inheritDoc} */
    @Override
    public ResourceEntity lookup(final ResourcePath path) {
        final FolderEntity root = root(PredefinedResourceNames.CONTENT);
        return (root == null) ? null : root.navigateTo(path);
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
    public List<FileEntity> images(final ResourceCriteria criteria,
                                   final FolderEntity f,
                                   final int pageNo,
                                   final int pageSize) {

        final StringBuffer query = new StringBuffer();
        query.append("SELECT r FROM ccc.domain.FileEntity r "
            + " LEFT JOIN r._lockedBy"
            + " LEFT JOIN r._publishedBy"
            + " WHERE r._history[r._currentRev]._mimeType._primaryType = :type ");
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("type", "image");
        appendMetaConditions(criteria.getMetadata(), query, params);
        appendCriteria(criteria, f, query, params);
        final boolean sorted =
            appendSorting(criteria.getSortField(), criteria.getSortOrder(), query);

        if (!sorted && criteria.getSortField().equalsIgnoreCase("size")) {
            query.append(" ORDER BY r._history[r._currentRev]._size) ");
            query.append(criteria.getSortOrder().name());
        }

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
        query.append("FROM ");
        query.append(TemplateEntity.class.getName());
        query.append(" WHERE ");
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
        if (resource == null || resource.isDeleted()) {
            return null;
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

        appendParaConditions(criteria, query, params);

        appendMetaConditions(criteria.getMetadata(), query, params);

        appendCriteria(
            criteria,
            (null==criteria.getParent())
                ? null : find(FolderEntity.class, criteria.getParent()),
            query,
            params);

        query.append((params.size()>0) ? " AND" : " WHERE");
        query.append(" r._currentRev=p._revNo");

        final boolean hasRegularSort = appendSorting(
            criteria.getSortField(),
            criteria.getSortOrder(),
            query);

        if (criteria.isSortedByPara()) {
            appendParaSort(criteria, hasRegularSort, query);
        }

        return
            _repository.listDyn(
                query.toString(),
                PageEntity.class,
                pageNo,
                pageSize > MAX_RESULTS ? MAX_RESULTS : pageSize,
                params);
    }


    private void appendParaSort(final PageCriteria criteria,
                                final boolean hasRegularSort,
                                final StringBuffer query) {
        final String sName = safeName(criteria.getParaSortField());
        query.append(hasRegularSort ? "," : " ORDER BY");
        query.append(" ps_"+sName+".");
        switch (criteria.getParaSortType()) {
            case BOOLEAN:
                query.append("_boolean ");
                break;
            case NUMBER:
            case TEXT:
                query.append("sortableText ");
                break;
            case DATE:
                query.append("_date ");
                break;
            default:
                throw new RuntimeException(
                    "Unsupported sort type: "+criteria.getType());
        }
        query.append(criteria.getParaSortOrder());
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

        appendParaConditions(criteria, query, params);

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
            final String mName = safeName(d.getKey());
            final String cName = "m_"+mName;

            query.append((params.size()>0) ? " AND" : " WHERE");
            query.append(" r._metadata['"+mName+"'] LIKE "+":"+cName);

            params.put(cName, d.getValue());
        }
    }


    private void appendParaConditions(final PageCriteria criteria,
                                      final StringBuffer query,
                                      final Map<String, Object> params) {

        for (final Paragraph p : criteria.getParaMatches()) {
            final String pName = safeName(p.getName());
            final String cName = "p_"+pName;
            query.append(", IN (p._content) "+cName);
        }

        for (final String p : criteria.getParaRanges().keySet()) {
            final String pName = safeName(p);
            final String cName = "pr_"+pName;
            query.append(", IN (p._content) "+cName);
        }

        if (criteria.isSortedByPara()) {
            final String sName = safeName(criteria.getParaSortField());
            final String cName = "ps_"+sName;
            final String pnParam = "psn_"+sName;


            query.append(", IN (p._content) "+cName);
            query.append(" WHERE "+cName+"._name=:"+pnParam);
            params.put(pnParam, criteria.getParaSortField());
        }

        for (final Paragraph p : criteria.getParaMatches()) {
            final String pName = safeName(p.getName());
            final String cName = "p_"+pName;
            final String pnParam = "pn_"+pName;

            query.append((params.size()>0) ? " AND" : " WHERE");
            query.append(" ("+cName+"._name=:"+pnParam);
            params.put(pnParam, p.getName());
            switch (p.getType()) {
                case BOOLEAN:
                    query.append(
                        " AND "+cName
                        +"._boolean = :"+cName+")");
                    params.put(cName, p.getBoolean());
                    break;
                case DATE:
                    query.append(
                        " AND "+cName
                        +"._date = :"+cName+")");
                    params.put(cName, p.getDate());
                    break;
                case TEXT:
                    query.append(
                        " AND "+cName
                        +"._text LIKE :"+cName+")");
                    params.put(cName, p.getText());
                    break;
//                case NUMBER:
//                    query.append(
//                        "' AND "+cName
//                        +"._text = :"+cName+")");
//                    params.put(cName, p.getNumber());
//                    break;
                default:
                    throw new RuntimeException(
                        "Unsupported paragraphe type: "+p.getType());
            }
        }

        for (final Map.Entry<String, Range<?>> p
                                        : criteria.getParaRanges().entrySet()) {
            final String pName = safeName(p.getKey());
            final String cName = "pr_"+pName;
            final String pnName = "prn_"+pName;
            final String eName = cName+"_end";
            final String sName = cName+"_start";
            final Range<?> pRange = p.getValue();

            if (null==pRange.getStart() && null==pRange.getEnd()) {
                continue;
            }

            query.append((params.size()>0) ? " AND" : " WHERE");
            query.append(" ("+cName+"._name=:"+pnName);
            params.put(pnName, pName);

            if (Date.class.equals(pRange.getType())) {
                if (null!=pRange.getStart()) {
                    query.append(" AND " + cName + "._date > :" + sName);
                    params.put(sName, pRange.getStart());
                }
                if (null!=pRange.getEnd()) {
                    query.append(" AND " + cName + "._date < :" + eName);
                    params.put(eName, pRange.getEnd());
                }

//            } else if (BigDecimal.class.equals(pRange.getType())) {
//                if (null!=pRange.getStart()) {
//                    query.append(" AND " + cName + "._text > :" + sName);
//                    params.put(sName, String.valueOf(pRange.getStart()));
//                }
//                if (null!=pRange.getEnd()) {
//                    query.append(" AND " + cName + "._text < :" + eName);
//                    params.put(eName, String.valueOf(pRange.getEnd()));
//                }
//
            } else if (String.class.equals(pRange.getType())) {
                if (null!=pRange.getStart()) {
                    query.append(" AND " + cName + "._text > :" + sName);
                    params.put(sName, pRange.getStart());
                }
                if (null!=pRange.getEnd()) {
                    query.append(" AND " + cName + "._text < :" + eName);
                    params.put(eName, pRange.getEnd());
                }

            } else {
                throw new RuntimeException(
                    "Unsupported range type: "+pRange.getType());
            }

            query.append(")");
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
        if (null!=criteria.getName()) {
            query.append((params.size()>0) ? " AND" : " WHERE");
            query.append(" lower(r._name) like lower(:name)");
            params.put("name", criteria.getName());
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


    private boolean appendSorting(final String sort,
                               final SortOrder sortOrder,
                               final StringBuffer query) {
        if (null != sort) {
            boolean knownSort = true;
            if ("title".equalsIgnoreCase(sort)) {
                query.append(" ORDER BY upper(r._title) ");

            } else if ("mm_include".equalsIgnoreCase(sort)
                       || ResourceSummary.MM_INCLUDE.equals(sort)) {
                query.append(" ORDER BY upper(r._includeInMainMenu) ");

            } else if ("locked".equalsIgnoreCase(sort)
                       || ResourceSummary.LOCKED.equals(sort)) {
                query.append(" ORDER BY upper(r._lockedBy._username) ");

            } else if ("published".equalsIgnoreCase(sort)
                       || ResourceSummary.PUBLISHED.equals(sort)) {
                query.append(" ORDER BY upper(r._publishedBy._username) ");

            } else if ("name".equalsIgnoreCase(sort)
                       || ResourceSummary.NAME.equals(sort)) {
                query.append(" ORDER BY upper(r._name) ");

            } else if ("type".equalsIgnoreCase(sort)
                       || ResourceSummary.TYPE.equals(sort)) {
                query.append(" ORDER BY r.class ");

            } else if ("manual".equalsIgnoreCase(sort)) {
                query.append(" ORDER BY r._parentIndex ");

            } else if ("date_changed".equalsIgnoreCase(sort)
                       || ResourceSummary.DATE_CHANGED.equals(sort)) {
                query.append(" ORDER BY r._dateChanged ");

            } else if ("date_created".equalsIgnoreCase(sort)
                       || ResourceSummary.DATE_CREATED.equals(sort)) {
                query.append(" ORDER BY r._dateCreated ");

            } else {
                knownSort = false;
            }

            if (knownSort) {
                query.append(sortOrder.name());
            }

            return knownSort;
        }
        return false;
    }


    /** {@inheritDoc} */
    @Override
    public long imagesCount(final ResourceCriteria criteria,
                            final FolderEntity f) {
        final Map<String, Object> params = new HashMap<String, Object>();
        final StringBuffer query = new StringBuffer();
        query.append("SELECT COUNT(r) FROM ccc.domain.FileEntity r "
            + " LEFT JOIN r._lockedBy"
            + " LEFT JOIN r._publishedBy"
            + " WHERE r._history[r._currentRev]._mimeType._primaryType = :type ");

        params.put("type", "image");
        appendMetaConditions(criteria.getMetadata(), query, params);
        appendCriteria(criteria, f, query, params);

        return _repository.scalarLong(query.toString(), params);
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


    private String safeName(final String unsafe) {
        return ResourceName.escape(unsafe).toString();
    }


    /** {@inheritDoc} */
    @Override
    public WorkingCopySupport<?, ?, ?> findWcAware(final UUID id) {
        final List<WorkingCopySupport> results =
            discardDeleted(
                _repository.list(
                    QueryNames.WC_BY_ID,
                    WorkingCopySupport.class,
                    id));
        if (results.size()<1) { return null; }
        if (results.size()>1) {
            throw new CCException("Multiple resources with ID: "+id);
        }
        return results.get(0);
    }
}
