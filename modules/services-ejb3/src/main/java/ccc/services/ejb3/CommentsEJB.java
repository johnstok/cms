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
package ccc.services.ejb3;

import static ccc.types.Permission.*;
import static javax.ejb.TransactionAttributeType.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;

import ccc.commands.InvalidCommandException;
import ccc.domain.CccCheckedException;
import ccc.domain.Comment;
import ccc.domain.LogEntry;
import ccc.domain.Resource;
import ccc.rest.Comments;
import ccc.rest.RestException;
import ccc.rest.dto.CommentDto;
import ccc.rest.dto.DtoCollection;
import ccc.serialization.JsonImpl;
import ccc.types.CommentStatus;
import ccc.types.EmailAddress;
import ccc.types.SortOrder;


/**
 * EJB implementation of the {@link Comments} interface.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=Comments.NAME)
@TransactionAttribute(REQUIRED)
@Local(Comments.class)
@RolesAllowed({})
public class CommentsEJB
    extends
        AbstractEJB
    implements
        Comments {


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({COMMENT_CREATE})
    public CommentDto create(final CommentDto comment) throws RestException {
        try {
            final Resource r =
                getResources().find(Resource.class, comment.getResourceId());

            final Comment c =
                new Comment(r, comment.getBody(), comment.getAuthor());
            if (comment.getUrl() != null) {
                try {
                    c.setUrl(new URL(comment.getUrl()));
                } catch (final MalformedURLException e) {
                    throw new InvalidCommandException();
                }
            }
            c.setEmail(new EmailAddress(comment.getEmail()));

            getComments().create(c);

            final CommentDto result = c.createDto();

            getAuditLog().record(
                new LogEntry(
                    currentUser(),
                    COMMENT_CREATE,
                    new Date(),
                    c.getId(),
                    new JsonImpl(result).getDetail()));

            return result;

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({COMMENT_READ})
    public CommentDto retrieve(final UUID commentId) throws RestException {
        try {
            return getComments().retrieve(commentId).createDto();
        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({COMMENT_UPDATE})
    public void update(final UUID commentId,
                       final CommentDto comment) throws RestException {
        try {
            final Comment c = getComments().retrieve(commentId);

            c.setBody(comment.getBody());
            c.setStatus(comment.getStatus());
            c.setAuthor(comment.getAuthor());
            if (comment.getUrl() == null || comment.getUrl().trim().isEmpty()) {
                c.setUrl(null);
            } else {
                try {
                    c.setUrl(new URL(comment.getUrl()));
                } catch (final MalformedURLException e) {
                    throw new InvalidCommandException();
                }
            }

            c.setEmail(
                (null==comment.getEmail())
                     ? null
                     : new EmailAddress(comment.getEmail()));

            getAuditLog().record(
                new LogEntry(
                    currentUser(),
                    COMMENT_UPDATE,
                    new Date(),
                    commentId,
                    new JsonImpl(c.createDto()).getDetail()));

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({COMMENT_DELETE})
    public void delete(final UUID commentId) throws RestException {
        try {
            getComments().delete(commentId);

            getAuditLog().record(
                new LogEntry(
                    currentUser(),
                    COMMENT_DELETE,
                    new Date(),
                    commentId,
                    new JsonImpl().getDetail()));

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({COMMENT_READ})
    public DtoCollection<CommentDto> list(final UUID resourceId,
                                          final CommentStatus status,
                                          final String sort,
                                          final SortOrder sortOrder,
                                          final int pageNo,
                                          final int pageSize)
    throws RestException {
        try {
            final Resource r =
                (null==resourceId)
                    ? null
                    : getResources().find(Resource.class, resourceId);
            return
                new DtoCollection<CommentDto>(
                    getComments().count(r, status),
                    Comment.map(
                        getComments().list(
                            r, status, sort, sortOrder, pageNo, pageSize)));

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }
}
