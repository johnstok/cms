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
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.gwt.core;

import java.util.HashMap;
import java.util.Map;

import ccc.api.core.UserDto;
import ccc.client.gwt.i18n.ActionNameConstants;
import ccc.client.gwt.i18n.ActionStatusConstants;
import ccc.client.gwt.i18n.CommandTypeConstants;
import ccc.client.gwt.i18n.ErrorDescriptions;
import ccc.client.gwt.i18n.ErrorResolutions;
import ccc.client.gwt.i18n.UIConstants;
import ccc.client.gwt.i18n.UIMessages;
import ccc.client.gwt.views.gxt.ErrorDialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.Window.ClosingHandler;


/**
 * {@link Globals} implementation.
 *
 * @author Civic Computing Ltd.
 */
public class GlobalsImpl
    implements
        Globals {

    private static final Map<String, String> SETTINGS =
        new HashMap<String, String>();

    private static final UIConstants UI_CONSTANTS =
        GWT.create(UIConstants.class);
    private static final UIMessages UI_MESSAGES =
        GWT.create(UIMessages.class);
    private static final ActionStatusConstants ACTION_STATUSES =
        GWT.create(ActionStatusConstants.class);
    private static final CommandTypeConstants COMMAND_TYPES =
        GWT.create(CommandTypeConstants.class);
    private static final ErrorDescriptions ERROR_DESCRIPTIONS =
        GWT.create(ErrorDescriptions.class);
    private static final ErrorResolutions ERROR_RESOLUTIONS =
        GWT.create(ErrorResolutions.class);
    private static final ActionNameConstants USER_ACTIONS =
        GWT.create(ActionNameConstants.class);
    private static final boolean ENABLE_EXIT_CONFIRMATION =
        (null == Window.Location.getParameter("dec"));

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
    @Deprecated
    public String apiURL(final boolean secure) {
        return
            GWT.getHostPageBaseURL()
            + API_URL
            + ((secure) ? "/secure" : "/public");
    }

    /** {@inheritDoc} */
    @Override
    public String appURL() {
        return GWT.getHostPageBaseURL();
    }

    /** {@inheritDoc} */
    @Override
    public UserDto currentUser() {
        return UserStore.currentUser();
    }

    /** {@inheritDoc} */
    @Override
    public void currentUser(final UserDto user) {
        UserStore.currentUser(user);
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
        if (ENABLE_EXIT_CONFIRMATION) {
            _handlerRegistration =
                Window.addWindowClosingHandler(new ExitHandler());
        }
    }

    /** {@inheritDoc} */
    @Override
    public String hostURL() {
        return GWT.getHostPageBaseURL();
    }

    /** {@inheritDoc} */
    @Override
    public void installUnexpectedExceptionHandler() {
        GWT.setUncaughtExceptionHandler(
            new UncaughtExceptionHandler(){
                public void onUncaughtException(final Throwable e) {
                    unexpectedError(e, USER_ACTIONS.unknownAction());
                }
            }
        );
    }

    /** {@inheritDoc} */
    @Override
    public void redirectTo(final String relativeURL) {
        redirect(hostURL()+relativeURL);
    }

    /** {@inheritDoc} */
    @Override
    public void refresh() {
        Window.Location.reload();
    }

    /** {@inheritDoc} */
    @Override
    public UIConstants uiConstants() {
       return UI_CONSTANTS;
    }

    /** {@inheritDoc} */
    @Override
    public UIMessages uiMessages() {
        return UI_MESSAGES;
    }

    /** {@inheritDoc} */
    @Override
    public void unexpectedError(final Throwable e, final String action) {
        // FIXME Convert type comparison to multiple methods.
        if (e instanceof RemoteException) {
            final RemoteException re = (RemoteException) e;
            new ErrorDialog(re, action, this).show();
        } else if (e instanceof SessionTimeoutException) {
            alert(uiConstants().sessionTimeOutPleaseRestart());
        } else {
            GWT.log("An unexpected error occured.", e);
            new ErrorDialog(e, action, this).show();
        }
    }

    /** {@inheritDoc} */
    @Override
    public ActionNameConstants userActions() {
        return USER_ACTIONS;
    }

    /**
     * Handler for window closing.
     *
     * @author Civic Computing Ltd.
     */
    private class ExitHandler implements ClosingHandler {

        /** {@inheritDoc} */
        @Override
        public void onWindowClosing(final ClosingEvent event) {
            event.setMessage(uiConstants().exitWarning());
        }
    }

    private void redirect(final String url) {
        Window.Location.assign(url);
    }

    /**
     * User Summary shared by all instances of {@link GlobalsImpl}.
     *
     * @author Civic Computing Ltd.
     */
    private static final class UserStore {
        private static UserDto usr;

        private UserStore() {
            // no-op
        }

        public static UserDto currentUser() {
            return usr;
        }

        public static void currentUser(final UserDto user) {
            usr = user;
        }
    }

    /** {@inheritDoc} */
    @Override
    public ActionStatusConstants actionStatusConstants() {
        return ACTION_STATUSES;
    }

    /** {@inheritDoc} */
    @Override
    public CommandTypeConstants commandTypeConstants() {
        return COMMAND_TYPES;
    }

    /** {@inheritDoc} */
    @Override
    public ErrorDescriptions errorDescriptions() {
        return ERROR_DESCRIPTIONS;
    }

    /** {@inheritDoc} */
    @Override
    public ErrorResolutions errorResolutions() {
        return ERROR_RESOLUTIONS;
    }

    /** {@inheritDoc} */
    @Override
    public String getSetting(final String settingName) {
        return SETTINGS.get(settingName);
    }

    /**
     * Provide additional settings for this global object.
     *
     * @param settings The settings to add.
     */
    public void setSettings(final Map<String, String> settings) {
        SETTINGS.putAll(settings);
    }
}
