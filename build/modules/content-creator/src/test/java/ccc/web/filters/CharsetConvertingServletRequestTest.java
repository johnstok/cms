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

import static ccc.web.TmpRenderer.*;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;

import junit.framework.TestCase;


/**
 * Tests for the {@link CharsetConvertingServletRequest}.
 *
 * @author Civic Computing Ltd.
 */
public class CharsetConvertingServletRequestTest
    extends
        TestCase {


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testBodyUsesCharsetWhenAvailable() throws Exception {

        // ARRANGE
        final String qString   =
            URLEncoder.encode("‡", "UTF-16")
            +"="
            +URLEncoder.encode("案例学习", "UTF-16");
        final ServletRequestStub reqStub =
            new ServletRequestStub(null, "/foo", "/bar");
        reqStub.setEntity(qString.getBytes("ASCII"));
        reqStub.setCharacterEncoding("UTF-16");
        reqStub.setContentType("application/x-www-form-urlencoded");
        final CharsetConvertingServletRequest req =

            // ACT
            new CharsetConvertingServletRequest(reqStub);

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
    public void testBodyTreatsMissingCharsetAsUtf8() throws Exception {

        // ARRANGE
        final String qString   =
            URLEncoder.encode("‡", DEFAULT_CHARSET)
            +"="
            +URLEncoder.encode("案例学习", DEFAULT_CHARSET);
        final ServletRequestStub reqStub =
            new ServletRequestStub(null, "/foo", "/bar");
        reqStub.setEntity(qString.getBytes("ASCII"));
        reqStub.setContentType("application/x-www-form-urlencoded");
        final CharsetConvertingServletRequest req =

            // ACT
            new CharsetConvertingServletRequest(reqStub);

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
    public void testUrlQueryHandlesUnicode() throws Exception {

        // ARRANGE
        final String qString   =
            URLEncoder.encode("‡", DEFAULT_CHARSET)
            +"="
            +URLEncoder.encode("案例学习", DEFAULT_CHARSET);
        final ServletRequestStub reqStub =
            new ServletRequestStub(null, "/foo", "/bar");
        reqStub.setQueryString(qString);
        final CharsetConvertingServletRequest req =

        // ACT
            new CharsetConvertingServletRequest(reqStub);

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
    public void testUrlQueryHandlesInvalid() throws Exception {

        // ARRANGE
        final String qString =
            "&&=a"
            +"&a=b="
            +"&a=b=c"
            +"&=b=c"
            +"&a==c"
            +"&=a==c="
            +"&a"
            +"&&";
        final ServletRequestStub reqStub =
            new ServletRequestStub(null, "/foo", "/bar");
        reqStub.setQueryString(qString);

        // ACT
        final CharsetConvertingServletRequest req =
            new CharsetConvertingServletRequest(reqStub);

        // ASSERT
        final Map<String, String[]> actualParams = req.getParameterMap();
        assertEquals(0, actualParams.size());

        final Enumeration<String> actualNames = req.getParameterNames();
        assertFalse(actualNames.hasMoreElements());
    }


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testUrlQueryHandlesEmpty() throws Exception {

        // ARRANGE
        final String qString   =
            URLEncoder.encode("‡", DEFAULT_CHARSET)+"=";
        final ServletRequestStub reqStub =
            new ServletRequestStub(null, "/foo", "/bar");
        reqStub.setQueryString(qString);

        // ACT
        final CharsetConvertingServletRequest req =
            new CharsetConvertingServletRequest(reqStub);

        // ASSERT
        assertEquals("", req.getParameter("‡"));

        final Map<String, String[]> actualParams = req.getParameterMap();
        assertEquals(1, actualParams.size());
        assertEquals(Collections.singleton("‡"), actualParams.keySet());
        assertTrue(Arrays.equals(new String[] {""}, actualParams.get("‡")));

        assertTrue(Arrays.equals(
            new String[] {""},
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
    public void testUrlQueryHandlesMultipleKeys() throws Exception {

        // ARRANGE
        final String qString   =
            URLEncoder.encode("a", DEFAULT_CHARSET)
            +"="
            +URLEncoder.encode("b", DEFAULT_CHARSET)
            +"&"
            +URLEncoder.encode("c", DEFAULT_CHARSET)
            +"="
            +URLEncoder.encode("d", DEFAULT_CHARSET);
        final ServletRequestStub reqStub =
            new ServletRequestStub(null, "/foo", "/bar");
        reqStub.setQueryString(qString);

        // ACT
        final CharsetConvertingServletRequest req =
            new CharsetConvertingServletRequest(reqStub);

        // ASSERT
        assertEquals("b", req.getParameter("a"));
        assertEquals("d", req.getParameter("c"));

        final Map<String, String[]> actualParams = req.getParameterMap();
        assertEquals(2, actualParams.size());
        assertEquals(
            new HashSet<String>() {{add("a"); add("c"); }},
            actualParams.keySet());
        assertTrue(Arrays.equals(new String[] {"b"}, actualParams.get("a")));
        assertTrue(Arrays.equals(new String[] {"d"}, actualParams.get("c")));

        assertTrue(Arrays.equals(
            new String[] {"b"},
            req.getParameterValues("a")));
        assertTrue(Arrays.equals(
            new String[] {"d"},
            req.getParameterValues("c")));

        final Enumeration<String> actualNames = req.getParameterNames();
        assertEquals("c", actualNames.nextElement());
        assertEquals("a", actualNames.nextElement());
        assertFalse(actualNames.hasMoreElements());
    }


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testUrlQueryHandlesMultipleValues() throws Exception {

        // ARRANGE
        final String qString   =
            URLEncoder.encode("foo", DEFAULT_CHARSET)
            +"="
            +URLEncoder.encode("bar", DEFAULT_CHARSET)
            +"&"
            +URLEncoder.encode("foo", DEFAULT_CHARSET)
            +"="
            +URLEncoder.encode("baz", DEFAULT_CHARSET);
        final ServletRequestStub reqStub =
            new ServletRequestStub(null, "/foo", "/bar");
        reqStub.setQueryString(qString);

        // ACT
        final CharsetConvertingServletRequest req =
            new CharsetConvertingServletRequest(reqStub);

        // ASSERT
        assertEquals("bar", req.getParameter("foo"));

        final Map<String, String[]> actualParams = req.getParameterMap();
        assertEquals(1, actualParams.size());
        assertEquals(Collections.singleton("foo"), actualParams.keySet());
        assertTrue(Arrays.equals(
            new String[] {"bar", "baz"}, actualParams.get("foo")));

        assertTrue(Arrays.equals(
            new String[] {"bar", "baz"},
            req.getParameterValues("foo")));

        final Enumeration<String> actualNames = req.getParameterNames();
        assertEquals("foo", actualNames.nextElement());
        assertFalse(actualNames.hasMoreElements());
    }


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testUrlQueryHandlesDuplicates() throws Exception {

        // ARRANGE
        final String qString   =
            URLEncoder.encode("foo", DEFAULT_CHARSET)
            +"="
            +URLEncoder.encode("bar", DEFAULT_CHARSET)
            +"&"
            +URLEncoder.encode("foo", DEFAULT_CHARSET)
            +"="
            +URLEncoder.encode("bar", DEFAULT_CHARSET);
        final ServletRequestStub reqStub =
            new ServletRequestStub(null, "/foo", "/bar");
        reqStub.setQueryString(qString);

        // ACT
        final CharsetConvertingServletRequest req =
            new CharsetConvertingServletRequest(reqStub);

        // ASSERT
        assertEquals("bar", req.getParameter("foo"));

        final Map<String, String[]> actualParams = req.getParameterMap();
        assertEquals(1, actualParams.size());
        assertEquals(Collections.singleton("foo"), actualParams.keySet());
        assertTrue(Arrays.equals(
            new String[] {"bar", "bar"}, actualParams.get("foo")));

        assertTrue(Arrays.equals(
            new String[] {"bar", "bar"},
            req.getParameterValues("foo")));

        final Enumeration<String> actualNames = req.getParameterNames();
        assertEquals("foo", actualNames.nextElement());
        assertFalse(actualNames.hasMoreElements());
    }
}
