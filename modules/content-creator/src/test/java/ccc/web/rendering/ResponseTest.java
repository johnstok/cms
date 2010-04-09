/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.web.rendering;

import static org.easymock.EasyMock.*;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;
import ccc.api.types.Duration;
import ccc.api.types.MimeType;
import ccc.commons.Resources;
import ccc.plugins.scripting.Script;
import ccc.web.rendering.Body;
import ccc.web.rendering.CharEncodingHeader;
import ccc.web.rendering.ContentTypeHeader;
import ccc.web.rendering.DateHeader;
import ccc.web.rendering.EmptyBody;
import ccc.web.rendering.Header;
import ccc.web.rendering.IntHeader;
import ccc.web.rendering.PageBody;
import ccc.web.rendering.Response;
import ccc.web.rendering.StringHeader;


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
        final Map<String, Header>headers = _r.getHeaders();

        // ASSERT
        assertTrue(
            headers.containsValue(
                new StringHeader("Content-Description", "foo")));
    }

    /**
     * Test.
     */
    public void testLengthProperty() {

        // ARRANGE
        _r.setLength(1);

        // ACT
        final Map<String, Header>headers = _r.getHeaders();

        // ASSERT
        assertTrue(
            headers.containsValue(new IntHeader("Content-Length", 1)));
    }

    /**
     * Test.
     */
    public void testCharSetProperty() {

        // ARRANGE
        _r.setCharSet("UTF-8");

        // ACT
        final Map<String, Header>headers = _r.getHeaders();

        // ASSERT
        assertTrue(
            headers.containsValue(
                new CharEncodingHeader(Charset.forName("UTF-8"))));
    }

    /**
     * Test.
     */
    public void testMimeTypeProperty() {

        // ARRANGE
        _r.setMimeType(MimeType.HTML);

        // ACT
        final Map<String, Header>headers = _r.getHeaders();

        // ASSERT
        assertTrue(
            headers.containsValue(
                new ContentTypeHeader(MimeType.HTML)));
    }

    /**
     * Test.
     */
    public void testExpiryPropertyOkForUncacheableResource() {

        // ARRANGE
        _r.setExpiry(null);

        // ACT
        final Map<String, Header>headers = _r.getHeaders();

        // ASSERT
        assertTrue(
            headers.containsValue(new DateHeader("Expires", new Date(0))));
    }
    /**
     * Test.
     */
    public void testExpiryPropertyOkForCacheableResource() {

        // ARRANGE
        _r.setExpiry(new Duration(MILLISECS_300));

        // ACT
        final Map<String, Header>headers = _r.getHeaders();

        // ASSERT
        assertTrue(
            headers.containsValue(
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
        final Map<String, Header>headers = _r.getHeaders();

        // ASSERT
        assertTrue(
            headers.containsValue(
                new StringHeader("Content-Disposition", "foo")));
    }

    /**
     * Test.
     */
    public void testBodyProperty() {

        // ARRANGE
        final Body expected =
            new PageBody(
                new Script(
                    Resources.readIntoString(
                        PageBody.class.getResource(
                        "/ccc/content/server/default-page-template.txt"),
                        Charset.forName("UTF-8")),
                "test"));
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
            new PageBody(null);
            fail("Should be rejected.");

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals("Specified value may not be NULL.", e.getMessage());
        }
    }


    /**
     * Test.
     */
    public void testHandleResponseSetsConstentDescription() {

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
     */
    public void testHandleResponseSetsExpiryOfZero() {

        // ARRANGE
        final Response r = new Response(new EmptyBody());
        r.setExpiry(new Duration(0));

        _response.setHeader("Pragma", null);
        _response.setHeader(
            "Cache-Control", "private, must-revalidate, max-age=0");
        _response.setDateHeader("Expires", 0);
        replayAll();

        // ACT
        r.writeHeaders(_response);

        // ASSERT
        verifyAll();
    }

    /**
     * Test.
     */
    public void testHandleResponseSetsConstentDisposition() {

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
     */
    public void testHandleResponseSetsConstentType() {

        // ARRANGE
        final Response r = new Response(new EmptyBody());
        r.setMimeType(MimeType.HTML);

        _response.setContentType("text/html");
        replayAll();

        // ACT
        r.writeHeaders(_response);

        // ASSERT
        verifyAll();
    }

    /**
     * Test.
     */
    public void testHandleResponseSetsCharacterEncoding() {

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
    protected void setUp() {
        _r = new Response(new EmptyBody());
        _response = createStrictMock(HttpServletResponse.class);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() {
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
    private HttpServletResponse _response;
}
