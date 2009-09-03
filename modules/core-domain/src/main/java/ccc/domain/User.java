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

import static ccc.types.DBC.*;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ccc.serialization.Json;
import ccc.serialization.JsonKeys;
import ccc.types.DBC;
import ccc.types.EmailAddress;


/**
 * A user of the CCC system.
 * <p>
 * TODO: Introduce the Username class.
 *
 * @author Civic Computing Ltd.
 */
public class User extends Entity {

    /** SYSTEM_USER : User. */
    public static final User SYSTEM_USER = new User("SYSTEM", "SYSTEM");
    private static final int MAXIMUM_DATUM_LENGTH = 1000;
    private static final int MAXIMUM_DATUM_KEY_LENGTH = 100;

    private String _username; // FIXME: Use the Username class.
    private EmailAddress _email;
    private byte[] _hash;
    private Set<String> _roles = new HashSet<String>(); // FIXME: Use the Role class.
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
     * @param passwordString The unhashed password as a string.
     */
    public User(final String username, final String passwordString) {
        DBC.require().notEmpty(username);
        _username = username;
        _hash = hash(passwordString, id().toString());
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
        _username = username;
    }


    /**
     * Mutator for the user's email.
     *
     * @param email The email.
     */
    public void email(final EmailAddress email) {
        DBC.require().notNull(email);
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


    /**
     * Mutator for the password.
     *
     * @param passwordString The new password.
     */
    public void password(final String passwordString) {
        _hash = hash(passwordString, id().toString());
    }


    /**
     * Compares hash of the passwordString to the field hash.
     *
     * @param passwordString The unhashed password as a string.
     * @return True if passwordString's hash matches.
     */
    public boolean matches(final String passwordString) {
        return Arrays.equals(hash(passwordString, id().toString()), _hash);
    }


    /**
     * Hash a password.
     *
     * @param passwordString A string representing the password to hash.
     * @param saltString A string representing the salt to use for hashing.
     * @return The hashed password as a byte array.
     */
    public static byte[] hash(final String passwordString,
                              final String saltString) {

        try {
            // Prepare
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            final Charset utf8 = Charset.forName("UTF-8");
            final byte[] salt = saltString.getBytes(utf8);
            final byte[] password = passwordString.getBytes(utf8);

            // Compute initial hash
            digest.reset();
            digest.update(salt);
            digest.update(password);
            byte[] hash = digest.digest();

            // Perform 1000 repetitions
            for (int i = 0; i < 1000; i++) {
                digest.reset();
                hash = digest.digest(hash);
            }

            return hash;

        } catch (final NoSuchAlgorithmException e) {
            throw new CCCException("Failed to compute password digest.", e);
        }
    }


    /**
     * Test whether a hash matches the specified password.
     *
     * @param expected The expected hash.
     * @param password The password to test.
     * @param salt The salt to use.
     * @return True if the hashed password matches the expected hash; false
     *      otherwise.
     */
    public static boolean matches(final byte[] expected,
                                  final String password,
                                  final String salt) {
        return Arrays.equals(expected, hash(password, salt));
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
