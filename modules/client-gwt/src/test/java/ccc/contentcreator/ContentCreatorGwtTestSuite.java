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
package ccc.contentcreator;

import junit.framework.Test;
import junit.framework.TestSuite;

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
        return gwtTestSuite;
    }

}
