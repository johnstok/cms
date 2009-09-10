package ccc.search.lucene;

import java.io.IOException;
import java.util.HashSet;
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

    private final Set<UUID> _hits = new HashSet<UUID>();
    private final int       _resultsPerPage;
    private final int       _page;
    private int             _searchResultCount = 0;

    /**
     * Constructor.
     *
     * @param resultsPerPage The number of results on a page.
     * @param page The page of results to capture.
     */
    public CapturingHandler(final int resultsPerPage, final int page) {
        _resultsPerPage = resultsPerPage;
        _page = page;
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
        final int firstResultIndex = _page*_resultsPerPage;
        final int lastResultIndex = (_page+1)*_resultsPerPage;
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
    public int getResultCount() {
        return _searchResultCount;
    }
}
