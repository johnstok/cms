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

import junit.framework.TestCase;

import org.h2.tools.Csv;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class StyleSheetSelectorTest extends TestCase {
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
                new StringReader("quit"),
                new String[]{"CODE"});

        // ACT
        final String result = new StyleSheetSelector().handle(rs);

        // ASSERT
        assertNotNull(result);
        assertEquals("quit", result);
    }
}
