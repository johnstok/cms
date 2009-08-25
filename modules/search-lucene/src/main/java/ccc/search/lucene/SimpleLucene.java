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
package ccc.search.lucene;

import ccc.search.Indexer;


/**
 * Simple API to lucene.
 *
 * @author Civic Computing Ltd.
 */
public interface SimpleLucene extends Indexer {

    /**
     * Search a lucene index.
     *
     * @param searchTerms The terms to match.
     * @param field The field to check.
     * @param maxHits The maximum number of results to retrieve.
     * @param sh The handler used to process the results.
     */
    void find(final String searchTerms,
              final String field,
              final int maxHits,
              final SearchHandler sh);
}
