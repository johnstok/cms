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
import ccc.api.types.CommandType;
import ccc.api.types.DBC;
import ccc.client.core.Globals;
import ccc.client.core.HttpMethod;
import ccc.client.core.I18n;
import ccc.client.core.InternalServices;
import ccc.client.core.RemotingAction;
import ccc.client.core.Request;
import ccc.client.core.ResponseHandlerAdapter;
import ccc.client.events.Event;
import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.json.CommentSerializer;


/**
 * Remote action for page updating.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateCommentAction
    extends
        RemotingAction {

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
    protected Request getRequest() {
        return update(_comment);
    }


    /**
     * Update this comment.
     *
     * @param comment The new comment data.
     *
     * @return The HTTP request to perform the update.
     */
    public Request update(final Comment comment) {
        final String path = Globals.API_URL + comment.self();

        final Json json = InternalServices.PARSER.newJson();
        new CommentSerializer().write(json, comment);

        return new Request(
            HttpMethod.PUT,
            path,
            json.toString(),
            new CommentUpdatedCallback(
                I18n.UI_CONSTANTS.updateComment(),
                comment));
    }


    /**
     * Callback handler for updating a comment.
     *
     * @author Civic Computing Ltd.
     */
    public class CommentUpdatedCallback extends ResponseHandlerAdapter {

        private final Comment _comment;

        /**
         * Constructor.
         *
         * @param name The action name.
         * @param comment The updated comment.
         */
        public CommentUpdatedCallback(final String name,
                                      final Comment comment) {
            super(name);
            _comment = DBC.require().notNull(comment);
        }

        /** {@inheritDoc} */
        @Override
        public void onOK(final ccc.client.core.Response response) {
            final Event<CommandType> event =
                new Event<CommandType>(CommandType.COMMENT_UPDATE);
            event.addProperty("comment", _comment);
            InternalServices.REMOTING_BUS.fireEvent(event);
        }
    }
}
