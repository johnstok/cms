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
package ccc.services;

import java.util.Set;
import java.util.UUID;


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
     * @param resultCount The number of results to return.
     * @param page The page of results to return (first page has index of 0).
     * @return The id's of entities that match the terms.
     */
    Set<UUID> find(final String searchTerms, int resultCount, int page);

    /**
     * Rebuild the search index.
     */
    void index();
}
