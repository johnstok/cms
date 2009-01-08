/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.services;

import java.util.Set;
import java.util.UUID;

import ccc.domain.Page;
import ccc.domain.Paragraph;


/**
 * DAO API for the {@link Page} class.
 *
 * @author Civic Computing Ltd.
 */
public interface PageDao {

    /**
     * Update a page.
     *
     * @param id The identifier for the page.
     * @param newTitle The new title for the page.
     * @param newParagraphs The new paragraphs for the page. All existing
     *      paragraphs will be removed and replaced with the paragraphs
     *      specified here.
     */
    void update(UUID id,
                String newTitle,
                Set<Paragraph> newParagraphs);

}
