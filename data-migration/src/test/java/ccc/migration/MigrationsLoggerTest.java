/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */
package ccc.migration;

import java.io.IOException;
import java.io.StringReader;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.h2.tools.Csv;

import junit.framework.TestCase;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class MigrationsLoggerTest extends TestCase {

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {

        super.setUp();
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {

        super.tearDown();
    }

    /**
     * Tests folder migration with collection logger.
     *
     * @throws SQLException
     * @throws IOException
     */
    public void testMigrateFolders() throws SQLException, IOException {

        // ARRANGE

        final ResultSet rs = Csv.getInstance().read(
            new StringReader("testFolder, 1, 0"),
            new String[]{"NAME", "CONTENT_ID", "PARENT_ID"});

        CollectingConsole console = new CollectingConsole();
        MigrationsLogger logger = new MigrationsLogger(console);

        // ACT
        logger.migrateFolders(rs);

        // ASSERT
        assertEquals(1, console.inputList.size());

    }

    public void testMigrateContent() throws SQLException, IOException {

        // ARRANGE
        final ResultSet rs = Csv.getInstance().read(
            new StringReader("testPage, 1, 0"),
            new String[]{"NAME", "CONTENT_ID", "PARENT_ID"});

        CollectingConsole console = new CollectingConsole();
        MigrationsLogger logger = new MigrationsLogger(console);

        // ACT
        logger.migratePages(rs);

        // ASSERT
        assertEquals(1, console.inputList.size());
    }

}
