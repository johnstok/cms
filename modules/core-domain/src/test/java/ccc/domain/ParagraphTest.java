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

import java.util.Date;

import junit.framework.TestCase;
import ccc.commons.Testing;


/**
 * Tests for the {@link Paragraph} class.
 *
 * @author Civic Computing Ltd
 */
public final class ParagraphTest extends TestCase {

    /**
     * Test.
     */
    public void testCreateSnapshot() {

        // ARRANGE
        final Paragraph p = Paragraph.fromText("foo", "bar");

        // ACT
        final Snapshot s = p.createSnapshot();

        // ASSERT
        assertEquals("foo", s.getString("name"));
        assertEquals("TEXT", s.getString("type"));
        assertEquals("bar", s.getString("text"));
    }

    /**
     * Test.
     */
    public void testFromSnapshot() {

        // ARRANGE
        final Snapshot s = new Snapshot();
        s.set("name", "bar");
        s.set("type", "TEXT");
        s.set("text", "foo");
        s.set("bool", true);
        s.set("date", new Date());

        // ACT
        final Paragraph p = Paragraph.fromSnapshot(s);

        // ASSERT
        assertEquals("bar", p.name());
        assertEquals(Paragraph.Type.TEXT, p.type());
        assertEquals("foo", p.text());
        assertNull(p.date());
        assertNull(p.bool());
    }

    /**
     * Test.
     */
    public void testMaxNameLengthIs256() {

        // ARRANGE

        // ACT
        try {
            Paragraph.fromText(
                Testing.dummyString('a', Paragraph.MAX_NAME_LENGTH), "foo");
            fail();

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals(
                "Specified string exceeds max length of 256.", e.getMessage());
        }
    }

    /**
     * Test.
     */
    public void testNameCannotBeEmpty() {

        // ARRANGE

        // ACT
        try {
            Paragraph.fromText(" ", "foo");
            fail();

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals(
                "Specified string must have length > 0.", e.getMessage());
        }

    }

    /**
     * Test.
     */
    public void testTextConstructor() {

        // ACT
        final Paragraph p = Paragraph.fromText("foo", "Hello world");

        // ASSERT
        assertEquals(Paragraph.Type.TEXT, p.type());
        assertEquals("Hello world", p.text());
        assertEquals("foo", p.name());
    }

    /**
     * Test.
     */
    public void testBooleanConstructor() {

        // ACT
        final Paragraph p = Paragraph.fromBoolean("foo", Boolean.TRUE);

        // ASSERT
        assertEquals(Paragraph.Type.BOOLEAN, p.type());
        assertEquals(Boolean.TRUE, p.bool());
        assertEquals("foo", p.name());
    }

    /**
     * Test.
     */
    public void testDateConstructor() {

        // ARRANGE
        final Date now = new Date();

        // ACT
        final Paragraph p = Paragraph.fromDate("foo", now);

        // ASSERT
        assertEquals(Paragraph.Type.DATE, p.type());
        assertEquals(now, p.date());
        assertEquals("foo", p.name());
    }

    /**
     * Test.
     */
    public void testTextConstructorRejectsNull() {

        // ACT
        try {
            Paragraph.fromText("foo", null);
            fail("NULL should be rejected.");

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals("Specified value may not be NULL.", e.getMessage());
        }
    }

    /**
     * Test.
     */
    public void testDateConstructorRejectsNull() {

        // ACT
        try {
            Paragraph.fromDate("foo", null);
            fail("NULL should be rejected.");

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals("Specified value may not be NULL.", e.getMessage());
        }
    }

    /**
     * Test.
     */
    public void testBooleanConstructorRejectsNull() {

        // ACT
        try {
            Paragraph.fromBoolean("foo", null);
            fail("NULL should be rejected.");

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals("Specified value may not be NULL.", e.getMessage());
        }
    }

    /**
     * Test.
     */
    public void testEqualsOnlyReliesOnName() {

        // ARRANGE
        final Paragraph t = Paragraph.fromText("foo", "Hello world");
        final Paragraph b = Paragraph.fromBoolean("foo", Boolean.TRUE);

        // ASSERT
        assertEquals(t, b);
        assertEquals(b, t);

    }
}
