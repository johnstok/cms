/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
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

import ccc.domain.CommandFailedException;
import ccc.rest.LogEntrySummary;
import ccc.rest.ResourceRevisionPU;
import ccc.rest.ResourceSummary;
import ccc.types.FailureCode;


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
        final ResourceSummary content = _queries.resourceForPath("/content");
        final ResourceSummary file = createFile(fName, "Hello!", content);

        _commands.lock(file.getId());
        _commands.createWorkingCopy(file.getId(), new ResourceRevisionPU(0));

        // ACT
        _commands.clearWorkingCopy(file.getId());

        // ASSERT
        final ResourceSummary fWC = _queries.resource(file.getId());
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
        final ResourceSummary content = _queries.resourceForPath("/content");
        final ResourceSummary file = createFile(fName, "Hello!", content);

        // History for first revision
        Collection<LogEntrySummary> revs = _queries.history(file.getId());
        assertEquals(1, revs.size());
        final LogEntrySummary rev1 = revs.iterator().next();
        assertEquals(0, rev1.getIndex());
        assertEquals("Hello!", previewContent(file, false));

        // Add a second revision
        _commands.lock(file.getId());
        updateTextFile("Update!", file);
        revs = _queries.history(file.getId());
        assertEquals(2, revs.size());
        Iterator<LogEntrySummary> i = revs.iterator();
        i.next();
        final LogEntrySummary rev2 = i.next();
        assertEquals(1, rev2.getIndex());
        assertEquals("Update!", previewContent(file, false));

        // Create working copy from rev 0.
        _commands.createWorkingCopy(file.getId(), new ResourceRevisionPU(0));
        ResourceSummary fWC = _queries.resource(file.getId());
        assertEquals("Update!", previewContent(file, false));
        assertEquals("Hello!", previewContent(file, true));
        assertTrue(fWC.isHasWorkingCopy());

        // Apply working copy
        _commands.applyWorkingCopy(file.getId());
        revs = _queries.history(file.getId());
        assertEquals(3, revs.size());
        i = revs.iterator();
        i.next();
        i.next();
        final LogEntrySummary rev3 = i.next();
        assertEquals(2, rev3.getIndex());
        assertEquals("Hello!", previewContent(file, false));
        fWC = _queries.resource(file.getId());
        assertFalse(fWC.isHasWorkingCopy());
    }

    /**
     * Test.
     *
     * @throws IOException If the test fails.
     * @throws CommandFailedException If the test fails.
     */
    public void testCreateFile() throws IOException, CommandFailedException {

        // ARRANGE
        final String fName = UUID.randomUUID().toString();
        final ResourceSummary filesFolder =
            _queries.resourceForPath("/content/files");

        // ACT
        final ResourceSummary rs =
            createFile(fName, "Hello!", filesFolder);

        // ASSERT
        assertEquals(fName, rs.getName());
        assertEquals("/content/files/"+fName, rs.getAbsolutePath());
        assertEquals("Hello!", previewContent(rs, false));
    }


    /**
     * Test.
     *
     * @throws IOException If the test fails.
     * @throws CommandFailedException If the test fails.
     */
    public void testCreateFileRejectsDuplicateNames()
    throws IOException, CommandFailedException {

        // ARRANGE
        final String fName = UUID.randomUUID().toString();
        final ResourceSummary filesFolder =
            _queries.resourceForPath("/content/files");
        final ResourceSummary rs =
            createFile(fName, "Hello!", filesFolder);

        // ACT
        try {
            createFile(fName, "Hello!", filesFolder);
        } catch (final CommandFailedException e) {
            assertEquals(FailureCode.EXISTS, e.getCode());
        }


        // ASSERT
        assertEquals(fName, rs.getName());
        assertEquals("/content/files/"+fName, rs.getAbsolutePath());
        assertEquals("Hello!", previewContent(rs, false));
    }


    /**
     * Test.
     *
     * @throws IOException If the test fails.
     * @throws CommandFailedException If the test fails.
     */
    public void testUpdateFile() throws IOException, CommandFailedException {

        // ARRANGE
        final String fName = UUID.randomUUID().toString();
        final ResourceSummary filesFolder =
            _queries.resourceForPath("/content/files");
        final ResourceSummary rs =
            createFile(fName, "Hello!", filesFolder);
        _commands.lock(rs.getId());

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
     * @throws CommandFailedException If the test fails.
     */
    public void testUpdateFileRequiresLock() throws IOException,
                                                    CommandFailedException {

        // ARRANGE
        final String fName = UUID.randomUUID().toString();
        final ResourceSummary filesFolder =
            _queries.resourceForPath("/content/files");
        final ResourceSummary rs =
            createFile(fName, "Hello!", filesFolder);

        // ACT
        try {
            updateTextFile("Update!", rs);

        // ASSERT
        } catch (final CommandFailedException e) {
            assertEquals(FailureCode.UNLOCKED, e.getCode());
        }
        assertEquals("Hello!", previewContent(rs, false));
    }
}
