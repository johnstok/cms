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
package ccc.domain.sorting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;
import ccc.domain.Folder;
import ccc.domain.Resource;


/**
 * Tests for the {@link AlphaNumericAscendingNameComparator} class.
 *
 * @author Civic Computing Ltd.
 */
public class AlphaNumericAscendingNameComparatorTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testCaseSensitiveSort() {

        // ARRANGE
        final List<Resource> unsorted = new ArrayList<Resource>();
        unsorted.add(new Folder("mmm"));
        unsorted.add(new Folder("zzz"));
        unsorted.add(new Folder("aaa"));

        // ACT
        Collections.sort(unsorted, new AlphaNumericAscendingNameComparator());

        // ASSERT
        assertEquals("aaa", unsorted.get(0).getName().toString());
        assertEquals("mmm", unsorted.get(1).getName().toString());
        assertEquals("zzz", unsorted.get(2).getName().toString());
    }

    /**
     * Test.
     */
    public void testCaseInsensitiveSort() {

        // ARRANGE
        final List<Resource> unsorted = new ArrayList<Resource>();
        unsorted.add(new Folder("zzz"));
        unsorted.add(new Folder("ZZZ"));
        unsorted.add(new Folder("aaa"));
        unsorted.add(new Folder("AAA"));

        // ACT
        Collections.sort(
            unsorted, new AlphaNumericAscendingNameComparator(false));

        // ASSERT
        assertEquals("aaa", unsorted.get(0).getName().toString());
        assertEquals("AAA", unsorted.get(1).getName().toString());
        assertEquals("zzz", unsorted.get(2).getName().toString());
        assertEquals("ZZZ", unsorted.get(3).getName().toString());
    }
}
