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

package ccc.persistence;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import ccc.domain.Entity;
import ccc.domain.EntityNotFoundException;


/**
 * Core DAO API.
 *
 * @author Civic Computing Ltd.
 */
interface Repository {

    /**
     * Find the object with the specified type and id.
     *
     * @param <T> The type of the object.
     * @param type A class instance representing the object's type.
     * @param id The id of the object.
     *
     * @throws EntityNotFoundException If no entity exists with the specified
     *  uuid.
     * @return The object with the specified id.
     */
    <T extends Entity> T find(Class<T> type, UUID id)
    throws EntityNotFoundException;

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
     *
     * @throws EntityNotFoundException If no entity is found for the specified
     *  query.
     *
     * @return The resource that matches the query, or NULL if no match is
     *  found.
     */
    <T> T find(String queryName, Class<T> resultType, Object... params)
    throws EntityNotFoundException;

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

    /**
     * Delete the specified entity.
     *
     * @param entity The entity to delete.
     */
    void delete(Entity entity);

}
