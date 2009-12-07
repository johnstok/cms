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
package ccc.contentcreator.actions;

import java.util.ArrayList;
import java.util.Collection;

import ccc.contentcreator.client.GwtJson;
import ccc.rest.dto.UserDto;

import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public abstract class ListUsersWithUsernameAction
    extends
        RemotingAction {

    private final String _username;

    /**
     * Constructor.
     *
     * @param username The username to search on.
     */
    public ListUsersWithUsernameAction(final String username) {
        super(USER_ACTIONS.viewUsers());
        _username = username;
    }

    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return
            "/users/username/"
            + encode(_username);
    }

    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final JSONArray result = JSONParser.parse(response.getText()).isArray();
        final Collection<UserDto> users = new ArrayList<UserDto>();
        for (int i=0; i<result.size(); i++) {
            users.add(new UserDto(new GwtJson(result.get(i).isObject())));
        }

        execute(users);
    }

    /**
     * Handle the result of a successful call.
     *
     * @param users The collection of users returned from the server.
     */
    protected abstract void execute(final Collection<UserDto> users);
}
