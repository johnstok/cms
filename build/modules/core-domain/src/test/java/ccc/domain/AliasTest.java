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
package ccc.domain;

import junit.framework.TestCase;

import org.json.JSONException;
import org.json.JSONObject;

import ccc.api.ResourceType;


/**
 * Tests for the {@link Alias} class.
 *
 * @author Civic Computing Ltd
 */
public class AliasTest extends TestCase {

    /**
     * Test.
     * @throws JSONException If the JSON is invalid.
     */
    public void testSnapshot() throws JSONException {

        // ARRANGE
        final Page p = new Page("foo");
        final Alias alias = new Alias("bar", p);

        // ACT
        final JSONObject o = new JSONObject(alias.createSnapshot().getDetail());

        // ASSERT
        assertEquals("bar", o.get("title"));
        assertEquals(p.id().toString(), o.get("target"));
    }

    /**
     * Test.
     */
    public void testCreateAliasWithTitle() {

        // ARRANGE
        final Page p = new Page("foo");

        // ACT
        final Alias alias = new Alias("bar", p);

        // ASSERT
        assertEquals(p, alias.target());
        assertEquals("bar", alias.title());
        assertEquals(new ResourceName("bar"), alias.name());
    }

    /**
     * Test.
     */
    public void testTypeReturnsAlias() {

        // ACT
        final ResourceType t = new Alias().type();

        // ASSERT
        assertEquals(ResourceType.ALIAS, t);
    }

    /**
     * Test.
     */
    public void testCreateAliasWithName() {

        // ARRANGE
        final Page p = new Page("foo");

        // ACT
        final Alias alias = new Alias("bar", p);

        // ASSERT
        assertEquals(p, alias.target());
    }

    /**
     * Test.
     */
    public void testNullTargetIsRejected() {

        // ACT
        try {
            new Alias("foo", null);
            fail("Null should be rejected");

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals("Specified value may not be NULL.", e.getMessage());
        }

    }
}
