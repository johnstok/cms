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
 * Changes: See subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.actions;

import ccc.api.core.User;
import ccc.client.core.Globals;
import ccc.client.core.HttpMethod;
import ccc.client.core.InternalServices;
import ccc.client.core.RemotingAction;
import ccc.client.core.Response;


/**
 * Log current user out.
 *
 * @author Civic Computing Ltd.
 */
public final class LogoutAction
    extends
        RemotingAction<Void> {


    /**
     * Constructor.
     */
    public LogoutAction() {
        super(UI_CONSTANTS.logout(), HttpMethod.POST);
    }


    /** {@inheritDoc} */
    @Override
    protected void onSuccess(final Void v) {
        InternalServices.globals.currentUser(null);
        InternalServices.window.disableExitConfirmation();
        InternalServices.window.redirectTo(Globals.APP_URL);
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return InternalServices.api.getLink(User.Links.CURRENT);
    }


    /** {@inheritDoc} */
    @Override
    protected Void parse(final Response response) { return null; }
}
