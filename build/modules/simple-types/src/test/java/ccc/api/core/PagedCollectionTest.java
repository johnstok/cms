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
package ccc.api.core;

import java.util.Arrays;

import junit.framework.TestCase;


/**
 * Tests for the {@link PagedCollection} class.
 *
 * @author Civic Computing Ltd.
 */
public class PagedCollectionTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testEmptyCollection() {

        // ARRANGE

        // ACT
        final PagedCollection<Object> empty =
            new PagedCollection<Object>(Object.class);

        // ASSERT
        assertEquals(0, empty.getTotalCount());
        assertEquals(Object.class, empty.getElementClass());
        assertEquals(0, empty.getElements().size());
    }


    /**
     * Test.
     */
    public void testSupportsIterableInterface() {

        // ARRANGE
        int i = 0;
        final Integer[] elements = {
            Integer.valueOf(0),
            Integer.valueOf(1),
            Integer.valueOf(2),
            Integer.valueOf(3),
            Integer.valueOf(4)
        };

        // ACT
        final Iterable<Integer> integers =
            new PagedCollection<Integer>(
                5, Integer.class, Arrays.asList(elements));

        // ASSERT
        for (final Integer integer: integers) {
            assertEquals(elements[i], integer);
            i++;
        }
    }
}
