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
    public static boolean isTimedout(final String message) {
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
