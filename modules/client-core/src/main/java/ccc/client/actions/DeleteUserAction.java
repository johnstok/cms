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
 * Revision      $Rev: 2966 $
 * Modified by   $Author: keith $
 * Modified on   $Date: 2010-07-07 10:31:54 +0100 (Wed, 07 Jul 2010) $
 *
 * Changes: See subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.actions;


import ccc.api.core.User;
import ccc.api.types.CommandType;
import ccc.client.core.HttpMethod;
import ccc.client.core.InternalServices;
import ccc.client.core.RemotingAction;
import ccc.client.events.Event;

/**
 * Delete a user.
 *
 * @author Civic Computing Ltd.
 */
public class DeleteUserAction
    extends
        RemotingAction<Void> {

    private final User _user;


    /**
     * Constructor.
     *
     * @param user The user to delete.
     */
    public DeleteUserAction(final User user) {
        super(UI_CONSTANTS.delete(), HttpMethod.DELETE);
        _user = user;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return _user.self();
    }


    /** {@inheritDoc} */
    @Override
    protected void onSuccess(final Void v) {
        final Event<CommandType> event =
            new Event<CommandType>(CommandType.USER_DELETE);
        event.addProperty("user", _user);
        fireEvent(event);
    }


    /** {@inheritDoc} */
    @Override
    protected boolean beforeExecute() {
        return InternalServices.window.confirm(
            "Are sure you want to delete the selected user?");
    }
}
