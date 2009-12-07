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
