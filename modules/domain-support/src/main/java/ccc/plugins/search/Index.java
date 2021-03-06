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
package ccc.plugins.search;

import ccc.api.core.ACL;
import ccc.api.types.SearchResult;
import ccc.api.types.SortOrder;


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
     * @param page The page of results to return (first page has index of 1).
     *
     * @return The SearchResult object with set entities and total count.
     */
    SearchResult find(String searchTerms, int resultCount, int page);

    /**
     * Find the results that match the specified search terms.
     *
     * @param searchTerms The terms to match.
     * @param sort The field to sort on.
     * @param order The order of the sort.
     * @param resultCount The number of results to return.
     * @param page The page of results to return (first page has index of 1).
     *
     * @return The SearchResult object with set entities and total count.
     */
    SearchResult find(String searchTerms,
                      String sort,
                      SortOrder order,
                      int resultCount,
                      int page);

    /**
     * Find the results that match the specified search terms.
     *
     * @param searchTerms The terms to match.
     * @param sort The field to sort on.
     * @param order The order of the sort.
     * @param userPerms The permissions for the user running the query.
     * @param resultCount The number of results to return.
     * @param page The page of results to return (first page has index of 0).
     *
     * @return The SearchResult object with set entities and total count.
     */
    SearchResult find(String searchTerms,
                      String sort,
                      SortOrder order,
                      ACL userPerms,
                      int resultCount,
                      int page);


    /**
     * Find the results that are similar to the specified page.
     *
     * @param uuid The UUID of the page to compare.
     * @param nofOfResultsPerPage The number of results to return.
     * @param pageNo The page of results to return (first page has index of 1).
     *
     * @return The SearchResult object with set entities and total count.
     */
    SearchResult similar(String uuid, int nofOfResultsPerPage, int pageNo);


}
