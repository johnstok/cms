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
package ccc.api.jaxrs;

import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.jboss.resteasy.annotations.cache.NoCache;

import ccc.api.core.Comment;
import ccc.api.core.PagedCollection;
import ccc.api.synchronous.Comments;
import ccc.api.types.CommentStatus;
import ccc.api.types.DBC;
import ccc.api.types.SortOrder;


/**
 * JAX-RS implementation of the {@link Comments} interface.
 *
 * @author Civic Computing Ltd.
 */
@Path("")
@Consumes("application/json")
@Produces("application/json")
@NoCache
public class CommentsImpl
    extends
        JaxrsCollection
    implements
        Comments {

    private final Comments _delegate;


    /**
     * Constructor.
     *
     * @param comments The comments implementation delegated to.
     */
    public CommentsImpl(final Comments comments) {
        _delegate = DBC.require().notNull(comments);
    }


    /** {@inheritDoc} */
    @Override
    public Comment create(final Comment comment) {
        try {
            return _delegate.create(comment);
        } catch (final RuntimeException cfe) {
            throw convertException(cfe);
        }
    }

    /** {@inheritDoc} */
    @Override
    public Comment retrieve(final UUID commentId) {
        try {
            return _delegate.retrieve(commentId);
        } catch (final RuntimeException cfe) {
            throw convertException(cfe);
        }
    }

    /** {@inheritDoc} */
    @Override
    public Comment update(final UUID commentId, final Comment comment) {
        try {
            return _delegate.update(commentId, comment);
        } catch (final RuntimeException cfe) {
            throw convertException(cfe);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void delete(final UUID commentId) {
        try {
            _delegate.delete(commentId);
        } catch (final RuntimeException cfe) {
            throw convertException(cfe);
        }
    }

    /** {@inheritDoc} */
    @Override
    public PagedCollection<Comment> query(final UUID resourceId,
                                          final CommentStatus status,
                                          final String sort,
                                          final SortOrder sortOrder,
                                          final int pageNo,
                                          final int pageSize) {
        try {
            return _delegate.query(
                resourceId, status, sort, sortOrder, pageNo, pageSize);
        } catch (final RuntimeException cfe) {
            throw convertException(cfe);
        }
    }
}
