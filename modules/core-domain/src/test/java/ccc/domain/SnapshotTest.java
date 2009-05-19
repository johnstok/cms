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
import java.util.Collection;

import junit.framework.TestCase;
import ccc.api.Decimal;
import ccc.api.Json;
import ccc.api.Jsonable;


/**
 * Tests for the snapshot class.
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
        final Collection<Jsonable> children = new ArrayList<Jsonable>();

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
        final Collection<Jsonable> children = new ArrayList<Jsonable>();
        children.add(
            new Jsonable(){
                @Override public void toJson(final Json json) { /* no op */ }});

        // ACT
        s.set("children", children);

        // ASSERT
        assertEquals("{\"children\":[{}]}", s.getDetail());
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
        s.set("key", new Decimal("10"));

        // ASSERT
        assertEquals("{\"key\":\"10\"}", s.getDetail());
    }

    /**
     * Test.
     */
    public void testConstructFromString() {

        // ARRANGE

        // ACT
        final Snapshot s = new Snapshot("{\"foo\":\"bar\"}");

        // ASSERT
        assertEquals("{\"foo\":\"bar\"}", s.getDetail());
        assertEquals("bar", s.getString("foo"));
    }

    /**
     * Test.
     */
    public void testConstructWithNullStringFails() {

        // ARRANGE

        // ACT
        try {
            new Snapshot(null);

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
