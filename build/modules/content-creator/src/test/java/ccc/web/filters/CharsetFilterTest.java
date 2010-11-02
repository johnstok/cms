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

import static ccc.commons.Testing.*;

import javax.servlet.Filter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import junit.framework.TestCase;


/**
 * Tests for the {@link CharsetFilter} class.
 *
 * @author Civic Computing Ltd.
 */
public class CharsetFilterTest
    extends
        TestCase {


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testFilterWrapsHttpServletRequests() throws Exception {

        // ARRANGE
        final Filter f = new CharsetFilter();
        final CapturingFilterChain fc = new CapturingFilterChain();

        // ACT
        f.init(null);
        f.doFilter(
            new ServletRequestStub(null, "/foo", "/bar"),
            dummy(ServletResponse.class),
            fc);
        f.destroy();

        // ASSERT
        assertTrue(fc.getRequest() instanceof CharsetConvertingServletRequest);
    }


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testFilterIgnoresNonHttpServletRequests() throws Exception {

        // ARRANGE
        final Filter f = new CharsetFilter();
        final CapturingFilterChain fc = new CapturingFilterChain();

        // ACT
        f.init(null);
        f.doFilter(
            dummy(ServletRequest.class),
            dummy(ServletResponse.class),
            fc);
        f.destroy();

        // ASSERT
        assertFalse(fc.getRequest() instanceof CharsetConvertingServletRequest);
    }
}
