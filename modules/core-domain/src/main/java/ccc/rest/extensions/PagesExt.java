/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.rest.extensions;

import java.util.Date;
import java.util.UUID;

import ccc.rest.RestException;
import ccc.rest.Pages;
import ccc.rest.dto.PageDelta;
import ccc.rest.dto.ResourceSummary;


/**
 * Page Commands API, used to update page data in CCC.
 *
 * @author Civic Computing Ltd.
 */
public interface PagesExt extends Pages {

    /**
     * Update the specified page on the server.
     *
     * @param pageId The id of the page to update.
     * @param delta The changes to apply.
     * @param comment A comment describing the changes.
     * @param isMajorEdit Is this a major change.
     * @param actorId The user id of the actor.
     * @param happenedOn When the command happened.
     *
     * @throws RestException If the method fails.
     */
    void updatePage(UUID pageId,
                    PageDelta delta,
                    String comment,
                    boolean isMajorEdit,
                    UUID actorId,
                    Date happenedOn) throws RestException;


    /**
     * Creates a new page.
     *
     * @param parentId The folder in which the page will be created.
     * @param delta The page's details.
     * @param name The page's name.
     * @param publish True if the folder should be published, false otherwise.
     * @param templateId The page's template.
     * @param title The page's title.
     * @param actorId The user id of the actor.
     * @param happenedOn When the command happened.
     * @param comment The comment of the page creation.
     * @param majorChange The boolean for major change.
     *
     * @throws RestException If the method fails.
     *
     * @return A resource summary describing the new page.
     */
    ResourceSummary createPage(UUID parentId,
                               PageDelta delta,
                               String name,
                               boolean publish,
                               UUID templateId,
                               String title,
                               UUID actorId,
                               Date happenedOn,
                               String comment,
                               boolean majorChange) throws RestException;
}