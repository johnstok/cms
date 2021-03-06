/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
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
package ccc.persistence;

import java.util.List;
import java.util.UUID;

import ccc.api.types.CommentStatus;
import ccc.api.types.SortOrder;
import ccc.domain.CommentEntity;
import ccc.domain.ResourceEntity;


/**
 * Repository for comments.
 *
 * @author Civic Computing Ltd.
 */
public interface CommentRepository {


    /**
     * Persist a newly created comment.
     *
     * @param comment The comment to persist.
     */
    void create(CommentEntity comment);


    /**
     * Delete a comment.
     *
     * @param commentId The ID of the comment to delete.
     */
    void delete(UUID commentId);


    /**
     * Retrieve a single comment.
     *
     * @param commentId The ID of the comment to retrieve.
     *
     * @return The corresponding comment.
     */
    CommentEntity retrieve(UUID commentId);


    /**
     * List existing comments.
     *
     * @param resource Filter comments by resource. NULL will return all.
     * @param status Filter comments based on status. NULL will return all.
     * @param sort The field to sort on.
     * @param sortOrder The order results be sorted in.
     * @param pageNo The page of results to return.
     * @param pageSize The number of results in a page.
     *
     * @return A list of comments.
     */
    List<CommentEntity> list(ResourceEntity resource,
                       CommentStatus status,
                       String sort,
                       SortOrder sortOrder,
                       int pageNo,
                       int pageSize);


    /**
     * Count existing comments.
     *
     * @param resource Filter comments by resource. NULL will return all.
     * @param status Filter comments based on status. NULL will return all.
     *
     * @return The number of comments.
     */
    long count(ResourceEntity resource, CommentStatus status);
}
