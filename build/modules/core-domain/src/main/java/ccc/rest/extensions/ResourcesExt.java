/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import ccc.action.ActionExecutor;
import ccc.rest.Resources;
import ccc.rest.RestException;
import ccc.rest.UnauthorizedException;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.snapshots.ResourceSnapshot;
import ccc.types.Duration;


/**
 * Command API, used to update data in CCC.
 *
 * @author Civic Computing Ltd.
 */
public interface ResourcesExt
    extends
        Resources,
        ActionExecutor {


    /**
     * Look up the resource for a specified path.
     *
     * @param path The absolute path.
     *
     * @throws RestException If the method fails
     * @throws UnauthorizedException If the resource is not accessible to the
     *  current user.
     *
     * @return A summary of the corresponding resource.
     */
    ResourceSnapshot resourceForPathSecure(String path)
    throws RestException, UnauthorizedException;


    /**
     * Look up the resource for a specified path.
     *
     * @param path The absolute path.
     * @param version The version number of the resource to retrieve.
     *
     * @throws RestException If the method fails
     * @throws UnauthorizedException If the resource is not accessible to the
     *  current user.
     *
     * @return A summary of the corresponding resource.
     */
    ResourceSnapshot revisionForPath(final String path, final int version)
    throws RestException, UnauthorizedException;


    /**
     * Look up the working copy for a specified path.
     *
     * @param path The absolute path.
     *
     * @throws RestException If the method fails
     * @throws UnauthorizedException If the resource is not accessible to the
     *  current user.
     *
     * @return A summary of the corresponding resource.
     */
    ResourceSnapshot workingCopyForPath(final String path)
    throws RestException, UnauthorizedException;


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
     * Update the specified resource's template on the server.
     *
     * @param resourceId The id of the resource to update.
     * @param templateId The new template to set for the resource.
     *
     * @throws RestException If the method fails.
     */
    void updateResourceTemplate(UUID resourceId, UUID templateId)
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
     * Specify whether a resource should be included in a site's main menu.
     *
     * @param resourceId The id of the resource to update.
     * @param include True if the resource should be included, false otherwise.
     *
     * @throws RestException If the method fails.
     */
    void includeInMainMenu(UUID resourceId, boolean include)
    throws RestException;

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
     * Update metadata of the resource.
     *
     * @param resourceId The id of the resource to update.
     * @param title The new title to set.
     * @param description The new description to set.
     * @param tags The new tags to set.
     * @param metadata The metadata to update.
     * @throws  RestException If the method fails.
     */
    void updateMetadata(UUID resourceId,
                        String title,
                        String description,
                        String tags,
                        Map<String, String> metadata)
    throws RestException;

    /**
     * Creates a new search.
     *
     * @param parentId The parent folder where the search should be created.
     * @param title The title of the search.
     *
     * @return A summary of the newly created search.
     *
     * @throws RestException If the method fails.
     */
    ResourceSummary createSearch(UUID parentId, String title)
    throws RestException;

    /**
     * Create a working copy for the specified resource, using the specified log
     * entry.
     *
     * @param resourceId The id of the resource.
     * @param index The index number of the log entry.
     *
     * @throws RestException If the method fails.
     */
    void createWorkingCopy(UUID resourceId, long index)
    throws RestException;

    /**
     * Change the security roles for a resource.
     *
     * @param resourceId The resource to update.
     * @param roles The new set of roles.
     * @param actorId The user id of the actor.
     * @param happenedOn When the command happened.
     *
     * @throws RestException If the method fails.
     */
    void changeRoles(UUID resourceId,
                     Collection<String> roles,
                     UUID actorId,
                     Date happenedOn)
    throws RestException;

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
     * Update the period that a resource should be cached for.
     *
     * @param resourceId The resource to update.
     * @param duration The cache duration.
     *
     * @throws RestException If the method fails.
     */
    void updateCacheDuration(UUID resourceId, Duration duration)
    throws RestException;


    /**
     * Look up the resource corresponding to a CCC6 ID.
     *
     * @param legacyId The CCC6 ID.
     *
     * @return The corresponding resource.
     *
     * @throws RestException If the method fails.
     */
    ResourceSummary lookupWithLegacyId(String legacyId) throws RestException;


    /**
     * Look up the contents of a file as a String.
     *
     * @param absolutePath The absolute path to the resource.
     * @param charset The character set for the file.
     *
     * @return The contents as a string.
     */
    String fileContentsFromPath(String absolutePath, String charset);


/* StatefulReader */
//    /**
//     * Look up a resource from its absolute path.
//     *
//     * @param absolutePath The absolute path to the resource.
//     * @return Resource The resource at the specified path, or NULL if it
//     *  doesn't exist.
//     */
//    IResource resourceFromPath(String absolutePath);
//
//
//    /**
//     * Look up a resource from its UUID.
//     *
//     * @param id The id of the resource.
//     * @return Resource The resource at the specified path, or NULL if it
//     *  doesn't exist.
//     */
//    IResource resourceFromId(String id);
//
//
//    /**
//     * Create UUID from a String.
//     *
//     * @param id The id as a string.
//     * @return The UUID.
//     */
//    UUID uuidFromString(String id);
}
