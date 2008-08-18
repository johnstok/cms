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
public class FileTest extends TestCase {

    /**
     * Test.
     *
     */
    public void testRejectNullParameters() {

        // ARRANGE
        final FileData fileData = null;

        // ACT
        try {
            new File(
                new ResourceName("file"), "title", "desc", fileData);
            fail("The constructor should reject a NULL file data.");

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals("Specified value may not be NULL.", e.getMessage());
        }
    }
}
