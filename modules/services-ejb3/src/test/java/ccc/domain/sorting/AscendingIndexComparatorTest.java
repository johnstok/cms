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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;
import ccc.domain.FolderEntity;
import ccc.domain.ResourceEntity;


/**
 * Tests for the {@link AscendingIndexComparator} class.
 *
 * @author Civic Computing Ltd.
 */
public class AscendingIndexComparatorTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testSimpleSort() {

        // ARRANGE
        final FolderEntity a = new FolderEntity("a");
        a.setIndexPosition(0);
        final FolderEntity b = new FolderEntity("b");
        b.setIndexPosition(1);
        final FolderEntity c = new FolderEntity("c");
        c.setIndexPosition(2);

        final List<ResourceEntity> folders = new ArrayList<ResourceEntity>();
        folders.add(c);
        folders.add(b);
        folders.add(a);

        // ACT
        Collections.sort(folders, new AscendingIndexComparator());

        // ASSERT
        assertEquals(Arrays.asList(new ResourceEntity[] {a, b, c}), folders);
    }

    /**
     * Test.
     */
    public void testHigherIndexesAreNegative() {

        // ARRANGE
        final FolderEntity a = new FolderEntity("a");
        a.setIndexPosition(0);
        final FolderEntity b = new FolderEntity("b");
        b.setIndexPosition(1);

        // ACT
        final int comparison = new AscendingIndexComparator().compare(a, b);

        // ASSERT
        assertEquals(-1, comparison);
    }


    /**
     * Test.
     */
    public void testLowerIndexesArePositive() {

        // ARRANGE
        final FolderEntity a = new FolderEntity("a");
        a.setIndexPosition(1);
        final FolderEntity b = new FolderEntity("b");
        b.setIndexPosition(0);

        // ACT
        final int comparison = new AscendingIndexComparator().compare(a, b);

        // ASSERT
        assertEquals(1, comparison);
    }


    /**
     * Test.
     */
    public void testEqualObjectsAreComparable() {

        // ARRANGE
        final FolderEntity a = new FolderEntity("a");
        a.setIndexPosition(0);
        final FolderEntity b = new FolderEntity("b");
        b.setIndexPosition(0);

        // ACT
        final int comparison = new AscendingIndexComparator().compare(a, b);

        // ASSERT
        assertEquals(0, comparison);
    }


    /**
     * Test.
     */
    public void testMissingFirstIndexIsHandled() {

        // ARRANGE
        final FolderEntity a = new FolderEntity("a");
        final FolderEntity b = new FolderEntity("b");
        b.setIndexPosition(0);

        // ACT
        final int comparison = new AscendingIndexComparator().compare(a, b);

        // ASSERT
        assertEquals(-1, comparison);
    }


    /**
     * Test.
     */
    public void testMissingSecondIndexIsHandled() {

        // ARRANGE
        final FolderEntity a = new FolderEntity("a");
        a.setIndexPosition(0);
        final FolderEntity b = new FolderEntity("b");

        // ACT
        final int comparison = new AscendingIndexComparator().compare(a, b);

        // ASSERT
        assertEquals(1, comparison);
    }


    /**
     * Test.
     */
    public void testMissingFirstEntityIsHandled() {

        // ARRANGE
        final FolderEntity b = new FolderEntity("b");
        b.setIndexPosition(0);

        // ACT
        final int comparison = new AscendingIndexComparator().compare(null, b);

        // ASSERT
        assertEquals(-1, comparison);
    }


    /**
     * Test.
     */
    public void testMissingSecondEntityIsHandled() {

        // ARRANGE
        final FolderEntity a = new FolderEntity("a");
        a.setIndexPosition(0);

        // ACT
        final int comparison = new AscendingIndexComparator().compare(a, null);

        // ASSERT
        assertEquals(1, comparison);
    }
}
