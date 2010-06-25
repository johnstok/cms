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

import ccc.api.core.Security;
import ccc.client.core.Globals;
import ccc.client.core.HttpMethod;
import ccc.client.core.InternalServices;
import ccc.client.core.RemotingAction;
import ccc.client.core.Response;
import ccc.client.gwt.core.GlobalsImpl;


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
        super(UI_CONSTANTS.logout(), HttpMethod.POST);
    }


    /** {@inheritDoc} */
    @Override
    protected void onNoContent(final Response response) {
        new GlobalsImpl().currentUser(null);
        InternalServices.WINDOW.disableExitConfirmation();
        InternalServices.WINDOW.redirectTo(Globals.APP_URL);
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return GlobalsImpl.getAPI().getLink(Security.CURRENT);
    }
}
