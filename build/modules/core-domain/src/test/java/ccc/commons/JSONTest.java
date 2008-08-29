/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */

package ccc.commons;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;


/**
 * Tests for the {@link JSON} class.
 *
 * @author Civic Computing Ltd
 */
public final class JSONTest extends TestCase {

    /**
     * Test.
     * TODO Add MUCH more depth to this test.
     */
    public void testStringEscaping() {

        // ACT
        final String json =
            JSON.object()
                .add("bad-chars", "\\")
                .toString();

        // ASSERT
        assertEquals("{\"bad-chars\": \"\\\\\"}", json);
    }

    /**
     * Test.
     */
    public void testEmptyObject() {

        // ACT
        final String json = JSON.object().toString();

        // ASSERT
        assertEquals("{}", json);
    }

    /**
     * Test.
     */
    public void testAddString() {

        // ACT
        final JSON.Object json = JSON.object();
        json.add("key", "value");

        // ASSERT
        assertEquals("{\"key\": \"value\"}", json.toString());
    }

    /**
     * Test.
     */
    public void testAddList() {

        // ARRANGE
        final List<JSONable> elements = new ArrayList<JSONable>();
        elements.add(new JSONable() {
            @Override
            public String toJSON() { return "{}"; }
        });

        // ACT
        final JSON.Object json = JSON.object();
        json.add("key", elements);

        // ASSERT
        assertEquals("{\"key\": [{}]}", json.toString());
    }
}
