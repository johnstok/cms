/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.remoting.actions;

import static ccc.commons.Exceptions.*;
import static org.easymock.EasyMock.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;
import ccc.persistence.ResourceRepository;
import ccc.rendering.NotFoundException;
import ccc.types.ResourcePath;


/**
 * Tests for the {@link LookupResourceAction} class.
 *
 * @author Civic Computing Ltd.
 */
public class LookupResourceActionTest
    extends
        TestCase {


    /**
     * Test.
     */
    public void testDetermineResourcePathHandlesInvalidPath() {

        // ARRANGE
        final LookupResourceAction rr =
            new LookupResourceAction("root");
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
     */
    public void testDetermineResourcePathRemovesTrailingSlash() {

        // ARRANGE
        final LookupResourceAction rr =
            new LookupResourceAction("root");
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
        final LookupResourceAction rr =
            new LookupResourceAction("root");
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
        final LookupResourceAction rr =
            new LookupResourceAction("root");
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
     * @throws Exception If the test fails.
     */
    public void testDoGetHandlesNotFound() throws Exception {

        // ARRANGE
        final LookupResourceAction rr =
            new LookupResourceAction("root");

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
        _rdao = null;
    }

    private void verifyAll() {
        verify(_response, _request, _rdao);
    }

    private void replayAll() {
        replay(_response, _request, _rdao);
    }

    private HttpServletResponse _response;
    private HttpServletRequest  _request;
    private ResourceRepository _rdao;
}
