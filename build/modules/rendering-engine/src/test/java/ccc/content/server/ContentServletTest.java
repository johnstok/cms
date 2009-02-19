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

package ccc.content.server;

import static ccc.commons.Exceptions.*;
import static org.easymock.EasyMock.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;
import ccc.domain.Folder;
import ccc.domain.Page;
import ccc.domain.ResourcePath;

/**
 * Tests for the ContentServlet.
 * TODO: Test init() method.
 *
 * @author Civic Computing Ltd
 */
public final class ContentServletTest extends TestCase {

    /**
     * Test.
     */
    public void testDetermineResourcePathHandlesInvalidPath() {

        // ARRANGE
        final String invalidPath = "$%^$%/^%$^";
        expect(_request.getPathInfo()).andReturn(invalidPath);
        replayAll();

        // ACT
        try {
            _cs.determineResourcePath(_request);
            fail("Should throw exception.");

        // ASSERT
        } catch (final NotFoundException e) {
            swallow(e);
        }
        verifyAll();

    }

    /**
     * Test.
     * @throws IOException From servlet API.
     */
    public void testHandleResponseSetsConstentDescription() throws IOException {

        // ARRANGE
        final Response r = new Response();
        r.setDescription("desc");

        _response.setHeader("Content-Description", "desc");
        replayAll();

        // ACT
        _cs.handle(_response, _request, r);

        // ASSERT
        verifyAll();
    }

    /**
     * Test.
     * @throws IOException From servlet API.
     */
    public void testHandleResponseSetsExpiryOfZero() throws IOException {

        // ARRANGE
        final Response r = new Response();
        r.setExpiry(Long.valueOf(0));

        _response.setHeader("Pragma", "no-cache");
        _response.setHeader(
            "Cache-Control",
            "private, must-revalidate, max-age=0");
        _response.setHeader("Expires", "0");
        replayAll();

        // ACT
        _cs.handle(_response, _request, r);

        // ASSERT
        verifyAll();
    }

    /**
     * Test.
     * @throws IOException From servlet API.
     */
    public void testHandleResponseSetsConstentDisposition() throws IOException {

        // ARRANGE
        final Response r = new Response();
        r.setDisposition("disp");

        _response.setHeader("Content-Disposition", "disp");
        replayAll();

        // ACT
        _cs.handle(_response, _request, r);

        // ASSERT
        verifyAll();
    }

    /**
     * Test.
     * @throws IOException From servlet API.
     */
    public void testHandleResponseSetsConstentType() throws IOException {

        // ARRANGE
        final Response r = new Response();
        r.setMimeType("text", "html");

        _response.setContentType("text/html");
        replayAll();

        // ACT
        _cs.handle(_response, _request, r);

        // ASSERT
        verifyAll();
    }

    /**
     * Test.
     * @throws IOException From servlet API.
     */
    public void testHandleResponseSetsCharacterEncoding() throws IOException {

        // ARRANGE
        final Response r = new Response();
        r.setCharSet("UTF-8");

        _response.setCharacterEncoding("UTF-8");
        replayAll();

        // ACT
        _cs.handle(_response, _request, r);

        // ASSERT
        verifyAll();
    }

    /**
     * Test.
     * TODO: Tests for length of 0 & negative numbers?
     * @throws IOException From servlet API.
     */
    public void testHandleResponseSetsConstentLength() throws IOException {

        // ARRANGE
        final Response r = new Response();
        r.setLength(Long.valueOf(1));

        _response.setHeader("Content-Length", "1");
        replayAll();

        // ACT
        _cs.handle(_response, _request, r);

        // ASSERT
        verifyAll();
    }

    /**
     * Test.
     *
     * @throws IOException If there is an error writing to the _response.
     */
    public void testHandleResponseWritesTheBody() throws IOException {

        // ARRANGE
        final ByteArrayServletOutputStream os =
            new ByteArrayServletOutputStream();

        final Response r = new Response();
        final Body b = createStrictMock(Body.class);
        r.setBody(b);

        expect(_response.getOutputStream()).andReturn(os);
        expect(_factory.getReader()).andReturn(null);
        b.write(os, null);
        replayAll();

        // ACT
        _cs.handle(_response, _request, r);

        // ASSERT
        verifyAll();
    }

    /**
     * Test.
     */
    public void testDetermineResourcePathRemovesTrailingSlash() {

        // ARRANGE
        expect(_request.getPathInfo()).andReturn("/foo/");
        replayAll();

        // ACT
        final ResourcePath path = _cs.determineResourcePath(_request);

        // VERIFY
        verifyAll();
        assertEquals(new ResourcePath("/foo"), path);
    }

