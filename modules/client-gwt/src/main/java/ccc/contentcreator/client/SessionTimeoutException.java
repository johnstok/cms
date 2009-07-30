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


/**
 * A client-side exception, indicating the user's session expired.
 *
 * @author Civic Computing Ltd.
 */
public class SessionTimeoutException
    extends
        Exception {

    /**
     * Constructor.
     *
     * @param message The server message indicating timeout.
     */
    public SessionTimeoutException(final String message) {
        super(message);
    }


    /**
     * Test if a server response indicates the user's session has expired.
     *
     * @param message The message to test.
     *
     * @return True if the session has expired, false otherwise.
     */
    public static boolean isTimeoutMessage(final String message) {
        return
            // For async HTTP requests.
            message.startsWith("<!-- LOGIN_REQUIRED -->")

            // For IFrame form submission.
            || message.contains("action=\"j_security_check\"")

            // For large requests (e.g. file uploads).
            || message.contains("The request body was too large to be "
                                + "cached during the authentication process");
    }
}
