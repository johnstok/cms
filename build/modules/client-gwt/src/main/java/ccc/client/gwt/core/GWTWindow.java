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

import ccc.client.core.I18n;
import ccc.client.core.InternalServices;
import ccc.client.remoting.LogoutAction;

import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.Window.ClosingHandler;


/**
 * GWT implementation of the {@link ccc.client.core.Window} API.
 *
 * @author Civic Computing Ltd.
 */
class GWTWindow
    implements
        ccc.client.core.Window {

    private HandlerRegistration _handlerRegistration = null;


    /** {@inheritDoc} */
    @Override
    public void alert(final String string) {
        Window.alert(string);
    }


    /** {@inheritDoc} */
    @Override
    public boolean confirm(final String string) {
        return Window.confirm(string);
    }


    /** {@inheritDoc} */
    @Override
    public void refresh() {
        Window.Location.reload();
    }


    /** {@inheritDoc} */
    @Override
    public void redirectTo(final String relativeURL) {
        Window.Location.assign(InternalServices.GLOBALS.hostURL()+relativeURL);
    }


    /** {@inheritDoc} */
    @Override
    public String getParameter(final String string) {
        return Window.Location.getParameter(string);
    }


    /** {@inheritDoc} */
    @Override
    public void disableExitConfirmation() {
        if (_handlerRegistration != null) {
            _handlerRegistration.removeHandler();
        }
    }


    /** {@inheritDoc} */
    @Override
    public void enableExitConfirmation() {
        _handlerRegistration =
            Window.addWindowClosingHandler(new ExitHandler());

            Window.addCloseHandler(new CloseHandler<Window>() {
                @Override
                public void onClose(final CloseEvent<Window> arg0) {
                    new LogoutAction().execute();
                }});
    }


    /**
     * Handler for window closing.
     *
     * @author Civic Computing Ltd.
     */
    public static class ExitHandler implements ClosingHandler {

        /** {@inheritDoc} */
        @Override
        public void onWindowClosing(final ClosingEvent event) {
            event.setMessage(I18n.UI_CONSTANTS.exitWarning());
        }
    }


    /** {@inheritDoc} */
    @Override
    public void openUrl(final String url,
                        final String windowName,
                        final String params) {
        Window.open(url, windowName, params);
    }
}
