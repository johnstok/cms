/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
package ccc.commands;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

import ccc.domain.Comment;
import ccc.domain.LogEntry;
import ccc.domain.User;
import ccc.persistence.IRepositoryFactory;
import ccc.rest.InvalidException;
import ccc.rest.dto.CommentDto;
import ccc.serialization.JsonImpl;
import ccc.types.CommandType;
import ccc.types.EmailAddress;


/**
 * Command to update a comment.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateCommentCommand
    extends
        Command<Comment> {

    private final CommentDto _comment;
    private final UUID       _commentId;


    /**
     * Constructor.
     *
     * @param repoFactory The repository factory for this command.
     * @param commentId The ID of the comment to update.
     * @param comment Details of the comment to update.
     */
    public UpdateCommentCommand(final IRepositoryFactory repoFactory,
                                final UUID commentId,
                                final CommentDto comment) {
        super(repoFactory);
        _comment = comment;
        _commentId = commentId;
    }


    /** {@inheritDoc} */
    @Override
    protected Comment doExecute(final User actor,
                                final Date happenedOn) {

        final Comment c = getComments().retrieve(_commentId);

        c.setBody(_comment.getBody());
        c.setStatus(_comment.getStatus());
        c.setAuthor(_comment.getAuthor());
        if (_comment.getUrl() == null || _comment.getUrl().trim().isEmpty()) {
            c.setUrl(null);
        } else {
            try {
                c.setUrl(new URL(_comment.getUrl()));
            } catch (final MalformedURLException e) {
                throw new InvalidException();
            }
        }

        c.setEmail(
            (null==_comment.getEmail())
                 ? null
                 : new EmailAddress(_comment.getEmail()));

        getAudit().record(
            new LogEntry(
                actor,
                getType(),
                happenedOn,
                _commentId,
                new JsonImpl(c).getDetail()));

        return c;
    }


    /** {@inheritDoc} */
    @Override
    protected CommandType getType() { return CommandType.COMMENT_UPDATE; }
}
