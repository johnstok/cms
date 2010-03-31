/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;

import ccc.rest.dto.UserDto;
import ccc.serialization.Json;
import ccc.types.DBC;
import ccc.types.EmailAddress;
import ccc.types.Username;


/**
 * A user of the CCC system.
 * FIXME: email should never be NULL.
 *
 * @author Civic Computing Ltd.
 */
public class User
    extends
        Principal {

    /** SYSTEM_USER : User. */
    public static final User SYSTEM_USER =
        new User(new Username("SYSTEM"), "SYSTEM", "SYSTEM");
    private static final int HASH_REPETITIONS = 1000;
    private static final int MAXIMUM_DATUM_LENGTH = 1000;
    private static final int MAXIMUM_DATUM_KEY_LENGTH = 100;

    private Username _username;
    private EmailAddress _email;
    private byte[] _hash;
    private Set<Group> _roles = new HashSet<Group>();
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
     * @param name The user's full name.
     * @param passwordString The unhashed password as a string.
     */
    public User(final Username username,
                final String name,
                final String passwordString) {
        DBC.require().notNull(username);
        DBC.require().notEmpty(username.toString());
        DBC.require().notEmpty(name);
        _username = username;
        setName(name);
        _hash = hash(passwordString, getId().toString());
    }


    /**
     * Constructor.
     *
     * @param username The user's unique name within CCC.
     * @param passwordString The unhashed password as a string.
     */
    public User(final Username username,
                final String passwordString) {
        DBC.require().notNull(username);
        DBC.require().notEmpty(username.toString());
        _username = username;
        setName(username.toString());
        _hash = hash(passwordString, getId().toString());
    }


    /**
     * Accessor for the username property.
     *
     * @return The username.
     */
    public Username getUsername() {
        return _username;
    }


    /**
     * Mutator for the username.
     *
     * @param username The username.
     */
    public void setUsername(final Username username) {
        _username = username;
    }


    /**
     * Mutator for the user's email.
     *
     * @param email The email.
     */
    public void setEmail(final EmailAddress email) {
        DBC.require().notNull(email);
        _email = email;
    }


    /**
     * Accessor for the email property.
     *
     * @return The email as a string.
     */
    public EmailAddress getEmail() {
        return _email;
    }


    /**
     * Assigns role to the user.
     *
     * @param newRole The role to assign.
     */
    public void addGroup(final Group newRole) {
        _roles.add(newRole);
    }


    /**
     * Helper method to check if the user has the specified permission.
     *
     * @param permission The permission to be checked.
     * @return True if the user has the permission.
     */
    public boolean hasPermission(final String permission) {
        for (final Group g : getGroups()) {
            if (g.hasPermission(permission)) { return true; }
        }
        return false;
    }


    /**
     * Accessor for user roles.
     *
     * @return Roles of the user.
     */
    public Set<Group> getGroups() {
        return new HashSet<Group>(_roles);
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
        if (value.isEmpty()) {
            clearMetadatum(key);
        } else {
            _metadata.put(key, value);
        }
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
    public Map<String, String> getMetadata() {
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
        for (final Entry<String, String> e : metadata.entrySet()) {
            if (e.getValue() == null || e.getValue().isEmpty()) {
                clearMetadatum(e.getKey());
            } else {
                _metadata.put(e.getKey(), e.getValue());
            }
        }
    }


    /**
     * Mutator for the password.
     *
     * @param passwordString The new password.
     */
    public void setPassword(final String passwordString) {
        _hash = hash(passwordString, getId().toString());
    }


    /**
     * Compares hash of the passwordString to the field hash.
     *
     * @param passwordString The unhashed password as a string.
     * @return True if passwordString's hash matches.
     */
    public boolean hasPassword(final String passwordString) {
        return Arrays.equals(hash(passwordString, getId().toString()), _hash);
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
            for (int i = 0; i < HASH_REPETITIONS; i++) {
                digest.reset();
                hash = digest.digest(hash);
            }

            return hash;

        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to compute password digest.", e);
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
        toDto().toJson(json);
    }


    /**
     * Query - return the IDs for all groups this user is a member of.
     *
     * @return The group IDs, as a set.
     */
    public Set<UUID> getGroupIds() {
        final Set<UUID> groupIds = new HashSet<UUID>();
        for (final Group g : getGroups()) {
            groupIds.add(g.getId());
        }
        return groupIds;
    }


    /**
     * Mutator - clear all groups for this user.
     */
    public void clearGroups() {
        _roles.clear();
    }


    /**
     * Convert a user to a DTO.
     *
     * @return A DTO representation of this user.
     */
    public UserDto toDto() {
        final UserDto dto = new UserDto();
        dto.setEmail(getEmail().getText());
        dto.setId(getId());
        dto.setUsername(getUsername());
        dto.setName(getName());
        dto.setRoles(getGroupIds());
        dto.setMetadata(getMetadata());
        dto.setPermissions(getPermissions());
        return dto;
    }


    /**
     * Query - determines all permissions available to this user.
     *
     * @return The collection of permissions.
     */
    public Collection<String> getPermissions() {
        final Set<String> perms = new HashSet<String>();
        for (final Group g : getGroups()) { perms.addAll(g.getPermissions()); }
        return perms;
    }


    /**
     * Create summaries for a list of users.
     *
     * @param users The users.
     * @return The corresponding summaries.
     */
    public static List<UserDto> map(final Collection<User> users) {
        final List<UserDto> mapped = new ArrayList<UserDto>();
        for (final User u : users) { mapped.add(u.toDto()); }
        return mapped;
    }


    /** {@inheritDoc} */
    @Override
    public boolean includes(final User user) { return equals(user); }
}
