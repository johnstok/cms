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
import java.util.UUID;

import ccc.api.FailureCode;
import ccc.api.ResourceSummary;
import ccc.commands.CommandFailedException;


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
        assertEquals("Hello!", previewContent(rs));
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
        assertEquals("Hello!", previewContent(rs));
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
        assertEquals("Update!", previewContent(rs));
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
        assertEquals("Hello!", previewContent(rs));
    }
}
