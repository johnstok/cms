/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */

package ccc.search;

import ccc.domain.File;
import ccc.domain.Page;


/**
 * API for rebuilding a search index.
 *
 * @author Civic Computing Ltd.
 */
public interface Indexer {

    /**
     * Start a transaction.
     *
     * @throws SearchException If starting the transaction fails.
     */
    void startUpdate() throws SearchException;

    /**
     * Commit a transaction.
     */
    void commitUpdate();

    /**
     * Roll back a transaction.
     */
    void rollbackUpdate();

    /**
     * Index a page.
     *
     * @param page The page to index.
     */
    void indexPage(final Page page);

    /**
     * Index a file.
     *
     * @param file The file to index.
     */
    void indexFile(final File file);

}
