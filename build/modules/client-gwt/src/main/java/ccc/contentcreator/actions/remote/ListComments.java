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

import java.util.ArrayList;
import java.util.Collection;

import ccc.contentcreator.actions.RemotingAction;
import ccc.contentcreator.client.GwtJson;
import ccc.rest.dto.CommentDto;
import ccc.types.CommentStatus;

import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;


/**
 * Retrieve a list of comments from the server.
 *
 * @author Civic Computing Ltd.
 */
public abstract class ListComments
    extends
        RemotingAction {

    private CommentStatus _status;

    /**
     * Constructor.
     */
    public ListComments() {
        super(USER_ACTIONS.viewComments());
    }

    /**
     * Constructor.
     *
     * @param status Filter comments based on status.
     */
    public ListComments(final CommentStatus status) {
        this();
        _status = status;
    }

    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return
            "/comments"
            + ((null==_status) ? "" : "?status="+_status.name());
    }

    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final JSONArray result = JSONParser.parse(response.getText()).isArray();
        final Collection<CommentDto> users = new ArrayList<CommentDto>();
        for (int i=0; i<result.size(); i++) {
            users.add(new CommentDto(new GwtJson(result.get(i).isObject())));
        }

        execute(users);
    }

    /**
     * Handle the result of a successful call.
     *
     * @param users The collection of users returned.
     */
    protected abstract void execute(Collection<CommentDto> users);
}
