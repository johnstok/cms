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
package ccc.contentcreator.actions;

import com.google.gwt.junit.client.GWTTestCase;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class ClearWorkingCopyActionGwtTest
    extends
        GWTTestCase {

    /**
     * Test.
     */
    public void testFoo() {

        // ARRANGE

        // ACT
        new ClearWorkingCopyAction(null);

        // ASSERT

    }

    /** {@inheritDoc} */
    @Override
    public String getModuleName() {
        return "ccc.contentcreator.ContentCreator";
    }
}
