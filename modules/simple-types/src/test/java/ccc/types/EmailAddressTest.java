/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.types;

import junit.framework.TestCase;


/**
 * Tests for the {@link EmailAddress} class.
 *
 * @author Civic Computing Ltd.
 */
public class EmailAddressTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testEmailEqualityIsLiteral() {

        // ARRANGE
        final EmailAddress simpleEmail =
            new EmailAddress("john.smith@somewhere.com");
        final EmailAddress quotedEmail =
            new EmailAddress("\"John Smith\" <john.smith@somewhere.com>");

        // ACT

        // ASSERT
        assertTrue(simpleEmail.equals(simpleEmail));
        assertTrue(
            simpleEmail.equals(new EmailAddress("john.smith@somewhere.com")));
        assertFalse(simpleEmail.equals(quotedEmail));
        assertFalse(simpleEmail.equals(new Object()));
        assertFalse(simpleEmail.equals(null));
    }

    /**
     * Test.
     */
    public void testInvalidEmailAddressesDetected() {

        // ARRANGE

        // ACT

        // ASSERT
        assertFalse(EmailAddress.isValidText("@helloworld@"));
        assertFalse(EmailAddress.isValidText(""));
        assertFalse(EmailAddress.isValidText("jack"));
    }

    /**
     * Test.
     */
    public void testEmailAddressToString() {

        // ARRANGE
        final EmailAddress simpleEmail = new EmailAddress("abc@def.com");
        final EmailAddress domainEmail =
            new EmailAddress("someone@[192.168.1.100]");
        final EmailAddress quotedEmail =
            new EmailAddress("\"John Smith\" <john.smith@somewhere.com>");

        // ACT

        // ASSERT
        assertEquals("abc@def.com", simpleEmail.toString());
        assertEquals("someone@[192.168.1.100]", domainEmail.toString());
        assertEquals(
            "\"John Smith\" <john.smith@somewhere.com>",
            quotedEmail.toString());
    }

    /**
     * Test.
     */
    public void testSupportsApostrophe() {

        // ARRANGE
        final EmailAddress a =
            new EmailAddress("Steven D'Arcy <steven.d'arcy@foo.com>");

        // ACT
        final boolean valid = a.isValid();

        // ASSERT
        assertTrue(valid);
    }

}
