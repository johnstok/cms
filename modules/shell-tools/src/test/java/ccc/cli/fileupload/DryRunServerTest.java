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
package ccc.cli.fileupload;

import java.io.File;
import java.util.UUID;

import junit.framework.TestCase;
import ccc.types.ResourcePath;


/**
 * Test for {@link DryRunServer}.
 *
 * @author Civic Computing Ltd.
 */
public class DryRunServerTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testCreateFolder() {

        // ARRANGE
        final DryRunServer drs = new DryRunServer(_rootPath);

        // ACT
        final UUID fooId = drs.createFolder(drs.getRoot(), "foo", false);

        // ASSERT
        assertEquals("/root/foo", drs.getFolderPath(fooId));
    }

    /**
     * Test.
     */
    public void testCreateFile() {

        // ARRANGE
        final File f = new File("test.txt");
        final DryRunServer drs = new DryRunServer(_rootPath);

        // ACT
        drs.createFile(drs.getRoot(), f, false);

        // ASSERT
        assertEquals(1, drs.getFiles().size());
        assertEquals(
            "/root/test.txt", drs.getFiles().iterator().next().toString());
    }

    private final ResourcePath _rootPath = new ResourcePath("/root");
}
