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
import ccc.domain.Page;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class VelocityProcessorTest extends TestCase {

    /**
     * Test.
     */
    public void testMacroLoading() {

        // ARRANGE
        final VelocityProcessor vp = new VelocityProcessor();

        // ACT
        final String actual =
            vp.render(new Page("foo"), null, "#hello()");


        // ASSERT
        assertEquals("hello!", actual);
    }
}
