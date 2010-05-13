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
package ccc.api.types;

import static org.easymock.EasyMock.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;
import ccc.api.temp.ParagraphSerializer;
import ccc.plugins.s11n.Json;


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
        assertEquals("one,two,three", p.getText());
        assertEquals(strings, p.getList());
        assertEquals(ParagraphType.LIST, p.getType());
    }

    /**
     * Test.
     */
    public void testListCondtructorWithComma() {
        // ARRANGE
        final List<String> strings = new ArrayList<String>();
        strings.add("one");
        strings.add(",");

        try {
            Paragraph.fromList("foo", strings);
            fail("List cannot contain a comma");

        } catch (final Exception e) {
            assertEquals("The ',' char is not allowed.", e.getMessage());
        }
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
        _json.set("boolean", (Boolean) null);
        _json.set("date", (Date) null);
        replay(_json);

        // ACT
        new ParagraphSerializer().write(_json, p);

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
        _json.set("text", "123.456");
        _json.set("boolean", (Boolean) null);
        _json.set("date", (Date) null);
        replay(_json);

        // ACT
        new ParagraphSerializer().write(_json, p);

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
        final Paragraph p = new ParagraphSerializer().read(_json);

        // ASSERT
        verify(_json);
        assertEquals("bar", p.getName());
        assertEquals(ParagraphType.TEXT, p.getType());
        assertEquals("foo", p.getText());
        assertNull(p.getDate());
        assertNull(p.getBoolean());
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
        final Paragraph p = new ParagraphSerializer().read(_json);

        // ASSERT
        verify(_json);
        assertEquals("bar", p.getName());
        assertEquals(ParagraphType.NUMBER, p.getType());
        assertEquals(new BigDecimal("123.456"), p.getNumber());
        assertEquals("123.456", p.getText());
        assertNull(p.getDate());
        assertNull(p.getBoolean());
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
        assertEquals(ParagraphType.TEXT, p.getType());
        assertEquals("Hello world", p.getText());
        assertEquals("foo", p.getName());
    }

    /**
     * Test.
     */
    public void testLongConstructor() {

        // ACT
        final Paragraph p = Paragraph.fromNumber("foo", 0);

        // ASSERT
        assertEquals(ParagraphType.NUMBER, p.getType());
        assertEquals("0", p.getNumber().toString());
        assertEquals("foo", p.getName());
    }

    /**
     * Test.
     */
    public void testFloatConstructor() {

        // ACT
        final Paragraph p = Paragraph.fromNumber("foo", 0.1d);

        // ASSERT
        assertEquals(ParagraphType.NUMBER, p.getType());
        assertEquals(String.valueOf(0.1d), p.getNumber().toString());
        assertEquals("foo", p.getName());
    }

    /**
     * Test.
     */
    public void testBigDecimalConstructor() {

        // ACT
        final BigDecimal bd = new BigDecimal("-1234.54");
        final Paragraph p = Paragraph.fromNumber("foo", bd);

        // ASSERT
        assertEquals(ParagraphType.NUMBER, p.getType());
        assertEquals("-1234.54", p.getNumber().toString());
        assertEquals("foo", p.getName());
    }

    /**
     * Test.
     */
    public void testBooleanConstructor() {

        // ACT
        final Paragraph p = Paragraph.fromBoolean("foo", Boolean.TRUE);

        // ASSERT
        assertEquals(ParagraphType.BOOLEAN, p.getType());
        assertEquals(Boolean.TRUE, p.getBoolean());
        assertEquals("foo", p.getName());
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
        assertEquals(ParagraphType.DATE, p.getType());
        assertEquals(now, p.getDate());
        assertEquals("foo", p.getName());
    }

    /**
     * Test.
     */
    public void testTextConstructorHandlesNull() {

        // ACT
        final Paragraph p = Paragraph.fromText("foo", null);

        // ASSERT
        assertEquals("foo", p.getName());
        assertEquals("", p.getText());
    }

    /**
     * Test.
     */
    public void testParagraphConstructorBooleanTypeJSON() {

        final Paragraph p = Paragraph.fromText("foo", "bar");

        _json.set("name", "foo");
        _json.set("type", "TEXT");
        _json.set("text", "bar");
        _json.set("boolean", (Boolean) null);
        _json.set("date", (Date) null);
        expect(_json.getString("name")).andReturn("bar");
        expect(_json.getString("type")).andReturn("BOOLEAN");
        expect(_json.getBool("boolean")).andReturn(true);
        replay(_json);
        new ParagraphSerializer().write(_json, p);


        final Paragraph p2 = new ParagraphSerializer().read(_json);
        assertNull(p.getBoolean());
        assertEquals(p2.getBoolean(), new Boolean(true));

    }

    /**
     * Test.
     */
    public void testParagraphConstructorDateTypeJSON() {

        final Date testDate = new Date(System.currentTimeMillis());

        final Paragraph p = Paragraph.fromDate("foo", testDate);
        _json.set("name", "foo");
        _json.set("type", "DATE");
        _json.set("text", (String) null);
        _json.set("boolean", (Boolean) null);
        _json.set("date", testDate);
        expect(_json.getString("name")).andReturn("bar");
        expect(_json.getString("type")).andReturn("DATE");
        expect(_json.getDate("date")).andReturn(testDate);
        replay(_json);
        new ParagraphSerializer().write(_json, p);

        final Paragraph p2 = new ParagraphSerializer().read(_json);
        assertEquals(p2.getDate(), testDate);

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

    /**
     * Test.
     */
    public void testEqualsHandleNull() {

        final Paragraph paragraph1 = Paragraph.fromText("foo", "Hello world");
        final Paragraph paragraph2 = null;
        assertFalse(paragraph1.equals(paragraph2));
    }

    /**
     * Test.
     */
    public void testEqualsHandlesDifferentClass() {

        final Paragraph paragraph1 = Paragraph.fromText("foo", "Hello world");
        final Object paragraph2 = new Object();
        assertFalse(paragraph1.equals(paragraph2));
    }

    /**
     * Test.
     */
    public void testEqulasHandlesIdenticalObjects() {
        final Paragraph paragraph1 = Paragraph.fromText("foo", "Hello world");
        assertTrue(paragraph1.equals(paragraph1));
    }

    /**
     * Test.
     */
    public void testEqualsHandlesDiffesrentNames() {
        final Paragraph paragraph1 = Paragraph.fromText("foo", "Hello world");
        final Paragraph paragraph2 = Paragraph.fromText("bar", "Hello world");
        assertFalse(paragraph1.equals(paragraph2));
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
