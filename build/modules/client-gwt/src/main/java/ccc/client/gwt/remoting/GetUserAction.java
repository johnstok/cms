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
package ccc.client.gwt.remoting;

import ccc.api.core.User;
import ccc.client.core.Globals;
import ccc.client.core.HttpMethod;
import ccc.client.core.RemotingAction;
import ccc.client.core.Request;
import ccc.client.core.Response;
import ccc.client.core.ResponseHandlerAdapter;
import ccc.client.gwt.core.GwtJson;
import ccc.plugins.s11n.json.UserSerializer;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;


/**
 * Fetch a user.
 *
 * @author Civic Computing Ltd.
 */
public abstract class GetUserAction
    extends RemotingAction {

    private final String _userPath;

    /**
     * Constructor.
     *
     * @param userPath The path to the user on the server.
     */
    public GetUserAction(final String userPath) {
        _userPath = userPath;
    }


    /** {@inheritDoc} */
    @Override
    protected Request getRequest() {
        return
            new Request(
                HttpMethod.GET,
                Globals.API_URL + _userPath,
                "",
                new ResponseHandlerAdapter(
                    USER_ACTIONS.internalAction()) {

                    /** {@inheritDoc} */
                    @Override
                    public void onOK(final Response response) {
                        final JSONObject result =
                            JSONParser.parse(response.getText()).isObject();
                        final User user =
                            new UserSerializer().read(new GwtJson(result));
                        execute(user);
                    }
                });
    }


    /**
     * Handle the result of a successful call.
     *
     * @param user The user returned.
     */
    protected abstract void execute(User user);
}