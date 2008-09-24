/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.commons;

import junit.framework.TestCase;


/**
 * Tests for the {@link Testing} class.
 *
 * @author Civic Computing Ltd.
 */
public class TestingTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testDummyString() {

        // ARRANGE
        final int length = 257;

        // ACT
        final String actual = Testing.dummyString('a', length);

        // ASSERT
        assertEquals(length, actual.length());
        assertTrue("String contains invalid characters.", actual.matches("a*"));
    }
}
