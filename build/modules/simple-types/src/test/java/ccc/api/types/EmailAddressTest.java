/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
package ccc.api.types;

import ccc.api.types.EmailAddress;
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
    public void testCanValidateLongEmail() {

        // ARRANGE
        final String longEmail = "ea8322fd-eac3-4f24-a0f1-bd05e10fda53@abc.def";

        // ACT
        final boolean isValid = EmailAddress.isValidText(longEmail);

        // ASSERT
        assertTrue(isValid);
    }

    /**
     * Test.
     */
    public void testEmailEquality() {

        // ARRANGE
        final EmailAddress simpleEmail =
            new EmailAddress("john.smith@somewhere.com");
        final EmailAddress quotedEmail =
            new EmailAddress("jack.smith@somewhere.com");

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

        // ACT

        // ASSERT
        assertEquals("abc@def.com", simpleEmail.toString());
    }


    /**
     * Test.
     */
    public void testSupportsApostrophe() {

        // ARRANGE

        // ACT
        final boolean valid =
            EmailAddress.isValidText("steven.d'arcy@foo.com");

        // ASSERT
        assertTrue(valid);
    }


    /**
     * Test.
     */
    public void testZlsIsInvalid() {

        // ARRANGE

        // ACT
        final boolean valid =
            EmailAddress.isValidText("");

        // ASSERT
        assertFalse(valid);
    }


    /**
     * Test.
     */
    public void testWhitespaceIsInvalid() {

        // ARRANGE

        // ACT
        final boolean valid =
            EmailAddress.isValidText("   ");

        // ASSERT
        assertFalse(valid);
    }

}
