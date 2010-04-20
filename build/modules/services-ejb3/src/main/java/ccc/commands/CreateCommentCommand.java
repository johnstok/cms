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

import ccc.api.dto.CommentDto;
import ccc.api.exceptions.CCException;
import ccc.api.types.CommandType;
import ccc.api.types.EmailAddress;
import ccc.domain.Comment;
import ccc.domain.LogEntry;
import ccc.domain.Resource;
import ccc.domain.User;
import ccc.persistence.IRepositoryFactory;
import ccc.plugins.s11n.json.JsonImpl;


/**
 * Command used for creating a comment.
 *
 * @author Civic Computing Ltd.
 */
public class CreateCommentCommand
    extends
        Command<Comment> {

    private final CommentDto _comment;


    /**
     * Constructor.
     *
     * @param repoFactory The repository factory for this command.
     * @param comment Details of the comment to create.
     */
    public CreateCommentCommand(final IRepositoryFactory repoFactory,
                                final CommentDto comment) {
        super(repoFactory);
        _comment = comment;
    }


    /** {@inheritDoc} */
    @Override
    protected Comment doExecute(final User actor,
                                final Date happenedOn) {

        final Resource r =
            getRepository().find(Resource.class, _comment.getResourceId());

        final Comment c =
            new Comment(r, _comment.getBody(), _comment.getAuthor());
        if (_comment.getUrl() != null) {
            try {
                c.setUrl(new URL(_comment.getUrl()));
            } catch (final MalformedURLException e) {
                // FIXME: Should be an InvalidException.
                throw new CCException("Bad URL: "+_comment.getUrl());
            }
        }
        c.setEmail(new EmailAddress(_comment.getEmail()));

        getComments().create(c);

        getAudit().record(
            new LogEntry(
                actor,
                getType(),
                happenedOn,
                c.getId(),
                new JsonImpl(c).getDetail()));

        return c;
    }


    /** {@inheritDoc} */
    @Override
    protected CommandType getType() { return CommandType.COMMENT_CREATE; }
}
