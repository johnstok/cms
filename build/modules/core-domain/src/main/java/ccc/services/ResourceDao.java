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
import java.util.Map;
import java.util.UUID;

import ccc.domain.Resource;
import ccc.domain.ResourcePath;
import ccc.domain.Revision;
import ccc.domain.User;


/**
 * DAO API for the {@link Resource} class.
 *
 * @author Civic Computing Ltd.
 */
public interface ResourceDao {

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
     * List the resources locked by a specific user.
     *
     * @param actor The user to search with.
     * @return The list of resources.
     */
    List<Resource> lockedByUser(final User actor);

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
     * @return The revisions for the resource.
     */
    Map<Integer, ? extends Revision> history(UUID resourceId);

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
     * Look up a resource.
     *
     * @param contentPath ResourcePath The path to the resource.
     * @param rootName The name of the root folder in which the resource exists.
     * @return Resource The resource at the specified path, or NULL if it
     *  doesn't exist.
     */
    Resource lookup(String rootName, ResourcePath contentPath);

    /**
     * Look up a resource, given its CCC6 id.
     *
     * @param legacyId The CCC6 id.
     * @return The corresponding resource in CCC7.
     */
    Resource lookupWithLegacyId(String legacyId);
}
