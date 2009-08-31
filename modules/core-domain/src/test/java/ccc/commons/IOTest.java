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
package ccc.commons;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import ccc.serialization.IO;

import junit.framework.TestCase;


/**
 * Tests for the {@link IO} class.
 *
 * @author Civic Computing Ltd.
 */
public class IOTest extends TestCase {

    /**
     * Test.
     * @throws IOException If a stream fails.
     */
    public void testCopy() throws IOException {

        // ARRANGE
        final byte[] expected = new byte[]{1};
        final ByteArrayInputStream is = new ByteArrayInputStream(expected);
        final ByteArrayOutputStream os = new ByteArrayOutputStream();

        // ACT
        IO.copy(is, os);

        // ASSERT
        assertTrue(
            "Data should be identical",
            Arrays.equals(expected, os.toByteArray()));
    }
}
