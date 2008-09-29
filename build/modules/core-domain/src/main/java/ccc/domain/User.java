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
package ccc.domain;

import ccc.commons.DBC;


/**
 * A user of the CCC system.
 *
 * @author Civic Computing Ltd.
 */
public class User extends Entity {

    private String _username;

    /**
     * Constructor.
     * N.B. This constructor should only be used for persistence.
     */
    @SuppressWarnings("unused")
    private User() { super(); }

    /**
     * Constructor.
     *
     * @param username The user's unique name within CCC.
     */
    public User(final String username) {
        DBC.require().notEmpty(username);
        DBC.require().minLength(username, USERNAME_MIN_LENGTH);

        _username = username;
    }

    /**
     * Accessor for the username property.
     *
     * @return The username as a string.
     */
    public String username() {
        return _username;
    }

    private static final int USERNAME_MIN_LENGTH = 4;
}
