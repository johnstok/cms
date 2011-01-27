/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
package ccc.services.ejb3;

import static ccc.api.types.Permission.*;
import static javax.ejb.TransactionAttributeType.*;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import ccc.api.core.File;
import ccc.api.core.SearchEngine;
import ccc.api.core.SearchEngine2;
import ccc.api.exceptions.EntityNotFoundException;
import ccc.api.types.Paragraph;
import ccc.api.types.ParagraphType;
import ccc.api.types.PredefinedResourceNames;
import ccc.api.types.SearchResult;
import ccc.api.types.SortOrder;
import ccc.domain.FileEntity;
import ccc.domain.PageEntity;
import ccc.domain.ResourceEntity;
import ccc.domain.Setting;
import ccc.persistence.DataRepository;
import ccc.persistence.IRepositoryFactory;
import ccc.persistence.ResourceRepository;
import ccc.persistence.SettingsRepository;
import ccc.plugins.PluginFactory;
import ccc.plugins.markup.XHTML;
import ccc.plugins.search.Index;
import ccc.plugins.search.Indexer;
import ccc.plugins.search.TextExtractor;


/**
 * Lucene Implementation of the {@link SearchEngine} interface.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=SearchEngine.NAME)
@TransactionAttribute(REQUIRED)
@Local(SearchEngine2.class)
@RolesAllowed({})
public class SearchEngineEJB
    extends
        AbstractEJB
    implements
        SearchEngine2 {

    private static final Logger LOG =
        Logger.getLogger(SearchEngineEJB.class.getName());

    @PersistenceContext private EntityManager _em;

    private ResourceRepository _resources;


    /** Constructor. */
    public SearchEngineEJB() { super(); }


    /**
     * Constructor.
     *
     * @param rdao The ResourceDao.
     */
    public SearchEngineEJB(final ResourceRepository rdao) {
        _resources = rdao;
    }


    /** {@inheritDoc} */
    @Override
    @PermitAll
    public SearchResult find(final String searchTerms,
                             final int resultCount,
                             final int page) {
        final long then = System.currentTimeMillis();
        final SearchResult res =
            createIndex().find(
                searchTerms,
                null,
                null,
                currentUser().getPrincipals(),
                resultCount,
                page);
        LOG.debug("Search time in millis: "+(System.currentTimeMillis()-then));
        return res;
    }


    /** {@inheritDoc} */
    @Override
    @PermitAll
    public SearchResult find(final String searchTerms,
                             final String sort,
                             final SortOrder order,
                             final int resultCount,
                             final int page) {
        final long then = System.currentTimeMillis();
        final SearchResult res =
            createIndex().find(
                searchTerms,
                sort,
                order,
                currentUser().getPrincipals(),
                resultCount,
                page);
        LOG.debug("Search time in millis: "+(System.currentTimeMillis()-then));
        return res;
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({SEARCH_REINDEX})
    public void index() {
        final Indexer lucene = createIndexer();
        try {
            lucene.startUpdate();
            indexPages(lucene);
            indexFiles(lucene);
            lucene.commitUpdate();
        } catch (final RuntimeException e) {
            LOG.error("Error indexing resources.", e);
            lucene.rollbackUpdate();
        }
    }


    /** {@inheritDoc} */
    @Override
    @PermitAll
    public SearchResult similar(final String uuid,
                                final int noOfResultsPerPage,
                                final int page) {
        return createIndex().similar(uuid, noOfResultsPerPage, page);
    }


    private void indexFiles(final Indexer lucene) {
        final List<FileEntity> files = _resources.files();
        for (final FileEntity f : files) {
            if (canIndex(f)) {
                if (!PredefinedResourceNames.CONTENT.equals(
                    f.getRoot().getName().toString())) {
                    LOG.debug(
                        "Skipped indexing for non content file : "
                        +f.getTitle());
                    continue;
                }

                final TextExtractor extractor =
                    lucene.createExtractor(f.getMimeType());
                if (null==extractor) {
                    LOG.debug("No extractor for mime-type: "+f.getMimeType());
                    continue;
                }

                // Work around JBAS-6615
                final DataRepository dr =
                    IRepositoryFactory.DEFAULT.create(_em)
                    .createDataRepository();
                dr.retrieve(f.getData(), extractor);
                final String content =
                    XHTML.cleanUpContent(f.getTitle()+" "+extractor.getText());
                final File fDto = f.forCurrentRevision();
                fDto.setContent(content);
                lucene.createDocument(fDto, f.getAclHierarchy());
                LOG.debug("Indexed file: "+f.getTitle());
            }
        }
    }


    private void indexPages(final Indexer lucene) {
        final List<PageEntity> pages = _resources.pages();
        for (final PageEntity p : pages) {
            if (canIndex(p)) {
                lucene.createDocument(
                    p.forCurrentRevision(), p.getAclHierarchy());
                LOG.debug("Indexed page: "+p.getTitle());
            }
        }
    }


    private String extractContent(final PageEntity page) {
        final StringBuilder sb = new StringBuilder(page.getTitle());
        for (final Paragraph p : page.currentRevision().getParagraphs()) {
            if (ParagraphType.TEXT == p.getType() && p.getText() != null) {
                sb.append(" ");
                sb.append(XHTML.cleanUpContent(p.getText()));
            }
        }
        return sb.toString();
    }


    private boolean canIndex(final ResourceEntity r) {
        return r.isVisible() && r.isIndexable();
    }


    @PostConstruct @SuppressWarnings("unused")
    private void configureCoreData() {
        _resources =
            IRepositoryFactory.DEFAULT.create(_em).createResourceRepository();
    }


    private Indexer createIndexer() {
        return new PluginFactory().createIndexer(getIndexPath());
    }


    private Index createIndex() {
        return new PluginFactory().createIndex(getIndexPath());
    }


    private String getIndexPath() {
        final SettingsRepository settings = new SettingsRepository(_em);
        Setting indexPath;
        try {
            indexPath = settings.find(Setting.Name.LUCENE_INDEX_PATH);
        } catch (final EntityNotFoundException e) {
            throw new RuntimeException(
                "No setting for "+Setting.Name.LUCENE_INDEX_PATH, e);
        }
        final String indexPathValue = indexPath.getValue();
        return indexPathValue;
    }
}
