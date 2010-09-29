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
package ccc.client.remoting;

import ccc.api.core.User;
import ccc.client.core.InternalServices;
import ccc.client.core.RemotingAction;
import ccc.client.core.Response;


/**
 * Retrieve details of the currently logged in user.
 *
 * @author Civic Computing Ltd.
 */
public class GetCurrentUserAction
    extends
        RemotingAction<User> {

    /**
     * Constructor.
     */
    public GetCurrentUserAction() {
        super(USER_ACTIONS.internalAction());
    }


    /** {@inheritDoc} */
    @Override
    protected void onSuccess(final User user) {
        InternalServices.GLOBALS.currentUser(user);
        new DrawMainWindowAction(user).execute();
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return InternalServices.USERS.getLink("me");
    }


    /** {@inheritDoc} */
    @Override
    protected User parse(final Response response) {
        return readUser(response);
    }
}
