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

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import ccc.domain.Page;
import ccc.domain.Paragraph;
import ccc.domain.Snapshot;
import ccc.domain.User;


/**
 * DAO API for the {@link Page} class.
 *
 * @author Civic Computing Ltd.
 */
public interface PageDao {

    /** NAME : String. */
    String NAME = "PageDao";

    /**
     * Update the live version of the page. Discards working copy.
     *
     * @param id The identifier for the page.
     * @param newTitle The new title for the page.
     * @param newParagraphs The new paragraphs for the page. All existing
     *      paragraphs will be removed and replaced with the paragraphs
     *      specified here.
     * @param comment The comment for the page edit.
     * @param isMajorEdit A boolean for major edit.
     */
    void update(UUID id,
                String newTitle,
                Set<Paragraph> newParagraphs,
                final String comment,
                final boolean isMajorEdit);

    /**
     * Updates the working copy.
     *
     * @param id The identifier for the page.
     * @param workingCopy The snapshot to use as a working copy.
     */
    void updateWorkingCopy(UUID id, Snapshot workingCopy);

    /**
     * Validate fields.
     *
     * @param delta Paragraphs of the page.
     * @param t Template definition.
     * @return List of error strings.
     */
    List<String> validateFields(final Set<Paragraph> delta, final String t);

    /**
     * Create a page.
     *
     * @param id The identifier for the page.
     * @param page Page create.
     */
    void create(UUID id, Page page);

    /**
     * Delete a page's working copy.
     *
     * @param id The page's id.
     */
    void clearWorkingCopy(UUID id);

    /**
     * Applies the current working copy to update a page.
     *
     * @param id The page's id.
     * @param comment The comment for the page edit.
     * @param isMajorEdit A boolean for major edit.
     * @param actor The actor that performed the update.
     * @param happenedOn The date the update took place.
     */
    void applyWorkingCopy(UUID id,
                          String comment,
                          boolean isMajorEdit,
                          User actor,
                          Date happenedOn);
}
