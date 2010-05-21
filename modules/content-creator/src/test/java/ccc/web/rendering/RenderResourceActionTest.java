/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */

package ccc.web.rendering;

import static org.easymock.EasyMock.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;
import ccc.plugins.scripting.Context;

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
        final ByteArrayServletOutputStream os =
            new ByteArrayServletOutputStream();

        final Body b = createStrictMock(Body.class);
        final Response r = new Response(b);

        _response.reset();
        expect(_response.getOutputStream()).andReturn(os);
        expect(_response.getCharacterEncoding()).andReturn("UTF-8");
        b.write(os, null, new Context(), null);
        replayAll();

        // ACT
        r.write(_response, new Context(), null);

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
