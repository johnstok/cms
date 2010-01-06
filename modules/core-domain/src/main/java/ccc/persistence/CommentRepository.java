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

import java.util.UUID;

import ccc.domain.Comment;
import ccc.domain.EntityNotFoundException;


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
    void create(Comment comment);

    /**
     * Delete a comment.
     *
     * @throws EntityNotFoundException If no comment has the specified ID.
     *
     * @param commentId The ID of the comment to delete.
     */
    void delete(UUID commentId) throws EntityNotFoundException;

    /**
     * Retrieve a single comment.
     *
     * @param commentId The ID of the comment to retrieve.
     *
     * @throws EntityNotFoundException If no comment has the specified ID.
     *
     * @return The corresponding comment.
     */
    Comment retrieve(UUID commentId) throws EntityNotFoundException;
}
