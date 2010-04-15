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
package ccc.tests.acceptance;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

import ccc.api.dto.FileDto;
import ccc.api.dto.ResourceDto;
import ccc.api.dto.ResourceSummary;
import ccc.api.dto.RevisionDto;
import ccc.api.dto.TextFileDelta;
import ccc.api.dto.TextFileDto;
import ccc.api.exceptions.InvalidException;
import ccc.api.exceptions.RestException;
import ccc.api.types.FailureCode;
import ccc.api.types.MimeType;
import ccc.api.types.ResourceName;


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
        final ResourceSummary file =
            getFiles().createTextFile(
                new TextFileDto(
                    content.getId(),
                    fName,
                    MimeType.TEXT,
                    true,
                    "",
                    "Hello!"));

        getCommands().lock(file.getId());
        getCommands().createWorkingCopy(
            file.getId(), new ResourceDto(Long.valueOf(0)));

        // ACT
        getCommands().clearWorkingCopy(file.getId());

        // ASSERT
        final ResourceSummary fWC = getCommands().resource(file.getId());
        assertEquals("Hello!", getBrowser().previewContent(file, false));
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
        final ResourceSummary file =
            getFiles().createTextFile(
                new TextFileDto(
                    content.getId(),
                    fName,
                    MimeType.TEXT,
                    true,
                    "",
                    "Hello!"));

        // History for first revision
        Collection<RevisionDto> revs = getCommands().history(file.getId());
        assertEquals(1, revs.size());
        final RevisionDto rev1 = revs.iterator().next();
        assertEquals(0, rev1.getIndex());
        assertEquals("Hello!", getBrowser().previewContent(file, false));

        // Add a second revision
        getCommands().lock(file.getId());
        getFiles().update(
            file.getId(),
            new TextFileDelta(
                file.getId(), "Update!", MimeType.TEXT, true, ""));
        revs = getCommands().history(file.getId());
        assertEquals(2, revs.size());
        Iterator<RevisionDto> i = revs.iterator();
        i.next();
        final RevisionDto rev2 = i.next();
        assertEquals(1, rev2.getIndex());
        assertEquals("Update!", getBrowser().previewContent(file, false));

        // Create working copy from rev 0.
        getCommands().createWorkingCopy(
            file.getId(), new ResourceDto(Long.valueOf(0)));
        ResourceSummary fWC = getCommands().resource(file.getId());
        assertEquals("Update!", getBrowser().previewContent(file, false));
        assertEquals("Hello!", getBrowser().previewContent(file, true));
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
        assertEquals("Hello!", getBrowser().previewContent(file, false));
        fWC = getCommands().resource(file.getId());
        assertFalse(fWC.isHasWorkingCopy());
    }

    /**
     * Test.
     */
    public void testCreateBinaryFile() {

        // ARRANGE
        final String fName = "log4j.properties";
        final ResourceSummary filesFolder = tempFolder();
        final FileDto f = new FileDto(
            new MimeType("application", "octet-stream"),
            null,
            null,
            new ResourceName(fName),
            fName,
            new HashMap<String, String>()
        );

        f.setDescription(fName);
        f.setParent(filesFolder.getId());
        f.setInputStream(new ByteArrayInputStream(new byte[] {0, 1, 2, 3, 4}));
        f.setSize(5);
        f.setPublished(false);
        f.setComment("Testing 1, 2, 3.");

        // ACT
        final ResourceSummary rs = getFiles().createFile(f);

        // ASSERT
        assertEquals(fName, rs.getName());
        assertEquals(filesFolder.getId(), rs.getParent());
    }

    /**
     * Test.
     */
    public void testCreateFile() {

        // ARRANGE
        final String fName = UUID.randomUUID().toString();
        final ResourceSummary filesFolder =
            getCommands().resourceForPath("/files");

        // ACT
        final ResourceSummary rs =
            getFiles().createTextFile(
                new TextFileDto(
                    filesFolder.getId(),
                    fName,
                    MimeType.TEXT,
                    true,
                    "",
                    "Hello!"));

        // ASSERT
        assertEquals(fName, rs.getName());
        assertEquals("/files/"+fName, rs.getAbsolutePath());
        assertEquals("Hello!", getBrowser().previewContent(rs, false));
    }


    /**
     * Test.
     */
    public void testCreateFileRejectsDuplicateNames() {

        // ARRANGE
        final String fName = UUID.randomUUID().toString();
        final ResourceSummary filesFolder =
            getCommands().resourceForPath("/files");
        final ResourceSummary rs =
            getFiles().createTextFile(
                new TextFileDto(
                    filesFolder.getId(),
                    fName,
                    MimeType.TEXT,
                    true,
                    "",
                    "Hello!"));

        // ACT
        try {
            getFiles().createTextFile(
                new TextFileDto(
                    filesFolder.getId(),
                    fName,
                    MimeType.TEXT,
                    true,
                    "",
                    "Hello!"));
        } catch (final RestException e) {
            assertEquals(FailureCode.EXISTS, e.getCode());
        }


        // ASSERT
        assertEquals(fName, rs.getName());
        assertEquals("/files/"+fName, rs.getAbsolutePath());
        assertEquals("Hello!", getBrowser().previewContent(rs, false));
    }


    /**
     * Test.
     */
    public void testUpdateFile() {

        // ARRANGE
        final String fName = UUID.randomUUID().toString();
        final ResourceSummary filesFolder =
            getCommands().resourceForPath("/files");
        final ResourceSummary rs =
            getFiles().createTextFile(
                new TextFileDto(
                    filesFolder.getId(),
                    fName,
                    MimeType.TEXT,
                    true,
                    "",
                    "Hello!"));
        getCommands().lock(rs.getId());

        // ACT
        getFiles().update(
            rs.getId(),
            new TextFileDelta(
                rs.getId(), "Update!", MimeType.TEXT, true, ""));

        // ASSERT
        assertEquals("Update!", getBrowser().previewContent(rs, false));
    }


    /**
     * Test.
     *
     * @throws IOException If the test fails.
     */
    public void testUpdateBinaryFile() throws IOException {

        // ARRANGE
        final String fName = UUID.randomUUID().toString();
        final ResourceSummary filesFolder =
            getCommands().resourceForPath("/files");
        final ResourceSummary rs =
            getFiles().createTextFile(
                new TextFileDto(
                    filesFolder.getId(),
                    fName,
                    MimeType.TEXT,
                    true,
                    "",
                    "Hello!"));
        getCommands().lock(rs.getId());
        final byte[] updateBytes = "Update!".getBytes("UTF-8");
        final FileDto f = new FileDto(
            new MimeType("text", "plain"),
            null,
            rs.getId(),
            new ResourceName(rs.getName()),
            rs.getTitle(),
            new HashMap<String, String>()
        );
        f.setDescription(rs.getDescription());
        f.setParent(rs.getParent());
        f.setInputStream(new ByteArrayInputStream(updateBytes));
        f.setSize(updateBytes.length);
        f.setPublished(false);
        f.setComment("Test update.");

        // ACT
        final ResourceSummary fs = getFiles().updateFile(rs.getId(), f);

        // ASSERT
        assertEquals("Update!", getBrowser().previewContent(rs, false));
    }


    /**
     * Test.
     */
    public void testUpdateFileViaApi() {

        // ARRANGE
        final String fName = UUID.randomUUID().toString();
        final ResourceSummary filesFolder =
            getCommands().resourceForPath("/files");
        final ResourceSummary rs =
            getFiles().createTextFile(
                new TextFileDto(
                    filesFolder.getId(),
                    fName,
                    MimeType.TEXT,
                    true,
                    "",
                    "Hello!"));
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
        assertEquals("Update!", getBrowser().previewContent(rs, false));
    }


    /**
     * Test.
     */
    public void testUpdateFileRequiresLock() {

        // ARRANGE
        final String fName = UUID.randomUUID().toString();
        final ResourceSummary filesFolder =
            getCommands().resourceForPath("/files");
        final ResourceSummary rs =
            getFiles().createTextFile(
                new TextFileDto(
                    filesFolder.getId(),
                    fName,
                    MimeType.TEXT,
                    true,
                    "",
                    "Hello!"));

        // ACT
        try {
            getFiles().update(
                rs.getId(),
                new TextFileDelta(
                    rs.getId(), "Update!", MimeType.TEXT, true, ""));

        // ASSERT
        } catch (final InvalidException e) {
            assertEquals(FailureCode.UNLOCKED, e.getCode());
        }
        assertEquals("Hello!", getBrowser().previewContent(rs, false));
    }
}
