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
package ccc.plugins.scripting;

import java.util.Map;

import junit.framework.TestCase;


/**
 * Tests for the {@link Context} class.
 *
 * @author Civic Computing Ltd.
 */
public class ContextTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testAddNewValue() {

        // ARRANGE
        final Context c = new Context();

        // ACT
        c.add("foo", "bar");

        // ASSERT
        assertEquals("bar", c.get("foo", String.class));
    }

    /**
     * Test.
     */
    public void testGetAll() {

        // ARRANGE
        final Context c = new Context();
        c.add("foo", "bar");

        // ACT
        final Map<String, Object> all = c.getAll();

        // ASSERT
        assertEquals(1, all.size());
        assertTrue(all.containsKey("foo"));
        assertEquals("bar", all.get("foo"));
    }
}
