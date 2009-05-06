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

import java.io.IOException;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TopDocs;


/**
 * Handles the results of a Lucene search.
 *
 * @author Civic Computing Ltd.
 */
public abstract class SearchHandler {

    /**
     * Process the results of a search.
     *
     * @param searcher The lucene index searcher used for the search.
     * @param docs The results from the search.
     * @throws IOException If an error occurs accessing the index.
     */
    protected abstract void handle(IndexSearcher searcher, TopDocs docs)
                                                             throws IOException;
}
