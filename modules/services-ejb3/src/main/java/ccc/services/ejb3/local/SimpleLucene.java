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

import java.util.Collection;

import org.apache.lucene.document.Document;


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
     * Remove document(s) from a lucene index.
     * Removes all documents matching the specified query.
     *
     * @param searchTerms The search terms to match.
     * @param field The field to query.
     */
    void remove(final String searchTerms, final String field);

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
     * Updates indexed resource's roles.
     *
     * @param id The Id of the resource.
     * @param roles The new roles.
     */
    void updateRolesField(String id,
                          Collection<String> roles);
}
