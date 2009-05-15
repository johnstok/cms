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
package ccc.api;

import ccc.api.ParamList;
import junit.framework.TestCase;


/**
 * Tests for the {@link ParamList} class.
 *
 * @author Civic Computing Ltd.
 */
public class ParamListTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testCanParseNullCononicalForm() {

        // ARRANGE

        // ACT
        final ParamList pl = new ParamList(null);

        // ASSERT
        assertEquals(0, pl.size());
    }

    /**
     * Test.
     */
    public void testCanParseZlsCononicalForm() {

        // ARRANGE

        // ACT
        final ParamList pl = new ParamList("");

        // ASSERT
        assertEquals(0, pl.size());
    }

    /**
     * Test.
     */
    public void testCanParseSingleNewlineCononicalForm() {

        // ARRANGE

        // ACT
        final ParamList pl = new ParamList("\n");

        // ASSERT
        assertEquals(0, pl.size());
    }

    /**
     * Test.
     */
    public void testMutatorSetsToTheCanonicalForm() {

        // ARRANGE
        final ParamList pl =
            new ParamList()
            .set("mmm", "baz");

        // ACT
        pl.setCanonicalForm("aaa=bar\nzzz=foo\n");

        // ASSERT
        assertEquals("bar", pl.get("aaa"));
        assertEquals("foo", pl.get("zzz"));
        assertNull(pl.get("mmm"));
    }

    /**
     * Test.
     */
    public void testAccessorProvidesCanonicalForm() {

        // ARRANGE
        final ParamList pl =
            new ParamList()
            .set("zzz", "foo")
            .set("aaa", "bar");

        // ACT
        final String canonicalForm = pl.getCanonicalForm();

        // ASSERT
        assertEquals("aaa=bar\nzzz=foo\n", canonicalForm);
    }

    /**
     * Test.
     */
    public void testTheStringConstructorParsesTheCanonicalForm() {

        // ARRANGE
        final String cForm = "aaa=bar\nzzz=foo\n";

        // ACT
        final ParamList pl = new ParamList(cForm);

        // ASSERT
        assertEquals("bar", pl.get("aaa"));
        assertEquals("foo", pl.get("zzz"));

    }

    /**
     * Test.
     */
    public void testTheTostringMethodGeneratesTheCanonicalForm() {

        // ARRANGE
        final ParamList pl =
            new ParamList()
            .set("zzz", "foo")
            .set("aaa", "bar");

        // ACT
        final String canonicalForm = pl.toString();

        // ASSERT
        assertEquals("aaa=bar\nzzz=foo\n", canonicalForm);
    }

    /**
     * Test.
     */
    public void testAddStringParam() {

        // ARRANGE
        final ParamList pl = new ParamList();

        // ACT
        pl.set("foo", "bar");

        // ASSERT
        assertEquals("bar", pl.get("foo"));
    }

    /**
     * Test.
     */
    public void testNullIsReturnedForMissingKeys() {

        // ARRANGE

        // ACT

        // ASSERT
        assertNull(new ParamList().get("missing key"));
    }

    /**
     * Test.
     */
    public void testEqualsCharIsDisallowedForKeys() {

        // ARRANGE
        final ParamList pl = new ParamList();

        // ACT
        try {
            pl.set("=", "bar");
            fail();

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals("The char '=' is not allowed", e.getMessage());
        }
    }

    /**
     * Test.
     */
    public void testEqualsCharIsDisallowedForValues() {

        // ARRANGE
        final ParamList pl = new ParamList();

        // ACT
        try {
            pl.set("foo", "=");
            fail();

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals("The char '=' is not allowed", e.getMessage());
        }
    }

    /**
     * Test.
     */
    public void testNewlineCharIsDisallowedForKeys() {

        // ARRANGE
        final ParamList pl = new ParamList();

        // ACT
        try {
            pl.set("\n", "bar");
            fail();

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals("The char '\\n' is not allowed", e.getMessage());
        }
    }

    /**
     * Test.
     */
    public void testNewlineCharIsDisallowedForValues() {

        // ARRANGE
        final ParamList pl = new ParamList();

        // ACT
        try {
            pl.set("foo", "\n");
            fail();

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals("The char '\\n' is not allowed", e.getMessage());
        }
    }
}
