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
package ccc.content.server;

import java.nio.charset.Charset;

import junit.framework.TestCase;
import ccc.domain.Page;


/**
 * Tests for the {@link Response} class.
 *
 * @author Civic Computing Ltd.
 */
public class ResponseTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testDescriptionProperty() {

        // ARRANGE
        _r.setDescription("foo");

        // ACT
        final String desc = _r.getDescription();

        // ASSERT
        assertEquals("foo", desc);
    }

    /**
     * Test.
     */
    public void testLengthProperty() {

        // ARRANGE
        _r.setLength(Long.valueOf(1));

        // ACT
        final Long length = _r.getLength();

        // ASSERT
        assertEquals(Long.valueOf(1), length);
    }

    /**
     * Test.
     */
    public void testCharSetProperty() {

        // ARRANGE
        _r.setCharSet("UTF-8");

        // ACT
        final String charset = _r.getCharSet();

        // ASSERT
        assertEquals("UTF-8", charset);
    }

    /**
     * Test.
     */
    public void testMimeTypeProperty() {

        // ARRANGE
        _r.setMimeType("text", "html");

        // ACT
        final String mimeType = _r.getMimeType();

        // ASSERT
        assertEquals("text/html", mimeType);
    }

    /**
     * Test.
     */
    public void testExpiryProperty() {

        // ARRANGE
        _r.setExpiry(Long.valueOf(0));

        // ACT
        final Long expiry = _r.getExpiry();

        // ASSERT
        assertEquals(Long.valueOf(0), expiry);
    }

    /**
     * Test.
     */
    public void testDispositionProperty() {

        // ARRANGE
        _r.setDisposition("foo");

        // ACT
        final String disp = _r.getDisposition();

        // ASSERT
        assertEquals("foo", disp);
    }

    /**
     * Test.
     */
    public void testBodyProperty() {

        // ARRANGE
        final Body expected = new PageBody(_p, UTF8, null);
        _r.setBody(expected);

        // ACT
        final Body actual = _r.getBody();

        // ASSERT
        assertEquals(expected, actual);
    }

    /**
     * Test.
     */
    public void testBodyMutatorRejectsNull() {

        // ARRANGE

        // ACT
        try {
            _r.setBody(null);
            fail("Should be rejected.");

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals("Specified value may not be NULL.", e.getMessage());
        }
    }




    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        _r = new Response();
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() throws Exception {
        _r = null;
    }

    private Response _r;
    private Page _p = new Page("my_page");

    /** UTF8 : Charset. */
    private static final Charset UTF8 = Charset.forName("UTF-8");
}
