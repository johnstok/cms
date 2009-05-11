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

package ccc.content.server;

import static ccc.commons.Exceptions.*;
import static org.easymock.EasyMock.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;
import ccc.content.actions.RenderResourceAction;
import ccc.content.exceptions.NotFoundException;
import ccc.content.response.Body;
import ccc.content.response.Renderer;
import ccc.content.response.Response;
import ccc.domain.ResourcePath;
import ccc.services.StatefulReader;

/**
 * Tests for the {@link RenderResourceAction} class.
 *
 * @author Civic Computing Ltd.
 */
public final class ContentServletTest extends TestCase {

    /**
     * Test.
     */
    public void testDetermineResourcePathHandlesInvalidPath() {

        // ARRANGE
        final RenderResourceAction rr =
            new RenderResourceAction(true, "root", "/login", null);
        final String invalidPath = "$%^$%/^%$^";
        expect(_request.getPathInfo()).andReturn(invalidPath);
        replayAll();

        // ACT
        try {
            rr.determineResourcePath(_request);
            fail("Should throw exception.");

        // ASSERT
        } catch (final NotFoundException e) {
            swallow(e);
        }
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

        final Body b = createStrictMock(Body.class);
        final Response r = new Response(b);

        expect(_response.getOutputStream()).andReturn(os);
        expect(_response.getCharacterEncoding()).andReturn("UTF-8");
        b.write(os, null);
        replayAll();

        // ACT
        r.write(_response);

        // ASSERT
        verifyAll();
    }

    /**
     * Test.
     */
    public void testDetermineResourcePathRemovesTrailingSlash() {

        // ARRANGE
        final RenderResourceAction rr =
            new RenderResourceAction(true, "root", "/login", null);
        expect(_request.getPathInfo()).andReturn("/foo/");
        replayAll();

        // ACT
        final ResourcePath path = rr.determineResourcePath(_request);

        // VERIFY
        verifyAll();
        assertEquals(new ResourcePath("/foo"), path);
    }

    /**
     * Test.
     */
    public void testDetermineResourcePathHandlesSingleForwardSlash() {

        // ARRANGE
        final RenderResourceAction rr =
            new RenderResourceAction(true, "root", "/login", null);
        expect(_request.getPathInfo()).andReturn("/");
        replayAll();

        // ACT
        final ResourcePath path = rr.determineResourcePath(_request);

        // VERIFY
        verifyAll();
        assertEquals(new ResourcePath(""), path);
    }

    /**
     * Test.
     */
    public void testDetermineResourcePathHandlesNull() {

        // ARRANGE
        final RenderResourceAction rr =
            new RenderResourceAction(true, "root", "/login", null);
        expect(_request.getPathInfo()).andReturn(null);
        replayAll();

        // ACT
        final ResourcePath path = rr.determineResourcePath(_request);

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
        final RenderResourceAction rr =
            new RenderResourceAction(true, "root", "/login", null);

        expect(_reader.lookup("root", new ResourcePath("/foo")))
            .andThrow(new NotFoundException());
        replayAll();

        // ACT
        try {
            rr.lookupResource(new ResourcePath("/foo"), _reader);
            fail();

        } catch (final NotFoundException e) {
            swallow(e);
        }

        // ASSERT
        verifyAll();
    }

//    /**
//     * Test.
//     *
//     * @throws IOException If there is an error writing to the _response.
//     * @throws ServletException If execution of the servlet fails.
//     */
//    public void testDoGetHandlesRedirect() throws ServletException,
//                                                      IOException {
//
//        // ARRANGE
//        final Page bar = new Page("bar");
//
//        expect(_request.getPathInfo()).andReturn("/foo");
//        expect(_request.getParameterMap())
//            .andReturn(new HashMap<String, String>());
//        expect(_factory.getReader()).andReturn(_reader);
//        expect(_reader.lookup(eq((String)null), isA(ResourcePath.class)))
//            .andReturn(bar);
//        expect(_factory.currentUser()).andReturn(new User("user"));
//        expect(_factory.createRenderer(_reader)).andReturn(_renderer);
//        expect(_renderer.render(bar, new HashMap<String, String[]>()))
//            .andThrow(new RedirectRequiredException(bar));
//        expect(_request.getContextPath()).andReturn("/context");
//        _response.sendRedirect("/context"+bar.absolutePath().toString());
//        replayAll();
//
//        // ACT
//        _cs.doGet(_request, _response);
//
//        // ASSERT
//        verifyAll();
//    }




    /**
     * {@inheritDoc}
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        _response = createStrictMock(HttpServletResponse.class);
        _request = createStrictMock(HttpServletRequest.class);
        _renderer = createStrictMock(Renderer.class);
        _reader = createStrictMock(StatefulReader.class);
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
        _reader = null;
    }

    private void verifyAll() {
        verify(_response, _request, _renderer, _reader);
    }

    private void replayAll() {
        replay(_response, _request, _renderer, _reader);
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
    private StatefulReader _reader;
}
