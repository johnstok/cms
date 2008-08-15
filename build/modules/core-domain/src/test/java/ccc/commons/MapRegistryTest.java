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
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class MapRegistryTest extends TestCase {

    /**
     * Test.
     */
    public void testAdd() {

        // ARRANGE
        final Registry reg = new MapRegistry();
        final Object o = new Object();

        // ACT
       final Registry actual = reg.put("foo", o);

        // ASSERT
       assertEquals(reg, actual);
       assertEquals(o, reg.get("foo"));
    }
}
