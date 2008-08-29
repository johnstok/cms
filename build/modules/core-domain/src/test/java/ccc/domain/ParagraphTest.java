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


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public final class ParagraphTest extends TestCase {

    /**
     * Test.
     */
    public void testConstructorRejectsBadData() {

        // ACT
        new Paragraph("foo");

        try {
            new Paragraph(null);
            fail("NULL should be rejected.");
        } catch (final IllegalArgumentException e) {
            assertEquals("Specified string may not be NULL.", e.getMessage());
        }

        try {
            new Paragraph("");
            fail("Zero length string should be rejected.");
        } catch (final IllegalArgumentException e) {
            assertEquals(
                "Specified string must have length > 0.",
                e.getMessage());
        }
    }

    /**
     * Test.
     */
    public void testBodyAccessor() {

        // ARRANGE
        final Paragraph foo = new Paragraph("foo");

        // ASSERT
        assertEquals("foo", foo.body());
    }
}
