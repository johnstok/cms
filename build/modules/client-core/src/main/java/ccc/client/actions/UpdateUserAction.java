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
import ccc.client.core.I18n;
import ccc.client.core.Parser;
import ccc.client.core.RemotingAction;
import ccc.client.core.Request;
import ccc.client.core.Response;


/**
 * Remote action for user updating.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateUserAction
    extends
        RemotingAction<User> {

    private final User _userDetails;


    /**
     * Constructor.
     * @param userDetails The updated user details.
     */
    public UpdateUserAction(final User userDetails) {
        super(I18n.UI_CONSTANTS.editUser());
        _userDetails = userDetails;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return Globals.API_URL + _userDetails.self();
    }


    /** {@inheritDoc} */
    @Override
    protected String getBody() {
        return writeUser(_userDetails);
    }


    /** {@inheritDoc} */
    @Override
    protected Request getRequest(final Callback<User> callback) {
        return
            new Request(
                HttpMethod.PUT,
                getPath(),
                getBody(),
                new CallbackResponseHandler<User>(
                    I18n.UI_CONSTANTS.editUser(),
                    callback,
                    new Parser<User>() {
                        @Override public User parse(final Response response) {
                            return readUser(response);
                        }}));
    }
}
