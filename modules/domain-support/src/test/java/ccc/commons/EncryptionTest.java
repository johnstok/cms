/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
package ccc.commons;

import static ccc.commons.Encryption.*;

import java.util.Arrays;

import junit.framework.TestCase;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class EncryptionTest
    extends
        TestCase {


    /**
     * Test.
     */
    public void testHash() {

        // ARRANGE
        final String password = "password";
        final String salt     = "salt";

        // ACT
        final byte[] hash = hash(password, salt);

        // ASSERT
        assertEquals(SHA_HASH_LENGTH, hash.length);
        assertTrue("Hashes should be equal.", Arrays.equals(EXPECTED, hash));
    }

    private static final byte[] EXPECTED = new byte[] {
        -91,   81, -57,  -78,   -7, -82, -64,   15,  52, -49,
         76, -113,  40,  104, -122, -46, -60, -114,  15, 106,
         12,   51,  40, -101,  -92,  85,  38,    4, -50,  26,
        -38,   87
    };
}
