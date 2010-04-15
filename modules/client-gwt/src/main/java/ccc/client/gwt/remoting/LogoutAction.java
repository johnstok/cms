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
package ccc.client.gwt.remoting;

import ccc.client.gwt.core.Globals;
import ccc.client.gwt.core.RemotingAction;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;


/**
 * Log current user out.
 *
 * @author Civic Computing Ltd.
 */
public final class LogoutAction
    extends
        RemotingAction {


    /**
     * Constructor.
     */
    public LogoutAction() {
        super(UI_CONSTANTS.logout(), RequestBuilder.POST, false);
    }


    /** {@inheritDoc} */
    @Override
    protected void onNoContent(final Response response) {
        GLOBALS.currentUser(null);
        GLOBALS.disableExitConfirmation();
        GLOBALS.redirectTo(Globals.APP_URL);
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return "/sessions/current";
    }
}