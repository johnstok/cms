/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
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
