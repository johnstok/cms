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
import ccc.client.core.Callback;
import ccc.client.core.CallbackResponseHandler;
import ccc.client.core.Globals;
import ccc.client.core.HttpMethod;
import ccc.client.core.I18n;
import ccc.client.core.Parser;
import ccc.client.core.RemotingAction;
import ccc.client.core.Request;
import ccc.client.core.Response;


/**
 * Remote action for page updating.
 *
 * @author Civic Computing Ltd.
 */
public final class UpdateCommentAction
    extends
        RemotingAction<Comment> {

    private final Comment _comment;


    /**
     * Constructor.
     *
     * @param comment The updated comment.
     */
    public UpdateCommentAction(final Comment comment) {
        super(I18n.UI_CONSTANTS.updateComment());
        _comment = DBC.require().notNull(comment);
    }


    /** {@inheritDoc} */
    @Override
    protected Request getRequest(final Callback<Comment> callback) {
        final String path = Globals.API_URL + _comment.self();

        return new Request(
            HttpMethod.PUT,
            path,
            writeComment(_comment),
            new CallbackResponseHandler<Comment>(
                I18n.UI_CONSTANTS.updateComment(),
                callback,
                new Parser<Comment>() {

                    @Override public Comment parse(final Response response) {
                        return readComment(response);
                    }}));
    }
}
