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
package ccc.tests.acceptance;

import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;

import ccc.api.core.File;
import ccc.api.core.Resource;
import ccc.api.core.ResourceSummary;
import ccc.api.types.MimeType;


/**
 * Tests for the content servlet.
 *
 * @author Civic Computing Ltd.
 */
public class ContentServletAcceptanceTest
    extends
        AbstractAcceptanceTest {

    /**
     * Test.
     */
    public void testPostRedirectReturns302() {

        // ARRANGE
        final Resource metadata = new Resource();
        metadata.setTitle("foo");
        metadata.setDescription("foo");
        metadata.setTags(new HashSet<String>());
        metadata.setMetadata(Collections.singletonMap("executable", "true"));
        final String fName = UUID.randomUUID().toString();
        final ResourceSummary filesFolder =
            getCommands().resourceForPath("");
        final ResourceSummary script =
            getFiles().createTextFile(
                new File(
                    filesFolder.getId(),
                    fName,
                    MimeType.TEXT,
                    true,
                    "",
                    "response.sendRedirect("
                        + "request.getContextPath()+'/welcome');"));
        getCommands().lock(script.getId());
        getCommands().updateMetadata(script.getId(), metadata);
        getCommands().publish(script.getId());

        // ACT
        try {
            getBrowser().post(script);
            fail();

            // ASSERT
        } catch (final RuntimeException e) {
            assertTrue(is302(e));
        }
    }

    /**
     * Test.
     */
    public void testForwardedRequestsForMissingResourcesReturn500() {

        // ARRANGE
        final Resource metadata = new Resource();
        metadata.setTitle("foo");
        metadata.setDescription("foo");
        metadata.setTags(new HashSet<String>());
        metadata.setMetadata(Collections.singletonMap("executable", "true"));
        final String fName = UUID.randomUUID().toString();
        final ResourceSummary filesFolder =
            getCommands().resourceForPath("/files");
        final ResourceSummary script =
            getFiles().createTextFile(
                new File(
                    filesFolder.getId(),
                    fName,
                    MimeType.TEXT,
                    true,
                    "",
                    "request.getRequestDispatcher(\"/doesnt/exist\")"
                    + ".forward(request,  response);"));
        getCommands().lock(script.getId());
        getCommands().updateMetadata(script.getId(), metadata);
        getCommands().publish(script.getId());

        // ACT
        try {
            getBrowser().previewContent(script, false);
            fail();

            // ASSERT
        } catch (final RuntimeException e) {
            assertTrue(is500(e));
        }
    }


    /**
     * Test.
     */
    public void testCommittedResponsesHandledDirectly() {

        // ARRANGE
        final Resource metadata = new Resource();
        metadata.setTitle("fail.js");
        metadata.setDescription("fail.js");
        metadata.setTags(new HashSet<String>());
        metadata.setMetadata(Collections.singletonMap("executable", "true"));
        final String fName = UUID.randomUUID().toString();
        final ResourceSummary filesFolder =
            getCommands().resourceForPath("/files");
        final ResourceSummary script =
            getFiles().createTextFile(
                new File(
                    filesFolder.getId(),
                    fName,
                    MimeType.TEXT,
                    true,
                    "",
                    "print('foo\\n'); response.flushBuffer(); throw 'foo';"));
        getCommands().lock(script.getId());
        getCommands().updateMetadata(script.getId(), metadata);
        getCommands().publish(script.getId());

        // ACT
        final String content = getBrowser().previewContent(script, false);

        // ASSERT
        assertTrue(content.startsWith("foo\nAn error occurred: "));
    }


    /**
     * Test.
     */
    public void testUncommittedResponsesHandledByJsp() {

        // ARRANGE
        final Resource metadata = new Resource();
        metadata.setTitle("fail.js");
        metadata.setDescription("fail.js");
        metadata.setTags(new HashSet<String>());
        metadata.setMetadata(Collections.singletonMap("executable", "true"));
        final String fName = UUID.randomUUID().toString();
        final ResourceSummary filesFolder =
            getCommands().resourceForPath("/files");
        final ResourceSummary script =
            getFiles().createTextFile(
                new File(
                    filesFolder.getId(),
                    fName,
                    MimeType.TEXT,
                    true,
                    "",
                    "throw 'foo';"));
        getCommands().lock(script.getId());
        getCommands().updateMetadata(script.getId(), metadata);
        getCommands().publish(script.getId());

        // ACT
        try {
            getBrowser().previewContent(script, false);
            fail();

        // ASSERT
        } catch (final RuntimeException e) {
            assertTrue(is500(e));
        }
    }


    private boolean is500(final RuntimeException e) {
        return e.getMessage().startsWith("500: <!-- An error occurred: ");
    }


    private boolean is302(final RuntimeException e) {
        return e.getMessage().startsWith("302: ");
    }
}
