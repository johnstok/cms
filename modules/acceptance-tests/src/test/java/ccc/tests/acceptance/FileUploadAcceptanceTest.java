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

import ccc.api.core.File;
import ccc.api.core.Resource;
import ccc.api.core.ResourceSummary;
import ccc.api.core.Revision;
import ccc.api.exceptions.ResourceExistsException;
import ccc.api.exceptions.UnlockedException;
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
     */
    public void testCancelWorkingCopy() {

        // ARRANGE
        final String fName = UUID.randomUUID().toString();
        final ResourceSummary content =
            getCommands().resourceForPath("");
        final ResourceSummary file =
            getFiles().createTextFile(
                new File(
                    content.getId(),
                    fName,
                    MimeType.TEXT,
                    true,
                    "",
                    "Hello!"));

        getCommands().lock(file.getId());
        getCommands().createWorkingCopy(
            file.getId(), new Resource(Long.valueOf(0)));

        // ACT
        getCommands().clearWorkingCopy(file.getId());

        // ASSERT
        final ResourceSummary fWC = getCommands().retrieve(file.getId());
        assertEquals("Hello!", getBrowser().previewContent(file, false));
        assertFalse(fWC.isHasWorkingCopy());
    }

    /**
     * Test.
     */
    public void testWorkingCopySupport() {

        // ARRANGE
        final String fName = UUID.randomUUID().toString();
        final ResourceSummary content = getCommands().resourceForPath("");
        final ResourceSummary file =
            getFiles().createTextFile(
                new File(
                    content.getId(),
                    fName,
                    MimeType.TEXT,
                    true,
                    "",
                    "Hello!"));

        // History for first revision
        Collection<Revision> revs =
            getCommands().history(file.getId()).getElements();
        assertEquals(1, revs.size());
        final Revision rev1 = revs.iterator().next();
        assertEquals(0, rev1.getIndex());
        assertEquals("Hello!", getBrowser().previewContent(file, false));

        // Add a second revision
        getCommands().lock(file.getId());
        getFiles().update(
            file.getId(),
            new File(
                file.getId(), "Update!", MimeType.TEXT, true, ""));
        revs = getCommands().history(file.getId()).getElements();
        assertEquals(2, revs.size());
        Iterator<Revision> i = revs.iterator();
        i.next();
        final Revision rev2 = i.next();
        assertEquals(1, rev2.getIndex());
        assertEquals("Update!", getBrowser().previewContent(file, false));

        // Create working copy from rev 0.
        getCommands().createWorkingCopy(
            file.getId(), new Resource(Long.valueOf(0)));
        ResourceSummary fWC = getCommands().retrieve(file.getId());
        assertEquals("Update!", getBrowser().previewContent(file, false));
        assertEquals("Hello!", getBrowser().previewContent(file, true));
        assertTrue(fWC.isHasWorkingCopy());

        // Apply working copy
        getCommands().applyWorkingCopy(file.getId());
        revs = getCommands().history(file.getId()).getElements();
        assertEquals(3, revs.size());
        i = revs.iterator();
        i.next();
        i.next();
        final Revision rev3 = i.next();
        assertEquals(2, rev3.getIndex());
        assertEquals("Hello!", getBrowser().previewContent(file, false));
        fWC = getCommands().retrieve(file.getId());
        assertFalse(fWC.isHasWorkingCopy());
    }

    /**
     * Test.
     */
    public void testCreateBinaryFile() {

        // ARRANGE
        final String fName = "log4j.properties";
        final ResourceSummary filesFolder = tempFolder();
        final File f = new File(
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

        // ACT
        final ResourceSummary rs = getFiles().create(f);

        // ASSERT
        final File actual = getFiles().retrieve(rs.getId());
        assertEquals(fName, rs.getName());
        assertEquals(filesFolder.getId(), rs.getParent());
        assertEquals(null, actual.getComment());
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
                new File(
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
        final File f = getFiles().retrieve(rs.getId());
        assertEquals("UTF-8", f.getProperties().get("text.charset"));
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
                new File(
                    filesFolder.getId(),
                    fName,
                    MimeType.TEXT,
                    true,
                    "",
                    "Hello!"));

        // ACT
        try {
            getFiles().createTextFile(
                new File(
                    filesFolder.getId(),
                    fName,
                    MimeType.TEXT,
                    true,
                    "",
                    "Hello!"));
        } catch (final ResourceExistsException e) {
            assertEquals(rs.getId(), e.getResourceId());
            assertEquals(fName, e.getResourceName());
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
                new File(
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
            new File(
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
                new File(
                    filesFolder.getId(),
                    fName,
                    MimeType.TEXT,
                    true,
                    "",
                    "Hello!"));
        getCommands().lock(rs.getId());
        final byte[] updateBytes = "Update!".getBytes("UTF-8");
        final File f = new File(
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
        getFiles().updateFile(rs.getId(), f);

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
                new File(
                    filesFolder.getId(),
                    fName,
                    MimeType.TEXT,
                    true,
                    "",
                    "Hello!"));
        getCommands().lock(rs.getId());

        // ACT
        getFiles().update(rs.getId(), new File(
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
    public void testGetFileContentsFromPath() {

        // ARRANGE
        final String fName = UUID.randomUUID().toString();
        final ResourceSummary filesFolder =
            getCommands().resourceForPath("/files");
        final ResourceSummary rs =
            getFiles().createTextFile(
                new File(
                    filesFolder.getId(),
                    fName,
                    MimeType.TEXT,
                    true,
                    "",
                    "Hello!"));

        // ACT
        final String content =
            getCommands().fileContentsFromPath(rs.getAbsolutePath(), "UTF-8");

        // ASSERT
        assertEquals("Hello!", content);
    }


    /**
     * Test.
     */
    public void testGetFileContentsIsEmptyForMissingPath() {

        // ARRANGE

        // ACT
        final String content =
            getCommands().fileContentsFromPath(
                "/this/path/doesnt/exist", "UTF-8");

        // ASSERT
        assertEquals("", content);
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
                new File(
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
                new File(
                    rs.getId(), "Update!", MimeType.TEXT, true, ""));

        // ASSERT
        } catch (final UnlockedException e) {
            assertEquals(rs.getId(), e.getResource());
        }
        assertEquals("Hello!", getBrowser().previewContent(rs, false));
    }
}
