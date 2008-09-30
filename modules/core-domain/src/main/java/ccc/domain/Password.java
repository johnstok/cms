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


/**
 * Represents a user's password.
 *
 * TODO: Add an update(String newPassword) method.
 * TODO: Persistence.
 *
 * @author Civic Computing Ltd.
 */
public class Password extends Entity {

    private User _user;
    private byte[] _hash;

    /**
     * Constructor.
     *
     * @param user
     * @param string
     */
    public Password(final User user, final String string) {
        _user = user;
        _hash = hash(string);

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
     * TODO: Add a description of this method.
     *
     * @param string
     * @return
     */
    public boolean matches(final String string) {
        return Arrays.equals(hash(string), _hash);
    }


    /**
     * Hash and salt a password.
     *
     * @param passwordString The un-hashed password as a string.
     * @return The hashed password as a byte array.
     */
    final byte[] hash(final String passwordString) {
        try {
            // Prepare
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            final Charset utf8 = Charset.forName("UTF-8");
            final byte[] salt = id().toString().getBytes(utf8);
            final byte[] password = passwordString.getBytes(utf8);

            // Compute
            digest.reset();
            digest.update(salt);
            digest.update(password);

            return digest.digest();

        } catch (final NoSuchAlgorithmException e) {
            throw new CCCException("Failed to compute password digest.", e);
        }
    }

}
