/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
 * Revision      $Rev: 2466 $
 * Modified by   $Author: keith $
 * Modified on   $Date: 2010-02-18 11:38:59 +0000 (Thu, 18 Feb 2010) $
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.gwt.events;

import ccc.api.core.User;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;


/**
 * Event signalling an user was created.
 *
 * @author Civic Computing Ltd.
 */
public class UserCreated
    extends
        GwtEvent<UserCreated.UserCreatedHandler> {

    private final User _user;


    /**
     * Constructor.
     *
     * @param group The newly created group.
     */
    public UserCreated(final User group) { _user = group; }


    /**
     * Accessor.
     *
     * @return Returns the new group.
     */
    public User getGroup() { return _user; }


    /** {@inheritDoc} */
    @Override
    protected void dispatch(final UserCreated.UserCreatedHandler handler) {
        handler.onCreate(this);
    }


    /** {@inheritDoc} */
    @Override
    public Type<UserCreatedHandler> getAssociatedType() { return TYPE; }


    /**
     * Handler for 'user created' events.
     *
     * @author Civic Computing Ltd.
     */
    public static interface UserCreatedHandler extends EventHandler {


        /**
         * Handle a 'user created' event.
         *
         * @param event The event to handle.
         */
        void onCreate(UserCreated event);
    }


    /** TYPE : Type. */
    public static final Type<UserCreatedHandler> TYPE =
        new Type<UserCreatedHandler>();
}
