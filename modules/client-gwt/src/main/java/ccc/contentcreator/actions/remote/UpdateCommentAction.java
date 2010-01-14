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
package ccc.contentcreator.actions.remote;

import ccc.contentcreator.actions.RemotingAction;
import ccc.contentcreator.client.GwtJson;
import ccc.rest.dto.CommentDto;

import com.google.gwt.http.client.RequestBuilder;


/**
 * Remote action for page updating.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateCommentAction
    extends
        RemotingAction {

    private final CommentDto _comment;


    /**
     * Constructor.
     *
     * @param comment The updated comment.
     */
    public UpdateCommentAction(final CommentDto comment) {
        super(UI_CONSTANTS.updateComment(), RequestBuilder.POST);
        _comment = comment;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return "/comments/"+_comment.getId();
    }


    /** {@inheritDoc} */
    @Override
    protected String getBody() {
        final GwtJson json = new GwtJson();
        _comment.toJson(json);
        return json.toString();
    }
}
