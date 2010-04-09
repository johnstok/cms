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
package ccc.client.gwt;

import junit.framework.Test;
import junit.framework.TestSuite;

import ccc.client.gwt.tests.FailureOverlayGwtTest;

import com.google.gwt.junit.tools.GWTTestSuite;


/**
 * The suite of tests to be run using GWT.
 *
 * @author Civic Computing Ltd.
 */
public class ContentCreatorGwtTestSuite
    extends
        TestSuite {

    /**
     * Creates the TestSuite that contains the GWTTestCases.
     *
     * @return The suite of GWT tests.
     */
    public static Test suite() {
        final TestSuite gwtTestSuite = new GWTTestSuite();
        gwtTestSuite.addTestSuite(FailureOverlayGwtTest.class);
        return gwtTestSuite;
    }
}
