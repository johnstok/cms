/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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

import ccc.contentcreator.api.CommandService;
import ccc.contentcreator.api.CommandServiceAsync;
import ccc.contentcreator.api.QueriesService;
import ccc.contentcreator.api.QueriesServiceAsync;
import ccc.contentcreator.api.SecurityService;
import ccc.contentcreator.api.SecurityServiceAsync;
import ccc.contentcreator.api.UIConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowCloseListener;


/**
 * Global factory class.
 *
 * @author Civic Computing Ltd.
 */
public final class Globals {

    private static final boolean ENABLE_EXIT_CONFIRMATION =
        (null == Window.Location.getParameter("dec"));

    private Globals() { super(); }

    /**
     * Install an exception handler for exceptions that would otherwise escape
     * to the browser.
     */
    public static void installUnexpectedExceptionHandler() {
        GWT.setUncaughtExceptionHandler(
            new UncaughtExceptionHandler(){
                public void onUncaughtException(final Throwable e) {
                    Globals.unexpectedError(e);
                }
            }
        );
    }

    /**
     * Factory for {@link UIConstants} objects.
     *
     * @return A new instance of {@link UIConstants}.
     */
    public static UIConstants uiConstants() {
        return GWT.create(UIConstants.class);
    }

    /**
     * Factory for {@link QueriesServiceAsync} objects.
     *
     * @return A new instance of {@link QueriesServiceAsync}.
     */
    public static QueriesServiceAsync queriesService() {
        return GWT.create(QueriesService.class);
    }

    /**
     * Factory for {@link CommandServiceAsync} objects.
     *
     * @return A new instance of {@link CommandServiceAsync}.
     */
    public static CommandServiceAsync commandService() {
        return GWT.create(CommandService.class);
    }

    /**
     * Factory for {@link SecurityServiceAsync} objects.
     *
     * @return A new instance of {@link SecurityServiceAsync}.
     */
    public static SecurityServiceAsync securityService() {
        return GWT.create(SecurityService.class);
    }

    /**
     * Factory for alert dialogs.
     *
     * @param string The message for the dialog.
     */
    public static void alert(final String string) {
        Window.alert(string);
    }

    /**
     * Determine the URL for the application's host.
     *
     * @return The host url as a string.
     */
    public static String hostURL() {
        return GWT.getHostPageBaseURL()
            .substring(
                0,
                GWT.getHostPageBaseURL()
                   .lastIndexOf(APP_URL));
    }

    /**
     * Determine the URL for the REST API.
     *
     * @return The host url as a string.
     */
    public static String apiURL() {
        return GWT.getHostPageBaseURL()+API_URL;
    }

    /**
     * Determine the URL for the application.
     *
     * @return The host url as a string.
     */
    public static String appURL() {
        return GWT.getHostPageBaseURL();
    }

    /**
     * Report an unexpected exception to the user.
     *
     * @param e The exception to report.
     */
    public static void unexpectedError(final Throwable e) {
        final String errorMesssage = e.getMessage();
        final String causeMessage =
            (null==e.getCause()) ? "" : e.getCause().getMessage();
        if (errorMesssage.startsWith("<!-- LOGIN_REQUIRED -->")){
            alert("Your session timed out - please restart the application.");
        } else if (causeMessage.startsWith("<!-- LOGIN_REQUIRED -->")) {
            alert("Your session timed out - please restart the application.");
        } else {
            GWT.log("An unexpected error occured.", e);
            alert("An unexpected error occured.");
        }
    }

    /**
     * Configure the app to request confirmation from the user if they try to
     * navigate away from the app.
     */
    public static void enableExitConfirmation() {
        if (ENABLE_EXIT_CONFIRMATION) {
            Window.addWindowCloseListener(CLOSE_LISTENER);
        }
    }

    /**
     * Disable app confirmation from the user if they try to navigate away from
     * the app.
     */
    public static void disableExitConfirmation() {
        Window.removeWindowCloseListener(CLOSE_LISTENER);
    }

    /**
     * Redirect to another url. Use with caution the application will exit and
     * all local state will be lost.
     *
     * @param relativeURL The host-relative URL.
     */
    public static void redirectTo(final String relativeURL) {
        redirect(hostURL()+relativeURL);
    }

    /**
     * Refresh the application.
     */
    public static void refresh() {
        Window.Location.reload();
    }

    private static void redirect(final String url) {
        Window.Location.assign(url);
    }

    private static final WindowCloseListener CLOSE_LISTENER =
        new WindowCloseListener(){

            public void onWindowClosed() { /* No Op */ }

            public String onWindowClosing() {
                return "This action will exit the application - "
                       + "any unsaved work will be lost!"; // TODO: I18n
            }
        };

    /** DEFAULT_WIDTH : int. */
    public static final int DEFAULT_WIDTH = 640;
    /** DEFAULT_HEIGHT : int. */
    public static final int DEFAULT_HEIGHT = 480;
    /** APP_URL : String. */
    public static final String APP_URL = "creator/";
    /** API_URL : String. */
    public static final String API_URL = "api/";
    /** MIN_USER_NAME_LENGTH : int. */
    public static final int MIN_USER_NAME_LENGTH = 4;
}
