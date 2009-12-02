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
package ccc.serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

import junit.framework.TestCase;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class IOTest
    extends
        TestCase {

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
