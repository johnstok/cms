/*-----------------------------------------------------------------------------
 * Copyright © 2011 Civic Computing Ltd.
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
 * Revision      $Rev: 3412 $
 * Modified by   $Author: keith $
 * Modified on   $Date: 2011-01-27 15:40:06 +0000 (Thu, 27 Jan 2011) $
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.api.synchronous;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import ccc.api.types.SearchResult;
import ccc.api.types.SortOrder;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public interface SearchEngine2 {


    /**
     * Find the entities that match the specified search terms.
     *
     * @param searchTerms The terms to match.
     * @param noOfResultsPerPage The number of results to return.
     * @param page The page of results to return (first page has index of 0).
     * @return The SearchResult object with set entities and total count.
     */
    @GET @Path(ccc.api.synchronous.ResourceIdentifiers.SearchEngine.FIND)
    SearchResult find(
              @QueryParam("terms") String searchTerms,
              @QueryParam("count") @DefaultValue("20") int noOfResultsPerPage,
              @QueryParam("page") @DefaultValue("0") int page);

    /**
     * Find the results that match the specified search terms.
     *
     * @param searchTerms The terms to match.
     * @param sort The field to sort on.
     * @param order The order of the sort.
     * @param resultCount The number of results to return.
     * @param page The page of results to return (first page has index of 0).
     *
     * @return The SearchResult object with set entities and total count.
     */
    @GET @Path(ccc.api.synchronous.ResourceIdentifiers.SearchEngine.FIND_SORT)
    SearchResult find(
              @QueryParam("terms") String searchTerms,
              @QueryParam("sort") String sort,
              @QueryParam("order") @DefaultValue("ASC") SortOrder order,
              @QueryParam("count") @DefaultValue("20") int resultCount,
              @QueryParam("page") @DefaultValue("0") int page);

    /**
     * Find the results that are similar to the specified page.
     *
     * @param uuid The page to match.
     * @param noOfResultsPerPage The number of results to return.
     * @param page The page of results to return (first page has index of 0).
     * @return The SearchResult object with set entities and total count.
     */
    @GET @Path(ccc.api.synchronous.ResourceIdentifiers.SearchEngine.SIMILAR)
    SearchResult similar(
              @QueryParam("uuid") final String uuid,
              @QueryParam("count") @DefaultValue("20")int noOfResultsPerPage,
              @QueryParam("page") @DefaultValue("0") int page);

    /**
     * Rebuild the search index.
     */
    @GET @Path(ccc.api.synchronous.ResourceIdentifiers.SearchEngine.INDEX)
    void index();
}
