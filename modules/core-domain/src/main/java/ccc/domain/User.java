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

import static ccc.api.DBC.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ccc.api.DBC;
import ccc.api.Json;
import ccc.api.JsonKeys;
import ccc.types.EmailAddress;


/**
 * A user of the CCC system.
 * <p>
 * TODO: Introduce the Username class.
 *
 * @author Civic Computing Ltd.
 */
public class User extends Entity {

    /** VALID_CHARACTERS : String. */
    public static final String  VALID_CHARACTERS = "[\\w]*";
    /** SYSTEM_USER : User. */
    public static final User SYSTEM_USER = new User("SYSTEM");
    private static final int USERNAME_MIN_LENGTH = 4;
    private static final int MAXIMUM_DATUM_LENGTH = 1000;
    private static final int MAXIMUM_DATUM_KEY_LENGTH = 100;

    private String _username;
    private EmailAddress _email;
    private Set<String> _roles = new HashSet<String>();
    private Map<String, String> _metadata = new HashMap<String, String>();

    /**
     * Constructor.
     * N.B. This constructor should only be used for persistence.
     */
    protected User() { super(); }

    /**
     * Constructor.
     *
     * @param username The user's unique name within CCC.
     */
    public User(final String username) {
        DBC.require().notEmpty(username);
//        DBC.require().minLength(username, USERNAME_MIN_LENGTH);
//        DBC.require().toMatch(VALID_CHARACTERS, username);

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

    /**
     * Mutator for the username.
     *
     * @param username The username.
     */
    public void username(final String username) {
        DBC.require().notEmpty(username);
        DBC.require().minLength(username, USERNAME_MIN_LENGTH);
        DBC.require().toMatch(VALID_CHARACTERS, username);

        _username = username;
    }


    /**
     * Mutator for the user's email.
     *
     * @param email The email.
     */
    public void email(final EmailAddress email) {
        DBC.require().notNull(email);
//        DBC.require().toBeTrue(email.isValid());
        _email = email;
    }

    /**
     * Accessor for the email property.
     *
     * @return The email as a string.
     */
    public EmailAddress email() {
        return _email;
    }

    /**
     * Assigns role to the user.
     *
     * @param newRole The role to assign.
     */
    public void addRole(final String newRole) {
        _roles.add(newRole);
    }

    /**
     * Helper method to check if the user has defined role.
     *
     * @param role The role to be checked.
     * @return True if the user has the role.
     */
    public boolean hasRole(final String role) {
        return _roles.contains(role);
    }

    /**
     * Accessor for user roles.
     *
     * @return Roles of the user.
     */
    public Set<String> roles() {
        return new HashSet<String>(_roles);
    }

    /**
     * Mutator in order to replace roles of the user.
     *
     * @param roles The set of new roles.
     */
    public void roles(final Set<String> roles) {
        _roles = roles;
    }


    /**
     * Add new metadata for this resource.
     *
     * @param key The key by which the datum will be accessed.
     * @param value The value of the datum. May not be NULL.
     */
    public void addMetadatum(final String key, final String value) {
        require().notEmpty(value);
        require().maxLength(value, MAXIMUM_DATUM_LENGTH);
        require().notEmpty(key);
        require().maxLength(key, MAXIMUM_DATUM_KEY_LENGTH);
        require().containsNoBrackets(key);
        require().containsNoBrackets(value);
        _metadata.put(key, value);
    }

    /**
     * Retrieve metadata for this resource.
     *
     * @param key The key with which the datum was stored.
     * @return The value of the datum. NULL if the datum doesn't exist.
     */
    public String getMetadatum(final String key) {
        return _metadata.get(key);
    }

    /**
     * Remove the metadatum with the specified key.
     *
     * @param key The key with which the datum was stored.
     */
    public void clearMetadatum(final String key) {
        require().notEmpty(key);
        _metadata.remove(key);
    }

    /**
     * Accessor for all metadata.
     *
     * @return The metadata as a hash map.
     */
    public Map<String, String> metadata() {
        return new HashMap<String, String>(_metadata);
    }

    /**
     * Remove all metadata for this resource.
     */
    public void clearMetadata() {
        _metadata.clear();
    }


    /**
     * Add metadata to this resource.
     *
     * @param metadata The metadata to add, as a hashmap.
     */
    public void addMetadata(final Map<String, String> metadata) {
        _metadata.putAll(metadata);
    }


    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        super.toJson(json);
        json.set(JsonKeys.USERNAME, username());
        json.set(JsonKeys.EMAIL, (null==email()) ? null : email().toString());
        json.setStrings(JsonKeys.ROLES, new ArrayList<String>(roles()));
        json.set(JsonKeys.METADATA, metadata());
    }
}
