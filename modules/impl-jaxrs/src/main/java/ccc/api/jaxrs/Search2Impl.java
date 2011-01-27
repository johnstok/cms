/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
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
package ccc.api.jaxrs;

import ccc.api.core.Scheduler;
import ccc.api.core.SearchEngine;
import ccc.api.core.SearchEngine2;
import ccc.api.types.DBC;
import ccc.api.types.SearchResult;
import ccc.api.types.SortOrder;


/**
 * Converts an {@link SearchEngine2} object to the {@link SearchEngine} API.
 *
 * @author Civic Computing Ltd.
 */
public class Search2Impl
    implements
        SearchEngine {

    private final SearchEngine2 _search;
    private final Scheduler _scheduler;


    /**
     * Constructor.
     *
     * @param search The search implementation delegated too.
     * @param scheduler The scheduler that will call the search API.
     */
    public Search2Impl(final SearchEngine2 search, final Scheduler scheduler) {
        _search = DBC.require().notNull(search);
        _scheduler = DBC.require().notNull(scheduler);
    }



    /** {@inheritDoc} */
    @Override
    public boolean isRunning() {
        return _scheduler.isRunning();
    }


    /** {@inheritDoc} */
    @Override
    public void start() {
        _scheduler.start();
    }


    /** {@inheritDoc} */
    @Override
    public void stop() {
        _scheduler.stop();
    }


    /** {@inheritDoc} */
    @Override
    public SearchResult find(final String searchTerms,
                             final int noOfResultsPerPage,
                             final int page) {
        return _search.find(searchTerms, noOfResultsPerPage, page);
    }


    /** {@inheritDoc} */
    @Override
    public void index() {
        _search.index();
    }


    /** {@inheritDoc} */
    @Override
    public SearchResult similar(final String uuid,
                                final int noOfResultsPerPage,
                                final int page) {
        return _search.similar(uuid, noOfResultsPerPage, page);
    }


    /** {@inheritDoc} */
    @Override
    public SearchResult find(final String searchTerms,
                             final String sort,
                             final SortOrder order,
                             final int resultCount,
                             final int page) {
        return _search.find(searchTerms, sort, order, resultCount, page);
    }
}
