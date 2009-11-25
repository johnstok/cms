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
package ccc.rest;

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
