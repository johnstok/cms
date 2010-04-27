/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;
import ccc.api.types.ResourceOrder;
import ccc.domain.sorting.Sorter;


/**
 * Tests for the resource ordering.
 *
 * @author Civic Computing Ltd.
 */
public class SorterTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testname() {

        // ARRANGE
        final List<ResourceEntity> resources = new ArrayList<ResourceEntity>() {{
            add(new FolderEntity("k"));
            add(new FolderEntity("z"));
            add(new FolderEntity("a"));
        }};

        // ACT
        Sorter.sort(resources, ResourceOrder.NAME_ALPHANUM_ASC);

        // ASSERT
        assertEquals("a", resources.get(0).getName().toString());
        assertEquals("k", resources.get(1).getName().toString());
        assertEquals("z", resources.get(2).getName().toString());
    }

    /**
     * Test.
     */
    public void testManualOrdering() {

        // ARRANGE
        final ResourceEntity z = new FolderEntity("z");
        z.setIndexPosition(0);
        final ResourceEntity a = new FolderEntity("a");
        a.setIndexPosition(1);
        final List<ResourceEntity> resources = Arrays.asList(a, z);

        // ACT
        Sorter.sort(resources, ResourceOrder.MANUAL);

        // ASSERT
        assertEquals("z", resources.get(0).getName().toString());
    }

    /**
     * Test.
     */
    public void testDateChangedAscOrdering() {

        // ARRANGE
        final List<ResourceEntity> resources = new ArrayList<ResourceEntity>() {{
            add(new FolderEntity("z"));
            add(new FolderEntity("a"));
        }};

        // ACT
        Sorter.sort(resources, ResourceOrder.DATE_CHANGED_ASC);

        // ASSERT
        assertEquals("z", resources.get(0).getName().toString());

    }
    /**
     * Test.
     */
    public void testDateChangedDescOrdering() {

        // ARRANGE
        final List<ResourceEntity> resources = new ArrayList<ResourceEntity>() {{
            add(new FolderEntity("z"));
            add(new FolderEntity("a"));
        }};

        // ACT
        Sorter.sort(resources, ResourceOrder.DATE_CHANGED_DESC);

        // ASSERT
        assertEquals("a", resources.get(0).getName().toString());

    }
}
