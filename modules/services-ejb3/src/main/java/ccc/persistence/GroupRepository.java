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
package ccc.persistence;

import java.util.Collection;
import java.util.UUID;

import ccc.api.types.SortOrder;
import ccc.domain.GroupEntity;

/**
 * API for group repositories.
 *
 * @author Civic Computing Ltd.
 */
public interface GroupRepository {


    /**
     * Look up a group from its Id.
     *
     * @param groupId The UUID for the group.
     *
     * @return The group corresponding to 'groupId'.
     */
    GroupEntity find(UUID groupId);


    /**
     * Create a new group in the repository.
     *
     * @param g The group to create.
     */
    void create(GroupEntity g);


    /**
     * List groups.
     *
     * @param name Filter groups based on name. NULL disables the filter.
     * @param sort The sort results be sorted in.
     * @param order The order results be sorted in.
     * @param pageNo The page of results to return.
     * @param pageSize The number of results in a page.
     *
     * @return A list of groups matching the search criteria.
     */
    Collection<GroupEntity> list(String name,
        String sort,
        SortOrder order,
        int pageNo,
        int pageSize);

    /**
     * Return number of group entities with given filter.
     *
     * @param name Filter groups based on name. NULL disables the filter.
     * @return The amount of the entities.
     */
    long totalCount(String name);
}
