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
package ccc.types;

import static org.easymock.EasyMock.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;
import ccc.serialization.Json;


/**
 * Tests for the {@link Paragraph} class.
 *
 * @author Civic Computing Ltd
 */
public final class ParagraphTest extends TestCase {


    /**
     * Test.
     */
    public void testListConstructor() {

        // ARRANGE
        final List<String> strings = new ArrayList<String>();
        strings.add("one");
        strings.add("two");
        strings.add("three");

        // ACT
        final Paragraph p = Paragraph.fromList("foo", strings);

        // ASSERT
        assertEquals("one,two,three", p.text());
        assertEquals(strings, p.list());
        assertEquals(ParagraphType.LIST, p.type());
    }


    /**
     * Test.
     */
    public void testCreateSnapshot() {

        // ARRANGE
        final Paragraph p = Paragraph.fromText("foo", "bar");

        _json.set("name", "foo");
        _json.set("type", "TEXT");
        _json.set("text", "bar");
        _json.set("bool", (Boolean) null);
        _json.set("date", (Date) null);
        _json.set("number", (BigDecimal) null);
        replay(_json);

        // ACT
        p.toJson(_json);

        // ASSERT
        verify(_json);
    }

    /**
     * Test.
     */
    public void testCreateNumberSnapshot() {

        // ARRANGE
        final Paragraph p =
            Paragraph.fromNumber("foo", new BigDecimal("123.456"));

        _json.set("name", "foo");
        _json.set("type", "NUMBER");
        _json.set("text", (String) null);
        _json.set("bool", (Boolean) null);
        _json.set("date", (Date) null);
        _json.set(eq("number"), isA(BigDecimal.class));
        replay(_json);

        // ACT
        p.toJson(_json);

        // ASSERT
        verify(_json);
    }

    /**
     * Test.
     */
    public void testFromSnapshot() {

        // ARRANGE
        expect(_json.getString("name")).andReturn("bar");
        expect(_json.getString("type")).andReturn("TEXT");
        expect(_json.getString("text")).andReturn("foo");
        replay(_json);

        // ACT
        final Paragraph p = Paragraph.fromSnapshot(_json);

        // ASSERT
        verify(_json);
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
        expect(_json.getString("name")).andReturn("bar");
        expect(_json.getString("type")).andReturn("NUMBER");
        expect(_json.getBigDecimal("number"))
            .andReturn(new BigDecimal("123.456"));
        replay(_json);

        // ACT
        final Paragraph p = Paragraph.fromSnapshot(_json);

        // ASSERT
        verify(_json);
        assertEquals("bar", p.name());
        assertEquals(ParagraphType.NUMBER, p.type());
        assertEquals("123.456", p.number().toString());
        assertNull(p.date());
        assertNull(p.bool());
        assertNull(p.text());
    }

    /**
     * Test.
     */
    public void testMaxNameLengthIs256() { // TODO

        // ARRANGE

//        // ACT
//        try {
//            Paragraph.fromText(
//                Testing.dummyString('a', Paragraph.MAX_NAME_LENGTH+1), "foo");
//            fail();
//
//        // ASSERT
//        } catch (final IllegalArgumentException e) {
//            assertEquals(
//                "Specified string exceeds max length of 256.",
//                e.getMessage());
//        }
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
        assertEquals("0", p.number().toString());
        assertEquals("foo", p.name());
    }

    /**
     * Test.
     */
    public void testFloatConstructor() {

        // ACT
        final Paragraph p = Paragraph.fromNumber("foo", 0.1d);

        // ASSERT
        assertEquals(ParagraphType.NUMBER, p.type());
        assertEquals(String.valueOf(0.1d), p.number().toString());
        assertEquals("foo", p.name());
    }

    /**
     * Test.
     */
    public void testBigDecimalConstructor() {

        // ACT
        final BigDecimal bd = new BigDecimal("-1234.54");
        final Paragraph p = Paragraph.fromNumber("foo", bd);

        // ASSERT
        assertEquals(ParagraphType.NUMBER, p.type());
        assertEquals("-1234.54", p.number().toString());
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
    public void testTextConstructorHandlesNull() {

        // ACT
        final Paragraph p = Paragraph.fromText("foo", null);

        // ASSERT
        assertEquals("foo", p.name());
        assertEquals("", p.text());
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

    /** {@inheritDoc} */
    @Override
    protected void setUp() {
        _json = createStrictMock(Json.class);
    }
    /** {@inheritDoc} */
    @Override
    protected void tearDown() {
        _json = null;
    }

    private Json _json;
}
