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

import java.util.Date;
import java.util.UUID;

import ccc.domain.CccCheckedException;
import ccc.domain.LogEntry;
import ccc.domain.User;
import ccc.persistence.IRepositoryFactory;
import ccc.serialization.JsonImpl;
import ccc.types.CommandType;


/**
 * Command used to delete a comment.
 *
 * @author Civic Computing Ltd.
 */
public class DeleteCommentCommand
    extends
        Command<Void> {

    private final UUID _commentId;


    /**
     * Constructor.
     *
     * @param repoFactory The repository factory for this command.
     * @param commentId The ID of the comment to delete.
     */
    public DeleteCommentCommand(final IRepositoryFactory repoFactory,
                                final UUID commentId) {
        super(repoFactory);
        _commentId = commentId;
    }


    /** {@inheritDoc} */
    @Override
    protected Void doExecute(final User actor,
                             final Date happenedOn) throws CccCheckedException {

        getComments().delete(_commentId);

        getAudit().record(
            new LogEntry(
                actor,
                getType(),
                happenedOn,
                _commentId,
                new JsonImpl().getDetail()));

        return null;
    }


    /** {@inheritDoc} */
    @Override
    protected CommandType getType() { return CommandType.COMMENT_DELETE; }
}
