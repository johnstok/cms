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
 * Tests for the {@link EmailAddress} class.
 *
 * @author Civic Computing Ltd.
 */
public class EmailAddressTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testSupportsApostrophe() {

        // ARRANGE
        final EmailAddress a =
            new EmailAddress("Steven D'Arcy <steven.d'arcy@foo.com>");

        // ACT
        final boolean valid = a.isValid();

        // ASSERT
        assertTrue(valid);
    }

}
