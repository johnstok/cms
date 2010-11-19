/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
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
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */

package ccc.plugins.search;

import java.util.Collection;

import ccc.api.core.ACL;
import ccc.api.core.File;
import ccc.api.core.Page;
import ccc.api.types.MimeType;


/**
 * API for rebuilding a search index.
 *
 * @author Civic Computing Ltd.
 */
public interface Indexer {

    /**
     * Start a transaction.
     */
    void startUpdate();


    /**
     * Commit a transaction.
     */
    void commitUpdate();


    /**
     * Roll back a transaction.
     */
    void rollbackUpdate();


    /**
     * Add a document to the search index.
     *
     * @param f   The file to add.
     * @param acl The ACL for the document.
     */
    void createDocument(File f, Collection<ACL> acl);


    /**
     * Add a document to the search index.
     *
     * @param p   The page to add.
     * @param acl The ACL for the document.
     */
    void createDocument(Page p, Collection<ACL> acl);


    /**
     * Creates a text extractor for PDF, Word and plain text files.
     *
     * @param mimeType MimeType used to match the file.
     * @return TextExtractor for the file or null if no matching found.
     */
    TextExtractor createExtractor(MimeType mimeType);
}
