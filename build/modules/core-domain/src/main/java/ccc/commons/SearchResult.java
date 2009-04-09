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
package ccc.commons;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class SearchResult {

    private Set<UUID> _hits = new HashSet<UUID>();
    private int _totalResults = 0;


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

}
