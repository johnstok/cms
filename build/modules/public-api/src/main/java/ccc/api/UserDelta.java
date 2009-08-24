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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ccc.types.Username;


/**
 * A delta class, for updating an user.
 *
 * @author Civic Computing Ltd.
 */
public final class UserDelta implements Serializable, Jsonable {
    private String _email;
    private Username _username;
    private HashSet<String> _roles;
    private Map<String, String> _metadata;


    @SuppressWarnings("unused") private UserDelta() { super(); }

    /**
     * Constructor.
     *
     * @param email The user's email.
     * @param username The user's username.
     * @param roles The user's roles.
     * @param metadata The user's metadata.
     */
    public UserDelta(final String email,
                     final Username username,
                     final Set<String> roles,
                     final Map<String, String> metadata) {
        _email = email;
        _username = username;
        _roles = new HashSet<String>(roles);
        _metadata = new HashMap<String, String>(metadata);
    }


    /**
     * Constructor.
     *
     * @param json
     */
    public UserDelta(final Json json) {
        this(
            json.getString(JsonKeys.EMAIL),
            new Username(json.getString(JsonKeys.USERNAME)),
            new HashSet<String>(json.getStrings(JsonKeys.ROLES)),
            json.getStringMap(JsonKeys.METADATA)
        );
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

    /**
     * Accessor.
     *
     * @return Returns the metadata.
     */
    public Map<String, String> getMetadata() {
        return _metadata;
    }


    /**
     * Mutator.
     *
     * @param metadata The metadata to set.
     */
    public void setMetadata(final Map<String, String> metadata) {
        _metadata = new HashMap<String, String>(metadata);
    }


    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        json.set(JsonKeys.USERNAME, getUsername().toString());
        json.set(JsonKeys.EMAIL, getEmail());
        json.setStrings(JsonKeys.ROLES, getRoles());
        json.set(JsonKeys.METADATA, getMetadata());
    }
}
