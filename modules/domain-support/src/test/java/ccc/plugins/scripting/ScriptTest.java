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

import junit.framework.TestCase;


/**
 * Tests for the {@link Script} class.
 *
 * @author Civic Computing Ltd.
 */
public class ScriptTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testGettingSetting() {

        // ARRANGE

        // ACT
        final Script s = new Script("foo", "bar");

        // ASSERT
        assertEquals("foo", s.getBody());
        assertEquals("bar", s.getTitle());
    }
}
