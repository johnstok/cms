/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.search;

import java.util.List;

import org.apache.log4j.Logger;

import ccc.api.synchronous.SearchEngine;
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
import ccc.persistence.ResourceRepository;
import ccc.persistence.SettingsRepository;
import ccc.plugins.PluginFactory;
import ccc.plugins.search.Index;
import ccc.plugins.search.Indexer;
import ccc.plugins.search.TextExtractor;


/**
 * Shared behaviour for full text search.
 *
 * @author Civic Computing Ltd.
 */
public class SearchHelper
    implements
        SearchEngine {

    private static final Logger LOG = Logger.getLogger(SearchHelper.class);

    private final ResourceRepository _resources;
    private final DataRepository     _data;
    private final SettingsRepository _settings;


    /**
     * Constructor.
     *
     * @param resources The resource repo for retrieving resources.
     * @param data      The data repo for retrieving file contents.
     * @param settings  The settings repo for retrieving CC settings.
     */
    public SearchHelper(final ResourceRepository resources,
                    final DataRepository data,
                    final SettingsRepository settings) {
        _resources = resources;
        _data = data;
        _settings = settings;
    }


    /** {@inheritDoc} */
    @Override
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
    public SearchResult find(final String searchTerms,
                             final int noOfResultsPerPage,
                             final int page) {
        return createIndex().find(searchTerms, noOfResultsPerPage, page);
    }


    /** {@inheritDoc} */
    @Override
    public SearchResult find(final String searchTerms,
                             final String sort,
                             final SortOrder order,
                             final int resultCount,
                             final int page) {
        return createIndex().find(searchTerms, sort, order, resultCount, page);
    }


    /** {@inheritDoc} */
    @Override
    public SearchResult similar(final String uuid,
                                final int noOfResultsPerPage,
                                final int page) {
        return createIndex().similar(uuid, noOfResultsPerPage, page);
    }


    /** {@inheritDoc} */
    @Override
    public boolean isRunning() {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void start() {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void stop() {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    private void indexFiles(final Indexer lucene) {
        final List<FileEntity> files = _resources.files();
        for (final FileEntity f : files) {
            indexFile(lucene, f);
        }
    }


    private void indexFile(final Indexer lucene, final FileEntity f) {
        if (canIndex(f)) {
            if (!PredefinedResourceNames.CONTENT.equals(
                f.getRoot().getName().toString())) {
                LOG.debug(
                    "Skipped indexing for non content file : "
                    +f.getTitle());
                return;
            }

            final TextExtractor extractor =
                lucene.createExtractor(f.getMimeType());
            if (null==extractor) {
                LOG.debug("No extractor for mime-type: "+f.getMimeType());
                return;
            }

            // Work around JBAS-6615
            _data.retrieve(f.getData(), extractor);
            final String content =
                new PluginFactory().html()
                    .cleanUpContent(f.getTitle()+" "+extractor.getText());
            lucene.createDocument(
                f.getId(),
                f.getAbsolutePath(),
                f.getName(),
                f.getTitle(),
                f.getTags(),
                content,
                null);
            LOG.debug("Indexed file: "+f.getTitle());
        }
    }


    private void indexPages(final Indexer lucene) {
        final List<PageEntity> pages = _resources.pages();
        for (final PageEntity p : pages) {
            if (canIndex(p)) {
                indexPage(lucene, p);
            }
        }
    }


    private void indexPage(final Indexer lucene, final PageEntity p) {
        lucene.createDocument(
            p.getId(),
            p.getAbsolutePath(),
            p.getName(),
            p.getTitle(),
            p.getTags(),
            extractContent(p),
            p.currentRevision().getParagraphs());
        LOG.debug("Indexed page: "+p.getTitle());
    }


    private String extractContent(final PageEntity page) {
        final StringBuilder sb = new StringBuilder(page.getTitle());
        for (final Paragraph p : page.currentRevision().getParagraphs()) {
            if (ParagraphType.TEXT == p.getType() && p.getText() != null) {
                sb.append(" ");
                sb.append(
                    new PluginFactory().html().cleanUpContent(p.getText()));
            }
        }
        return sb.toString();
    }


    private boolean canIndex(final ResourceEntity r) {
        final boolean visible = r.isVisible();
        final boolean secure  = r.isSecure();
        final boolean indexable = r.isIndexable();
        final boolean canIndex = visible && !secure && indexable;

        LOG.debug(
            "canIndex="+canIndex
            +" {secure="+secure
            +", visible="+visible
            +", indexable="+indexable+"} "
            +r.getAbsolutePath());
        return canIndex;
    }


    private Indexer createIndexer() {
        return new PluginFactory().createIndexer(getIndexPath());
    }


    private String getIndexPath() {
        final Setting indexPath  =
            _settings.find(Setting.Name.LUCENE_INDEX_PATH);
        if (indexPath == null) {
            throw new RuntimeException(
                "No setting for "+Setting.Name.LUCENE_INDEX_PATH);
        }
        final String indexPathValue = indexPath.getValue();
        return indexPathValue;
    }


    private Index createIndex() {
        return new PluginFactory().createIndex(getIndexPath());
    }


    /**
     * Re-index a specific resource.
     *
     * @param resource The resource to re-index.
     */
    public void index(final ResourceEntity resource) {
        final Indexer lucene = createIndexer();

        try {
            lucene.startAddition();

            if (resource instanceof PageEntity) {
                final PageEntity page = (PageEntity) resource;
                indexPage(lucene, page);
                LOG.info("Indexed "+resource.getAbsolutePath());

            } else if (resource instanceof FileEntity) {
                final FileEntity file = (FileEntity) resource;
                indexFile(lucene, file);
                LOG.info("Indexed "+resource.getAbsolutePath());

            } else {
                LOG.debug(
                    "Ignored request to index resource type "
                    +resource.getType());
            }

            lucene.commitUpdate();
        } catch (final RuntimeException e) {
            LOG.error("Error indexing resource.", e);
            lucene.rollbackUpdate();
        }
    }
}
