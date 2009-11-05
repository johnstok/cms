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

import static org.easymock.EasyMock.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;
import ccc.rendering.Body;
import ccc.rendering.Context;
import ccc.rendering.Response;
import ccc.rendering.TextProcessor;
import ccc.rendering.velocity.VelocityProcessor;

/**
 * Tests for the {@link RenderResourceAction} class.
 *
 * @author Civic Computing Ltd.
 */
public final class RenderResourceActionTest extends TestCase {


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
        b.write(os, null, new Context(), tp);
        replayAll();

        // ACT
        r.write(_response, new Context(), tp);

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
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        _response = null;
        _request = null;
    }


    private void verifyAll() {
        verify(_response, _request);
    }


    private void replayAll() {
        replay(_response, _request);
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
}
