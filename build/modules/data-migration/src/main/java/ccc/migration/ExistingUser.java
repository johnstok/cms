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
    public final UserDelta _user;
    public final String    _password;

    /**
     * Constructor.
     *
     * @param user
     * @param password
     */
    public ExistingUser(final UserDelta user, final String password) {
        _user = user;
        _password = password;
    }
}