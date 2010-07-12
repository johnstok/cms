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

import junit.framework.TestCase;
import ccc.api.types.MimeType;


/**
 * Tests for the {@link HTTP} class.
 *
 * @author Civic Computing Ltd.
 */
public class HTTPTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testConstructor() {

        // ARRANGE

        // ACT
        Testing.construct(HTTP.class);

        // ASSERT

    }


    /**
     * Test.
     */
    public void testEncodeURL() {

        // ARRANGE

        // ACT
        final String encoded = HTTP.encode("/foo&bar?baz:", "utf-8");

        // ASSERT
        assertEquals("%2Ffoo%26bar%3Fbaz%3A", encoded);
    }


    /**
     * Test.
     */
    public void testDecodeURL() {

        // ARRANGE

        // ACT
        final String decoded = HTTP.decode("%2Ffoo%26bar%3Fbaz%3A", "utf-8");

        // ASSERT
        assertEquals("/foo&bar?baz:", decoded);
    }


    /**
     * Test.
     */
    public void testDetermineMimeType() {

        // ARRANGE

        // ACT

        // ASSERT
        assertEquals(MimeType.HTML, HTTP.determineMimetype("foo.html"));
        assertEquals(MimeType.JPEG, HTTP.determineMimetype("foo.jpg"));
        assertEquals(MimeType.TEXT, HTTP.determineMimetype("foo.txt"));
    }


    /**
     * Test.
     */
    public void testDetermineMimeTypeHandlesEmptyName() {

        // ARRANGE

        // ACT
        try {
            HTTP.determineMimetype("");
            fail();

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals(
                "Specified string must have length > 0.", e.getMessage());
        }
    }


    /**
     * Test.
     */
    public void testDetermineMimeTypeHandlesNullName() {

        // ARRANGE

        // ACT
        try {
            HTTP.determineMimetype(null);
            fail();

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals("Specified value may not be NULL.", e.getMessage());
        }
    }


    /**
     * Test.
     */
    public void testDetermineMimeTypeHandlesAmbiguousName() {

        // ARRANGE

        // ACT

        // ASSERT
        assertEquals(MimeType.BINARY_DATA, HTTP.determineMimetype("foo"));
    }
}
