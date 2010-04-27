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
package ccc.api.core;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


/**
 * Value object for search results.
 *
 * @author Civic Computing Ltd.
 */
public class SearchResult {

    private Set<UUID> _hits = new HashSet<UUID>();
    private int _totalResults = 0;
    private String _terms = "";
    private int _pageNo;
    private int _noOfResultsPerPage;




    /**
     * Constructor.
     *
     * @param hits The search hits.
     * @param totalResults The total number of results for this search.
     * @param noOfResultsPerPage The number of results displayed on each page
     * @param terms The search terms used.
     * @param pageNo The current page of results returned.
     */
    public SearchResult(final Set<UUID> hits,
                        final int totalResults,
                        final int noOfResultsPerPage,
                        final String terms,
                        final int pageNo) {
        _hits = hits;
        _totalResults = totalResults;
        _noOfResultsPerPage = noOfResultsPerPage;
        _terms = terms;
        _pageNo = pageNo;
    }

    /**
     * Accessor.
     *
     * @return Number of total results.
     */
    public int totalResults() {
        return _totalResults;
    }

    /**
     * Mutator.
     *
     * @param totalResults The total results.
     */
    public void totalResults(final int totalResults) {
        _totalResults = totalResults;
    }


    /**
     * Accessor.
     *
     * @return Set of UUID of currently shown search results.
     */
    public Set<UUID> hits() {
        return _hits;
    }


    /**
     * Mutator.
     *
     * @param hits Set of UUID.
     */
    public void hits(final Set<UUID> hits) {
        _hits = hits;
    }


    /**
     * Accessor.
     *
     * @return Returns the terms.
     */
    public final String getTerms() {
        return _terms;
    }


    /**
     * Accessor.
     *
     * @return Returns the pageNo.
     */
    public final int getPageNo() {
        return _pageNo;
    }

    /**
     * Mutator.
     *
     * @param noOfResultsPerPage Number of results per page.
     */
    public void noOfResultsPerPage(final int noOfResultsPerPage) {
        _noOfResultsPerPage = noOfResultsPerPage;
    }

    /**
     * Accessor.
     *
     * @return noOfResultsPerPage
     */
    public int noOfResultsPerPage() {
        return _noOfResultsPerPage;
    }
}
