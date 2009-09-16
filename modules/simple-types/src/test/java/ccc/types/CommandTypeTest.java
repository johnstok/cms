/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.types;

import junit.framework.TestCase;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class CommandTypeTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testConvertNameToCamelCase() {

        // ARRANGE

        // ACT

        // ASSERT
        assertEquals("actionCancel", CommandType.ACTION_CANCEL.camelCaseName());
        assertEquals("actionCreate", CommandType.ACTION_CREATE.camelCaseName());
        assertEquals("aliasUpdate", CommandType.ALIAS_UPDATE.camelCaseName());
    }
}
