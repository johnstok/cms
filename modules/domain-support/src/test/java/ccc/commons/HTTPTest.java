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
}
