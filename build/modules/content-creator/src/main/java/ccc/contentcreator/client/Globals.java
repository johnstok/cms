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

import ccc.api.UserSummary;
import ccc.contentcreator.api.SecurityService;
import ccc.contentcreator.api.SecurityServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;


/**
 * Global factory class.
 *
 * @deprecated Use an implementation of the {@link IGlobals} interface instead.
 *
 * @author Civic Computing Ltd.
 */
@Deprecated
public final class Globals {


    private static UserSummary _user;

    private Globals() { super(); }


    /**
     * Factory for {@link SecurityServiceAsync} objects.
     *
     * @return A new instance of {@link SecurityServiceAsync}.
     */
    public static SecurityServiceAsync securityService() {
        return GWT.create(SecurityService.class);
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



    /**
     * Accessor.
     *
     * @return The current logged in user.
     */
    public static UserSummary currentUser() {
        return _user;
    }

    /**
     * Mutator.
     *
     * @param user The current logged in user.
     */
    public static void currentUser(final UserSummary user) {
        _user = user;
    }

    /** APP_URL : String. */
    public static final String APP_URL = "/";
    /** API_URL : String. */
    public static final String API_URL = "api/";

}
