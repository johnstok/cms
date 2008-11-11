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
package ccc.migration;

import ccc.domain.User;

/**
 * Wrapper for ccc.domain.User and password.
 *
 * @author Civic Computing Ltd.
 */
public class UserBean {
    private User _user = null;
    private String _password = null;


    /**
     * Constructor.
     *
     * @param user The User.
     * @param password The password for the user.
     */
    public UserBean(final User user, final String password) {
        _user = user;
        _password = password;
    }

    /**
     * Accessor for user.
     *
     * @return The user.
     */
    public User user() {
        return _user;
    }

    /**
     * Accessor for password.
     *
     * @return The password.
     */
    public String password() {
        return _password;
    }
}
