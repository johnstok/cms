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

import java.util.List;
import java.util.UUID;

import ccc.domain.Entity;
import ccc.domain.LogEntry;
import ccc.domain.Resource;
import ccc.domain.Template;


/**
 * DAO API for the {@link Resource} class.
 *
 * @author Civic Computing Ltd.
 */
public interface ResourceDao {

    /**
     * Lock the specified resource.
     * The resource will be locked by the currently logged in user.
     * If the resource is already locked a CCCException will be thrown.
     *
     * @param resourceId The uuid of the resource to lock.
     * @return The current version of resource.
     */
    Resource lock(String resourceId);

    /**
     * Unlock the specified Resource.
     * If the logged in user does not have privileges to unlock this resource a
     * CCCException will be thrown.
     * Unlocking an unlocked resource has no effect.
     *
     * @param resourceId The resource to unlock.
     * @return The current version of resource.
     */
    Resource unlock(String resourceId);

    /**
     * List the resources locked by the currently logged in user.
     *
     * @return The list of resources.
     */
    List<Resource> lockedByCurrentUser();

    /**
     * List all locked resources.
     *
     * @return The list of resources.
     */
    List<Resource> locked();

    /**
     * Retrieve the history of a resource.
     *
     * @param resourceId The id of the resource whose history we will look up.
     * @return The list of resources.
     */
    List<LogEntry> history(String resourceId);

    /**
     * Update the tags for a resource.
     *
     * @param resourceId The resource to update.
     * @param tags The tags to set.
     */
    void updateTags(
                String resourceId,
                String tags);

    /**
     * Publishes the resource.
     *
     * @param resourceId The id of the resource to update.
     * @return The current version of resource.
     */
    Resource publish(String resourceId);

    /**
     * Un-publishes the resource.
     *
     * @param resourceId The id of the resource to update.
     * @return The current version of resource.
     */
    Resource unpublish(String resourceId);

    /**
     * Change the template for the specified resource.
     *
     * @param resourceId The id of the resource to change.
     * @param template The new template to set.
     */
    void updateTemplateForResource(UUID resourceId, Template template);

    /**
     * Move a resource to a new parent.
     *
     * @param resourceId The id of the resource to move.
     * @param newParentId The id of the new parent.
     */
    void move(UUID resourceId, UUID newParentId);

    /**
     * Rename a resource.
     *
     * @param resourceId The id of the resource to change.
     * @param name The new name to set.
     */
    void rename(UUID resourceId, String name);

    /**
     * Find a resource using its unique id.
     * TODO: Should be <T extends Resource>?
     *
     * @param <T> The of the resource to return.
     * @param type A class representing the type of the resource to return.
     * @param id The id of the resource to find.
     * @return The resource for the specified id.
     */
    <T extends Entity> T find(final Class<T> type, final UUID id);
}
