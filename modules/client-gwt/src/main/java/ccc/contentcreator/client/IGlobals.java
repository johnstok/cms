/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.client;

import ccc.contentcreator.api.ActionNameConstants;
import ccc.contentcreator.api.ActionStatusConstants;
import ccc.contentcreator.api.CommandTypeConstants;
import ccc.contentcreator.api.ErrorDescriptions;
import ccc.contentcreator.api.ErrorResolutions;
import ccc.contentcreator.api.UIConstants;
import ccc.contentcreator.api.UIMessages;
import ccc.rest.dto.UserDto;


/**
 * API for creating application scope objects.
 *
 * @author Civic Computing Ltd.
 */
public interface IGlobals {

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
     * Determine the URL for the application's host.
     *
     * @return The host url as a string.
     */
    String hostURL();

    /**
     * Determine the URL for the REST API.
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
    UserDto currentUser();

    /**
     * Mutator.
     *
     * @param user The current logged in user.
     */
    void currentUser(final UserDto user);

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
    String APP_URL = "ContentCreator.jsp";
    /** API_URL : String. */
    String API_URL = "api";
    /** MIN_USER_NAME_LENGTH : int. */
    int MIN_USER_NAME_LENGTH = 4;

    /** ADMINISTRATOR : String. */
    String ADMINISTRATOR = "ADMINISTRATOR";
    /** SITE_BUILDER : String. */
    String SITE_BUILDER = "SITE_BUILDER";
    /** CONTENT_CREATOR : String. */
    String CONTENT_CREATOR = "CONTENT_CREATOR";

    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    ActionStatusConstants actionStatusConstants();

    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    CommandTypeConstants commandTypeConstants();

    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    ErrorDescriptions errorDescriptions();

    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    ErrorResolutions errorResolutions();
}
