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

import junit.framework.TestCase;


/**
 * Tests for the {@link MimeType} class.
 *
 * @author Civic Computing Ltd.
 */
public class MimeTypeTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testHashcodeEquality() {

        // ARRANGE
        final MimeType html = new MimeType("text", "html");

        // ACT

        // ASSERT
        assertEquals(html.hashCode(), MimeType.HTML.hashCode());
        assertFalse(html.hashCode()==MimeType.BINARY_DATA.hashCode());
    }


    /**
     * Test.
     */
    public void testMimeTypeToString() {

        // ARRANGE

        // ACT
        final String mtString = MimeType.HTML.toString();

        // ASSERT
        assertEquals("text/html", mtString);
    }

    /**
     * Test.
     */
    public void testMimeTypeEquality() {

        // ARRANGE
        final MimeType t1 = new MimeType("text", "html");
        final MimeType t2 = new MimeType("text", "html");

        // ACT

        // ASSERT
        assertTrue(t1.equals(t2));
        assertTrue(t1.equals(t1));
        assertFalse(t1.equals(new MimeType("text", "plain")));
        assertFalse(t1.equals(new MimeType("application", "html")));
        assertFalse(t1.equals(new MimeType("application", "xml")));
        assertFalse(t1.equals(new Object()));
        assertFalse(t1.equals(null));

    }

    /**
     * Test.
     */
    public void testAccessors() {

        // ARRANGE
        final MimeType type = new MimeType("text", "html");

        // ACT
        final String primary = type.getPrimaryType();
        final String sub = type.getSubType();

        // ASSERT
        assertEquals("text", primary);
        assertEquals("html", sub);
    }
}
