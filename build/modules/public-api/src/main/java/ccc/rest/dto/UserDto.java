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
package ccc.rest.dto;

import static ccc.serialization.JsonKeys.*;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import ccc.serialization.Json;
import ccc.serialization.Jsonable;
import ccc.types.Username;


/**
 * A summary of a user.
 *
 * @author Civic Computing Ltd.
 */
public final class UserDto implements Serializable, Jsonable {
    private String _email;
    private String _name;
    private UUID _id;
    private Username _username;
    private HashSet<String> _roles;
    private Map<String, String> _metadata;
    private String _password;

    @SuppressWarnings("unused") private UserDto() { super(); }

    /**
     * Constructor.
     *
     * @param email The user's email.
     * @param id The user's id.
     * @param username The user's username.
     * @param name The user's full name.
     * @param roles The user's roles.
     * @param metadata The user's metadata.
     */
    public UserDto(final String email,
                   final UUID id,
                   final Username username,
                   final String name,
                   final Set<String> roles,
                   final Map<String, String> metadata) {
        _email = email;
        _name = name;
        _id = id;
        _username = username;
        _roles = new HashSet<String>(roles);
        _metadata = metadata;
    }


    /**
     * Constructor.
     *
     * @param json The JSON representation of a user.
     */
    public UserDto(final Json json) {
        _id = json.getId(ID);
        _email = json.getString(EMAIL);
        _name = json.getString(NAME);
        _password = json.getString(PASSWORD);

        final String un = json.getString(USERNAME);
        _username = (null==un) ? null : new Username(un);

        final Collection<String> r = json.getStrings(ROLES);
        _roles = (null==r) ? null : new HashSet<String>(r);

        final Map<String, String> md = json.getStringMap(METADATA);
        _metadata =
            (null==md)
                ? new HashMap<String, String>()
                : new HashMap<String, String>(md);
    }


    /**
     * Constructor.
     *
     * @param email The user's email.
     * @param username The user's username.
     * @param name The user's  full name.
     * @param roles The user's roles.
     * @param metadata The user's metadata.
     */
    public UserDto(final String email,
                   final Username username,
                   final String name,
                   final Set<String> roles,
                   final Map<String, String> metadata) {
        _email = email;
        _username = username;
        _name = name;
        _roles = new HashSet<String>(roles);
        _metadata = new HashMap<String, String>(metadata);
    }


    /**
     * Constructor.
     *
     * @param password The user's password.
     */
    public UserDto(final String password) {
        _password = password;
    }


    /**
     * Constructor.
     *
     * @param email The user's email address.
     * @param username The user's username.
     * @param name The user's full name.
     * @param roles The user's roles.
     * @param metadata Metadata associated with the user.
     * @param password The user's password.
     */
    public UserDto(final String email,
                   final Username username,
                   final String name,
                   final Set<String> roles,
                   final Map<String, String> metadata,
                   final String password) {
        _email = email;
        _name = name;
        _username = username;
        _roles = new HashSet<String>(roles);
        _metadata = metadata;
        _password = password;
    }


    /**
     * Constructor.
     *
     * @param email The user's email address.
     * @param password The user's password.
     */
    public UserDto(final String email, final String password) {
        _email = email;
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
     * Accessor.
     *
     * @return Returns the id.
     */
    public UUID getId() {
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
     * @return Returns the name.
     */
    public String getName() {
        return _name;
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


    /**
     * Mutator.
     *
     * @param email The email to set.
     */
    public void setEmail(final String email) {
        _email = email;
    }


    /**
     * Mutator.
     *
     * @param id The id to set.
     */
    public void setId(final UUID id) {
        _id = id;
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
     * Mutator.
     *
     * @param name The name to set.
     */
    public void setName(final String name) {
        _name = name;
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

    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        json.set(ID, getId());
        json.set(EMAIL, getEmail());
        json.set(NAME, getName());
        json.set(
            USERNAME, (null==getUsername()) ? null : getUsername().toString());
        json.setStrings(ROLES, getRoles());
        json.set(METADATA, _metadata);
        json.set(PASSWORD, _password);
    }
}
