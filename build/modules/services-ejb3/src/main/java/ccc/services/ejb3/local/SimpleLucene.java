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
package ccc.services.ejb3.local;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;


/**
 * TODO: Add Description for this type.
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
     * TODO: Add a description of this method.
     * @throws ParseException
     * @throws IOException
     * @throws CorruptIndexException
     *
     */
    void clearIndex() throws CorruptIndexException, IOException, ParseException;

    /**
     * TODO: Add a description of this method.
     * @throws IOException
     *
     */
    void startUpdate() throws IOException;

    /**
     * TODO: Add a description of this method.
     * @throws IOException
     * @throws CorruptIndexException
     *
     */
    void commitUpdate() throws CorruptIndexException, IOException;

    /**
     * TODO: Add a description of this method.
     *
     */
    void rollbackUpdate();
}
