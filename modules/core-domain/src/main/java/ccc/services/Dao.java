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

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import ccc.domain.Entity;


/**
 * Core DAO API.
 *
 * @author Civic Computing Ltd.
 */
public interface Dao {

    /**
     * Find the object with the specified type and id.
     *
     * @param <T> The type of the object.
     * @param type A class instance representing the object's type.
     * @param id The id of the object.
     * @return The object with the specified id.
     */
    <T extends Entity> T find(Class<T> type, UUID id);

    /**
     * List zero or more matches for a query - duplicates may be possible.
     *
     * @param <T> The type of the resource to be searched for.
     * @param queryName The name of the query used to perform the search.
     * @param resultType The class representing the type of the resource.
     * @param params The query parameters.
     * @return A collection of objects of type T.
     */
    <T> List<T> list(String queryName, Class<T> resultType, Object... params);

    /**
     * List zero or more matches for a query - each result is guaranteed to
     * exist only once in the resulting collection.
     *
     * @param <T> The type of the resource to be searched for.
     * @param queryName The name of the query used to perform the search.
     * @param resultType The class representing the type of the resource.
     * @param params The query parameters.
     * @return A collection of objects of type T.
     */
    <T> Collection<T> uniquify(
        String queryName, Class<T> resultType, Object... params);

    /**
     * Find a single object using a query.
     *
     * @param <T> The type of the resource to be searched for.
     * @param queryName The name of the query used to perform the search.
     * @param resultType The class representing the type of the resource.
     * @param params The query parameters.
     * @return The resource that matches the query, or NULL if no match is
     *  found.
     */
    <T> T find(String queryName, Class<T> resultType, Object... params);

    /**
     * Determine whether a resource exists.
     *
     * @param <T> The type of the resource to be searched for.
     * @param queryName The name of the query used to perform the search.
     * @param resultType The class representing the type of the resource.
     * @param params The query parameters.
     * @return True if the resource exists, false otherwise.
     */
    <T> boolean exists(String queryName, Class<T> resultType, Object... params);

    /**
     * Persist the specified entity.
     *
     * @param entity The un-persisted entity.
     */
    void create(Entity entity);

}
