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

import org.apache.lucene.document.Document;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public interface SimpleLucene {

    void add(final Document document);

    void remove(final String searchTerms, final String field);

    void find(final String searchTerms,
              final String field,
              final int maxHits,
              final SearchHandler sh);
}
