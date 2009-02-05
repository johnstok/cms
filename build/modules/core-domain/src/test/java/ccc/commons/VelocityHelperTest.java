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
package ccc.commons;

import java.util.Calendar;

import junit.framework.TestCase;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class VelocityHelperTest extends TestCase {

    /**
     * Test.
     *
     */
    public void testPath() {

        // ARRANGE
        final VelocityHelper helper = new VelocityHelper();
        final String expected = ""+Calendar.getInstance().get(Calendar.YEAR);

        // ACT
        final String year = helper.currentYear();

        // ASSERT
        assertEquals(expected, year);
    }
}
