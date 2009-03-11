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

import ccc.domain.Page;


/**
 * The search API.
 *
 * @author Civic Computing Ltd.
 */
public interface ISearch {

    /** NAME : String. */
    String NAME = "Search";

    /**
     * Find the entities that match the specified search terms..
     *
     * @param searchTerms The terms to match.
     * @return The id's of entities that match the terms.
     */
    Set<UUID> find(final String searchTerms);

    /**
     * Add a page to the search index.
     *
     * @param page The page to index.
     */
    void add(Page page);

    /**
     * Update a page that is already indexed.
     *
     * @param page The page to index.
     */
    void update(Page page);

}
