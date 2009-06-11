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
package ccc.migration.ccc6.handlers;

import java.io.IOException;
import java.io.StringReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import junit.framework.TestCase;

import org.h2.tools.Csv;


/**
 * Tests for the {@link ShowInMainMenuSelector} class.
 *
 * @author Civic Computing Ltd.
 */
public class ShowInMainMenuSelectorTest
    extends
        TestCase {

    /**
     * Test.
     * @throws IOException If reading CSV fails.
     * @throws SQLException From JDBC API.
     */
    public void testHandle() throws SQLException, IOException {

        // ARRANGE
        final Csv csv = Csv.getInstance();
        final ResultSet rs =
            csv.read(
                new StringReader("1\n2"),
                new String[]{"CONTENT_ID"});

        // ACT
        final Set<Integer> result = new ShowInMainMenuSelector().handle(rs);

        // ASSERT
        assertEquals(2, result.size());
        assertTrue(result.contains(Integer.valueOf(1)));
    }
}
