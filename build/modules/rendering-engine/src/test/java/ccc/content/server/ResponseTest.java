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
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;

import junit.framework.TestCase;
import ccc.commons.Testing;
import ccc.domain.Page;
import ccc.services.StatefulReader;


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
        final List<Header> headers = _r.getHeaders();

        // ASSERT
        assertTrue(
            headers.contains(new StringHeader("Content-Description", "foo")));
    }

    /**
     * Test.
     */
    public void testLengthProperty() {

        // ARRANGE
        _r.setLength(1);

        // ACT
        final List<Header> headers = _r.getHeaders();

        // ASSERT
        assertTrue(
            headers.contains(new IntHeader("Content-Length", 1)));
    }

    /**
     * Test.
     */
    public void testCharSetProperty() {

        // ARRANGE
        _r.setCharSet("UTF-8");

        // ACT
        final List<Header> headers = _r.getHeaders();

        // ASSERT
        assertTrue(
            headers.contains(new CharEncodingHeader(Charset.forName("UTF-8"))));
    }

    /**
     * Test.
     * @throws MimeTypeParseException If a mime type is invalid.
     */
    public void testMimeTypeProperty() throws MimeTypeParseException {

        // ARRANGE
        _r.setMimeType("text", "html");

        // ACT
        final List<Header> headers = _r.getHeaders();

        // ASSERT
        assertTrue(
            headers.contains(
                new ContentTypeHeader(new MimeType("text", "html"))));
    }

    /**
     * Test.
     */
    public void testExpiryProperty() {

        // ARRANGE
        _r.setExpiry(Long.valueOf(0));

        // ACT
        final List<Header> headers = _r.getHeaders();

        // ASSERT
        assertTrue(headers.contains(new DateHeader("Expires", new Date(0))));
    }

    /**
     * Test.
     */
    public void testDispositionProperty() {

        // ARRANGE
        _r.setDisposition("foo");

        // ACT
        final List<Header> headers = _r.getHeaders();

        // ASSERT
        assertTrue(
            headers.contains(new StringHeader("Content-Disposition", "foo")));
    }

    /**
     * Test.
     */
    public void testBodyProperty() {

        // ARRANGE
        final Body expected =
            new PageBody(
                _p,
                Testing.dummy(StatefulReader.class),
                new HashMap<String, String[]>());
        final Response r = new Response(expected);

        // ACT
        final Body actual = r.getBody();

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
            new PageBody(
                null,
                Testing.dummy(StatefulReader.class),
                new HashMap<String, String[]>());
            fail("Should be rejected.");

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals("Specified value may not be NULL.", e.getMessage());
        }
    }




    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        _r = new Response(new ByteArrayBody(new byte[]{}));
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
