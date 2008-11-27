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

import ccc.contentcreator.api.ResourceService;
import ccc.contentcreator.api.ResourceServiceAsync;
import ccc.contentcreator.api.UIConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowCloseListener;


/**
 * Global factory class.
 *
 * @author Civic Computing Ltd.
 */
public final class Globals {

    private Globals() { super(); }

    /**
     * Factory for {@link UIConstants} objects.
     *
     * @return A new instance of {@link UIConstants}.
     */
    public static UIConstants uiConstants() {
        return GWT.create(UIConstants.class);
    }

    /**
     * Factory for {@link ResourceServiceAsync} objects.
     *
     * @return A new instance of {@link ResourceServiceAsync}.
     */
    public static ResourceServiceAsync resourceService() {
        return GWT.create(ResourceService.class);
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
     * TODO: Add a description of this method.
     *
     * @param exception
     */
    public static void unexpectedError(final Throwable exception) {
        GWT.log("An unexpected error occured.", exception);
        alert("An unexpected error occured.");
    }

    /**
     * Configure the app to request confirmation from the user if they try to
     * navigate away from the app.
     */
    public static void enableExitConfirmation() {
        Window.addWindowCloseListener(CLOSE_LISTENER);
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

    /*
     * TODO: Find a better solution?
     */
    private static native void redirect(final String url)/*-{
          $wnd.location = url;
    }-*/;

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

}
