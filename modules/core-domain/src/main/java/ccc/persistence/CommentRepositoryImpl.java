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

import javax.persistence.EntityManager;

import ccc.domain.Comment;
import ccc.domain.EntityNotFoundException;
import ccc.types.DBC;


/**
 * JPA implementation of a comment repository.
 *
 * @author Civic Computing Ltd.
 */
public class CommentRepositoryImpl
    implements
        CommentRepository {

    private Repository _repo;


    /**
     * Constructor.
     *
     * @param repo The repository used to perform queries.
     */
    public CommentRepositoryImpl(final Repository repo) {
        DBC.require().notNull(repo);
        _repo = repo;
    }


    /**
     * Constructor.
     *
     * @param em The JPA entity manager for this repository.
     */
    public CommentRepositoryImpl(final EntityManager em) {
        this(new JpaRepository(em));
    }


    /** {@inheritDoc} */
    @Override public void create(final Comment comment) {
        _repo.create(comment);
    }


    /** {@inheritDoc} */
    @Override
    public void delete(final UUID commentId) throws EntityNotFoundException {
        _repo.delete(retrieve(commentId));
    }


    /** {@inheritDoc} */
    @Override
    public Comment retrieve(final UUID commentId)
    throws EntityNotFoundException {
        return _repo.find(Comment.class, commentId);
    }
}
