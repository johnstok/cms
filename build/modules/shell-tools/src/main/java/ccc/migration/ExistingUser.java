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
package ccc.migration;

import ccc.api.UserDelta;

/**
 * An existing user in CCC6.
 *
 * @author Civic Computing Ltd.
 */
public class ExistingUser {
    /** _user : UserDelta. */
    private final UserDelta _user;
    /** _password : String. */
    private final String    _password;

    /**
     * Constructor.
     *
     * @param user The user details.
     * @param password The user's password.
     */
    public ExistingUser(final UserDelta user,
                        final String password) {
        _user = user;
        _password = password;
    }


    /**
     * Accessor.
     *
     * @return Returns the user details.
     */
    UserDelta getUser() {
        return _user;
    }


    /**
     * Accessor.
     *
     * @return Returns the user password.
     */
    String getPassword() {
        return _password;
    }
}
