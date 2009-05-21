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

import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;


/**
 * Simple API to lucene.
 *
 * @author Civic Computing Ltd.
 */
public interface SimpleLucene {

    /**
     * Add a document to the lucene index.
     *
     * @param document The document to add.
     */
    void add(final Document document);

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

    /**
     * Removes all entries from the lucene index.
     *
     * @throws ParseException If the document query fails.
     * @throws IOException If index writing fails.
     */
    void clearIndex() throws IOException, ParseException;

    /**
     * Start a lucene transaction.
     *
     * @throws IOException If opening of the lucene index fails.
     */
    void startUpdate() throws IOException;

    /**
     * Commit a lucene transaction.
     *
     * @throws IOException If committing the lucene index fails.
     */
    void commitUpdate() throws IOException;

    /**
     * Roll back a lucene transaction.
     */
    void rollbackUpdate();
}
