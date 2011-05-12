/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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
package ccc.commons;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

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


    /**
     * Test.
     * @throws Exception If the test fails.
     */
    public void testCopyZeroLengthStream() throws Exception {

        // ARRANGE
        final ByteArrayOutputStream expected = new ByteArrayOutputStream();
        final byte[] actual = {};

        // ACT
        IO.copy(new ByteArrayInputStream(actual), expected);

        // ASSERT
        assertTrue(Arrays.equals(actual, expected.toByteArray()));
    }


    /**
     * Test.
     * @throws Exception If the test fails.
     */
    public void testCopyOneByteStream() throws Exception {

        // ARRANGE
        final ByteArrayOutputStream expected = new ByteArrayOutputStream();
        final byte[] actual = {0};

        // ACT
        IO.copy(new ByteArrayInputStream(actual), expected);

        // ASSERT
        assertTrue(Arrays.equals(actual, expected.toByteArray()));
    }


    /**
     * Test.
     * @throws Exception If the test fails.
     */
    public void testCopyBufferSizeStream() throws Exception {

        // ARRANGE
        final ByteArrayOutputStream expected = new ByteArrayOutputStream();
        final byte[] actual = new byte[IO.BUFFER_SIZE];
        Arrays.fill(actual, (byte) 4);

        // ACT
        IO.copy(new ByteArrayInputStream(actual), expected);

        // ASSERT
        assertTrue(Arrays.equals(actual, expected.toByteArray()));
    }


    /**
     * Test.
     * @throws Exception If the test fails.
     */
    public void testCopyBufferPlusOneSizeStream() throws Exception {

        // ARRANGE
        final ByteArrayOutputStream expected = new ByteArrayOutputStream();
        final byte[] actual = new byte[IO.BUFFER_SIZE+1];
        Arrays.fill(actual, (byte) 4);

        // ACT
        IO.copy(new ByteArrayInputStream(actual), expected);

        // ASSERT
        assertTrue(Arrays.equals(actual, expected.toByteArray()));
    }


    /**
     * Test.
     * @throws Exception If the test fails.
     */
    public void testHelloWorldStreamToString() throws Exception {

        // ARRANGE
        final byte[] helloWorld = "Hello World".getBytes("UTF-8");

        // ACT
        final String actual =
            IO.toString(
                new ByteArrayInputStream(helloWorld),
                Charset.forName("UTF8"));

        // ASSERT
        assertEquals("Hello World", actual);
    }
}
