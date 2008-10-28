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


/**
 * Tests for the {@link Paragraph} class.
 * TODO: Test equals().
 *
 * @author Civic Computing Ltd
 */
public final class ParagraphTest extends TestCase {

    /**
     * Test.
     */
    public void testTextConstructor() {

        // ACT
        final Paragraph p = Paragraph.fromText("Hello world");

        // ASSERT
        assertEquals(Paragraph.Type.TEXT, p.type());
        assertEquals("Hello world", p.text());
    }

    /**
     * Test.
     */
    public void testBooleanConstructor() {

        // ACT
        final Paragraph p = Paragraph.fromBoolean(Boolean.TRUE);

        // ASSERT
        assertEquals(Paragraph.Type.BOOLEAN, p.type());
        assertEquals(Boolean.TRUE, p.bool());
    }

    /**
     * Test.
     */
    public void testDateConstructor() {

        // ARRANGE
        final Date now = new Date();

        // ACT
        final Paragraph p = Paragraph.fromDate(now);

        // ASSERT
        assertEquals(Paragraph.Type.DATE, p.type());
        assertEquals(now, p.date());
    }

    /**
     * Test.
     */
    public void testTextConstructorRejectsNull() {

        // ACT
        try {
            Paragraph.fromText(null);
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
            Paragraph.fromDate(null);
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
            Paragraph.fromBoolean(null);
            fail("NULL should be rejected.");

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals("Specified value may not be NULL.", e.getMessage());
        }
    }
}
