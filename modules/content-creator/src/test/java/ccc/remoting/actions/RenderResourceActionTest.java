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

package ccc.remoting.actions;

import static ccc.commons.Exceptions.*;
import static org.easymock.EasyMock.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;
import ccc.persistence.ResourceRepository;
import ccc.remoting.actions.RenderResourceAction;
import ccc.rendering.Body;
import ccc.rendering.NotFoundException;
import ccc.rendering.Renderer;
import ccc.rendering.Response;
import ccc.rendering.TextProcessor;
import ccc.rendering.velocity.VelocityProcessor;
import ccc.types.ResourcePath;

/**
 * Tests for the {@link RenderResourceAction} class.
 *
 * @author Civic Computing Ltd.
 */
public final class RenderResourceActionTest extends TestCase {

    /**
     * Test.
     */
    public void testDetermineResourcePathHandlesInvalidPath() {

        // ARRANGE
        final RenderResourceAction rr =
            new RenderResourceAction(true, "root", "/login", null);
        final String invalidPath = "$%^$%/^%$^";
        expect(_request.getPathInfo()).andReturn(invalidPath);
        expect(_request.getContextPath()).andReturn("");
        replayAll();

        // ACT
        try {
            rr.determineResourcePath(_request);
            fail("Should throw exception.");

        // ASSERT
        } catch (final IllegalArgumentException e) {
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
        final TextProcessor tp = new VelocityProcessor();
        final ByteArrayServletOutputStream os =
            new ByteArrayServletOutputStream();

        final Body b = createStrictMock(Body.class);
        final Response r = new Response(b);

        expect(_response.getOutputStream()).andReturn(os);
        expect(_response.getCharacterEncoding()).andReturn("UTF-8");
        b.write(os, null, tp);
        replayAll();

        // ACT
        r.write(_response, tp);

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
        expect(_request.getContextPath()).andReturn("");
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
        expect(_request.getContextPath()).andReturn("");
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
        expect(_request.getContextPath()).andReturn("");
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
    public void testDoGetHandlesNotFound() {

        // ARRANGE
        final RenderResourceAction rr =
            new RenderResourceAction(true, "root", "/login", null);

        expect(_rdao.lookup("root", new ResourcePath("/foo")))
            .andThrow(new NotFoundException());
        replayAll();

        // ACT
        try {
            rr.lookupResource(new ResourcePath("/foo"), _rdao);
            fail();

        } catch (final NotFoundException e) {
            swallow(e);
        }

        // ASSERT
        verifyAll();
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
        _rdao = createStrictMock(ResourceRepository.class);
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
        _rdao = null;
    }

    private void verifyAll() {
        verify(_response, _request, _renderer, _rdao);
    }

    private void replayAll() {
        replay(_response, _request, _renderer, _rdao);
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
        @Override public void write(final int b) {
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
    private ResourceRepository _rdao;
}
