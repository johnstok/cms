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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;


/**
 * Tests for class ErrorServlet.
 *
 * @author Civic Computing Ltd
 */
public final class ErrorServletTest extends TestCase {

    /**
     * Test.
     *
     * @throws IOException If there is an error writing to the response.
     * @throws ServletException If execution of the servlet fails.
     */
    public void testGetMethodReportsException() throws ServletException,
                                                       IOException {

        // ARRANGE
        final StringWriter output = new StringWriter();
        final HttpServletRequest request = createMock(HttpServletRequest.class);
        final HttpServletResponse response =
            createStrictMock(HttpServletResponse.class);
        final RuntimeException re = new RuntimeException();

        response.reset();
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        expect(request.getAttribute(SessionKeys.EXCEPTION_KEY)).andReturn(re);
        expect(response.getWriter()).andReturn(new PrintWriter(output));
        replay(request, response);

        // ACT
        new ErrorServlet().doGet(request, response);

        // ASSERT
        verify(request, response);
        assertEquals(stackTraceFor(re), output.toString());
    }

    /**
     * Test.
     */
    public void testGetExceptionHandlesMissingException() {

        // ARRANGE
        final HttpServletRequest request = createMock(HttpServletRequest.class);
        expect(request.getAttribute(SessionKeys.EXCEPTION_KEY)).andReturn(null);
        replay(request);

        // ACT
        final Exception e = new ErrorServlet().getException(request);

        // ASSERT
        verify(request);
        assertNotNull(
            "getException() should always return an exception",
            e);
        assertEquals(RuntimeException.class, e.getClass());
        assertEquals(
            "No exception was found at the expected location: "
            +SessionKeys.EXCEPTION_KEY,
            e.getMessage());
    }

    /**
     * Test.
     */
    public void testGetExceptionHandlesNonExcpetion() {

        // ARRANGE
        final HttpServletRequest request = createMock(HttpServletRequest.class);
        expect(request.getAttribute(SessionKeys.EXCEPTION_KEY))
            .andReturn(new Object());
        replay(request);

        // ACT
        final Exception e = new ErrorServlet().getException(request);

        // ASSERT
        verify(request);
        assertNotNull(
            "getException() should always return an exception",
            e);
        assertEquals(RuntimeException.class, e.getClass());
        assertEquals(
            "Object at location: "
            +SessionKeys.EXCEPTION_KEY
            +" was not an exception.",
            e.getMessage());
    }

    /**
     * Test.
     */
    public void testGetExceptionReturnsExistingException() {

        // ARRANGE
        final RuntimeException re = new RuntimeException();
        final HttpServletRequest request = createMock(HttpServletRequest.class);
        expect(request.getAttribute(SessionKeys.EXCEPTION_KEY)).andReturn(re);
        replay(request);

        // ACT
        final Exception e = new ErrorServlet().getException(request);

        // ASSERT
        verify(request);
        assertNotNull(
            "getException() should always return an exception",
            e);
        assertEquals(re, e);
    }
}
