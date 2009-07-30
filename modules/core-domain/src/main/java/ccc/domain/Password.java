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

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import ccc.api.DBC;


/**
 * Represents a user's password.
 *
 * @author Civic Computing Ltd.
 */
public class Password extends Entity {

    private User _user;
    private byte[] _hash;

    /** Constructor: for persistence only. */
    protected Password() { super(); }

    /**
     * Constructor.
     *
     * @param user The user.
     * @param passwordString The unhashed password as a string.
     */
    public Password(final User user, final String passwordString) {
        DBC.require().notNull(user);
        _user = user;
        _hash = hash(passwordString, id().toString());

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
     * Accessor for the user.
     *
     * @return The user.
     */
    public User user() {
        return _user;
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
}
