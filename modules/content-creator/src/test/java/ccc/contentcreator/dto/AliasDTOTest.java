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
package ccc.contentcreator.dto;

import junit.framework.TestCase;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class AliasDTOTest extends TestCase {

    /**
     * Test.
     */
    public void testConstructor() {

        // ACT
        final AliasDTO aliasDTO =
            new AliasDTO("id","ALIAS", "name", "title", "targetId");

        // ASSERT
        assertEquals("targetId", aliasDTO.getTargetId());

    }
}
