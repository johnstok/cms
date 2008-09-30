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

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import ccc.commons.DBC;
import ccc.commons.EmailAddress;


/**
 * A user of the CCC system.
 *
 * TODO: Change email field to type EmailAddress
 *
 * @author Civic Computing Ltd.
 */
public class User extends Entity {

    public static final String  VALID_CHARACTERS = "[\\w]*";

    private String _username;
    private String _email;
    private Set<CreatorRoles> _roles = new HashSet<CreatorRoles>();

    /**
     * Constructor.
     * N.B. This constructor should only be used for persistence.
     */
    @SuppressWarnings("unused")
    private User() { super(); }

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

    private static final int USERNAME_MIN_LENGTH = 4;

    /**
     * Mutator for the user's email.
     *
     * @param email The email.
     */
    public void email(final String email) {
        DBC.require().notEmpty(email);
        DBC.require().toBeTrue(EmailAddress.isValidText(email));
        _email = email;
    }

    /**
     * Accessor for the email property.
     *
     * @return The email as a string.
     */
    public String email() {
        return _email;
    }

    /**
     * Assigns role to the user.
     *
     * @param newRole The role to assign.
     */
    public void addRole(final CreatorRoles newRole) {
        _roles.add(newRole);
    }

    /**
     * Helper method to check if the user has defined role.
     *
     * @param role The role to be checked.
     * @return True if the user has the role.
     */
    public boolean hasRole(final CreatorRoles role) {
        return _roles.contains(role);
    }

    /**
     * Accessor for user roles.
     *
     * @return Roles of the user.
     */
    public EnumSet<CreatorRoles> roles() {
        return EnumSet.copyOf(_roles);
    }
}
