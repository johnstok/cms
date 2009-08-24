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

package ccc.contentcreator.tests;

import ccc.contentcreator.overlays.FailureOverlay;
import ccc.types.FailureCode;

import com.google.gwt.junit.client.GWTTestCase;


/**
 * Tests for the {@link FailureOverlay} class.
 *
 * @author Civic Computing Ltd.
 */
public class FailureOverlayGwtTest
    extends
        GWTTestCase {

    /**
     * Test.
     */
    public void testConstructFromJson() {

        // ARRANGE

        // ACT
        final FailureOverlay failure = FailureOverlay.fromJson(EXAMPLE);

        // ASSERT
        assertEquals("f4c9925b-6d2b-4fb3-902e-6c0d7ad82e28", failure.getId());
        assertEquals(FailureCode.UNEXPECTED, failure.getCode());
    }

    /** {@inheritDoc} */
    @Override
    public String getModuleName() {
        return "ccc.contentcreator.ContentCreator";
    }

    private static final String EXAMPLE =
          "{"
        +     "\"id\":\"f4c9925b-6d2b-4fb3-902e-6c0d7ad82e28\","
        +     "\"code\":\""+FailureCode.UNEXPECTED.name()+"\""
        + "}";
}
