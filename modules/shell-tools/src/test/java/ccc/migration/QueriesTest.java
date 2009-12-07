/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: See subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.migration;

import junit.framework.TestCase;

/**
 * Tests for the {@link LegacyDBQueries} class.
 *
 * @author Civic Computing Ltd
 */
public class QueriesTest extends TestCase {

    /**
     * Test.
     *
     */
    public void testConstructRejectsNull() {

        // ACT
        try {
            new LegacyDBQueries(null);
            fail("Queries failed to reject a NULL connection.");
        } catch (final IllegalArgumentException e) {

            //ASSERT
            assertEquals("Specified value may not be NULL.", e.getMessage());
        }
    }
}
