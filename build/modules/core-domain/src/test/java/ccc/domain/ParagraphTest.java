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

import java.math.BigDecimal;
import java.util.Date;

import junit.framework.TestCase;
import ccc.commons.Testing;
import ccc.services.api.ParagraphType;


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
    public void testCreateNumberSnapshot() {

        // ARRANGE
        final Paragraph p =
            Paragraph.fromNumber("foo", new BigDecimal("123.456"));

        // ACT
        final Snapshot s = p.createSnapshot();

        // ASSERT
        assertEquals("foo", s.getString("name"));
        assertEquals("NUMBER", s.getString("type"));
        assertEquals(new BigDecimal("123.456"), s.getBigDecimal("number"));
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
        s.set("bool", Boolean.TRUE);
        s.set("date", new Date());
        s.set("number", new BigDecimal("123.456"));

        // ACT
        final Paragraph p = Paragraph.fromSnapshot(s);

        // ASSERT
        assertEquals("bar", p.name());
        assertEquals(ParagraphType.TEXT, p.type());
        assertEquals("foo", p.text());
        assertNull(p.date());
        assertNull(p.bool());
        assertNull(p.number());
    }

    /**
     * Test.
     */
    public void testFromNumberSnapshot() {

        // ARRANGE
        final Snapshot s = new Snapshot();
        s.set("name", "bar");
        s.set("type", "NUMBER");
        s.set("text", "foo");
        s.set("bool", Boolean.TRUE);
        s.set("date", new Date());
        s.set("number", new BigDecimal("123.456"));

        // ACT
        final Paragraph p = Paragraph.fromSnapshot(s);

        // ASSERT
        assertEquals("bar", p.name());
        assertEquals(ParagraphType.NUMBER, p.type());
        assertEquals(new BigDecimal("123.456"), p.number());
        assertNull(p.date());
        assertNull(p.bool());
        assertNull(p.text());
    }

    /**
     * Test.
     */
    public void testMaxNameLengthIs256() {

        // ARRANGE

        // ACT
        try {
            Paragraph.fromText(
                Testing.dummyString('a', Paragraph.MAX_NAME_LENGTH+1), "foo");
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
        assertEquals(ParagraphType.TEXT, p.type());
        assertEquals("Hello world", p.text());
        assertEquals("foo", p.name());
    }

    /**
     * Test.
     */
    public void testLongConstructor() {

        // ACT
        final Paragraph p = Paragraph.fromNumber("foo", 0);

        // ASSERT
        assertEquals(ParagraphType.NUMBER, p.type());
        assertEquals(BigDecimal.valueOf(0), p.number());
        assertEquals("foo", p.name());
    }

    /**
     * Test.
     */
    public void testFloatConstructor() {

        // ACT
        final Paragraph p = Paragraph.fromNumber("foo", 0.1f);

        // ASSERT
        assertEquals(ParagraphType.NUMBER, p.type());
        assertEquals(BigDecimal.valueOf(0.1f), p.number());
        assertEquals("foo", p.name());
    }

    /**
     * Test.
     */
    public void testBigDecimalConstructor() {

        // ACT
        final BigDecimal bd = new BigDecimal("-1234.5400390625");
        final Paragraph p = Paragraph.fromNumber("foo", bd);

        // ASSERT
        assertEquals(ParagraphType.NUMBER, p.type());
        assertEquals(BigDecimal.valueOf(-1234.54f), p.number());
        assertEquals("foo", p.name());
    }

    /**
     * Test.
     */
    public void testBooleanConstructor() {

        // ACT
        final Paragraph p = Paragraph.fromBoolean("foo", Boolean.TRUE);

        // ASSERT
        assertEquals(ParagraphType.BOOLEAN, p.type());
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
        assertEquals(ParagraphType.DATE, p.type());
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
