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

import static ccc.commons.Exceptions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import junit.framework.TestCase;
import ccc.serialization.InvalidSnapshotException;
import ccc.serialization.Json;
import ccc.serialization.JsonImpl;
import ccc.serialization.Jsonable;


/**
 * Tests for the {@link JsonImpl} class.
 *
 * @author Civic Computing Ltd.
 */
public class JsonImplTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testGetDate() {

        // ARRANGE
        final Date d = new Date();
        final JsonImpl s = new JsonImpl("{\"d\"="+d.getTime()+"}");

        // ACT
        final Date i = s.getDate("d");

        // ASSERT
        assertEquals(d, i);
    }

    /**
     * Test.
     */
    public void testGetSmallDate() {

        // ARRANGE
        final JsonImpl s = new JsonImpl("{\"d\"=3}");

        // ACT
        final Date i = s.getDate("d");

        // ASSERT
        assertEquals(new Date(3), i);
    }

    /**
     * Test.
     */
    public void testGetBoolean() {

        // ARRANGE
        final JsonImpl s = new JsonImpl("{\"bool\"=true}");

        // ACT
        final Boolean b = s.getBool("bool");

        // ASSERT
        assertTrue(b.booleanValue());
    }

    /**
     * Test.
     */
    public void testGetInteger() {

        // ARRANGE
        final JsonImpl s = new JsonImpl("{\"num\"=3}");

        // ACT
        final Integer i = s.getInt("num");

        // ASSERT
        assertEquals(Integer.valueOf(3), i);
    }

    /**
     * Test.
     */
    public void testMutatorsAddKeyForNullValue() {

        // ARRANGE
        final JsonImpl s = new JsonImpl();

        // ACT
        s.set("null", (String) null);

        // ASSERT
        assertNull(s.getString("null"));
        assertNull(s.getCollection("null"));
        assertNull(s.getInt("null"));
        assertNull(s.getBool("null"));
        assertNull(s.getDate("null"));
        assertNull(s.getBigDecimal("null"));
        assertNull(s.getId("null"));
        assertNull(s.getJson("null"));
    }

    /**
     * Test.
     */
    public void testMissingKeyGivesException() {

        // ARRANGE
        final JsonImpl s = new JsonImpl();


        // ACT
        try {
            s.getString("missing");
            fail();

        // ASSERT
        } catch (final InvalidSnapshotException e) { swallow(e); }


        // ACT
        try {
            s.getBool("missing");
            fail();

        // ASSERT
        } catch (final InvalidSnapshotException e) { swallow(e); }


        // ACT
        try {
            s.getCollection("missing");
            fail();

        // ASSERT
        } catch (final InvalidSnapshotException e) { swallow(e); }


        // ACT
        try {
            s.getInt("missing");
            fail();

        // ASSERT
        } catch (final InvalidSnapshotException e) { swallow(e); }


        // ACT
        try {
            s.getId("missing");
            fail();

        // ASSERT
        } catch (final InvalidSnapshotException e) { swallow(e); }


        // ACT
        try {
            s.getBigDecimal("missing");
            fail();

        // ASSERT
        } catch (final InvalidSnapshotException e) { swallow(e); }


        // ACT
        try {
            s.getDate("missing");
            fail();

        // ASSERT
        } catch (final InvalidSnapshotException e) { swallow(e); }


        // ACT
        try {
            s.getJson("missing");
            fail();

        // ASSERT
        } catch (final InvalidSnapshotException e) { swallow(e); }
    }

    /**
     * Test.
     */
    public void testAddEmptyCollection() {

        // ARRANGE
        final JsonImpl s = new JsonImpl();
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
        final JsonImpl s = new JsonImpl();
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
        final JsonImpl s = new JsonImpl();

        // ACT
        s.set("key", "value");

        // ASSERT
        assertEquals("{\"key\":\"value\"}", s.getDetail());
    }

    /**
     * Test.
     */
    public void testAddDecimal() {

        // ARRANGE
        final JsonImpl s = new JsonImpl();

        // ACT
        s.set("key", new BigDecimal("10"));

        // ASSERT
        assertEquals("{\"key\":\"10\"}", s.getDetail());
    }

    /**
     * Test.
     */
    public void testConstructFromString() {

        // ARRANGE

        // ACT
        final JsonImpl s = new JsonImpl("{\"foo\":\"bar\"}");

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
            new JsonImpl((String) null);

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
        final JsonImpl s = new JsonImpl();

        // ASSERT
        assertEquals("{}", s.getDetail());
    }
}
