/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
package ccc.services;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import ccc.rest.dto.AclDto;
import ccc.rest.dto.PageDelta;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.exceptions.RestException;


/**
 * API for CC6 migration.
 *
 * @author Civic Computing Ltd.
 */
public interface Migration {

    /** NAME : String. */
    String NAME = "Migration";

    /**
     * Create a folder with the specified name and title.
     *
     * @param parentId The folder in which the new folder should be created.
     * @param name The name of the new folder.
     * @param title The title of the folder.
     * @param publish True if the title should be published, false otherwise.
     * @param actorId The user id of the actor.
     * @param happenedOn When the command happened.
     *
     * @throws RestException If the method fails.
     *
     * @return A resource summary describing the new folder.
     */
    ResourceSummary createFolder(UUID parentId,
                                 String name,
                                 String title,
                                 boolean publish,
                                 UUID actorId,
                                 Date happenedOn)
    throws RestException;


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


    /**
     * Apply a resource's working copy.
     *
     * @param resourceId The id of the resource.
     * @param userId The user applying the working copy.
     * @param happenedOn When the command happened.
     * @param isMajorEdit Was this a major change.
     * @param comment A comment describing the change.
     *
     * @throws RestException If the method fails.
     */
    void applyWorkingCopy(UUID resourceId,
                          UUID userId,
                          Date happenedOn,
                          boolean isMajorEdit,
                          String comment) throws RestException;


    /**
     * Change the security roles for a resource.
     *
     * @param resourceId The resource to update.
     * @param acl The access control list for this resource.
     * @param actorId The user id of the actor.
     * @param happenedOn When the command happened.
     *
     * @throws RestException If the method fails.
     */
    void changeRoles(UUID resourceId,
                     AclDto acl,
                     UUID actorId,
                     Date happenedOn)
    throws RestException;



    /**
     * Delete a resource.
     *
     * @param resourceId The id of the existing resource.
     * @param actorId The user id of the actor.
     * @param happenedOn When the command happened.
     *
     * @throws RestException If the method fails.
     */
    void deleteResource(UUID resourceId,
                UUID actorId,
                Date happenedOn) throws RestException;


    /**
     * Specify whether a resource should be included in a site's main menu.
     *
     * @param resourceId The id of the resource to update.
     * @param include True if the resource should be included, false otherwise.
     * @param actorId The user id of the actor.
     * @param happenedOn When the command happened.
     *
     * @throws RestException If the method fails.
     */
    void includeInMainMenu(UUID resourceId,
                           boolean include,
                           UUID actorId,
                           Date happenedOn)
    throws RestException;


    /**
     * Lock the specified resource.
     * The resource will be locked by the currently logged in user.
     * If the resource is already locked a CCCException will be thrown.
     *
     * @param resourceId The uuid of the resource to lock.
     * @param actorId The user id of the actor.
     * @param happenedOn When the command happened.
     *
     * @throws RestException If the method fails.
     */
    void lock(UUID resourceId, UUID actorId, Date happenedOn)
    throws RestException;


    /**
     * Publish the specified resource.
     *
     * @param resourceId The id of the resource to update.
     * @param userId The id of the publishing user.
     * @param publishDate The date the resource was published.
     *
     * @throws RestException If the method fails.
     */
    void publish(UUID resourceId, UUID userId, Date publishDate)
    throws RestException;



    /**
     * Unpublish the specified resource.
     *
     * @param resourceId The id of the resource to update.
     * @param userId The id of the un-publishing user.
     * @param publishDate The date the resource was un-published.
     *
     * @throws RestException If the method fails.
     */
    void unpublish(UUID resourceId, UUID userId, Date publishDate)
                                                  throws RestException;



    /**
     * Update metadata of the resource.
     *
     * @param resourceId The id of the resource to update.
     * @param title The new title to set.
     * @param description The new description to set.
     * @param tags The new tags to set.
     * @param metadata The metadata to update.
     * @param actorId The user id of the actor.
     * @param happenedOn When the command happened.
     *
     * @throws RestException If the method fails.
     */
    void updateMetadata(UUID resourceId,
                        String title,
                        String description,
                        String tags,
                        Map<String, String> metadata,
                        UUID actorId,
                        Date happenedOn)
    throws RestException;


    /**
     * Update the specified resource's template on the server.
     *
     * @param resourceId The id of the resource to update.
     * @param templateId The new template to set for the resource.
     * @param actorId The user id of the actor.
     * @param happenedOn When the command happened.
     *
     * @throws RestException If the method fails.
     */
    void updateResourceTemplate(UUID resourceId,
                                UUID templateId,
                                UUID actorId,
                                Date happenedOn)
    throws RestException;



    /**
     * Unlock the specified Resource.
     * If the logged in user does not have privileges to unlock this resource a
     * CCCException will be thrown.
     * Unlocking an unlocked resource has no effect.
     *
     * @param resourceId The resource to unlock.
     * @param actorId The user id of the actor.
     * @param happenedOn When the command happened.
     *
     * @throws RestException If the method fails.
     */
    void unlock(UUID resourceId, UUID actorId, Date happenedOn)
    throws RestException;


}
