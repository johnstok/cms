/*-----------------------------------------------------------------------------
r * Copyright (c) 2009 Civic Computing Ltd.
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

import java.util.Collection;

import javax.annotation.Resource;
import javax.annotation.Resource.AuthenticationType;
import javax.ejb.EJBContext;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TransactionAttribute;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;

import org.apache.log4j.Logger;

import ccc.api.synchronous.SearchEngine;
import ccc.api.types.SearchResult;
import ccc.api.types.SortOrder;
import ccc.commands.SearchReindexCommand;
import ccc.search.SearchHelper;


/**
 * Lucene Implementation of the {@link SearchEngine} interface.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=SearchEngine.NAME)
@TransactionAttribute(REQUIRED)
@Local(SearchEngine.class)
public class SearchEngineEJB
    extends
        AbstractEJB
    implements
        SearchEngine {

    private static final int TIMEOUT_DELAY_SECS = 60*60*1000;
    private static final int INITIAL_DELAY_SECS = 1;
    private static final String TIMER_NAME = "index_scheduler";
    private static final Logger LOG =
        Logger.getLogger(SearchEngineEJB.class.getName());

    @javax.annotation.Resource private EJBContext _context;

    @Resource(
        name="topic_conn_factory",
        authenticationType=AuthenticationType.CONTAINER,
        type=TopicConnectionFactory.class)
    private TopicConnectionFactory _connectionFactory;

    @Resource(name="topic_broadcast")
    private Topic _broadcast;

    /** Constructor. */
    public SearchEngineEJB() { super(); }


    /** {@inheritDoc} */
    @Override
    public SearchResult find(final String searchTerms,
                             final int resultCount,
                             final int page) {
        return
            new SearchHelper(
                getRepoFactory().createResourceRepository(),
                getRepoFactory().createDataRepository(),
                getRepoFactory().createSettingsRepository())
            .find(searchTerms, resultCount, page);
    }


    /** {@inheritDoc} */
    @Override
    public SearchResult find(final String searchTerms,
                             final String sort,
                             final SortOrder order,
                             final int resultCount,
                             final int page) {
        return
            new SearchHelper(
                getRepoFactory().createResourceRepository(),
                getRepoFactory().createDataRepository(),
                getRepoFactory().createSettingsRepository())
            .find(searchTerms, sort, order, resultCount, page);
    }


    /** {@inheritDoc} */
    @Override
    public SearchResult similar(final String uuid,
                                final int noOfResultsPerPage,
                                final int page) {
        return
            new SearchHelper(
                getRepoFactory().createResourceRepository(),
                getRepoFactory().createDataRepository(),
                getRepoFactory().createSettingsRepository())
            .similar(uuid, noOfResultsPerPage, page);
    }


    /** {@inheritDoc} */
    @Override
    public void index() {
        execute(
            new SearchReindexCommand(
                getRepoFactory(),
                new JmsProducer(_connectionFactory, _broadcast)));
    }



    /* ====================================================================
     * Scheduler implementation.
     * ================================================================== */

    /** {@inheritDoc} */
    @Override
    public void start() {
        checkPermission(SEARCH_SCHEDULE);

        LOG.debug("Starting indexer.");

        if (isRunning()) {
            LOG.debug("Indexer already running.");
        } else {
            _context.getTimerService().createTimer(
                INITIAL_DELAY_SECS, TIMEOUT_DELAY_SECS, TIMER_NAME);
            LOG.debug("Started indexer.");
        }
    }


    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("unchecked") // JEE API.
    public void stop() {
        checkPermission(SEARCH_SCHEDULE);

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
    @SuppressWarnings("unchecked") // JEE API.
    public boolean isRunning() {
        checkPermission(SEARCH_SCHEDULE);

        final Collection<Timer> c = _context.getTimerService().getTimers();
        for (final Timer t : c) {
            if (TIMER_NAME.equals(t.getInfo())) {
                return true;
            }
        }
        return false;
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
}
