/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
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
import java.util.List;

import junit.framework.TestCase;
import ccc.domain.sorting.Sorter;
import ccc.types.ResourceOrder;


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
        final List<Resource> resources = new ArrayList<Resource>() {{
            add(new Folder("k"));
            add(new Folder("z"));
            add(new Folder("a"));
        }};

        // ACT
        Sorter.sort(resources, ResourceOrder.NAME_ALPHANUM_ASC);

        // ASSERT
        assertEquals("a", resources.get(0).name().toString());
        assertEquals("k", resources.get(1).name().toString());
        assertEquals("z", resources.get(2).name().toString());
    }

    /**
     * Test.
     */
    public void testManualOrdering() {

        // ARRANGE
        final List<Resource> resources = new ArrayList<Resource>() {{
            add(new Folder("z"));
            add(new Folder("a"));
        }};

        // ACT
        Sorter.sort(resources, ResourceOrder.MANUAL);

        // ASSERT
        assertEquals("z", resources.get(0).name().toString());
    }

    /**
     * Test.
     */
    public void testDateChangedAscOrdering() {

        // ARRANGE
        final List<Resource> resources = new ArrayList<Resource>() {{
            add(new Folder("z"));
            add(new Folder("a"));
        }};

        // ACT
        Sorter.sort(resources, ResourceOrder.DATE_CHANGED_ASC);

        // ASSERT
        assertEquals("z", resources.get(0).name().toString());

    }
    /**
     * Test.
     */
    public void testDateChangedDescOrdering() {

        // ARRANGE
        final List<Resource> resources = new ArrayList<Resource>() {{
            add(new Folder("z"));
            add(new Folder("a"));
        }};

        // ACT
        Sorter.sort(resources, ResourceOrder.DATE_CHANGED_DESC);

        // ASSERT
        assertEquals("a", resources.get(0).name().toString());

    }
}
