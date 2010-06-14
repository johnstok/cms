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
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.gwt.core;

import ccc.client.gwt.views.gxt.ErrorDialog;

import com.google.gwt.core.client.GWT;


/**
 * GWT implementation of the {@link ExceptionHandler} API.
 *
 * @author Civic Computing Ltd.
 */
public class GWTExceptionHandler
    implements
        ExceptionHandler {

    private final Window _window;


    /**
     * Constructor.
     *
     * @param window The main application window.
     */
    public GWTExceptionHandler(final Window window) {
        _window = window;
    }


    /** {@inheritDoc} */
    @Override
    public void unexpectedError(final Throwable e, final String action) {

        // FIXME Convert type comparison to multiple methods.
        if (e instanceof RemoteException) {
            final RemoteException re = (RemoteException) e;
            new ErrorDialog(re, action, new GlobalsImpl()).show();
        } else if (e instanceof SessionTimeoutException) {
            _window.alert(
                I18n.UI_CONSTANTS.sessionTimeOutPleaseRestart());
        } else {
            GWT.log("An unexpected error occured.", e);
            new ErrorDialog(e, action, new GlobalsImpl()).show();
        }
    }
}
