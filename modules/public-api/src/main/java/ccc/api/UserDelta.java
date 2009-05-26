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
package ccc.api;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


/**
 * A delta class, for updating a user.
 *
 * @author Civic Computing Ltd.
 */
public final class UserDelta implements Serializable, Jsonable {
    private String _email;
    private Username _username;
    private HashSet<String> _roles;

    @SuppressWarnings("unused") private UserDelta() { super(); }

    /**
     * Constructor.
     *
     * @param email
     * @param username
     * @param roles
     */
    public UserDelta(final String email,
                     final Username username,
                     final Set<String> roles) {
        _email = email;
        _username = username;
        _roles = new HashSet<String>(roles);
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
    public Username getUsername() {
        return _username;
    }


    /**
     * Mutator.
     *
     * @param username The username to set.
     */
    public void setUsername(final Username username) {
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
        _roles = new HashSet<String>(roles);
    }


    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        json.set(JsonKeys.USERNAME, getUsername().toString());
        json.set(JsonKeys.EMAIL, getEmail());
        json.setStrings(JsonKeys.ROLES, getRoles());
    }
}
