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
     * @return The id's of entities that match the terms.
     */
    Set<UUID> find(final String searchTerms);

    void index();

    /**
     * Start the indexer running.
     */
    void start();

    /**
     * Stop the indexer running.
     */
    void stop();

    /**
     * Query whether the indexer is running.
     *
     * @return True if the indexer is running; false otherwise.
     */
    boolean isRunning();

}
