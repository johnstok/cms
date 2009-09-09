/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.types;

import junit.framework.TestCase;


/**
 * Tests for the {@link Password} class.
 *
 * @author Civic Computing Ltd.
 */
public class PasswordTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testPasswordStrengthTest() {

        // ARRANGE

        // ACT

        // ASSERT
        assertTrue(Password.isStrong("Testtest00-"));
        assertFalse(Password.isStrong("Testte00-"));   // Too short
        assertFalse(Password.isStrong("testtest00-")); // No capitals
        assertFalse(Password.isStrong("TESTTEST00-")); // No lower case
        assertFalse(Password.isStrong("testtestoo-")); // No digit
        assertFalse(Password.isStrong("testtestooo")); // No non-alphanum char's
    }
}
