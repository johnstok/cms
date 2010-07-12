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
import ccc.api.types.SortOrder;


/**
 * Tests for the {@link EnumTools} class.
 *
 * @author Civic Computing Ltd.
 */
public class EnumToolsTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testLookupEnum() {

        // ARRANGE

        // ACT
        final Enum<?> order =
            new EnumTools().of(SortOrder.class.getName(), "ASC");

        // ASSERT
        assertEquals(SortOrder.ASC, order);
    }

    /**
     * Test.
     */
    public void testLookupMissingEnum() {

        // ARRANGE

        // ACT
        try {
            new EnumTools().of("not.AnEnum", "ASC");
            fail();

        // ASSERT
        } catch (final RuntimeException e) {
            assertTrue(e.getCause() instanceof ClassNotFoundException);
        }
    }
}
