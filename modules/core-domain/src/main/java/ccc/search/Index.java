/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.search;

import ccc.rest.SearchResult;


/**
 * API for querying a search index.
 *
 * @author Civic Computing Ltd.
 */
public interface Index {

    /**
     * Find the results that match the specified search terms.
     *
     * @param searchTerms The terms to match.
     * @param resultCount The number of results to return.
     * @param page The page of results to return (first page has index of 0).
     * @return The SearchResult object with set entities and total count.
     */
    SearchResult find(final String searchTerms, int resultCount, int page);
}
