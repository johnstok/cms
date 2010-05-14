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
package ccc.api.core;

import static ccc.plugins.s11n.JsonKeys.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import ccc.api.types.DBC;
import ccc.api.types.Username;
import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.JsonKeys;


/**
 * A summary of a user.
 *
 * @author Civic Computing Ltd.
 */
public class User
    extends
        Res {

    private String _email; // FIXME: Should be type EmailAddress.
    private String _name;
    private UUID _id;
    private Username _username;
    private Set<UUID> _groups = new HashSet<UUID>();
    private Set<String> _permissions = new HashSet<String>();
    private Map<String, String> _metadata = new HashMap<String, String>();
    private String _password; // FIXME: Should be type Password.


    /**
     * Constructor.
     */
    public User() { super(); }


    /**
     * Constructor.
     *
     * @param json The JSON representation of a user.
     */
    public User(final Json json) {
        fromJson(json);
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
     * @return Returns the groups.
     */
    public Set<UUID> getGroups() {
        return _groups;
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
     *
     * @return Returns 'this' reference, to allow method chaining.
     */
    public User setMetadata(final Map<String, String> metadata) {
        DBC.require().notNull(metadata);
        _metadata = new HashMap<String, String>(metadata);
        return this;
    }


    /**
     * Mutator.
     *
     * @param email The email to set.
     *
     * @return Returns 'this' reference, to allow method chaining.
     */
    public User setEmail(final String email) {
        _email = email;
        return this;
    }


    /**
     * Mutator.
     *
     * @param id The id to set.
     *
     * @return Returns 'this' reference, to allow method chaining.
     */
    public User setId(final UUID id) {
        _id = id;
        return this;
    }


    /**
     * Mutator.
     *
     * @param username The username to set.
     *
     * @return Returns 'this' reference, to allow method chaining.
     */
    public User setUsername(final Username username) {
        _username = username;
        return this;
    }


    /**
     * Mutator.
     *
     * @param name The name to set.
     *
     * @return Returns 'this' reference, to allow method chaining.
     */
    public User setName(final String name) {
        _name = name;
        return this;
    }


    /**
     * Mutator.
     *
     * @param groups The groups to set.
     *
     * @return Returns 'this' reference, to allow method chaining.
     */
    public User setGroups(final Set<UUID> groups) {
        DBC.require().notNull(groups);
        _groups = new HashSet<UUID>(groups);
        return this;
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
     *
     * @return Returns 'this' reference, to allow method chaining.
     */
    public User setPassword(final String password) {
        _password = password;
        return this;
    }


    /**
     * Accessor.
     *
     * @return Returns the permissions.
     */
    public Set<String> getPermissions() {
        return _permissions;
    }


    /**
     * Mutator.
     *
     * @param permissions The permissions to set.
     */
    public void setPermissions(final Collection<String> permissions) {
        DBC.require().notNull(permissions);
        _permissions = new HashSet<String>(permissions);
    }


    /**
     * Helper method to check if the user has a specified permission.
     *
     * @param perm The permission to be checked.
     * @return True if the user has the permission.
     */
    public boolean hasPermission(final String perm) {
        return _permissions.contains(perm);
    }


    /** {@inheritDoc} */
    @Override
    public void fromJson(final Json json) {
        super.fromJson(json);
        setId(json.getId(ID));
        setEmail(json.getString(EMAIL));
        setName(json.getString(NAME));
        setPassword(json.getString(JsonKeys.PASSWORD));
        setPermissions(json.getStrings(PERMISSIONS));

        final String un = json.getString(USERNAME);
        setUsername((null==un) ? null : new Username(un));

        final Collection<String> r = json.getStrings(ROLES);
        setGroups(
            (null==r)
                ? new HashSet<UUID>()
                : new HashSet<UUID>(mapUuid(r)));

        final Map<String, String> md = json.getStringMap(JsonKeys.METADATA);
        setMetadata(
            (null==md)
                ? new HashMap<String, String>()
                : new HashMap<String, String>(md));

        final Map<String, String> links = json.getStringMap("links");
    }


    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        super.toJson(json);
        json.set(ID, getId());
        json.set(EMAIL, getEmail());
        json.set(NAME, getName());
        json.set(
            USERNAME, (null==getUsername()) ? null : getUsername().toString());
        json.setStrings(ROLES, mapString(getGroups()));
        json.set(JsonKeys.METADATA, _metadata);
        json.set(JsonKeys.PASSWORD, _password);
        json.setStrings(PERMISSIONS, _permissions);
    }


    private Set<String> mapString(final Set<UUID> uuids) {
        final Set<String> strings = new HashSet<String>();
        for (final UUID uuid : uuids) {
            strings.add(uuid.toString());
        }
        return strings;
    }


    private Collection<? extends UUID> mapUuid(final Collection<String> s) {
        final List<UUID> uuids = new ArrayList<UUID>();
        for (final String string : s) {
            uuids.add(UUID.fromString(string));
        }
        return uuids;
    }


    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public String self() { return getLink("self"); }


    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public String uriPassword() { return getLink(PASSWORD); }


    /** PASSWORD : String. */
    public static final String PASSWORD = "password";
}
