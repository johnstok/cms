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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import junit.framework.TestCase;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class SnapshotTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testAddEmptyCollection() {

        // ARRANGE
        final Snapshot s = new Snapshot();
        final Collection<Snapshot> children = new ArrayList<Snapshot>();

        // ACT
        s.set("children", children);

        // ASSERT
        assertEquals("{\"children\":[]}", s.getDetail());
    }

    /**
     * Test.
     */
    public void testAddCollection() {

        // ARRANGE
        final Snapshot s = new Snapshot();
        final Collection<Snapshot> children = new ArrayList<Snapshot>();
        children.add(new Snapshot());
        children.add(new Snapshot());

        // ACT
        s.set("children", children);

        // ASSERT
        assertEquals("{\"children\":[{},{}]}", s.getDetail());
    }

    /**
     * Test.
     */
    public void testAddString() {

        // ARRANGE
        final Snapshot s = new Snapshot();

        // ACT
        s.set("key", "value");

        // ASSERT
        assertEquals("{\"key\":\"value\"}", s.getDetail());
    }

    /**
     * Test.
     */
    public void testAddBigDecimal() {

        // ARRANGE
        final Snapshot s = new Snapshot();

        // ACT
        s.set("key", BigDecimal.TEN);

        // ASSERT
        assertEquals("{\"key\":\"10\"}", s.getDetail());
    }

    /**
     * Test.
     */
    public void testDetailProperty() {

        // ARRANGE
        final Snapshot s = new Snapshot();

        // ACT
        s.setDetail("{\"foo\":\"bar\"}");

        // ASSERT
        assertEquals("{\"foo\":\"bar\"}", s.getDetail());
    }

    /**
     * Test.
     */
    public void testDetailMutatorRejectsNull() {

        // ARRANGE
        final Snapshot s = new Snapshot();

        // ACT
        try {
            s.setDetail(null);

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals("Specified value may not be NULL.", e.getMessage());
        }
    }

    /**
     * Test.
     */
    public void testConstructor() {

        // ARRANGE

        // ACT
        final Snapshot s = new Snapshot();

        // ASSERT
        assertEquals("{}", s.getDetail());
    }
}
