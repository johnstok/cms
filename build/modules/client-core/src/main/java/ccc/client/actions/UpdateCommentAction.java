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
package ccc.client.actions;

import ccc.api.core.Comment;
import ccc.api.types.DBC;
import ccc.client.callbacks.CommentUpdatedCallback;
import ccc.client.commands.UpdateCommentCommand;
import ccc.client.core.Action;
import ccc.client.core.I18n;


/**
 * Remote action for page updating.
 *
 * @author Civic Computing Ltd.
 */
public final class UpdateCommentAction
    implements
        Action {

    private final Comment _comment;


    /**
     * Constructor.
     *
     * @param comment The updated comment.
     */
    public UpdateCommentAction(final Comment comment) {
        _comment = DBC.require().notNull(comment);
    }


    /** {@inheritDoc} */
    @Override
    public void execute() {
        new UpdateCommentCommand().invoke(
            _comment,
            new CommentUpdatedCallback(
                I18n.UI_CONSTANTS.updateComment(), _comment));
    }
}
