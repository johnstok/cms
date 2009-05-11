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

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import ccc.domain.Folder;
import ccc.domain.LogEntry;
import ccc.domain.Resource;
import ccc.domain.ResourcePath;
import ccc.domain.User;
import ccc.services.api.Duration;


/**
 * DAO API for the {@link Resource} class.
 *
 * @author Civic Computing Ltd.
 */
public interface ResourceDao {

    /** NAME : String. */
    String NAME = "ResourceDao";


    /*
     * QUERIES
     */

    /**
     * Find 0 or more objects using a query.
     *
     * @param <T> The type of the results.
     * @param queryName The name of the query to run.
     * @param resultType A class representing the type of the results.
     * @param params The query parameters.
     * @return A collection of results.
     */
    <T> List<T> list(String queryName, Class<T> resultType, Object... params);

    /**
     * Find a single object using a query.
     *
     * @param <T> The type of the result.
     * @param queryName The name of the query to run.
     * @param resultType A class representing the type of the result.
     * @param params The query parameters.
     * @return The result.
     */
    <T> T find(String queryName, Class<T> resultType, Object... params);

    /**
     * List the resources locked by the currently logged in user.
     *
     * @return The list of resources.
     */
    List<Resource> lockedByCurrentUser(final User actor);

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
    List<LogEntry> history(UUID resourceId);

    /**
     * Find a resource using its unique id.
     *
     * @param <T> The type of the resource to return.
     * @param type A class representing the type of the resource to return.
     * @param id The id of the resource to find.
     * @return The resource for the specified id.
     */
    <T extends Resource> T find(final Class<T> type, final UUID id);

    /**
     * Find the resource with the specified UUID. This method confirms that the
     * resource is locked by the currently 'logged in' user.
     *
     * @param <T> The type of the resource to return.
     * @param type A class representing the type of the resource to return.
     * @param id The UUID of the resource.
     * @return The resource for the specified id.
     */
    <T extends Resource> T findLocked(Class<T> type,
                                      UUID id,
                                      final User lockedBy);

    /**
     * Look up a resource.
     *
     * @param contentPath ResourcePath The path to the resource.
     * @param rootName The name of the root folder in which the resource exists.
     * @return Resource The resource at the specified path, or NULL if it
     *  doesn't exist.
     */
    Resource lookup(String rootName, ResourcePath contentPath);


    /*
     * COMMANDS
     */

    /**
     * Create a new root folder. The name is checked against existing root
     * folders in order to prevent conflicts.
     *
     * @param folder The root folder to persists.
     */
    void createRoot(final User actor,
                    Folder folder);

    /**
     * Lock the specified resource.
     * The resource will be locked by the currently logged in user.
     * If the resource is already locked a CCCException will be thrown.
     *
     * @param resourceId The uuid of the resource to lock.
     * @return The current version of resource.
     */
    Resource lock(final User actor,
                  final Date happenedOn,
                  UUID resourceId);

    /**
     * Unlock the specified Resource.
     * If the logged in user does not have privileges to unlock this resource a
     * CCCException will be thrown.
     * Unlocking an unlocked resource has no effect.
     *
     * @param resourceId The resource to unlock.
     * @return The current version of resource.
     */
    Resource unlock(final User actor,
                    final Date happenedOn,
                    UUID resourceId);

    /**
     * Update the tags for a resource.
     *
     * @param resourceId The resource to update.
     * @param tags The tags to set.
     */
    void updateTags(final User actor,
                    final Date happenedOn,
                    UUID resourceId, String tags);

    /**
     * Publishes the resource.
     *
     * @param resourceId The id of the resource to update.
     * @return The current version of resource.
     */
    Resource publish(final User actor,
                     final Date happenedOn,
                     UUID resourceId);

    /**
     * Publishes the resource by specified user.
     *
     * @param resourceId The id of the resource to update.
     * @param userId The id of the publishing user.
     * @param publishedOn The date the resource was published.
     * @return The current version of resource.
     */
    Resource publish(UUID resourceId, UUID userId, Date publishedOn);

    /**
     * Un-publishes the resource.
     *
     * @param resourceId The id of the resource to update.
     * @return The current version of resource.
     */
    Resource unpublish(final User actor,
                       final Date happenedOn,
                       UUID resourceId);

    /**
     * Un-publishes the resource.
     *
     * @param resourceId The id of the resource to update.
     * @param actor The user that unpublished the resource.
     * @param happendedOn The date that the resource was unpublished.
     * @return The current version of resource.
     */
    Resource unpublish(UUID resourceId, UUID actor, Date happendedOn);

    /**
     * Change the template for the specified resource.
     *
     * @param resourceId The id of the resource to change.
     * @param templateId The id of template to set (NULL is allowed).
     */
    void updateTemplateForResource(final User actor,
                                   final Date happenedOn,
                                   UUID resourceId,
                                   UUID templateId);

    /**
     * Move a resource to a new parent.
     *
     * @param resourceId The id of the resource to move.
     * @param newParentId The id of the new parent.
     */
    void move(final User actor,
              final Date happenedOn,
              UUID resourceId,
              UUID newParentId);

    /**
     * Rename a resource.
     *
     * @param resourceId The id of the resource to change.
     * @param name The new name to set.
     */
    void rename(final User actor,
                final Date happenedOn,
                UUID resourceId,
                String name);

    /**
     * Specify whether this resource should be included in the main menu.
     *
     * @param id The id of the resource to change.
     * @param b True if the resource should be included; false otherwise.
     */
    void includeInMainMenu(final User actor,
                           final Date happenedOn,
                           final UUID id,
                           final boolean b);

    /**
     * Update metadata of the resource.
     *
     * @param resourceId The resource to update.
     * @param metadata The new metadata to set.
     */
    void updateMetadata(final User actor,
                        final Date happenedOn,
                        UUID resourceId,
                        Map<String, String> metadata);

    /**
     * Update the security roles for the specified resource.
     *
     * @param resourceId The resource to update.
     * @param roles The new roles.
     */
    void changeRoles(final User actor,
                     final Date happenedOn,
                     UUID resourceId,
                     Collection<String> roles);

    /**
     * Look up a resource, given its CCC6 id.
     *
     * @param legacyId The CCC6 id.
     * @return The corresponding resource in CCC7.
     */
    Resource lookupWithLegacyId(String legacyId);

    /**
     * TODO: Add a description of this method.
     *
     * @param actor
     * @param happenedOn
     * @param resourceId
     * @param duration
     */
    void updateCache(User actor,
                     Date happenedOn,
                     UUID resourceId,
                     Duration duration);

}
