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

import junit.framework.TestCase;


/**
 * Tests for the {@link Password} class.
 * TODO: Disallow NULL user.
 * TODO: Disallow NULL or empty password.
 * TODO: Validate password characters, min length, max length(?)
 *
 * @author Civic Computing Ltd.
 */
public class PasswordTest extends TestCase {

    /**
     * Test.
     */
    public void testConstructor() {

        // ARRANGE
        final User u = new User("testUser");

        // ACT
        final Password pw = new Password(u, "hash");

        // ASSERT
        assertEquals(u, pw.user());
        assertTrue("Password should match.", pw.matches("hash"));
    }

    /**
     * Test.
     */
    public void testHash() {

        // ARRANGE
        final String password = "password";
        final Password pw = new Password(new User("testUser"), "hash");

        // ACT
        final byte[] hash = Password.hash(password, pw.id().toString());

        // ASSERT
        assertEquals(SHA_HASH_LENGTH, hash.length);
        assertTrue(
            "Hashes should be equal.",
            Arrays.equals(hash(pw, "password"), hash));
    }

    private byte[] hash(final Password pw, final String passwordString) {
        try {
            // Prepare
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            final Charset utf8 = Charset.forName("UTF-8");
            final byte[] salt = pw.id().toString().getBytes(utf8);
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

    /**
     * Test.
     */
    public void testChangePassword() {
        // ARRANGE
        final String password = "newPass";
        final Password pw = new Password(new User("testUser"), "hash");

        // ACT
        pw.password("newPass");
        final byte[] hash = Password.hash(password, pw.id().toString());

        // ASSERT
        assertEquals(SHA_HASH_LENGTH, hash.length);
        assertTrue(
            "Hashes should be equal.",
            Arrays.equals(hash(pw, password), hash));
    }

    private static final int SHA_HASH_LENGTH = 32;
}
