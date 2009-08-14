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

import java.util.Map;

import junit.framework.TestCase;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class CCCPropertiesTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testGetProperty() {

        // ARRANGE

        // ACT
        final String value = CCCProperties.get("test.prop");

        // ASSERT
        assertEquals("hello world!", value);
    }

    /**
     * Test.
     */
    public void testMissingProperty() {

        // ARRANGE

        // ACT
        final String value = CCCProperties.get("missing.prop");

        // ASSERT
        assertNull(value);
    }

    /**
     * Test.
     */
    public void testGetAllProperties() {

        // ARRANGE

        // ACT
        final Map<String, String> map = CCCProperties.getAll();

        // ASSERT
        assertNull("Should contain only client side properties",
            map.get("test.prop"));
    }
}
