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

import java.util.HashSet;
import java.util.Set;

import ccc.api.DBC;
import ccc.api.Username;
import ccc.commons.EmailAddress;


/**
 * A user of the CCC system.
 * <p>
 * TODO: Introduce the {@link Username} class.
 *
 * @author Civic Computing Ltd.
 */
public class User extends VersionedEntity {

    /** VALID_CHARACTERS : String. */
    public static final String  VALID_CHARACTERS = "[\\w]*";

    private String _username;
    private EmailAddress _email;
    private Set<String> _roles = new HashSet<String>();

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
        DBC.require().minLength(username, USERNAME_MIN_LENGTH);
        DBC.require().toMatch(VALID_CHARACTERS, username);

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

    private static final int USERNAME_MIN_LENGTH = 4;

    /**
     * Mutator for the user's email.
     *
     * @param email The email.
     */
    public void email(final EmailAddress email) {
        DBC.require().notNull(email);
        DBC.require().toBeTrue(email.isValid());
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
}
