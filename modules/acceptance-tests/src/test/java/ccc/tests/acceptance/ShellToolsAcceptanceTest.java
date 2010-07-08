/*-----------------------------------------------------------------------------
 * Copyright (c) 2010 Civic Computing Ltd.
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

import java.nio.charset.Charset;

import ccc.api.core.ResourceSummary;
import ccc.api.types.Username;
import ccc.cli.FileUpload;
import ccc.cli.Scheduling;
import ccc.commons.Resources;


/**
 * Acceptance tests for the CLI tools.
 *
 * @author Civic Computing Ltd.
 */
public class ShellToolsAcceptanceTest
    extends
        AbstractAcceptanceTest {

    /*
     * Missing tests:
     *
     * Missing local path for bulk file upload.
     */


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testSuccessfulFileUpload() throws Exception {

        // ARRANGE
        final ResourceSummary f = tempFolder();

        final FileUpload fu = new FileUpload();
        fu.setUsername("migration");
        fu.setPassword("migration");
        fu.setIncludeHidden(false);
        fu.setLocalPath("src/test/resources/upload");
        fu.setRemotePath(f.getAbsolutePath());
        fu.setPublish(true);
        fu.setUploadUrl("http://localhost:8080/cc7");

        // ACT
        fu.run();

        // ASSERT
        final ResourceSummary rs =
            getCommands().resourceForPath(
                f.getAbsolutePath()+"/example/hello.txt");
        final String contents =
            getCommands().fileContentsFromPath(
                f.getAbsolutePath()+"/example/hello.txt", "UTF-8");

        assertEquals(
            Resources.readIntoString(
                Resources.open("upload/example/hello.txt"),
                Charset.forName("UTF-8")),
            contents);
        assertEquals(new Username("migration"), rs.getPublishedBy());
    }


    /**
     * Test.
     */
    public void testCheckIfActionSchedulerIsRunning() {

        // ARRANGE
        final Scheduling s = new Scheduling();

        s.setAction("running");
        s.setUsername("migration");
        s.setPassword("migration");
        s.setBaseUrl("http://localhost:8080/cc7");

        // ACT
        s.run();

        // ASSERT
    }


    /**
     * Test.
     */
    public void testActionSchedulerHandlesBadPassword() {

        // ARRANGE
        final Scheduling s = new Scheduling();

        s.setAction("running");
        s.setUsername("migration");
        s.setPassword("foo");
        s.setBaseUrl("http://localhost:8080/cc7");

        // ACT
        try {
            s.run();

        // ASSERT
        } catch (final RuntimeException e) {
            assertEquals("Failed to authenticate.", e.getMessage());
        }
    }


    /**
     * Test.
     */
    public void testActionSchedulerHandlesBadCommand() {

        // ARRANGE
        final Scheduling s = new Scheduling();

        s.setAction("foo");
        s.setUsername("migration");
        s.setPassword("migration");
        s.setBaseUrl("http://localhost:8080/cc7");

        // ACT
        try {
            s.run();

            // ASSERT
        } catch (final RuntimeException e) {
            assertEquals("Invalid command: foo", e.getMessage());
        }
    }
}
