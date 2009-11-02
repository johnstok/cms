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
package ccc.commons;

import java.io.PrintWriter;
import java.util.HashMap;

import junit.framework.TestCase;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class ScriptRunnerTest
    extends
        TestCase {

    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testEvalString() throws Exception {

        // ARRANGE
        final ScriptRunner runner = new ScriptRunner();

        // ACT
        runner.eval(
            "print(java.lang.String);",
            new HashMap<String, Object>(),
            new PrintWriter(System.out));

        // ASSERT

    }
}
