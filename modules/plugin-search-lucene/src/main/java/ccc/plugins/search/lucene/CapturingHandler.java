/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
 * Changes: See subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.plugins.search.lucene;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TopDocs;


/**
 * Captures the results of a Lucene search into memory.
 *
 * @author Civic Computing Ltd.
 */
public class CapturingHandler {

    private final Set<UUID> _hits = new LinkedHashSet<UUID>();
    private final int       _noOfResultsPerPage;
    private final int       _pageNo;
    private int             _searchResultCount = 0;

    /**
     * Constructor.
     *
     * @param noOfResultsPerPage The number of results on a page.
     * @param pageNo The page of results to capture.
     */
    public CapturingHandler(final int noOfResultsPerPage, final int pageNo) {
        _noOfResultsPerPage = noOfResultsPerPage;
        _pageNo = pageNo;
    }

    /**
     * Process the results of a search.
     *
     * @param searcher The lucene index searcher used for the search.
     * @param docs The results from the search.
     * @throws IOException If an error occurs accessing the index.
     */
    public void handle(final IndexSearcher searcher,
                       final TopDocs docs) throws IOException {
        final int firstResultIndex = _pageNo*_noOfResultsPerPage;
        final int lastResultIndex = (_pageNo+1)*_noOfResultsPerPage;
        _searchResultCount = docs.totalHits;

        for (int i=firstResultIndex;
        i<lastResultIndex && i<docs.scoreDocs.length;
        i++) {
            final int docId = docs.scoreDocs[i].doc;
            _hits.add(lookupResourceId(searcher, docId));
        }
    }

    /**
     * Determine the CCC resource id for a search hit.
     *
     * @param searcher The lucene index searcher.
     * @param docId The lucene document id.
     *
     * @return The CCC resource id.
     *
     * @throws IOException If an error occurs while reading the index.
     */
    protected UUID lookupResourceId(final IndexSearcher searcher,
                          final int docId) throws IOException {
        return UUID.fromString(
            searcher.doc(docId).getField("id").stringValue()
        );
    }

    /**
     * Accessor.
     *
     * @return The current hits for this capturing handler.
     */
    public Set<UUID> getHits() {
        return _hits;
    }

    /**
     * Accessor.
     *
     * @return The current number of results found by this capturing handler.
     */
    public int getTotalResultsCount() {
        return _searchResultCount;
    }
}
