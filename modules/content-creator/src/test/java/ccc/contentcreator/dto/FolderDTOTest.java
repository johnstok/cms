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
package ccc.contentcreator.dto;

import junit.framework.TestCase;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class FolderDTOTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testConstructor() {

        // ARRANGE

        // ACT
        final FolderDTO f = new FolderDTO("id", 0, "name", "title", 0);

        // ASSERT
        assertEquals("id", f.getId());
    }
}
