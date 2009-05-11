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
import ccc.domain.User;
import ccc.services.api.PageDelta;


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
     * @param delta The changes to apply.
     * @param comment The comment for the page edit.
     * @param isMajorEdit A boolean for major edit.
     */
    void update(User actor,
                Date happenedOn,
                UUID id,
                PageDelta delta,
                String comment,
                boolean isMajorEdit);

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
    void create(final User actor, UUID id, Page page);
}
