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
import ccc.api.types.CommandType;
import ccc.client.core.HttpMethod;
import ccc.client.core.InternalServices;
import ccc.client.core.RemotingAction;
import ccc.client.core.Response;
import ccc.client.events.Event;


/**
 * Create a new user.
 *
 * @author Civic Computing Ltd.
 */
public class CreateUserAction
    extends
        RemotingAction<User> {

    private final User _userDelta;


    /**
     * Constructor.
     *
     * @param userDelta The user's details.
     */
    public CreateUserAction(final User userDelta) {
        super(UI_CONSTANTS.createUser(), HttpMethod.POST);
        _userDelta = userDelta;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return InternalServices.API.users();
    }


    /** {@inheritDoc} */
    @Override
    protected String getBody() { return writeUser(_userDelta); }


    /** {@inheritDoc} */
    @Override
    protected void onSuccess(final User user) {
        final Event<CommandType> event =
            new Event<CommandType>(CommandType.USER_CREATE);
        event.addProperty("user", user);

        fireEvent(event);
    }


    /** {@inheritDoc} */
    @Override
    protected User parse(final Response response) {
        return readUser(response);
    }
}
