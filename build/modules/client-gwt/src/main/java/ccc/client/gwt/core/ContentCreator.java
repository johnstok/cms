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
package ccc.client.gwt.core;


import ccc.api.core.API;
import ccc.client.actions.GetServicesAction;
import ccc.client.actions.IsLoggedInAction;
import ccc.client.core.CoreEvents;
import ccc.client.core.I18n;
import ccc.client.core.InternalServices;
import ccc.client.events.Event;
import ccc.client.events.EventHandler;
import ccc.client.gwt.i18n.GWTActionNameConstants;
import ccc.client.gwt.i18n.GWTActionStatusConstants;
import ccc.client.gwt.i18n.GWTCommandTypeConstants;
import ccc.client.gwt.i18n.GWTErrorDescriptions;
import ccc.client.gwt.i18n.GWTErrorResolutions;
import ccc.client.gwt.i18n.GWTUIConstants;
import ccc.client.gwt.i18n.GWTUIMessages;
import ccc.client.i18n.ActionNameConstants;
import ccc.client.i18n.ActionStatusConstants;
import ccc.client.i18n.CommandTypeConstants;
import ccc.client.i18n.ErrorDescriptions;
import ccc.client.i18n.ErrorResolutions;
import ccc.client.i18n.UIConstants;
import ccc.client.i18n.UIMessages;
import ccc.plugins.s11n.json.SerializerFactory;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public final class ContentCreator implements EntryPoint {

    private GWTGlobals _globals = new GWTGlobals();


    /**
     * Constructor.
     */
    public ContentCreator() {

        I18n.UI_CONSTANTS =
            GWT.<UIConstants>create(GWTUIConstants.class);
        I18n.UI_MESSAGES =
            GWT.<UIMessages>create(GWTUIMessages.class);
        I18n.ERROR_DESCRIPTIONS =
            GWT.<ErrorDescriptions>create(GWTErrorDescriptions.class);
        I18n.ERROR_RESOLUTIONS =
            GWT.<ErrorResolutions>create(GWTErrorResolutions.class);

        GWTGlobals.setUserActions(
            GWT.<ActionNameConstants>create(GWTActionNameConstants.class));
        GWTGlobals.setActionConstants(
            GWT.<ActionStatusConstants>create(GWTActionStatusConstants.class));
        GWTGlobals.setCommandConstants(
            GWT.<CommandTypeConstants>create(GWTCommandTypeConstants.class));

        InternalServices.PARSER      = new GWTTextParser();
        InternalServices.GLOBALS     = _globals;
        InternalServices.TIMERS      = new GWTTimers();
        InternalServices.VALIDATOR   = new GWTValidations();
        InternalServices.EXECUTOR    = new GWTRequestExecutor();
        InternalServices.SERIALIZERS =
            new SerializerFactory(InternalServices.PARSER);
        InternalServices.ENCODER     = new GWTTemplateEncoder();
        InternalServices.WINDOW      = new GWTWindow();
        InternalServices.EX_HANDLER  =
            new GWTExceptionHandler(InternalServices.WINDOW);
        InternalServices.DIALOGS     = new GWTDialogFactory();


        if (paramExists("dec")) {
            InternalServices.WINDOW.enableExitConfirmation();
        }
    }


    private boolean paramExists(final String paramName) {
        return null!=InternalServices.WINDOW.getParameter(paramName);
    }


    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {

        installUnexpectedExceptionHandler();

        InternalServices.CORE_BUS.registerHandler(
            new EventHandler<CoreEvents>() {
                @Override
                public void handle(final Event<CoreEvents> event) {
                    switch (event.getType()) {
                        case ERROR:
                            InternalServices.EX_HANDLER.unexpectedError(
                                event.<Throwable>getProperty("exception"),
                                event.<String>getProperty("name"));
                        default:
                            break;
                    }
                }
            });

        new GetServicesAction() {
            /** {@inheritDoc} */
            @Override
            protected void onSuccess(final API api) {
                super.onSuccess(api);
                _globals.setSettings(InternalServices.API.getProps());
                new IsLoggedInAction().execute();
            }}.execute();

    }


    private void installUnexpectedExceptionHandler() {
        GWT.setUncaughtExceptionHandler(
            new UncaughtExceptionHandler(){
                public void onUncaughtException(final Throwable e) {
                    InternalServices.EX_HANDLER.unexpectedError(
                        e, I18n.USER_ACTIONS.unknownAction());
                }
            }
        );
    }
}
