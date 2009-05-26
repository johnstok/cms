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
package ccc.content.response;

import static org.easymock.EasyMock.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;
import ccc.api.Duration;
import ccc.api.MimeType;
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

    /** MILLISECS_300 : int. */
    private static final int MILLISECS_300 = 300;
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
     */
    public void testMimeTypeProperty() {

        // ARRANGE
        _r.setMimeType("text", "html");

        // ACT
        final List<Header> headers = _r.getHeaders();

        // ASSERT
        assertTrue(
            headers.contains(
                new ContentTypeHeader(MimeType.HTML)));
    }

    /**
     * Test.
     */
    public void testExpiryPropertyOkForUncacheableResource() {

        // ARRANGE
        _r.setExpiry(null);

        // ACT
        final List<Header> headers = _r.getHeaders();

        // ASSERT
        assertTrue(headers.contains(new DateHeader("Expires", new Date(0))));
    }
    /**
     * Test.
     */
    public void testExpiryPropertyOkForCacheableResource() {

        // ARRANGE
        _r.setExpiry(new Duration(MILLISECS_300));

        // ACT
        final List<Header> headers = _r.getHeaders();

        // ASSERT
        assertTrue(
            headers.contains(
                new StringHeader("Cache-Control", "max-age="+MILLISECS_300)));
        System.out.println(headers);
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


    /**
     * Test.
     * @throws IOException From servlet API.
     */
    public void testHandleResponseSetsConstentDescription() throws IOException {

        // ARRANGE
        final Response r = new Response(new EmptyBody());
        r.setDescription("desc");

        _response.setHeader("Content-Description", "desc");
        replayAll();

        // ACT
        r.writeHeaders(_response);

        // ASSERT
        verifyAll();
    }

    /**
     * Test.
     * @throws IOException From servlet API.
     */
    public void testHandleResponseSetsExpiryOfZero() throws IOException {

        // ARRANGE
        final Response r = new Response(new EmptyBody());
        r.setExpiry(new Duration(0));

        _response.setHeader("Pragma", "no-cache");
        _response.setHeader(
            "Cache-Control", "no-store, must-revalidate, max-age=0");
        _response.setDateHeader("Expires", 0);
        replayAll();

        // ACT
        r.writeHeaders(_response);

        // ASSERT
        verifyAll();
    }

    /**
     * Test.
     * @throws IOException From servlet API.
     */
    public void testHandleResponseSetsConstentDisposition() throws IOException {

        // ARRANGE
        final Response r = new Response(new EmptyBody());
        r.setDisposition("disp");

        _response.setHeader("Content-Disposition", "disp");
        replayAll();

        // ACT
        r.writeHeaders(_response);

        // ASSERT
        verifyAll();
    }

    /**
     * Test.
     * @throws IOException From servlet API.
     */
    public void testHandleResponseSetsConstentType() throws IOException {

        // ARRANGE
        final Response r = new Response(new EmptyBody());
        r.setMimeType("text", "html");

        _response.setContentType("text/html");
        replayAll();

        // ACT
        r.writeHeaders(_response);

        // ASSERT
        verifyAll();
    }

    /**
     * Test.
     * @throws IOException From servlet API.
     */
    public void testHandleResponseSetsCharacterEncoding() throws IOException {

        // ARRANGE
        final Response r = new Response(new EmptyBody());
        r.setCharSet("UTF-8");

        _response.setCharacterEncoding("UTF-8");
        replayAll();

        // ACT
        r.writeHeaders(_response);

        // ASSERT
        verifyAll();
    }

    /**
     * Test.
     * TODO: Tests for length of 0 & negative numbers?
     */
    public void testHandleResponseSetsConstentLength() {

        // ARRANGE
        final Response r = new Response(new EmptyBody());
        r.setLength(1);

        _response.setIntHeader("Content-Length", 1);
        replayAll();

        // ACT
        r.writeHeaders(_response);

        // ASSERT
        verifyAll();
    }




    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        _r = new Response(new EmptyBody());
        _response = createStrictMock(HttpServletResponse.class);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() throws Exception {
        _response = null;
        _r = null;
    }

    private void verifyAll() {
        verify(_response);
    }

    private void replayAll() {
        replay(_response);
    }

    private Response _r;
    private Page _p = new Page("my_page");
    private HttpServletResponse _response;
}
