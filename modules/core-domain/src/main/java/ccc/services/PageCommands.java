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
package ccc.services;

import java.util.Date;

import ccc.api.PageDelta;
import ccc.api.ResourceSummary;
import ccc.domain.CommandFailedException;
import ccc.types.ID;


/**
 * Page Commands API, used to update page data in CCC.
 *
 * @author Civic Computing Ltd.
 */
public interface PageCommands {

    /** NAME : String. */
    String NAME = "PublicPageCommands";


    /**
     * Delete the working copy for a page.
     *
     * @param pageId The id of the page with a working copy.
     *
     * @throws CommandFailedException If the method fails.
     */
    void clearWorkingCopy(ID pageId) throws CommandFailedException;

    /**
     * Update the specified page on the server.
     *
     * @param pageId The id of the page to update.
     * @param delta The changes to apply.
     * @param comment A comment describing the changes.
     * @param isMajorEdit Is this a major change.
     *
     * @throws CommandFailedException If the method fails.
     */
    void updatePage(ID pageId,
                    PageDelta delta,
                    String comment,
                    boolean isMajorEdit) throws CommandFailedException;


    /**
     * Update the specified page on the server.
     *
     * @param pageId The id of the page to update.
     * @param delta The changes to apply.
     * @param comment A comment describing the changes.
     * @param isMajorEdit Is this a major change.
     *
     * @throws CommandFailedException If the method fails.
     */
    void updatePage(ID pageId,
                    PageDelta delta,
                    String comment,
                    boolean isMajorEdit,
                    ID actorId,
                    Date happenedOn) throws CommandFailedException;

    /**
     * Update the working copy of the specified page.
     *
     * @param pageId The id of the page to update.
     * @param delta The changes to apply.
     *
     * @throws CommandFailedException If the method fails.
     */
    void updateWorkingCopy(ID pageId, PageDelta delta)
    throws CommandFailedException;

    /**
     * Creates a new page.
     *
     * @param parentId The folder in which the page will be created.
     * @param delta The page's details.
     * @param name The page's name.
     * @param publish True if the folder should be published, false otherwise.
     * @param templateId The page's template.
     *
     * @throws CommandFailedException If the method fails.
     *
     * @return A resource summary describing the new page.
     */
    ResourceSummary createPage(ID parentId,
                               PageDelta delta,
                               String name,
                               boolean publish,
                               ID templateId,
                               String title,
                               ID actorId,
                               Date happenedOn,
                               String comment,
                               boolean majorChange) throws CommandFailedException;


    /**
     * Creates a new page.
     *
     * @param parentId The folder in which the page will be created.
     * @param delta The page's details.
     * @param name The page's name.
     * @param publish True if the folder should be published, false otherwise.
     * @param templateId The page's template.
     *
     * @throws CommandFailedException If the method fails.
     *
     * @return A resource summary describing the new page.
     */
    ResourceSummary createPage(ID parentId,
                               PageDelta delta,
                               String name,
                               final boolean publish,
                               ID templateId,
                               String title,
                               String comment,
                               boolean majorChange) throws CommandFailedException;


}
