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
package ccc.services.api;

import java.io.Serializable;
import java.util.Set;


/**
 * A summary of a user.
 *
 * @author Civic Computing Ltd.
 */
public final class UserSummary implements Serializable {
    private String _email;
    private ID _id;
    private String _username;
    private Set<String> _roles;

    @SuppressWarnings("unused") private UserSummary() { super(); }

    /**
     * Constructor.
     *
     * @param email The user's email.
     * @param id The user's id.
     * @param username The user's username.
     * @param roles The user's roles.
     */
    public UserSummary(final String email,
                       final ID id,
                       final String username,
                       final Set<String> roles) {
        _email = email;
        _id = id;
        _username = username;
        _roles = roles;
    }


    /**
     * Accessor.
     *
     * @return Returns the email.
     */
    public String getEmail() {
        return _email;
    }


    /**
     * Accessor.
     *
     * @return Returns the id.
     */
    public ID getId() {
        return _id;
    }


    /**
     * Accessor.
     *
     * @return Returns the username.
     */
    public String getUsername() {
        return _username;
    }


    /**
     * Accessor.
     *
     * @return Returns the roles.
     */
    public Set<String> getRoles() {
        return _roles;
    }
}
