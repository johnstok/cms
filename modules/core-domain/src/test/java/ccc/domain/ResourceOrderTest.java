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


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceOrderTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testname() {

        // ARRANGE
        final List<Resource> resources = new ArrayList<Resource>() {{
            add(new DummyResource("k"));
            add(new DummyResource("z"));
            add(new DummyResource("a"));
        }};

        // ACT
        ResourceOrder.NAME_ALPHANUM_ASC.sort(resources);

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
            add(new DummyResource("z"));
            add(new DummyResource("a"));
        }};

        // ACT
        ResourceOrder.MANUAL.sort(resources);

        // ASSERT
        assertEquals("z", resources.get(0).name().toString());
    }

    /**
     * Test.
     */
    public void testDateChangedAscOrdering() {

        // ARRANGE
        final List<Resource> resources = new ArrayList<Resource>() {{
            add(new DummyResource("z"));
            add(new DummyResource("a"));
        }};

        // ACT
        ResourceOrder.DATE_CHANGED_ASC.sort(resources);

        // ASSERT
        assertEquals("z", resources.get(0).name().toString());

    }
    /**
     * Test.
     */
    public void testDateChangedDescOrdering() {

        // ARRANGE
        final List<Resource> resources = new ArrayList<Resource>() {{
            add(new DummyResource("z"));
            add(new DummyResource("a"));
        }};

        // ACT
        ResourceOrder.DATE_CHANGED_DESC.sort(resources);

        // ASSERT
        assertEquals("a", resources.get(0).name().toString());

    }
}
