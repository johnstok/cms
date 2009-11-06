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

import junit.framework.TestCase;


/**
 * Tests for the {@link CCCProperties} class.
 *
 * @author Civic Computing Ltd.
 */
public class CCCPropertiesTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testVersion() {

        // ARRANGE

        // ACT
        final String value = CCCProperties.version();

        // ASSERT
        assertEquals("7.0.0-SNAPSHOT", value);
    }
}
