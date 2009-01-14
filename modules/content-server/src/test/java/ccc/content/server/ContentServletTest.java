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

import static org.easymock.EasyMock.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;
import ccc.commons.MapRegistry;
import ccc.commons.VelocityProcessor;
import ccc.domain.Folder;
import ccc.domain.Page;
import ccc.domain.Paragraph;
import ccc.domain.ResourcePath;

/**
 * Tests for the ContentServlet.
 * TODO: test redirect
 * TODO: test pathInfo = null
 * TODO: test pathInfo = '/'
 *
 * @author Civic Computing Ltd
 */
public final class ContentServletTest extends TestCase {

    /**
     * Test.
     * @throws IOException
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
     * TODO: Set 'Expires' to 'now' instead?
     * @throws IOException
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
     * @throws IOException
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
     * @throws IOException
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
     * @throws IOException
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
     * @throws IOException
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
     * TODO: move to core-domain.
     */
    public void testRenderResource() {

        // ARRANGE
        final Page foo = new Page("foo");
        foo.addParagraph(Paragraph.fromText("bar", "baz"));
        final String template = "Hello $resource.id()";

        // ACT
        final String html = new VelocityProcessor().render(foo, template);

        // ASSERT
        assertEquals("Hello "+foo.id(), html);
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
        b.write(os);
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
    public void testDoSafeGetHandlesNotFound() throws ServletException,
                                                      IOException {

        // ARRANGE
        final RequestDispatcher rd = createStrictMock(RequestDispatcher.class);
        final Folder foo = new Folder("foo");
        final Folder baz = new Folder("baz");
        foo.add(baz);

        expect(_request.getPathInfo()).andReturn("/foo");
        expect(_renderer.render(isA(ResourcePath.class)))
            .andThrow(new NotFoundException());
        expect(_request.getRequestDispatcher("/notfound")).andReturn(rd);
        rd.forward(_request, _response);
        replayAll();

        // ACT
        _cs.doSafeGet(_request, _response, _renderer);

        // ASSERT
        verifyAll();
    }

    /**
     * Test.
     *
     * @throws IOException If there is an error writing to the _response.
     * @throws ServletException If execution of the servlet fails.
     */
    public void testDoSafeGetHandlesRedirect() throws ServletException,
                                                      IOException {

        // ARRANGE
        final Page bar = new Page("bar");

        expect(_request.getPathInfo()).andReturn("/foo");
        expect(_renderer.render(isA(ResourcePath.class)))
            .andThrow(new RedirectRequiredException(bar));
        expect(_request.getContextPath()).andReturn("/context");
        _response.sendRedirect("/context"+bar.absolutePath().toString());
        replayAll();

        // ACT
        _cs.doSafeGet(_request, _response, _renderer);

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
        _cs.disableCachingFor(_response);

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
        _renderer = createStrictMock(ResourceRenderer.class);
        _cs = new ContentServlet(new MapRegistry());
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
        _cs = null;
    }

    private void verifyAll() {
        verify(_response, _request, _renderer);
    }

    private void replayAll() {
        replay(_response, _request, _renderer);
    }

    /**
     * TODO: Add Description for this type.
     *
     * @author Civic Computing Ltd.
     */
    private static final class ByteArrayServletOutputStream
        extends
            ServletOutputStream {

        private final ByteArrayOutputStream _baos =
            new ByteArrayOutputStream();

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
    private ResourceRenderer _renderer;
    private ContentServlet _cs;
}
