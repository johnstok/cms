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

import ccc.api.core.User;
import ccc.client.core.Callback;
import ccc.client.core.CallbackResponseHandler;
import ccc.client.core.Globals;
import ccc.client.core.HttpMethod;
import ccc.client.core.Parser;
import ccc.client.core.RemotingAction;
import ccc.client.core.Request;
import ccc.client.core.Response;


/**
 * Fetch a user.
 *
 * @author Civic Computing Ltd.
 */
public class GetUserAction
    extends
        RemotingAction<User> {

    private final String _userPath;

    /**
     * Constructor.
     *
     * @param userPath The path to the user on the server.
     */
    public GetUserAction(final String userPath) {
        super(USER_ACTIONS.internalAction());
        _userPath = userPath;
    }


    /** {@inheritDoc} */
    @Override
    protected Request getRequest(final Callback<User> callback) {
        return
            new Request(
                HttpMethod.GET,
                Globals.API_URL + _userPath,
                "",
                new CallbackResponseHandler<User>(
                    USER_ACTIONS.internalAction(),
                    callback,
                    new Parser<User>() {
                        @Override public User parse(final Response response) {
                            return readUser(response);
                        }}));
    }
}
