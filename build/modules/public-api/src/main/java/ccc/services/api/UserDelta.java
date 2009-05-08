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
 * TODO: Add Description for this type.
 * TODO: Can we subclass {@link UserSummary}?
 *
 * @author Civic Computing Ltd.
 */
public final class UserDelta implements Serializable {
    private ID _id;
    private String _password;
    private String _email;
    private String _username;
    private Set<String> _roles;

    @SuppressWarnings("unused") private UserDelta() { super(); }

    /**
     * Constructor.
     *
     * @param id
     * @param password
     * @param email
     * @param username
     * @param roles
     */
    public UserDelta(final ID id,
                     final String password,
                     final String email,
                     final String username,
                     final Set<String> roles) {
        _id = id;
        _password = password;
        _email = email;
        _username = username;
        _roles = roles;
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
     * Mutator.
     *
     * @param id The id to set.
     */
    public void setId(final ID id) {
        _id = id;
    }


    /**
     * Accessor.
     *
     * @return Returns the password.
     */
    public String getPassword() {
        return _password;
    }


    /**
     * Mutator.
     *
     * @param password The password to set.
     */
    public void setPassword(final String password) {
        _password = password;
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
     * Mutator.
     *
     * @param email The email to set.
     */
    public void setEmail(final String email) {
        _email = email;
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
     * Mutator.
     *
     * @param username The username to set.
     */
    public void setUsername(final String username) {
        _username = username;
    }


    /**
     * Accessor.
     *
     * @return Returns the roles.
     */
    public Set<String> getRoles() {
        return _roles;
    }


    /**
     * Mutator.
     *
     * @param roles The roles to set.
     */
    public void setRoles(final Set<String> roles) {
        _roles = roles;
    }
}
