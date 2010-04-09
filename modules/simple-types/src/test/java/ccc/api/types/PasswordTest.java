/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
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
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.api.types;

import ccc.api.types.Password;
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