    /**
     * Test.
     */
    public void testDetermineResourcePathHandlesSingleForwardSlash() {

        // ARRANGE
        expect(_request.getPathInfo()).andReturn("/");
        replayAll();

        // ACT
        final ResourcePath path = _cs.determineResourcePath(_request);

        // VERIFY
        verifyAll();
        assertEquals(new ResourcePath(""), path);
    }

    /**
     * Test.
     */
    public void testDetermineResourcePathHandlesNull() {

        // ARRANGE
        expect(_request.getPathInfo()).andReturn(null);
        replayAll();

        // ACT
        final ResourcePath path = _cs.determineResourcePath(_request);

        // VERIFY
        verifyAll();
        assertEquals(new ResourcePath(""), path);
    }

    /**
     * Test.
     *
     * @throws IOException If there is an error writing to the _response.
     * @throws ServletException If execution of the servlet fails.
     */
    public void testDoGetHandlesNotFound() throws ServletException,
                                                      IOException {

        // ARRANGE
        final RequestDispatcher rd = createStrictMock(RequestDispatcher.class);
        final Folder foo = new Folder("foo");
        final Folder baz = new Folder("baz");
        foo.add(baz);

        expect(_request.getPathInfo()).andReturn("/foo");
        expect(_factory.createLocator()).andReturn(_locator);
        expect(_locator.locate(isA(ResourcePath.class)))
            .andThrow(new NotFoundException());
        expect(_request.getRequestDispatcher("/notfound")).andReturn(rd);
        rd.forward(_request, _response);
        replayAll();

        // ACT
        _cs.doGet(_request, _response); // How to inject renderer???

        // ASSERT
        verifyAll();
    }

    /**
     * Test.
     *
     * @throws IOException If there is an error writing to the _response.
     * @throws ServletException If execution of the servlet fails.
     */
    public void testDoGetHandlesRedirect() throws ServletException,
                                                      IOException {

        // ARRANGE
        final Page bar = new Page("bar");

        expect(_request.getPathInfo()).andReturn("/foo");
        expect(_request.getParameterMap())
            .andReturn(new HashMap<String, String>());
        expect(_factory.createLocator()).andReturn(_locator);
        expect(_factory.createRenderer()).andReturn(_renderer);
        expect(_locator.locate(isA(ResourcePath.class)))
            .andReturn(bar);
        expect(_renderer.render(bar))
            .andThrow(new RedirectRequiredException(bar));
        expect(_request.getContextPath()).andReturn("/context");
        _response.sendRedirect("/context"+bar.absolutePath().toString());
        replayAll();

        // ACT
        _cs.doGet(_request, _response);

        // ASSERT
        verifyAll();
    }

    /**
     * Test.
     */
    public void testDisablementOfResponseCaching() {

        // ARRANGE
        _response.setHeader("Pragma", "no-cache");   // non-spec, but supported
        _response.setHeader(
            "Cache-Control",
            "private, must-revalidate, max-age=0"); // equivalent to 'no-cache'
        _response.setHeader("Expires", "0");
        replay(_response);

        // ACT
        _cs.disableCaching(_response);

        // VERIFY
        verify(_response);
    }




    /**
     * {@inheritDoc}
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        _response = createStrictMock(HttpServletResponse.class);
        _request = createStrictMock(HttpServletRequest.class);
        _renderer = createStrictMock(Renderer.class);
        _locator = createStrictMock(Locator.class);
        _factory = createStrictMock(ObjectFactory.class);
        _cs = new ContentServlet(_factory);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        _response = null;
        _request = null;
        _renderer = null;
        _locator = null;
        _factory = null;
        _cs = null;
    }

    private void verifyAll() {
        verify(_response, _request, _renderer, _locator, _factory);
    }

    private void replayAll() {
        replay(_response, _request, _renderer, _locator, _factory);
    }

    /**
     * Helper class for testing.
     *
     * @author Civic Computing Ltd.
     */
    static final class ByteArrayServletOutputStream
        extends
            ServletOutputStream {

        private final ByteArrayOutputStream _baos =
            new ByteArrayOutputStream();

        /** {@inheritDoc} */
        @Override public void write(final int b) throws IOException {
            _baos.write(b);
        }

        /** {@inheritDoc} */
        @Override public String toString() {
            return new String(_baos.toByteArray(), Charset.forName("UTF-8"));
        }
    }

    private HttpServletResponse _response;
    private HttpServletRequest  _request;
    private Renderer _renderer;
    private Locator _locator;
    private ObjectFactory _factory;
    private ContentServlet _cs;
}
