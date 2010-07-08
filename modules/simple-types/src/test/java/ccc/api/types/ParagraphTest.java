/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.api.types;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;
import ccc.api.types.Paragraph;
import ccc.api.types.ParagraphType;


/**
 * Tests for the {@link Paragraph} class.
 *
 * @author Civic Computing Ltd
 */
public final class ParagraphTest extends TestCase {

    /**
     * Test.
     */
    public void testEscape() {

        // ARRANGE

        // ACT
        final String escaped = Paragraph.escape("'<&>\"");

        // ASSERT
        assertEquals("'&lt;&amp;&gt;&quot;", escaped);
    }


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
}
