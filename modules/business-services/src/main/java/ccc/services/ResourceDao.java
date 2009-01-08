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

import ccc.domain.Folder;
import ccc.domain.LogEntry;
import ccc.domain.Resource;
import ccc.domain.Template;


/**
 * DAO API for the {@link Resource} class.
 *
 * @author Civic Computing Ltd.
 */
public interface ResourceDao {

    <T> List<T> list(String queryName, Class<T> resultType, Object... params);

    <T> T find(String queryName, Class<T> resultType, Object... params);

    void create(final UUID folderId, final Resource newResource);

    /**
     * Create a new root folder. The name is checked against existing root
     * folders in order to prevent conflicts.
     *
     * @param folder The root folder to persists.
     */
    public void createRoot(Folder folder);

    /**
     * Lock the specified resource.
     * The resource will be locked by the currently logged in user.
     * If the resource is already locked a CCCException will be thrown.
     *
     * @param resourceId The uuid of the resource to lock.
     * @return The current version of resource.
     */
    Resource lock(UUID resourceId, long version);

    /**
     * Unlock the specified Resource.
     * If the logged in user does not have privileges to unlock this resource a
     * CCCException will be thrown.
     * Unlocking an unlocked resource has no effect.
     *
     * @param resourceId The resource to unlock.
     * @return The current version of resource.
     */
    Resource unlock(UUID resourceId, long version);

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
     * @param version The version of the resource.
     * @param tags The tags to set.
     */
    void updateTags(
                UUID resourceId,
                long version,
                String tags);

    /**
     * Publishes the resource.
     *
     * @param resourceId The id of the resource to update.
     * @param version The version of the resource.
     * @return The current version of resource.
     */
    Resource publish(UUID resourceId,
                     long version);

    /**
     * Un-publishes the resource.
     *
     * @param resourceId The id of the resource to update.
     * @param version The version of the resource.
     * @return The current version of resource.
     */
    Resource unpublish(UUID resourceId,
                       long version);

    /**
     * Change the template for the specified resource.
     *
     * @param resourceId The id of the resource to change.
     * @param version The version of the resource.
     * @param template The new template to set.
     */
    void updateTemplateForResource(UUID resourceId,
                                   long version,
                                   Template template);

    /**
     * Move a resource to a new parent.
     *
     * @param resourceId The id of the resource to move.
     * @param version The version of the resource.
     * @param newParentId The id of the new parent.
     */
    void move(UUID resourceId, long version, UUID newParentId);

    /**
     * Rename a resource.
     *
     * @param resourceId The id of the resource to change.
     * @param version The version of the resource.
     * @param name The new name to set.
     */
    void rename(UUID resourceId, long version, String name);

    /**
     * Find a resource using its unique id.
     *
     * @param <T> The of the resource to return.
     * @param type A class representing the type of the resource to return.
     * @param id The id of the resource to find.
     * @return The resource for the specified id.
     */
    <T extends Resource> T find(final Class<T> type, final UUID id);

    /**
     * TODO: Add a description of this method.
     *
     * @param type
     * @param id
     * @return
     */
    <T extends Resource> T findLocked(Class<T> type, UUID id);

    /**
     * TODO: Add a description of this method.
     *
     * @param resource
     */
    void update(Resource resource);
}
