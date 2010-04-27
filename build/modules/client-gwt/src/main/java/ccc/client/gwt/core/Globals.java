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

import ccc.api.core.User;
import ccc.client.gwt.i18n.ActionNameConstants;
import ccc.client.gwt.i18n.ActionStatusConstants;
import ccc.client.gwt.i18n.CommandTypeConstants;
import ccc.client.gwt.i18n.ErrorDescriptions;
import ccc.client.gwt.i18n.ErrorResolutions;
import ccc.client.gwt.i18n.UIConstants;
import ccc.client.gwt.i18n.UIMessages;


/**
 * API for creating application scope objects.
 *
 * @author Civic Computing Ltd.
 */
public interface Globals {

    /**
     * Install an exception handler for exceptions that would otherwise escape
     * to the browser.
     */
    void installUnexpectedExceptionHandler();

    /**
     * Factory for {@link UIConstants} objects.
     *
     * @return A new instance of {@link UIConstants}.
     */
    UIConstants uiConstants();

    /**
     * Factory for {@link UIMessages} objects.
     *
     * @return A new instance of {@link UIMessages}.
     */
    UIMessages uiMessages();

    /**
     * Factory for alert dialogs.
     *
     * @param string The message for the dialog.
     */
    void alert(final String string);

    /**
     * Factory for confirm dialogs.
     *
     * @param string The message for the dialog.
     *
     * @return True if the user confirmed the action, false otherwise.
     */
    boolean confirm(final String string);

    /**
     * Determine the URL for the application's host.
     *
     * @return The host url as a string.
     */
    String hostURL();

    /**
     * Determine the URL for the REST API.
     *
     * @param secure If true the secure URL will be returned, otherwise the
     *  public URL will be returned.
     *
     * @return The host url as a string.
     */
    String apiURL(boolean secure);

    /**
     * Determine the URL for the application.
     *
     * @return The host url as a string.
     */
    String appURL();

    /**
     * Report an unexpected exception to the user.
     *
     * @param e The exception to report.
     * @param action The action being performed.
     */
    void unexpectedError(final Throwable e, final String action);

    /**
     * Configure the app to request confirmation from the user if they try to
     * navigate away from the app.
     */
    void enableExitConfirmation();

    /**
     * Disable app confirmation from the user if they try to navigate away from
     * the app.
     */
    void disableExitConfirmation();

    /**
     * Redirect to another url. Use with caution the application will exit and
     * all local state will be lost.
     *
     * @param relativeURL The host-relative URL.
     */
    void redirectTo(final String relativeURL);

    /**
     * Refresh the application.
     */
    void refresh();

    /**
     * Accessor.
     *
     * @return The current logged in user.
     */
    User currentUser();

    /**
     * Mutator.
     *
     * @param user The current logged in user.
     */
    void currentUser(final User user);

    /**
     * Factory for {@link ActionNameConstants} objects.
     *
     * @return A new instance of {@link ActionNameConstants}.
     */
    ActionNameConstants userActions();

    /** DEFAULT_WIDTH : int. */
    int DEFAULT_WIDTH = 640;
    /** MIN_WIDTH : int. */
    int MIN_WIDTH = 375;
    /** DEFAULT_HEIGHT : int. */
    int DEFAULT_HEIGHT = 480;
    /** DEFAULT_UPLOAD_HEIGHT : int. */
    int DEFAULT_UPLOAD_HEIGHT = 250;
    /** DEFAULT_MIN_HEIGHT : int. */
    int DEFAULT_MIN_HEIGHT = 150;
    /** APP_URL : String. */
    String APP_URL = "client";
    /** API_URL : String. */
    String API_URL = "api";
    /** MIN_USER_NAME_LENGTH : int. */
    int MIN_USER_NAME_LENGTH = 4;

    /**
     * Retrieve an instance of the action status constants.
     *
     * @return An ActionStatusConstants object.
     */
    ActionStatusConstants actionStatusConstants();

    /**
     * Retrieve an instance of the command type constants.
     *
     * @return An CommandTypeConstants object.
     */
    CommandTypeConstants commandTypeConstants();

    /**
     * Retrieve an instance of the error description constants.
     *
     * @return An ErrorDescriptions object.
     */
    ErrorDescriptions errorDescriptions();

    /**
     * Retrieve an instance of the error resolution constants.
     *
     * @return An ErrorResolutions object.
     */
    ErrorResolutions errorResolutions();

    /**
     * Retrieve a setting value.
     *
     * @param settingName The name of the setting to retrieve.
     *
     * @return The value of the setting as a string or NULL, if the setting
     *  doesn't exist.
     */
    String getSetting(String settingName);
}
