/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.services.ejb3;

import static ccc.types.CreatorRoles.*;
import static javax.ejb.TransactionAttributeType.*;

import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBContext;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import ccc.domain.File;
import ccc.domain.Page;
import ccc.domain.Scheduler;
import ccc.domain.Setting;
import ccc.persistence.FileRepositoryImpl;
import ccc.persistence.QueryNames;
import ccc.persistence.Repository;
import ccc.persistence.ResourceRepository;
import ccc.persistence.ResourceRepositoryImpl;
import ccc.persistence.SettingsRepository;
import ccc.persistence.jpa.FsCoreData;
import ccc.persistence.jpa.JpaRepository;
import ccc.search.SearchEngine;
import ccc.search.SearchResult;
import ccc.search.lucene.SimpleLucene;
import ccc.search.lucene.SimpleLuceneFS;


/**
 * Lucene Implementation of the {@link SearchEngine} interface.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=SearchEngine.NAME)
@TransactionAttribute(REQUIRED)
@Remote(Scheduler.class)
@Local(SearchEngine.class)
@RolesAllowed({ADMINISTRATOR})
public class SearchEngineEJB  implements SearchEngine, Scheduler {

    private static final int TIMEOUT_DELAY_SECS = 60*60*1000;
    private static final int INITIAL_DELAY_SECS = 1;
    private static final String TIMER_NAME = "index_scheduler";
    private static final Logger LOG =
        Logger.getLogger(SearchEngineEJB.class.getName());

    @javax.annotation.Resource private EJBContext _context;
    @PersistenceContext private EntityManager _em;

    private ResourceRepository _resources;
    private Repository _repo;

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
    public void start() {
        LOG.debug("Starting indexer.");
        _context.getTimerService().createTimer(
            INITIAL_DELAY_SECS, TIMEOUT_DELAY_SECS, TIMER_NAME);
        LOG.debug("Started indexer.");
    }


    /** {@inheritDoc} */
    @Override
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
        final List<File> files =
            _resources.list(QueryNames.ALL_FILES, File.class);
        for (final File f : files) {
            if (f.isVisible() && f.roles().isEmpty()) {
                lucene.indexFile(f);
            }
        }
    }


    private void indexPages(final SimpleLucene lucene) {
        final List<Page> pages =
            _resources.list(QueryNames.ALL_PAGES, Page.class);
        for (final Page p : pages) {
            if (p.isVisible() && p.roles().isEmpty()) {
                lucene.indexPage(p);
            }
        }
    }


    @PostConstruct @SuppressWarnings("unused")
    private void configureCoreData() {
        _repo = new JpaRepository(_em);
        _resources = new ResourceRepositoryImpl(_repo);
    }


    private SimpleLucene createLucene() {
        final SettingsRepository settings = new SettingsRepository(_repo);
        final Setting indexPath = settings.find(Setting.Name.LUCENE_INDEX_PATH);
        return new SimpleLuceneFS(
            new FileRepositoryImpl(new FsCoreData(), _repo), indexPath.value());
    }
}
