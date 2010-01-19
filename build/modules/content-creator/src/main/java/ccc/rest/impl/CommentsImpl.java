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
package ccc.rest.impl;

import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import ccc.rest.Comments;
import ccc.rest.RestException;
import ccc.rest.dto.CommentDto;
import ccc.rest.dto.DtoCollection;
import ccc.types.CommentStatus;
import ccc.types.SortOrder;


/**
 * JAX-RS implementation of the {@link Comments} interface.
 *
 * @author Civic Computing Ltd.
 */
@Path("/secure")
@Consumes("application/json")
@Produces("application/json")
public class CommentsImpl
    extends
        JaxrsCollection
    implements
        Comments {

    /** {@inheritDoc} */
    @Override
    public CommentDto create(final CommentDto comment)
    throws RestException {
        return getComments().create(comment);
    }

    /** {@inheritDoc} */
    @Override
    public CommentDto retrieve(final UUID commentId) throws RestException {
        return getComments().retrieve(commentId);
    }

    /** {@inheritDoc} */
    @Override
    public void update(final UUID commentId,
                       final CommentDto comment) throws RestException {
        getComments().update(commentId, comment);
    }

    /** {@inheritDoc} */
    @Override
    public void delete(final UUID commentId) throws RestException {
        getComments().delete(commentId);
    }

    /** {@inheritDoc} */
    @Override
    public DtoCollection<CommentDto> list(final UUID resourceId,
                                          final CommentStatus status,
                                          final String sort,
                                          final SortOrder sortOrder,
                                          final int pageNo,
                                          final int pageSize)
    throws RestException {
        return getComments().list(
            resourceId, status, sort, sortOrder, pageNo, pageSize);
    }
}
