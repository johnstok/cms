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

import ccc.api.core.API;
import ccc.api.core.ActionSummary;
import ccc.api.core.Comment;
import ccc.api.core.Group;
import ccc.api.core.PagedCollection;
import ccc.api.core.User;
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

    private static UIConstants UI_CONSTANTS;
    private static UIMessages UI_MESSAGES;
    private static ActionStatusConstants ACTION_STATUSES;
    private static CommandTypeConstants COMMAND_TYPES;
    private static ErrorDescriptions ERROR_DESCRIPTIONS;
    private static ErrorResolutions ERROR_RESOLUTIONS;
    private static ActionNameConstants USER_ACTIONS;

    private static boolean ENABLE_EXIT_CONFIRMATION = false;

    private HandlerRegistration _handlerRegistration = null;

    private static PagedCollection<User> USERS;
    private static PagedCollection<ActionSummary> ACTIONS;
    private static PagedCollection<Comment> COMMENTS;
    private static PagedCollection<Group> GROUPS;

    private static API API;


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
    public String appURL() {
        return GWT.getHostPageBaseURL();
    }

    /** {@inheritDoc} */
    @Override
    public User currentUser() {
        return UserStore.currentUser();
    }

    /** {@inheritDoc} */
    @Override
    public void currentUser(final User user) {
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


    /**
     * Accessor for the {@link UIConstants} object.
     *
     * @return A new instance of {@link UIConstants}.
     */
    public static UIConstants uiConstants() {
       return UI_CONSTANTS;
    }


    /**
     * Mutator for the {@link UIConstants} object.
     *
     * @param uiConstants The new UI constants.
     */
    public static void setUiConstants(final UIConstants uiConstants) {
       UI_CONSTANTS = uiConstants;
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

    /**
     * Accessor for the {@link ActionNameConstants} object.
     *
     * @return A new instance of {@link ActionNameConstants}.
     */
    public static ActionNameConstants userActions() {
        return USER_ACTIONS;
    }


    /**
     * Mutator.
     *
     * @param userActions The action names to set.
     */
    public static void setUserActions(final ActionNameConstants userActions) {
        USER_ACTIONS = userActions;
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
        private static User usr;

        private UserStore() {
            // no-op
        }

        public static User currentUser() {
            return usr;
        }

        public static void currentUser(final User user) {
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


    /** {@inheritDoc} */
    @Override
    public PagedCollection<User> users() { return USERS; }


    /**
     * Set the collection for managing users.
     *
     * @param users The user collection to access.
     */
    public static void users(final PagedCollection<User> users) {
        USERS = users;
    }


    /** {@inheritDoc} */
    @Override
    public PagedCollection<ActionSummary> actions() { return ACTIONS; }


    /**
     * Set the collection for managing actions.
     *
     * @param actions The action collection to access.
     */
    public static void actions(final PagedCollection<ActionSummary> actions) {
        ACTIONS = actions;
    }


    /** {@inheritDoc} */
    @Override
    public PagedCollection<Comment> comments() { return COMMENTS; }


    /**
     * Set the collection for managing comments.
     *
     * @param comments The comment collection to access.
     */
    public static void comments(final PagedCollection<Comment> comments) {
        COMMENTS = comments;
    }


    /** {@inheritDoc} */
    @Override
    public PagedCollection<Group> groups() { return GROUPS; }


    /**
     * Set the collection for managing groups.
     *
     * @param groups The group collection to access.
     */
    public static void groups(final PagedCollection<Group> groups) {
        GROUPS = groups;
    }


    /**
     * Set whether an 'exit confirmation' dialog should be displayed.
     *
     * @param enabled True if the dialog should be displayed, false otherwise.
     */
    public static void setEnableExitConfirmation(final boolean enabled) {
        ENABLE_EXIT_CONFIRMATION = enabled;
    }


    /**
     * Mutator.
     *
     * @param create The UI messages.
     */
    public static void setUiMessages(final UIMessages create) {
        UI_MESSAGES = create;
    }


    /**
     * Mutator.
     *
     * @param create The action statuses.
     */
    public static void setActionConstants(final ActionStatusConstants create) {
        ACTION_STATUSES = create;
    }


    /**
     * Mutator.
     *
     * @param create The command types.
     */
    public static void setCommandConstants(final CommandTypeConstants create) {
        COMMAND_TYPES = create;
    }


    /**
     * Mutator.
     *
     * @param create The error descriptions.
     */
    public static void setErrorDescriptions(final ErrorDescriptions create) {
        ERROR_DESCRIPTIONS = create;
    }


    /**
     * Mutator.
     *
     * @param create The error resolutions.
     */
    public static void setErrorResolutions(final ErrorResolutions create) {
        ERROR_RESOLUTIONS = create;
    }


    /**
     * Mutator.
     *
     * @param api The remote API.
     */
    public static void setAPI(final API api) {
        API = api;
    }


    /**
     * Accessor.
     *
     * @return The remote API.
     */
    public static API getAPI() {
        return API;
    }
}
