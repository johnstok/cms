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



import static org.easymock.EasyMock.*;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;

import junit.framework.TestCase;
import ccc.api.core.ResourceSummary;
import ccc.api.synchronous.Resources;
import ccc.commons.Testing;
import ccc.web.rendering.RedirectRequiredException;


/**
 * Tests for the {@link LegacyLinkFilter} class.
 *
 * @author Civic Computing Ltd.
 */
public class LegacyLinkFilterTest
    extends
        TestCase {


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testDoesntRedirect() throws Exception {

        // ARRANGE
        final LegacyLinkFilter f = new LegacyLinkFilter();
        final String goodPath = "/images/titles/organise.jpg";


        // ACT
        f.doFilter(
            new ServletRequestStub("/shc", "", goodPath),
            null,
            Testing.stub(FilterChain.class));

        // ASSERT
    }


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testHandlesSimpleNumberedPages() throws Exception {

        // ARRANGE
        final ResourceSummary rs = new ResourceSummary();
        rs.setAbsolutePath("/foo");
        final LegacyLinkFilter f = new LegacyLinkFilter(_resources);
        final String badPath = "/1234.html";

        // EXPECT
        expect(_resources.resourceForLegacyId("1234")).andReturn(rs);
        replayAll();


        // ACT
        try {
            f.doFilter(
                new ServletRequestStub("/shc", "", badPath),
                null,
                null);

        // ASSERT
        } catch (final RedirectRequiredException rre) {
            assertTrue(rre.isPermanent());
            assertEquals(rs.getAbsolutePath(), rre.getTarget());
        }
        verifyAll();
    }


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testHandlesComplexNumberedPages() throws Exception {

        // ARRANGE
        final ResourceSummary rs = new ResourceSummary();
        rs.setAbsolutePath("/foo");
        final LegacyLinkFilter f = new LegacyLinkFilter(_resources);
        final String badPath = "/141.1.81.htm";

        // EXPECT
        expect(_resources.resourceForLegacyId("141")).andReturn(rs);
        replayAll();


        // ACT
        try {
            f.doFilter(
                new ServletRequestStub("/shc", "", badPath),
                null,
                null);

            // ASSERT
        } catch (final RedirectRequiredException rre) {
            assertTrue(rre.isPermanent());
            assertEquals(rs.getAbsolutePath(), rre.getTarget());
        }
        verifyAll();
    }


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testHandlesPathsWithSlash() throws Exception {

        // ARRANGE
        final LegacyLinkFilter f = new LegacyLinkFilter();
        final String badPath = "/images/stories/story 01.jpg";


        // ACT
        try {
            f.doFilter(
                new ServletRequestStub("/shc", "", badPath),
                null,
                null);

        // ASSERT
        } catch (final RedirectRequiredException rre) {
            assertTrue(rre.isPermanent());
            assertEquals("/images/stories/story_01.jpg", rre.getTarget());
        }
    }


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testHandlesFilesWithSpaces() throws Exception {

        // ARRANGE
        final LegacyLinkFilter f = new LegacyLinkFilter();
        final String badPath = "/files/shc Minutes 09.02.pdf";


        // ACT
        try {
            f.doFilter(
                new ServletRequestStub("/shc", "", badPath),
                null,
                null);

            // ASSERT
        } catch (final RedirectRequiredException rre) {
            assertTrue(rre.isPermanent());
            assertEquals("/files/shc_Minutes_09.02.pdf", rre.getTarget());
        }
    }


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testHandlesImagesWithSpaces() throws Exception {

        // ARRANGE
        final LegacyLinkFilter f = new LegacyLinkFilter();
        final String badPath = "/images/shc logo.gif";


        // ACT
        try {
            f.doFilter(
                new ServletRequestStub("/shc", "", badPath),
                null,
                null);

            // ASSERT
        } catch (final RedirectRequiredException rre) {
            assertTrue(rre.isPermanent());
            assertEquals("/images/shc_logo.gif", rre.getTarget());
        }
    }


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testHandlesControllerStyleUrls() throws Exception {

        // ARRANGE
        final ResourceSummary rs = new ResourceSummary();
        rs.setAbsolutePath("/bar");
        expect(_resources.resourceForLegacyId("415")).andStubReturn(rs);
        replayAll();

        final LegacyLinkFilter f = new LegacyLinkFilter(_resources);
        final String goodPath = "/foo";
        final Map<String, String[]> queryParams =
            new HashMap<String, String[]>();
        queryParams.put("p_applic",   new String[] {"CCC"});
        queryParams.put("p_service",  new String[] {"Content.show"});
        queryParams.put("pContentID", new String[] {"415"});

        // ACT
        try {
            f.doFilter(
                new ServletRequestStub("/shc", "", goodPath, queryParams),
                null,
                null);

        // ASSERT
        } catch (final RedirectRequiredException rre) {
            assertTrue(rre.isPermanent());
            assertEquals("/bar", rre.getTarget());
            verifyAll();
        }
    }




    /**
     * {@inheritDoc}
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        _resources = createStrictMock(Resources.class);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        _resources = null;
    }


    private void verifyAll() {
        verify(_resources);
    }


    private void replayAll() {
        replay(_resources);
    }


    private Resources _resources;
}
