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
package ccc.web.filters;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

import junit.framework.TestCase;
import ccc.web.TmpRenderer;


/**
 * Tests for the {@link CharsetConvertingServletRequest}.
 *
 * @author Civic Computing Ltd.
 */
public class CharsetConvertingServletRequestTest
    extends
        TestCase {

    private static final String TARGET_CS = TmpRenderer.DEFAULT_CHARSET;

    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testRecodeString() throws Exception {

        // ARRANGE
        final String key   =
            URLDecoder.decode(
                URLEncoder.encode("‡", TARGET_CS),
                "iso-8859-1");
        final String value =
            URLDecoder.decode(
                URLEncoder.encode("案例学习", TARGET_CS),
                "iso-8859-1");
        final Map<String, String[]> params =
            Collections.singletonMap(key, new String[] {value});


        // ACT
        final CharsetConvertingServletRequest req =
            new CharsetConvertingServletRequest(
                new ServletRequestStub(null, "/foo", "/bar", params));

        // ASSERT
        assertEquals("案例学习", req.getParameter("‡"));

        final Map<String, String[]> actualParams = req.getParameterMap();
        assertEquals(1, actualParams.size());
        assertEquals(Collections.singleton("‡"), actualParams.keySet());
        assertTrue(Arrays.equals(new String[] {"案例学习"}, actualParams.get("‡")));

        assertTrue(Arrays.equals(
            new String[] {"案例学习"},
            req.getParameterValues("‡")));

        final Enumeration<String> actualNames = req.getParameterNames();
        assertEquals("‡", actualNames.nextElement());
        assertFalse(actualNames.hasMoreElements());
    }

    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testRecodeStringHandlesNull() throws Exception {

        // ARRANGE
        final String key   =
            URLDecoder.decode(
                URLEncoder.encode("‡", TARGET_CS),
            "iso-8859-1");
        final String value = null;
        final Map<String, String[]> params =
            Collections.singletonMap(key, new String[] {value});


        // ACT
        final CharsetConvertingServletRequest req =
            new CharsetConvertingServletRequest(
                new ServletRequestStub(null, "/foo", "/bar", params));

        // ASSERT
        assertEquals(null, req.getParameter("‡"));

        final Map<String, String[]> actualParams = req.getParameterMap();
        assertEquals(1, actualParams.size());
        assertEquals(Collections.singleton("‡"), actualParams.keySet());
        assertTrue(Arrays.equals(new String[] {null}, actualParams.get("‡")));

        assertTrue(Arrays.equals(
            new String[] {null},
            req.getParameterValues("‡")));

        final Enumeration<String> actualNames = req.getParameterNames();
        assertEquals("‡", actualNames.nextElement());
        assertFalse(actualNames.hasMoreElements());
    }

    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testRecodeStringHandlesNullArray() throws Exception {

        // ARRANGE
        final String key   =
            URLDecoder.decode(
                URLEncoder.encode("‡", TARGET_CS),
            "iso-8859-1");
        final Map<String, String[]> params =
            Collections.singletonMap(key, null);


        // ACT
        final CharsetConvertingServletRequest req =
            new CharsetConvertingServletRequest(
                new ServletRequestStub(null, "/foo", "/bar", params));

        // ASSERT
        assertEquals(null, req.getParameter("‡"));

        final Map<String, String[]> actualParams = req.getParameterMap();
        assertEquals(1, actualParams.size());
        assertEquals(Collections.singleton("‡"), actualParams.keySet());
        assertNull(actualParams.get("‡"));

        assertNull(req.getParameterValues("‡"));

        final Enumeration<String> actualNames = req.getParameterNames();
        assertEquals("‡", actualNames.nextElement());
        assertFalse(actualNames.hasMoreElements());
    }
}
