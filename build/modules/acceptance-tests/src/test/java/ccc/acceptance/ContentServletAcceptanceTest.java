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
package ccc.acceptance;

import java.util.Collections;
import java.util.UUID;

import ccc.rest.dto.ResourceSummary;
import ccc.serialization.Json;
import ccc.serialization.JsonImpl;


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
     *
     * @throws Exception If the test fails.
     */
    public void testCommittedResponsesHandledDirectly() throws Exception {

        // ARRANGE
        final Json metadata = new JsonImpl();
        metadata.set("title", "fail.js");
        metadata.set("description", "fail.js");
        metadata.set("tags", "");
        metadata.set(
            "metadata", Collections.singletonMap("executable", "true"));
        final String fName = UUID.randomUUID().toString();
        final ResourceSummary filesFolder = resourceForPath("/files");
        final ResourceSummary script = createFile(
            fName,
            "print('foo\\n'); response.flushBuffer(); throw 'foo';",
            filesFolder);
        getCommands().lock(script.getId());
        getCommands().updateMetadata(script.getId(), metadata);
        getCommands().publish(script.getId());

        // ACT
        final String content = previewContent(script, false);

        // ASSERT
        assertTrue(content.startsWith("foo\nAn error occurred: "));
    }


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testUncommittedResponsesHandledByJsp() throws Exception {

        // ARRANGE
        final Json metadata = new JsonImpl();
        metadata.set("title", "fail.js");
        metadata.set("description", "fail.js");
        metadata.set("tags", "");
        metadata.set(
            "metadata", Collections.singletonMap("executable", "true"));
        final String fName = UUID.randomUUID().toString();
        final ResourceSummary filesFolder = resourceForPath("/files");
        final ResourceSummary script = createFile(
            fName,
            "throw 'foo';",
            filesFolder);
        getCommands().lock(script.getId());
        getCommands().updateMetadata(script.getId(), metadata);
        getCommands().publish(script.getId());

        // ACT
        try {
            previewContent(script, false);
            fail();

        // ASSERT
        } catch (final RuntimeException e) {
            assertTrue(
                e.getMessage().startsWith("500: <!-- An error occurred: "));
        }
    }
}
