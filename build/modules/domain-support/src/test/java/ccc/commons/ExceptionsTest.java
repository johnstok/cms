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
package ccc.commons;

import java.io.PrintWriter;
import java.io.StringWriter;

import junit.framework.TestCase;


/**
 * Tests for the {@link Exceptions} class.
 *
 * @author Civic Computing Ltd
 */
public final class ExceptionsTest extends TestCase {

    /**
     * Test.
     */
    public void testStackTraceFor() {

        // ARRANGE
        final Exception re = new Exception();
        final StringWriter output = new StringWriter();
        re.printStackTrace(new PrintWriter(output));

        // ACT
        final String actual = Exceptions.stackTraceFor(re);

        // ASSERT
        assertEquals(output.toString(), actual);
    }
}
