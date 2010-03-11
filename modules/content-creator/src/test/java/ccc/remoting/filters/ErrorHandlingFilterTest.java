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
package ccc.remoting.filters;

import static org.easymock.EasyMock.*;

import java.util.HashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;
import ccc.rendering.AuthenticationRequiredException;
import ccc.rendering.NotFoundException;
import ccc.rendering.RedirectRequiredException;
import ccc.types.HttpStatusCode;


/**
 * Tests for the {@link ErrorHandlingFilter} class.
 *
 * @author Civic Computing Ltd.
 */
public class ErrorHandlingFilterTest
    extends
        TestCase {

    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testNotFoundExceptionCauses404() throws Exception {

        // EXPECT
        _response.sendError(HttpStatusCode.NOT_FOUND);
        replayAll();

        // ARRANGE
        final Filter f = new ErrorHandlingFilter();

        // ACT
        f.doFilter(
            new ServletRequestStub(
                "/context", "/servlet", "/path", new HashMap<String, String>()),
            _response,
            new FilterChain() {
                @Override
                public void doFilter(final ServletRequest request,
                                     final ServletResponse response) {
                    throw new NotFoundException();
                }
            });

        // ASSERT
        verifyAll();
    }


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testRedirectRequiredExceptionCausesRedirect() throws Exception {

        // EXPECT
        _response.sendRedirect("/context/servlet/foo");
        replayAll();

        // ARRANGE
        final Filter f = new ErrorHandlingFilter();

        // ACT
        f.doFilter(
            new ServletRequestStub(
                "/context", "/servlet", "/path", new HashMap<String, String>()),
                _response,
                new FilterChain() {
                @Override
                public void doFilter(final ServletRequest request,
                                     final ServletResponse response) {
                    throw new RedirectRequiredException("/foo");
                }
            });

        // ASSERT
        verifyAll();
    }


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testAuthRequiredExceptionCausesRedirect() throws Exception {

        // EXPECT
        _response.sendRedirect("/context/login.html?tg=/foo");
        replayAll();

        // ARRANGE
        final Filter f = new ErrorHandlingFilter();

        // ACT
        f.doFilter(
            new ServletRequestStub(
                "/context", "/servlet", "/path", new HashMap<String, String>()),
                _response,
                new FilterChain() {
                @Override
                public void doFilter(final ServletRequest request,
                                     final ServletResponse response) {
                    throw new AuthenticationRequiredException("/foo");
                }
            });

        // ASSERT
        verifyAll();
    }


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testExOnCommitedTextResponseWritesErrorMsg() throws Exception {

        // EXPECT
        final CapturingServletOutputStream sos =
            new CapturingServletOutputStream();
        expect(_response.isCommitted()).andReturn(Boolean.TRUE);
        expect(_response.getOutputStream()).andReturn(sos);
        replayAll();

        // ARRANGE
        final Filter f = new ErrorHandlingFilter();

        // ACT
        f.doFilter(
            new ServletRequestStub(
                "/context", "/servlet", "/path", new HashMap<String, String>()),
                _response,
                new FilterChain() {
                @Override
                public void doFilter(final ServletRequest request,
                                     final ServletResponse response) {
                    throw new RuntimeException();
                }
            });

        // ASSERT
        verifyAll();
        assertTrue(sos.toString().startsWith("An error occurred: "));
    }


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testIrrelevantExceptionsIgnored() throws Exception {

        // EXPECT
        replayAll();

        // ARRANGE
        final Filter f = new ErrorHandlingFilter();

        // ACT
        f.doFilter(
            new ServletRequestStub(
                "/context", "/servlet", "/path", new HashMap<String, String>()),
                _response,
                new FilterChain() {
                @Override
                public void doFilter(final ServletRequest request,
                                     final ServletResponse response) {
                    throw new RuntimeException(new ClientAbortException());
                }
            });

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
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        _response = null;
    }


    private void verifyAll() {
        verify(_response);
    }


    private void replayAll() {
        replay(_response);
    }


    HttpServletResponse _response;


    /**
     * Helper class for testing the Servlet API.
     *
     * @author Civic Computing Ltd.
     */
    static final class CapturingServletOutputStream
        extends
            ServletOutputStream {

        private final StringBuilder _sb = new StringBuilder();

        @Override
        public void write(final int b) { _sb.append((char)b); }

        /** {@inheritDoc} */
        @Override
        public String toString() { return _sb.toString(); }
    }

    private static final class ClientAbortException extends Exception {
        /* No methods. */
    }
}
