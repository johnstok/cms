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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.FilterChain;

import junit.framework.TestCase;
import ccc.commons.Testing;
import ccc.rendering.RedirectRequiredException;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.extensions.ResourcesExt;
import ccc.types.ResourceType;


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
            new ServletRequestStub(
                "/shc", "", goodPath, new HashMap<String, String>()),
                null,
                Testing.stub(FilterChain.class));

        // ASSERT
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
                new ServletRequestStub(
                    "/shc", "", badPath, new HashMap<String, String>()),
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
                new ServletRequestStub(
                    "/shc", "", badPath, new HashMap<String, String>()),
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
                new ServletRequestStub(
                    "/shc", "", badPath, new HashMap<String, String>()),
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
        expect(_resources.lookupWithLegacyId("415")).andStubReturn(
            new ResourceSummary(
                UUID.randomUUID(),
                null,
                "bar",
                null,
                "", null,
                ResourceType.PAGE,
                0,
                0,
                false,
                null,
                false,
                new Date(),
                new Date(),
                null,
                "",
                "/bar",
                null,
                "",
                null,
                null));
        replayAll();

        final LegacyLinkFilter f = new LegacyLinkFilter(_resources);
        final String goodPath = "/foo";
        final Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("p_applic", "CCC");
        queryParams.put("p_service", "Content.show");
        queryParams.put("pContentID", "415");

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
        _resources = createStrictMock(ResourcesExt.class);
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


    private ResourcesExt _resources;
}
