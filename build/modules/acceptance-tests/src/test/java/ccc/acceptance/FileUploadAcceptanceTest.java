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
package ccc.acceptance;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;

import ccc.rest.dto.ResourceDto;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.dto.RevisionDto;
import ccc.rest.dto.TextFileDelta;
import ccc.rest.exceptions.RestException;
import ccc.types.FailureCode;
import ccc.types.MimeType;


/**
 * Tests for file upload.
 *
 * @author Civic Computing Ltd.
 */
public class FileUploadAcceptanceTest
    extends
        AbstractAcceptanceTest {

    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testCancelWorkingCopy() throws Exception {

        // ARRANGE
        final String fName = UUID.randomUUID().toString();
        final ResourceSummary content =
            getCommands().resourceForPath("");
        final ResourceSummary file = createFile(fName, "Hello!", content);

        getCommands().lock(file.getId());
        getCommands().createWorkingCopy(
            file.getId(), new ResourceDto(Long.valueOf(0)));

        // ACT
        getCommands().clearWorkingCopy(file.getId());

        // ASSERT
        final ResourceSummary fWC = getCommands().resource(file.getId());
        assertEquals("Hello!", previewContent(file, false));
        assertFalse(fWC.isHasWorkingCopy());
    }

    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testWorkingCopySupport() throws Exception {

        // ARRANGE
        final String fName = UUID.randomUUID().toString();
        final ResourceSummary content = getCommands().resourceForPath("");
        final ResourceSummary file = createFile(fName, "Hello!", content);

        // History for first revision
        Collection<RevisionDto> revs = getCommands().history(file.getId());
        assertEquals(1, revs.size());
        final RevisionDto rev1 = revs.iterator().next();
        assertEquals(0, rev1.getIndex());
        assertEquals("Hello!", previewContent(file, false));

        // Add a second revision
        getCommands().lock(file.getId());
        updateTextFile("Update!", file);
        revs = getCommands().history(file.getId());
        assertEquals(2, revs.size());
        Iterator<RevisionDto> i = revs.iterator();
        i.next();
        final RevisionDto rev2 = i.next();
        assertEquals(1, rev2.getIndex());
        assertEquals("Update!", previewContent(file, false));

        // Create working copy from rev 0.
        getCommands().createWorkingCopy(
            file.getId(), new ResourceDto(Long.valueOf(0)));
        ResourceSummary fWC = getCommands().resource(file.getId());
        assertEquals("Update!", previewContent(file, false));
        assertEquals("Hello!", previewContent(file, true));
        assertTrue(fWC.isHasWorkingCopy());

        // Apply working copy
        getCommands().applyWorkingCopy(file.getId());
        revs = getCommands().history(file.getId());
        assertEquals(3, revs.size());
        i = revs.iterator();
        i.next();
        i.next();
        final RevisionDto rev3 = i.next();
        assertEquals(2, rev3.getIndex());
        assertEquals("Hello!", previewContent(file, false));
        fWC = getCommands().resource(file.getId());
        assertFalse(fWC.isHasWorkingCopy());
    }

    /**
     * Test.
     *
     * @throws IOException If the test fails.
     * @throws RestException If the test fails.
     */
    public void testCreateFile() throws IOException, RestException {

        // ARRANGE
        final String fName = UUID.randomUUID().toString();
        final ResourceSummary filesFolder =
            getCommands().resourceForPath("/files");

        // ACT
        final ResourceSummary rs =
            createFile(fName, "Hello!", filesFolder);

        // ASSERT
        assertEquals(fName, rs.getName());
        assertEquals("/files/"+fName, rs.getAbsolutePath());
        assertEquals("Hello!", previewContent(rs, false));
    }


    /**
     * Test.
     *
     * @throws IOException If the test fails.
     * @throws RestException If the test fails.
     */
    public void testCreateFileRejectsDuplicateNames()
    throws IOException, RestException {

        // ARRANGE
        final String fName = UUID.randomUUID().toString();
        final ResourceSummary filesFolder =
            getCommands().resourceForPath("/files");
        final ResourceSummary rs =
            createFile(fName, "Hello!", filesFolder);

        // ACT
        try {
            createFile(fName, "Hello!", filesFolder);
        } catch (final RestException e) {
            assertEquals(FailureCode.EXISTS, e.getCode());
        }


        // ASSERT
        assertEquals(fName, rs.getName());
        assertEquals("/files/"+fName, rs.getAbsolutePath());
        assertEquals("Hello!", previewContent(rs, false));
    }


    /**
     * Test.
     *
     * @throws IOException If the test fails.
     * @throws RestException If the test fails.
     */
    public void testUpdateFile() throws IOException, RestException {

        // ARRANGE
        final String fName = UUID.randomUUID().toString();
        final ResourceSummary filesFolder =
            getCommands().resourceForPath("/files");
        final ResourceSummary rs =
            createFile(fName, "Hello!", filesFolder);
        getCommands().lock(rs.getId());

        // ACT
        final String body = updateTextFile("Update!", rs);

        // ASSERT
        assertEquals("NULL", body);
        assertEquals("Update!", previewContent(rs, false));
    }


    /**
     * Test.
     *
     * @throws IOException If the test fails.
     * @throws RestException If the test fails.
     */
    public void testUpdateFileViaApi() throws IOException, RestException {

        // ARRANGE
        final String fName = UUID.randomUUID().toString();
        final ResourceSummary filesFolder =
            getCommands().resourceForPath("/files");
        final ResourceSummary rs =
            createFile(fName, "Hello!", filesFolder);
        getCommands().lock(rs.getId());

        // ACT
        getFiles().update(rs.getId(), new TextFileDelta(
            rs.getId(),
            "Update!",
            new MimeType("text", "plain"),
            true,
            "Meh."
        ));

        // ASSERT
        assertEquals("Update!", previewContent(rs, false));
    }


    /**
     * Test.
     *
     * @throws IOException If the test fails.
     * @throws RestException If the test fails.
     */
    public void testUpdateFileRequiresLock() throws IOException,
                                                    RestException {

        // ARRANGE
        final String fName = UUID.randomUUID().toString();
        final ResourceSummary filesFolder =
            getCommands().resourceForPath("/files");
        final ResourceSummary rs =
            createFile(fName, "Hello!", filesFolder);

        // ACT
        try {
            updateTextFile("Update!", rs);

        // ASSERT
        } catch (final RestException e) {
            assertEquals(FailureCode.UNLOCKED, e.getCode());
        }
        assertEquals("Hello!", previewContent(rs, false));
    }
}
