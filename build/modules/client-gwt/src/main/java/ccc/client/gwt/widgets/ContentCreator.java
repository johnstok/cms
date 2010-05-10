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
package ccc.client.gwt.widgets;


import ccc.client.gwt.core.GlobalsImpl;
import ccc.client.gwt.core.Response;
import ccc.client.gwt.events.Error;
import ccc.client.gwt.i18n.ActionNameConstants;
import ccc.client.gwt.i18n.ActionStatusConstants;
import ccc.client.gwt.i18n.CommandTypeConstants;
import ccc.client.gwt.i18n.ErrorDescriptions;
import ccc.client.gwt.i18n.ErrorResolutions;
import ccc.client.gwt.i18n.UIConstants;
import ccc.client.gwt.i18n.UIMessages;
import ccc.client.gwt.remoting.GetPropertyAction;
import ccc.client.gwt.remoting.GetServicesAction;
import ccc.client.gwt.remoting.IsLoggedInAction;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public final class ContentCreator implements EntryPoint {

    public ContentCreator() {
        GlobalsImpl.setUserActions(GWT.<ActionNameConstants>create(ActionNameConstants.class));
        GlobalsImpl.setUiConstants(GWT.<UIConstants>create(UIConstants.class));
        GlobalsImpl.setUiMessages(GWT.<UIMessages>create(UIMessages.class));
        GlobalsImpl.setActionConstants(GWT.<ActionStatusConstants>create(ActionStatusConstants.class));
        GlobalsImpl.setCommandConstants(GWT.<CommandTypeConstants>create(CommandTypeConstants.class));
        GlobalsImpl.setErrorDescriptions(GWT.<ErrorDescriptions>create(ErrorDescriptions.class));
        GlobalsImpl.setErrorResolutions(GWT.<ErrorResolutions>create(ErrorResolutions.class));

        GlobalsImpl.setEnableExitConfirmation(null == Window.Location.getParameter("dec"));
    }

    /** EVENT_BUS : HandlerManager. */
    public static final HandlerManager EVENT_BUS =
        new HandlerManager("Event bus");
    private GlobalsImpl _globals = new GlobalsImpl();


    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        _globals.installUnexpectedExceptionHandler();
        EVENT_BUS.addHandler(Error.TYPE, new Error.ErrorHandler() {
            @Override public void onError(final Error event) {
                _globals.unexpectedError(event.getException(), event.getName());
            }
        });

        new GetServicesAction() {
            /** {@inheritDoc} */
            @Override
            protected void onOK(final Response response) {
                super.onOK(response);
                loadSettings();
            }}.execute();

    }


    private void loadSettings() {
        new GetPropertyAction() {
            /** {@inheritDoc} */
            @Override protected void onOK(final Response response) {
                _globals.setSettings(parseMapString(response));
                new IsLoggedInAction().execute();
            }
        }.execute();
    }
}
