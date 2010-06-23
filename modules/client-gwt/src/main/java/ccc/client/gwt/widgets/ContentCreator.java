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


import ccc.client.core.CoreEvents;
import ccc.client.core.I18n;
import ccc.client.core.InternalServices;
import ccc.client.core.Response;
import ccc.client.core.Window;
import ccc.client.events.Event;
import ccc.client.events.EventHandler;
import ccc.client.gwt.core.GWTExceptionHandler;
import ccc.client.gwt.core.GWTTemplateEncoder;
import ccc.client.gwt.core.GWTTextParser;
import ccc.client.gwt.core.GWTWindow;
import ccc.client.gwt.core.GlobalsImpl;
import ccc.client.gwt.core.GwtRequestExecutor;
import ccc.client.gwt.i18n.GWTActionNameConstants;
import ccc.client.gwt.i18n.GWTActionStatusConstants;
import ccc.client.gwt.i18n.GWTCommandTypeConstants;
import ccc.client.gwt.i18n.GWTErrorDescriptions;
import ccc.client.gwt.i18n.GWTErrorResolutions;
import ccc.client.gwt.i18n.GWTUIConstants;
import ccc.client.gwt.i18n.GWTUIMessages;
import ccc.client.gwt.remoting.GetPropertyAction;
import ccc.client.gwt.remoting.GetServicesAction;
import ccc.client.gwt.remoting.IsLoggedInAction;
import ccc.client.gwt.validation.Validations;
import ccc.client.i18n.ActionNameConstants;
import ccc.client.i18n.ActionStatusConstants;
import ccc.client.i18n.CommandTypeConstants;
import ccc.client.i18n.ErrorDescriptions;
import ccc.client.i18n.ErrorResolutions;
import ccc.client.i18n.UIConstants;
import ccc.client.i18n.UIMessages;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public final class ContentCreator implements EntryPoint {

    /** WINDOW : Window. */
    public static final Window WINDOW =
        new GWTWindow();

    private GlobalsImpl _globals = new GlobalsImpl();


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

        GlobalsImpl.setUserActions(
            GWT.<ActionNameConstants>create(GWTActionNameConstants.class));
        GlobalsImpl.setActionConstants(
            GWT.<ActionStatusConstants>create(GWTActionStatusConstants.class));
        GlobalsImpl.setCommandConstants(
            GWT.<CommandTypeConstants>create(GWTCommandTypeConstants.class));

        InternalServices.VALIDATOR  = new Validations();
        InternalServices.EXECUTOR   = new GwtRequestExecutor();
        InternalServices.PARSER     = new GWTTextParser();
        InternalServices.ENCODER    = new GWTTemplateEncoder();
        InternalServices.EX_HANDLER = new GWTExceptionHandler(WINDOW);

        if (!(null == WINDOW.getParameter("dec"))) { // 'dec' param is missing.
            WINDOW.enableExitConfirmation();
        }
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
            protected void onOK(final Response response) {
                super.onOK(response);
                loadSettings();
            }}.execute();

    }


    private void loadSettings() {
        new GetPropertyAction() {
            /** {@inheritDoc} */
            @Override protected void onOK(final Response response) {
                _globals.setSettings(
                    getParser().parseMapString(response.getText()));
                new IsLoggedInAction().execute();
            }
        }.execute();
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
