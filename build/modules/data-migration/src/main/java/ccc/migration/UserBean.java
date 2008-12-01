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
    private int _legacyId = -1;


    /**
     * Constructor.
     *
     * @param user The User.
     * @param password The password for the user.
     * @param legacyId The legacyId for the user.
     */
    public UserBean(final User user,
                    final String password,
                    final int legacyId) {
        _user = user;
        _password = password;
        _legacyId = legacyId;
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

    /**
     * Accessor for legacy Id.
     *
     * @return The legacy Id.
     */
    public int legacyId() {
        return _legacyId;
    }


}
