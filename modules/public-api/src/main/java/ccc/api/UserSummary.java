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

import static ccc.api.JsonKeys.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import ccc.types.Username;


/**
 * A summary of a user.
 *
 * @author Civic Computing Ltd.
 */
public final class UserSummary implements Serializable, Jsonable {
    private String _email;
    private ID _id;
    private Username _username;
    private HashSet<String> _roles;

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
                       final Username username,
                       final Set<String> roles) {
        _email = email;
        _id = id;
        _username = username;
        _roles = new HashSet<String>(roles);
    }


    /**
     * Constructor.
     *
     * @param json The JSON representation of a user.
     */
    public UserSummary(final Json json) {
        _id = json.getId(ID);
        _email = json.getString(EMAIL);
        _username = new Username(json.getString(USERNAME));
        _roles = new HashSet<String>(json.getStrings(ROLES));
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
    public Username getUsername() {
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

    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        json.set(ID, getId());
        json.set(EMAIL, getEmail());
        json.set(USERNAME, getUsername().toString());
        json.setStrings(ROLES, getRoles());
    }
}
