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
package ccc.services.ejb3;

import java.io.IOException;
import java.util.UUID;

import junit.framework.TestCase;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import ccc.search.lucene.CapturingHandler;




/**
 * Tests for the {@link CapturingHandler} class.
 *
 * @author Civic Computing Ltd.
 */
public class CapturingHandlerTest
    extends
        TestCase {

    /**
     * Test.
     * @throws IOException From Lucene API.
     */
    public void testHandlesPagingA() throws IOException {

        // ARRANGE
        final CapturingHandler h =
            new CapturingHandler(5, 1){
                /** {@inheritDoc} */ @Override
                protected UUID lookupResourceId(final IndexSearcher searcher,
                                                final int docId) {
                    return getResourceIds()[docId];
                }
            };

        // ACT
        h.handle(null, _td);

        // ASSERT
        assertEquals(5, h.getHits().size());
        assertTrue(h.getHits().contains(_resourceIds[5]));
        assertTrue(h.getHits().contains(_resourceIds[6]));
        assertTrue(h.getHits().contains(_resourceIds[7]));
        assertTrue(h.getHits().contains(_resourceIds[8]));
        assertTrue(h.getHits().contains(_resourceIds[9]));
    }

    /**
     * Test.
     * @throws IOException From Lucene API.
     */
    public void testHandlesPagingB() throws IOException {

        // ARRANGE
        final CapturingHandler h =
            new CapturingHandler(7, 0){
            /** {@inheritDoc} */ @Override
            protected UUID lookupResourceId(final IndexSearcher searcher,
                                            final int docId) {
                return getResourceIds()[docId];
            }
        };

        // ACT
        h.handle(null, _td);

        // ASSERT
        assertEquals(7, h.getHits().size());
        assertTrue(h.getHits().contains(_resourceIds[0]));
        assertTrue(h.getHits().contains(_resourceIds[1]));
        assertTrue(h.getHits().contains(_resourceIds[2]));
        assertTrue(h.getHits().contains(_resourceIds[3]));
        assertTrue(h.getHits().contains(_resourceIds[4]));
        assertTrue(h.getHits().contains(_resourceIds[5]));
        assertTrue(h.getHits().contains(_resourceIds[6]));
    }

    private final TopDocs _td = new TopDocs(
        15,
        new ScoreDoc[]{
            new ScoreDoc(0, 0),
            new ScoreDoc(1, 0),
            new ScoreDoc(2, 0),
            new ScoreDoc(3, 0),
            new ScoreDoc(4, 0),
            new ScoreDoc(5, 0),
            new ScoreDoc(6, 0),
            new ScoreDoc(7, 0),
            new ScoreDoc(8, 0),
            new ScoreDoc(9, 0),
            new ScoreDoc(10, 0),
            new ScoreDoc(11, 0),
            new ScoreDoc(12, 0),
            new ScoreDoc(13, 0),
            new ScoreDoc(14, 0)},
        0);
    private final UUID[] _resourceIds = new UUID[]{
        UUID.randomUUID(),
        UUID.randomUUID(),
        UUID.randomUUID(),
        UUID.randomUUID(),
        UUID.randomUUID(),
        UUID.randomUUID(),
        UUID.randomUUID(),
        UUID.randomUUID(),
        UUID.randomUUID(),
        UUID.randomUUID(),
        UUID.randomUUID(),
        UUID.randomUUID(),
        UUID.randomUUID(),
        UUID.randomUUID(),
        UUID.randomUUID()};

    /**
     * Accessor.
     *
     * @return Returns the resourceIds.
     */
    UUID[] getResourceIds() { return _resourceIds; }
}
