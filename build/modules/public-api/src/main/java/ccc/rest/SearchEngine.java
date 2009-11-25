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


/**
 * The search API.
 *
 * @author Civic Computing Ltd.
 */
public interface SearchEngine {

    /** NAME : String. */
    String NAME = "Search";

    /**
     * Find the entities that match the specified search terms..
     *
     * @param searchTerms The terms to match.
     * @param noOfResultsPerPage The number of results to return.
     * @param page The page of results to return (first page has index of 0).
     * @return The SearchResult object with set entities and total count.
     */
    SearchResult find(final String searchTerms,
                      int noOfResultsPerPage,
                      int page);

    /**
     * Rebuild the search index.
     */
    void index();
}
