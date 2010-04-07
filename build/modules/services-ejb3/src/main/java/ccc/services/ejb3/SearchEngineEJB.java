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

import static ccc.types.Permission.*;
import static javax.ejb.TransactionAttributeType.*;

import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBContext;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import ccc.commons.XHTML;
import ccc.domain.File;
import ccc.domain.Page;
import ccc.domain.Resource;
import ccc.domain.Setting;
import ccc.persistence.DataRepository;
import ccc.persistence.IRepositoryFactory;
import ccc.persistence.ResourceRepository;
import ccc.persistence.SettingsRepository;
import ccc.plugins.search.TextExtractor;
import ccc.rest.SearchEngine;
import ccc.rest.SearchResult;
import ccc.rest.exceptions.EntityNotFoundException;
import ccc.search.SimpleLucene;
import ccc.search.lucene.SimpleLuceneFS;
import ccc.types.Paragraph;
import ccc.types.ParagraphType;
import ccc.types.PredefinedResourceNames;


/**
 * Lucene Implementation of the {@link SearchEngine} interface.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=SearchEngine.NAME)
@TransactionAttribute(REQUIRED)
@Local(SearchEngine.class)
@RolesAllowed({})
public class SearchEngineEJB  implements SearchEngine {

    private static final int TIMEOUT_DELAY_SECS = 60*60*1000;
    private static final int INITIAL_DELAY_SECS = 1;
    private static final String TIMER_NAME = "index_scheduler";
    private static final Logger LOG =
        Logger.getLogger(SearchEngineEJB.class.getName());

    @javax.annotation.Resource private EJBContext _context;
    @PersistenceContext private EntityManager _em;

    private ResourceRepository _resources;
    private DataRepository     _dr;

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
        return createLucene().find(searchTerms, resultCount, page);
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({SEARCH_REINDEX})
    public void index() {
        final SimpleLucene lucene = createLucene();
        try {
            lucene.startUpdate();
            indexPages(lucene);
            indexFiles(lucene);
            lucene.commitUpdate();
        } catch (final Exception e) {
            LOG.error("Error indexing resources.", e);
            lucene.rollbackUpdate();
        }
    }


    /**
     * Run the scheduled action.
     *
     * @param timer The timer that called this method.
     */
    @Timeout
    public void run(@SuppressWarnings("unused") final Timer timer) {
        index();
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({SEARCH_SCHEDULE})
    public void start() {
        LOG.debug("Starting indexer.");
        _context.getTimerService().createTimer(
            INITIAL_DELAY_SECS, TIMEOUT_DELAY_SECS, TIMER_NAME);
        LOG.debug("Started indexer.");
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({SEARCH_SCHEDULE})
    @SuppressWarnings("unchecked")
    public void stop() {
        LOG.debug("Stopping indexer.");
        final Collection<Timer> c = _context.getTimerService().getTimers();
        for (final Timer t : c) {
            if (TIMER_NAME.equals(t.getInfo())) {
                t.cancel();
            }
        }
        LOG.debug("Stopped indexer.");
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({SEARCH_SCHEDULE})
    @SuppressWarnings("unchecked")
    public boolean isRunning() {
        final Collection<Timer> c = _context.getTimerService().getTimers();
        for (final Timer t : c) {
            if (TIMER_NAME.equals(t.getInfo())) {
                return true;
            }
        }
        return false;
    }


    private void indexFiles(final SimpleLucene lucene) {
        final List<File> files = _resources.files();
        for (final File f : files) {
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
                _dr.retrieve(f.getData(), extractor);
                final String content =
                    XHTML.cleanUpContent(extractor.getText());
                lucene.createDocument(f.getId(), content);
                LOG.debug("Indexed file: "+f.getTitle());
            }
        }
    }


    private void indexPages(final SimpleLucene lucene) {
        final List<Page> pages = _resources.pages();
        for (final Page p : pages) {
            if (canIndex(p)) {
                lucene.createDocument(p.getId(), extractContent(p));
                LOG.debug("Indexed page: "+p.getTitle());
            }
        }
    }


    private String extractContent(final Page page) {
        final StringBuilder sb = new StringBuilder(page.getTitle());
        for (final Paragraph p : page.currentRevision().getParagraphs()) {
            if (ParagraphType.TEXT == p.getType() && p.getText() != null) {
                sb.append(" ");
                sb.append(XHTML.cleanUpContent(p.getText()));
            }
        }
        return sb.toString();
    }


    private boolean canIndex(final Resource r) {
        return r.isVisible() && !r.isSecure() && r.isIndexable();
    }


    @PostConstruct @SuppressWarnings("unused")
    private void configureCoreData() {
        _resources =
            IRepositoryFactory.DEFAULT.create(_em).createResourceRepository();
        _dr = IRepositoryFactory.DEFAULT.create(_em).createDataRepository();
    }


    private SimpleLucene createLucene() {
        final SettingsRepository settings = new SettingsRepository(_em);
        Setting indexPath;
        try {
            indexPath = settings.find(Setting.Name.LUCENE_INDEX_PATH);
        } catch (final EntityNotFoundException e) {
            throw new RuntimeException(
                "No setting for "+Setting.Name.LUCENE_INDEX_PATH, e);
        }

        return new SimpleLuceneFS(indexPath.getValue());
    }


    /** {@inheritDoc} */
    @Override
    @PermitAll
    public SearchResult similar(final String uuid,
                                final int noOfResultsPerPage,
                                final int page) {
        return createLucene().similar(uuid, noOfResultsPerPage, page);
    }
}
